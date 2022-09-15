package com.huashang.coursecompetition.domain.dto;

import java.io.Serializable;

/**
 * @author linjianhua
 * @date 2022/9/15
 */
public class ApiResult <T> implements Serializable {

    private int code;

    private T data;

    public ApiResult(T data) {
        this.data = data;
    }

    public static <T> ApiResult<T> success() {
        return success(null);
    }

    public static <T> ApiResult<T> success(T data) {
        return new ApiResult<T>(data);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
