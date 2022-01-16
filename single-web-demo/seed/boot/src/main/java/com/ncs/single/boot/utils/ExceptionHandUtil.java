package com.ncs.single.boot.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.ncs.commons.api.Result;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.commons.api.ResultCode;
import com.ncs.commons.utils.ResponseUtil;
import com.ncs.single.boot.exception.InnerError;
import com.ncs.single.boot.exception.ServiceException;
import com.ncs.single.boot.exception.UnAuthException;
import com.ncs.single.boot.exception.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @desc 异常处理工具类
 * @date 2019/12/24
 */
public class ExceptionHandUtil {
    public static final Logger LOG = LoggerFactory.getLogger(ExceptionHandUtil.class);

    public static ModelAndView handleException(Throwable e, HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView();
        InnerError innerError = handleException(request, response, e);
        if (isJson(request, null)) {
            Result errorResult=ResultBuilder.failed(innerError.getErrorCode(),innerError.getErrorMsg());
            //处理json请求
            ResponseUtil.responseJson(response,JSON.toJSONString(errorResult),innerError.getHttpCode());
            return modelAndView;
        } else {
            // 处理页面转发请求
            modelAndView.setStatus(HttpStatus.resolve(innerError.getHttpCode()));
            ResultCode errorCode = ResultCode.resultCode(innerError.getErrorCode());
            String viewName="error/error";
            switch (errorCode){
                case INTERNAL_SERVER_ERROR:viewName="error/500";break;
                case NOT_FOUND:viewName="error/404";break;
                case UNAUTHORIZED:viewName="error/no-auth";break;
            }
            modelAndView.setViewName(viewName);
            modelAndView.addObject("errorMsg", innerError.getErrorMsg());
            modelAndView.addObject("error",innerError);
            LOG.error("请求地址[{}] 出现异常，方法：{}.{}，异常摘要:{}", request.getRequestURI(), e);
            return modelAndView;

        }
    }

    public static InnerError handleException(HttpServletRequest request, HttpServletResponse response, Throwable e) {
        HttpStatus httpStatus = HttpStatus.OK;
        Integer errorCode=null;
        String errorMsg=e.getMessage();
        if(e instanceof  ValidatorException){
            httpStatus=HttpStatus.BAD_REQUEST;
            errorCode=ResultCode.PARAM_ERROR.code;
        }else if (e instanceof MethodArgumentNotValidException) {
            httpStatus=HttpStatus.BAD_REQUEST;
            errorCode=ResultCode.PARAM_ERROR.code;
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            if (bindingResult.hasErrors()) {
                errorMsg = "参数异常:" + bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("\n"));
            }
        }else if(e instanceof BindException){
            httpStatus=HttpStatus.BAD_REQUEST;
            errorCode=ResultCode.PARAM_ERROR.code;
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            if (bindingResult.hasErrors()) {
                errorMsg = "参数异常:" + bindingResult.getFieldErrors().stream().map(FieldError::getDefaultMessage).collect(Collectors.joining("\n"));
            }
        } else if (e instanceof ServiceException) {
            ServiceException exception = (ServiceException) e;
            errorCode=exception.getErrorCode();
        } else if (e instanceof UnAuthException) {
            httpStatus = HttpStatus.UNAUTHORIZED;
            errorCode=ResultCode.UNAUTHORIZED.code;
        } else if (e instanceof HttpMediaTypeNotSupportedException) {
            httpStatus = HttpStatus.UNSUPPORTED_MEDIA_TYPE;
            errorCode=ResultCode.UN_SUPPORT_MEDIATYPE.code;
            HttpMediaTypeNotSupportedException mediaEx = (HttpMediaTypeNotSupportedException) e;
            errorMsg="不支持该媒体类型:" + mediaEx.getContentType();
        } else if (e instanceof JSONException) {
            httpStatus=HttpStatus.BAD_REQUEST;
            errorCode=ResultCode.PARAM_ERROR.code;
            errorMsg = "参数异常,json格式非法";
        } else if (e instanceof ServletException) {
            httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode=ResultCode.INTERNAL_SERVER_ERROR.code;
        } else if (e instanceof NoHandlerFoundException) {
            httpStatus=HttpStatus.NOT_FOUND;
            errorCode=ResultCode.NOT_FOUND.code;
            errorMsg="请求 [" + ((NoHandlerFoundException) e).getRequestURL() + "] 不存在";
        } else {
            httpStatus=HttpStatus.INTERNAL_SERVER_ERROR;
            errorCode=ResultCode.INTERNAL_SERVER_ERROR.code;
            errorMsg="系统内部错误，请联系管理员";
            LOG.error("系统未知异常,异常信息:", e);
        }
        InnerError innerError=new InnerError();
        innerError.setErrorCode(errorCode);
        innerError.setHttpCode(httpStatus.value());
        innerError.setErrorMsg(errorMsg);
        innerError.setException(e);
        return  innerError;
    }

    public static boolean isJson(HttpServletRequest request, Object handler) {
        String accept = request.getHeader("Accept");
        if ((accept != null && accept.contains("json"))) {
            return true;
        } else {
            if (ResponseUtil.isAjaxRequest(request)) {
                return true;
            }

            return false;
        }


    }
}
