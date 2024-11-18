package cn.maoyanluo.anywhere.door.controller;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.entity.User;
import cn.maoyanluo.anywhere.door.repository.UserRepository;
import cn.maoyanluo.anywhere.door.tools.JwtTools;
import cn.maoyanluo.anywhere.door.tools.MD5Tools;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    public UserController(UserRepository repository, MD5Tools md5Tools, JwtTools jwtTools) {
        this.repository = repository;
        this.md5Tools = md5Tools;
        this.jwtTools = jwtTools;
    }

    private final UserRepository repository;
    private final MD5Tools md5Tools;
    private final JwtTools jwtTools;

    @GetMapping("/register")
    public Response<Map<String, Object>> register(@RequestParam("username") String username,
                                                  @RequestParam("password") String password) {
        Map<String, Object> map = new HashMap<>();
        Response<Map<String, Object>> response = Response.success(map);
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            response.setCode(-2);
            response.setMsg("failed");
            map.put("status", "USERNAME_OR_PASSWORD_EMPTY");
            return response;
        }
        User findByUsername = repository.findByUsername(username);
        if (findByUsername != null) {
            response.setCode(-2);
            response.setMsg("failed");
            map.put("status", "USERNAME_EXIST");
            return response;
        }
        User user = new User(null, username, md5Tools.md5(password));
        repository.save(user);
        String token = jwtTools.generateToken(username);
        String flushToken = jwtTools.generateFlushToken(username);
        map.put("token", token);
        map.put("flush_token", flushToken);
        return response;
    }

    @GetMapping("/login")
    public Response<Map<String, Object>> login(@RequestParam("username") String username,
                                               @RequestParam("password") String password) {
        Map<String, Object> map = new HashMap<>();
        Response<Map<String, Object>> response = Response.success(map);
        User user = repository.findByUsername(username);
        if (md5Tools.md5(password).equals(user.getPassword())) {
            String token = jwtTools.generateToken(username);
            String flushToken = jwtTools.generateFlushToken(username);
            map.put("token", token);
            map.put("flush_token", flushToken);
        } else {
            response.setCode(-2);
            response.setMsg("failed");
            map.put("status", "PASSWORD_WRONG");
        }
        return response;
    }

    @GetMapping("/flush_token")
    public Response<Map<String, Object>> flushToken(@RequestParam("token") String token,
                                                    @RequestParam("flush_token") String flushToken) {
        Map<String, Object> map = new HashMap<>();
        Response<Map<String, Object>> response = Response.success(map);
        JwtTools.Pair<String, Boolean> tokenParse = jwtTools.parseToken(token);
        JwtTools.Pair<String, Boolean> flushTokenParse = jwtTools.parseToken(flushToken);
        if (tokenParse.getFirst() != null && tokenParse.getFirst().equals(flushTokenParse.getFirst())) {
            if (flushTokenParse.getSecond()) {
                String newToken = jwtTools.generateToken(tokenParse.getFirst());
                String newFlushToken = jwtTools.generateFlushToken(tokenParse.getFirst());
                map.put("token", newToken);
                map.put("flush_token", newFlushToken);
            } else {
                response.setCode(-2);
                response.setMsg("failed");
                map.put("status", "FLUSH_TOKEN_EXPIRE");
            }
        } else {
            response.setCode(-2);
            response.setMsg("failed");
            map.put("status", "TOKEN_FLUSH_TOKEN_ERROR");
        }
        return response;
    }

    @PutMapping("/update_password")
    public Response<Map<String, Object>> updatePassword(@RequestAttribute("username") String username, @RequestBody Map<String, String> params) {
        String password = params.get("password");
        String newPassword = params.get("new_password");
        Map<String, Object> map = new HashMap<>();
        Response<Map<String, Object>> response = Response.success(map);
        if (username != null) {
            User user = repository.findByUsername(username);
            if (md5Tools.md5(password).equals(user.getPassword()) && !StringUtils.isEmpty(newPassword)) {
                user.setPassword(md5Tools.md5(newPassword));
                repository.save(user);
            } else {
                response.setCode(-2);
                response.setMsg("failed");
                map.put("status", "PASSWORD_WRONG");
            }
        } else {
            response.setCode(-2);
            response.setMsg("failed");
            map.put("status", "TOKEN_WRONG");
        }
        return response;
    }

}
