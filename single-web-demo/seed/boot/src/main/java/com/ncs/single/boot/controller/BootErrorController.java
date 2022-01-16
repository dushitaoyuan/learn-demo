package com.ncs.single.boot.controller;

import com.alibaba.fastjson.JSON;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.commons.utils.ResponseUtil;
import com.ncs.single.boot.utils.ExceptionHandUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author dushitaoyuan
 * @desc ErrorController 自定义配置 覆盖  @BasicErrorController
 * @date 2019/12/24
 */
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class BootErrorController implements ErrorController {
    @Autowired
    ErrorAttributes errorAttributes;

    @Override
    public String getErrorPath() {
        return null;
    }

    @RequestMapping
    public ModelAndView doHandleError(HttpServletRequest request, HttpServletResponse response) {
        WebRequest webRequest = new ServletWebRequest(request, response);
        Throwable e = errorAttributes.getError(webRequest);
        if (e != null) {
            return ExceptionHandUtil.handleException(e, request, response);
        } else {
            Map<String, Object> errorAttributes = this.errorAttributes.getErrorAttributes(webRequest, false);
            String errorMsg = String.format("%s   %s", errorAttributes.get("path"), errorAttributes.get("error"));
            Integer status = (Integer) errorAttributes.get("status");
            ResponseUtil.responseJson(response, JSON.toJSONString(ResultBuilder.failed(errorMsg)), status);
            return new ModelAndView();
        }

    }
}
