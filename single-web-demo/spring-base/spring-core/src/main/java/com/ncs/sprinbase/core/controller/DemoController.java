package com.ncs.sprinbase.core.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ncs.sprinbase.core.aop.LogAop;
import com.ncs.sprinbase.core.commons.Result;
import com.ncs.sprinbase.core.commons.ResultBuilder;
import com.ncs.sprinbase.core.dto.SimpleUserDTO;
import com.ncs.sprinbase.core.interceptor.TimeMonitor;
import com.ncs.sprinbase.core.log.OperateAudit;
import com.ncs.sprinbase.core.service.SimpleUserService;
import com.ncs.sprinbase.core.utils.IdGenUtil;

/**
 * @author lianglei
 * @date 2019/10/16 15:06
 * @desc 测试
 */
@Controller
@TimeMonitor
@RequestMapping("demo")
public class DemoController {
    @Autowired
    SimpleUserService simpleUserService;

    @GetMapping("log")
    @OperateAudit(code = 101)
    public void log() {
        LogAop.logMsg("测试日志001" + IdGenUtil.genUuid());
        LogAop.logContext("userId", "111111");
    }

    @GetMapping({ "notwarn" })
    @ResponseBody
    @TimeMonitor(need = false)
    public Result noWarning(Integer value) {
        try {
            Thread.sleep(6000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResultBuilder.success(value);
    }

    @GetMapping({ "warn" })
    @ResponseBody
    public Result warning(Integer value) {
        try {
            Thread.sleep(6000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return ResultBuilder.success(value);
    }

    @GetMapping({ "warn2" })
    @ResponseBody
    public Result warning2(Integer value) {
        return ResultBuilder.success(value);
    }

    @PostMapping({ "user/get" })
    @ResponseBody
    @TimeMonitor(need = false)
    public SimpleUserDTO getUser(Long id) {
        return simpleUserService.findById(id);
    }
}


