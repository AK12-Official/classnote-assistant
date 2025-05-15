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

    public static <T> Result<T> success(T data) {
        Result<T> r = new Result<>();
        r.code = 200;
        r.msg = "success";
        r.data = data;
        return r;
    }

    public static <T> Result<T> error(String message) {
        Result<T> r = new Result<>();
        r.code = 500;
        r.msg = message;
        r.data = null;
        return r;
    }
}
