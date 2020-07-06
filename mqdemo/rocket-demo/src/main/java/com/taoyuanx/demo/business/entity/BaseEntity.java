package com.taoyuanx.demo.business.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
@Data
public class BaseEntity implements Serializable {
    @TableId(type = IdType.ID_WORKER)
    private Long id;
    private Date createtime;
}
