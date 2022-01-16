package com.ncs.single.boot.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * api权限设计-开放账户表
 * </p>
 *
 * @author NCS
 * @since 2019-12-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("open_account")
public class OpenAccountEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long id;

    /**
     * api账户id
     */
    private String openId;

    /**
     * api账户密钥
     */
    private String openSecret;

    /**
     * 状态,1:正常,2:锁定,3:异常 0删除
     */
    private Integer status;

    /**
     * 备注:接入厂商,企业,用户,用途描述
     */
    @TableField("`desc`")
    private String desc;

    /**
     * 账户白名单
     */
    private String whiteIp;

    /**
     * 账户结束时间
     */
    private Date endTime;

    /**
     * 账户创建时间
     */
    private Date createTime;


}
