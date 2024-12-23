package cn.maoyanluo.anywhere.door.config.auth;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.constant.ErrorCode;
import cn.maoyanluo.anywhere.door.constant.ErrorMessage;
import cn.maoyanluo.anywhere.door.constant.ParamsConstant;
import cn.maoyanluo.anywhere.door.tools.JwtTools;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.common.util.StringUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

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
        String token = request.getHeader(ParamsConstant.TOKEN);
        if (StringUtils.isEmpty(token)) {
            response.getOutputStream().println(objectMapper.writeValueAsString(Response.failed(ErrorCode.EMPTY_TOKEN, ErrorMessage.EMPTY_TOKEN)));
            return false;
        }
        JwtTools.Pair<String, Boolean> parseToken = jwtTools.parseToken(token);
        if (parseToken == null) {
            response.getOutputStream().println(objectMapper.writeValueAsString(Response.failed(ErrorCode.ERROR_TOKEN, ErrorMessage.ERROR_TOKEN)));
            return false;
        }
        if (!parseToken.getSecond()) {
            response.getOutputStream().println(objectMapper.writeValueAsString(Response.failed(ErrorCode.EXPIRE_TOKEN, ErrorMessage.EXPIRE_TOKEN)));
            return false;
        }
        request.setAttribute(ParamsConstant.USERNAME, parseToken.getFirst());
        return true;
    }

}
