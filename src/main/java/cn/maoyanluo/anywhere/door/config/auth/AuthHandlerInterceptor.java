package cn.maoyanluo.anywhere.door.config.auth;

import cn.maoyanluo.anywhere.door.Response;
import cn.maoyanluo.anywhere.door.tools.JwtTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.HashMap;
import java.util.Map;

@Component
public class AuthHandlerInterceptor implements HandlerInterceptor {

    private final JwtTools jwtTools;
    private final ObjectMapper objectMapper;

    @Autowired
    public AuthHandlerInterceptor(JwtTools jwtTools, ObjectMapper objectMapper) {
        this.jwtTools = jwtTools;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        Response<Map<String, Object>> responseEntity = new Response<>();
        responseEntity.setCode(-1);
        responseEntity.setMsg("failed");
        if (StringUtils.isEmpty(token)) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "EMPTY_TOKEN");
            responseEntity.setData(result);
            response.getOutputStream().println(objectMapper.writeValueAsString(responseEntity));
            return false;
        }
        JwtTools.Pair<String, Boolean> parseToken = jwtTools.parseToken(token);
        if (parseToken == null) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "ERROR_TOKEN");
            responseEntity.setData(result);
            response.getOutputStream().println(objectMapper.writeValueAsString(responseEntity));
            return false;
        }
        if (!parseToken.getSecond()) {
            Map<String, Object> result = new HashMap<>();
            result.put("status", "EXPIRE_TOKEN");
            responseEntity.setData(result);
            response.getOutputStream().println(objectMapper.writeValueAsString(responseEntity));
            return false;
        }
        request.setAttribute("username", parseToken.getFirst());
        return true;
    }


}
