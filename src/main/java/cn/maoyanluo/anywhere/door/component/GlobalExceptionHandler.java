package cn.maoyanluo.anywhere.door.component;

import cn.maoyanluo.anywhere.door.bean.Response;
import cn.maoyanluo.anywhere.door.constant.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class GlobalExceptionHandler {

    public static final String TAG = GlobalExceptionHandler.class.getSimpleName();

    private final ObjectMapper objectMapper;
    private final LogTools logTools;

    @Autowired
    public GlobalExceptionHandler(ObjectMapper objectMapper, LogTools logTools) {
        this.objectMapper = objectMapper;
        this.logTools = logTools;
    }


    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public String handleException(Exception e) {
        try {
            logTools.w(TAG, "request handler exception, error: " + e.getMessage());
            return objectMapper.writeValueAsString(Response.failed(ErrorCode.GLOBAL_EXCEPTION, e.getMessage()));
        } catch (JsonProcessingException ex) {
            logTools.e(TAG, "convert resp to json exception, error: " + ex.getMessage());
            return e.getMessage();
        }
    }

}
