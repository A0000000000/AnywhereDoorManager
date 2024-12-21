package cn.maoyanluo.anywhere.door.bean;

import cn.maoyanluo.anywhere.door.constant.ErrorCode;
import cn.maoyanluo.anywhere.door.constant.ErrorMessage;
import lombok.Data;

@Data
public class Response<T> {

    private int code;
    private String msg;
    private T data;

    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(ErrorCode.SUCCESS);
        response.setMsg(ErrorMessage.SUCCESS);
        response.setData(data);
        return response;
    }

    public static <T> Response<T> failed(int errorCode, String errorMessage) {
        Response<T> response = new Response<>();
        response.setCode(errorCode);
        response.setMsg(errorMessage);
        return response;
    }

}
