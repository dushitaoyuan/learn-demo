package com.ncs.single.boot.config;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ncs.commons.api.Result;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.commons.api.ResultCode;
import com.ncs.commons.utils.ResponseUtil;
import com.ncs.single.boot.controller.BootErrorController;
import com.ncs.single.boot.exception.ServiceException;
import com.ncs.single.boot.exception.SystemExceptionHandler;
import com.ncs.single.boot.exception.UnAuthException;
import com.ncs.single.boot.exception.ValidatorException;
import com.ncs.single.boot.utils.ExceptionHandUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author dushitaoyuan
 * @desc 注解异常处理例子
 * @date 2019/12/24
 */
/*@Configuration
@ConditionalOnMissingBean({SystemExceptionHandler.class, BootErrorController.class})*/
public class ExceptionHandler {
    public static final Logger LOG = LoggerFactory.getLogger(SeedBootMvcConfig.ResponseControllerAdviceHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception e, HttpServletRequest request, HttpServletResponse response) {
        return ExceptionHandUtil.handleException(e,request,response);
    }
    }



