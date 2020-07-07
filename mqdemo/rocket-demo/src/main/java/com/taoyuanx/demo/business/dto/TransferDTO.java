package com.taoyuanx.demo.business.dto;

import com.taoyuanx.demo.business.entity.UserAccountEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
@Data
public class TransferDTO implements Serializable {
    /**
     * 流水号
     */
    private String serialNo;
    /**
     * 付款账户
     */
    private UserAccountEntity from;
    /**
     * 收款账户
     */
    private UserAccountEntity to;
    /**
     * 金额
     */
    private Double money;
    /**
     * 转账方式
     */
    private String transferType;


}
