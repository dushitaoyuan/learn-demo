package com.taoyuanx.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 
 * </p>
 *
 * @author taoyuan
 * @since 2020-09-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("role")
public class RoleEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id",type = IdType.ASSIGN_ID)
    private Long id;

    @TableField("name")
    private String name;


}
