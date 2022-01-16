package com.ncs.sprinbase.core.entity;

import lombok.Data;

/**
 * @author dushitaoyuan
 *  @desc 简单user实体
 * @date 2019/12/17
 */
@Data
public class SimpleUserEntity {
    private Long    id;
    private String  username;
    private Integer password;
}


