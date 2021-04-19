package com.taoyuanx.thrift.core.exception;

public class MyThriftExceptioin  extends RuntimeException{

    public MyThriftExceptioin() {
    }

    public MyThriftExceptioin(String message) {
        super(message);
    }

    public MyThriftExceptioin(String message, Throwable cause) {
        super(message, cause);
    }

    public MyThriftExceptioin(Throwable cause) {
        super(cause);
    }

    public MyThriftExceptioin(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
