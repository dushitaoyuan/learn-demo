package com.taoyuanx.demo.business.service;

import com.taoyuanx.demo.business.entity.UserAccountEntity;

/**
 * @author dushitaoyuan
 * @desc 转账服务
 * @date 2020/7/6
 */
public interface TransferMoneyService {
    void updateMoneyForA(UserAccountEntity userAccountEntity, Double money);

    void updateMoneyForB(UserAccountEntity userAccountEntity, Double money);

    void transferMoney(UserAccountEntity from, UserAccountEntity to, Double money);
}
