package com.ncs.single.boot.controller;

/**
 * @author dushitaoyuan
 * @desc 测试
 * @date 2019/12/23
 */

import com.ncs.commons.api.MapResultBuilder;
import com.ncs.commons.bean.CBeanMapper;
import com.ncs.commons.utils.PropertiesUtil;
import com.ncs.single.boot.dto.OpenAccountDTO;
import com.ncs.single.boot.service.OpenAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RequestMapping("test")
@RestController
public class TestController {
    @Autowired
    OpenAccountService openAccountService;
    @RequestMapping("test")
    @ResponseBody
    public MapResultBuilder value(){
        return MapResultBuilder.newBuilder(1).put("config", PropertiesUtil.getSystemProperty("application.value"));
    }
    @RequestMapping("test2")
    @ResponseBody
    public Map<String, Object> value2(){
         return MapResultBuilder.newBuilder(1).put("config", PropertiesUtil.getSystemProperty("application.value")).build();
    }

    @RequestMapping("openAccount/list")
    @ResponseBody
    public List<OpenAccountDTO> listAccount(){
        return CBeanMapper.mapList(openAccountService.list(),OpenAccountDTO.class);
    }
}
