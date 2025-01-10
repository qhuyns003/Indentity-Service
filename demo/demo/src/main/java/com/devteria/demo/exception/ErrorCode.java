package com.devteria.demo.exception;

public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"Uncategorized error"),
    INVALID_KEY(1001,"Invalid enum key"),
    USER_EXISTED(1002,"User already existed"),
    USERNAME_INVALID(1003,"Username is invalid"),
    PASSWORD_INVALID(1004,"Password is invalid"),
    USER_NOT_EXISTED(1005,"User is not existed"),
    UNAUTHENTICATED(1006,"Unauthenticated");
    private Integer code;
    private String message;
    private ErrorCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    };
    public Integer getCode() {
        return code;
    }
    public String getMessage() {
        return message;
    }
}
