package cn.maoyanluo.anywhere.door.controller;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.bean.dto.Token;
import cn.maoyanluo.anywhere.door.constant.ErrorCode;
import cn.maoyanluo.anywhere.door.constant.ErrorMessage;
import cn.maoyanluo.anywhere.door.entity.User;
import cn.maoyanluo.anywhere.door.repository.UserRepository;
import cn.maoyanluo.anywhere.door.tools.JwtTools;
import cn.maoyanluo.anywhere.door.tools.MD5Tools;
import io.micrometer.common.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public Response<Token> register(@RequestParam("username") String username,
                                    @RequestParam("password") String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            return Response.failed(ErrorCode.USERNAME_OR_PASSWORD_EMPTY, ErrorMessage.USERNAME_OR_PASSWORD_EMPTY);
        }
        User findByUsername = repository.findByUsername(username);
        if (findByUsername != null) {
            return Response.failed(ErrorCode.USERNAME_EXIST, ErrorMessage.USERNAME_EXIST);
        }
        User user = new User(null, username, md5Tools.md5(password));
        repository.save(user);
        String token = jwtTools.generateToken(username);
        String flushToken = jwtTools.generateFlushToken(username);
        return Response.success(new Token(token, flushToken));
    }

    @GetMapping("/login")
    public Response<Token> login(@RequestParam("username") String username,
                                               @RequestParam("password") String password) {
        User user = repository.findByUsername(username);
        if (user != null && md5Tools.md5(password).equals(user.getPassword())) {
            String token = jwtTools.generateToken(username);
            String flushToken = jwtTools.generateFlushToken(username);
            return Response.success(new Token(token, flushToken));
        } else {
            return Response.failed(ErrorCode.PASSWORD_WRONG, ErrorMessage.PASSWORD_WRONG);
        }
    }

    @GetMapping("/flush_token")
    public Response<Token> flushToken(@RequestParam("token") String token,
                                                    @RequestParam("flush_token") String flushToken) {
        JwtTools.Pair<String, Boolean> tokenParse = jwtTools.parseToken(token);
        JwtTools.Pair<String, Boolean> flushTokenParse = jwtTools.parseToken(flushToken);
        if (tokenParse != null && flushTokenParse != null) {
            if (flushTokenParse.getSecond()) {
                String newToken = jwtTools.generateToken(flushTokenParse.getFirst());
                String newFlushToken = jwtTools.generateFlushToken(flushTokenParse.getFirst());
                return Response.success(new Token(newToken, newFlushToken));
            } else {
                return Response.failed(ErrorCode.FLUSH_TOKEN_EXPIRE, ErrorMessage.FLUSH_TOKEN_EXPIRE);
            }
        } else {
            return Response.failed(ErrorCode.TOKEN_FLUSH_TOKEN_ERROR, ErrorMessage.TOKEN_FLUSH_TOKEN_ERROR);
        }
    }

    @PutMapping("/update_password")
    public Response<Object> updatePassword(@RequestAttribute("username") String username, @RequestBody Map<String, String> params) {
        String password = params.get("password");
        String newPassword = params.get("new_password");
        if (username != null) {
            User user = repository.findByUsername(username);
            if (md5Tools.md5(password).equals(user.getPassword()) && !StringUtils.isEmpty(newPassword)) {
                user.setPassword(md5Tools.md5(newPassword));
                repository.save(user);
                return Response.success(null);
            } else {
                return Response.failed(ErrorCode.PASSWORD_WRONG, ErrorMessage.PASSWORD_WRONG);
            }
        } else {
            return Response.failed(ErrorCode.TOKEN_WRONG, ErrorMessage.TOKEN_WRONG);
        }
    }

}
