package com.ncs.single.mvc.exception;

import org.springframework.web.bind.annotation.RestController;

/**
 * 权限异常 http状态码401
 */
public class UnAuthException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public UnAuthException(String message) {
        super(message);
    }


}
