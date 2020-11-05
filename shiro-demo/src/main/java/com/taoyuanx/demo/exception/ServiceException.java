package com.taoyuanx.demo.exception;


import com.taoyuanx.demo.api.ResultCode;

/**
 * 业务异常
 */
public class ServiceException extends RuntimeException {
    private Integer errorCode = ResultCode.BUSSINESS_ERROR.code;
    public ServiceException(String msg) {
        super(msg);
    }

    public ServiceException(Integer errorCode, String msg) {
        super(msg);
        if (errorCode != null) {
            this.errorCode = errorCode;
        }

    }

    public Integer getErrorCode() {
        return errorCode;
    }


}
