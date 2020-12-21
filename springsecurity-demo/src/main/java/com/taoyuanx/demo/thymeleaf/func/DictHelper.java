package com.taoyuanx.demo.thymeleaf.func;

import com.taoyuanx.demo.thymeleaf.model.DictModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author dushitaoyuan
 * @date 2020/10/1122:43
 * @desc: 字典帮助类
 */
@Component
public class DictHelper {
    /**
     * 测试数据
     */
    private static List<DictModel> dictModelList = new ArrayList<>();
    static {
        dictModelList.add(demoDictModel("身份证", "0", "1"));
        dictModelList.add(demoDictModel("户口本", "1", "1"));
        dictModelList.add(demoDictModel("港澳通行证", "2", "1"));
        dictModelList.add(demoDictModel("士兵证", "3", "1"));
    }

    /**
     * 根据字典类型查询字典列表
     */
    public List<DictModel> list(String dictType) {
        return dictModelList.stream().filter(dictModel -> {
            return dictModel.getType().equals(dictType);
        }).collect(Collectors.toList());
    }

    /**
     * 根据字典code和和字典类型获取字典
     */
    public String value(String dictType, String dictCode) {
        Optional<DictModel> first = dictModelList.stream().filter(dictModel -> {
            return dictModel.getType().equals(dictType) && dictModel.getCode().equals(dictCode);
        }).findFirst();
        if (first.isPresent()) {
            return first.get().getValue();
        }
        return null;
    }

    /**
     * 根据字典值和字典类型获取字典
     */
    public String code(String dictType, String dictValue) {
        Optional<DictModel> first = dictModelList.stream().filter(dictModel -> {
            return dictModel.getType().equals(dictType) && dictModel.getValue().equals(dictValue);
        }).findFirst();
        if (first.isPresent()) {
            return first.get().getCode();
        }
        return null;
    }

    private static DictModel demoDictModel(String value, String code, String type) {
        DictModel demo = new DictModel();
        demo.setCode(code);
        demo.setValue(value);
        demo.setType(type);
        return demo;
    }


}
