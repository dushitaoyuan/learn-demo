package com.ncs.single.mvc.entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author dushitaoyuan
 *  @desc 账户实体
 * @date 2019/12/17
 */
@Data
public class AccountEntity {
    private Long id;
    private String username;
    private String phone;
    private String email;
    @JSONField(serialize = false)
    private String password;
    private Integer status;
    private Long bindUser;
    private Integer type;
}
