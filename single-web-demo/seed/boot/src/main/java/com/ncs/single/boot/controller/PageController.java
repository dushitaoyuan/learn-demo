package com.ncs.single.boot.controller;

import com.ncs.commons.api.MapResultBuilder;
import com.ncs.commons.api.Result;
import com.ncs.commons.api.ResultBuilder;
import com.ncs.single.boot.exception.UnAuthException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author dushitaoyuan
 * @desc 页面转发
 * @date 2019/12/20
 */
@Controller
@RequestMapping("page")
public class PageController {
    @RequestMapping("{page}")
    public  String page(@PathVariable("page") String page, Model model){
        model.addAttribute("msg","tmpl数据");
        return  page;
    }
    @RequestMapping("testError")
    @ResponseBody
    public  MapResultBuilder error(){
        int i=0;
        return MapResultBuilder.newBuilder(1).put("num", 5/i);
    }

    @RequestMapping("result")
    @ResponseBody
    public Result result(){
        return ResultBuilder.success("hello world");
    }
    @RequestMapping("testError2")
    public  void error2(){
        throw  new UnAuthException("权限异常");
    }

}
