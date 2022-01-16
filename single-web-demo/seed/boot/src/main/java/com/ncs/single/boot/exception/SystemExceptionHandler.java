package com.ncs.single.boot.exception;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ncs.commons.api.Result;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.commons.api.ResultCode;
import com.ncs.commons.utils.ResponseUtil;
import com.ncs.single.boot.utils.ExceptionHandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理
 */
public class SystemExceptionHandler implements HandlerExceptionResolver {
    public static final Logger LOG = LoggerFactory.getLogger(SystemExceptionHandler.class);

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception e) {
        return ExceptionHandUtil.handleException(e,request,response);
    }




}
