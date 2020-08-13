package com.taoyuanx.demo.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dushitaoyuan
 * @date 2020/8/11
 */
@TableName("user")
@Data
public class UserEntity implements Serializable {
    private Long id;
    private String username;
    private Integer age;
    private Integer sex;
    @TableField("create_time")
    private Date createTime;
}
