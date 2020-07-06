package com.taoyuanx.demo.transaction;

import com.taoyuanx.demo.business.entity.UserAccountEntity;
import lombok.Data;

import java.io.Serializable;

/**
 * @author dushitaoyuan
 * @desc 转账消息
 * @date 2020/7/6
 */
@Data
public class TransferMoneyMsg implements Serializable {
    private String target;
    private UserAccountEntity account;
    private Double money;
}
