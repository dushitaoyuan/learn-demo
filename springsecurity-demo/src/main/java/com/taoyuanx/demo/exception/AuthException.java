package com.taoyuanx.demo.exception;


import com.taoyuanx.demo.api.ResultCode;

/**
 * 认证异常
 */
public class AuthException extends RuntimeException {
    private static final long serialVersionUID = 8793672380339632040L;
    private Integer errorCode = ResultCode.UNAUTHORIZED.code;

    public AuthException(String msg) {
        super(msg);
    }

    public AuthException(Integer errorCode, String msg) {
        super(msg);
        if (errorCode != null) {
            this.errorCode = errorCode;
        }

    }

    public Integer getErrorCode() {
        return errorCode;
    }


}
