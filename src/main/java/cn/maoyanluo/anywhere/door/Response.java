package cn.maoyanluo.anywhere.door;

import lombok.Data;

@Data
public class Response<T> {

    private int code;
    private String msg;
    private T data;


    public static <T> Response<T> success(T data) {
        Response<T> response = new Response<>();
        response.setCode(200);
        response.setMsg("success");
        response.setData(data);
        return response;
    }

}
