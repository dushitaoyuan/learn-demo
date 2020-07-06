package com.taoyuanx.demo.business.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
@TableName("user_account")
@Data
public class UserAccountEntity extends BaseEntity {
    private String username;
    private Double money;
    @TableField("transfer_password")
    private String transferPassword;
}
