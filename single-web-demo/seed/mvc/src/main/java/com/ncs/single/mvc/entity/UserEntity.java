package com.ncs.single.mvc.entity;

import lombok.Data;

/**
 * @author dushitaoyuan
 *  @desc 账户实体
 * @date 2019/12/17
 */
@Data
public class UserEntity {
    private Long id;
    private String name;
    private Integer age;
    private String address;
}
