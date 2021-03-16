package com.taoyuanx.demo.thymeleaf.model;

import lombok.Data;

/**
 * @author dushitaoyuan
 * @date 2020/10/1122:32
 * @desc: 字典模型
 */
@Data
public class DictModel {
    private Long id;
    private String value;
    private String code;
    private String type;
    private Integer enable;
}
