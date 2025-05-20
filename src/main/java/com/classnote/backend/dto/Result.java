package com.classnote.backend.dto;

import lombok.Data;

/**
 * ClassName：Result
 * Package:com.classnote.backend.dto
 * Description:
 *
 * @Auther：zh
 * @Create 2025/5/8 19:06
 * @Version 1.0
 */

@Data
public class Result<T> {
    private int code;
    private String msg;
    private T data;

    // 手动添加 getter/setter 方法（傻逼Lombok）
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.setCode(200);
        r.setMsg("success");
        r.setData(data);
        return r;
    }

    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.setCode(500);
        r.setMsg(message);
        r.setData(null);
        return r;
    }
}
