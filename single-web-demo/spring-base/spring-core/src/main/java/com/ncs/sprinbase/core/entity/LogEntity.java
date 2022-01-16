package com.ncs.sprinbase.core.entity;

import java.util.Date;

import lombok.Data;

/**
 * @author dushitaoyuan
 *  @desc 简单日志实体
 * @date 2019/12/17
 */
@Data
public class LogEntity {
    private Long    id;
    private Integer code;
    private String  msg;
    private String  msgContext;
    private Date    createtime;
}


