package com.taoyuanx.demo.business.service.impl;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.taoyuanx.demo.business.dao.UserAccountDao;
import com.taoyuanx.demo.business.entity.UserAccountEntity;
import com.taoyuanx.demo.business.service.TransferMoneyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
@Service
public class TransferMoneyServiceImpl implements TransferMoneyService {

    @Autowired
    UserAccountDao userAccountDao;

    @Override
    @DS("money_a")
    public void updateMoneyForA(UserAccountEntity userAccountEntity, Double money) {
        int row = 0;
        if (money > 0) {
            row = userAccountDao.addMoney(userAccountEntity.getId(), money);
        } else if (money < 0) {
            row = userAccountDao.subMoney(userAccountEntity.getId(), money);
        }
        if (row == 0) {
            throw new RuntimeException("转账失败,请检查账户余额");
        }

    }

    @Override
    @DS("money_b")
    public void updateMoneyForB(UserAccountEntity userAccountEntity, Double money) {
        int row = 0;
        if (money > 0) {
            row = userAccountDao.addMoney(userAccountEntity.getId(), money);
        } else if (money < 0) {
            row = userAccountDao.subMoney(userAccountEntity.getId(), money);
        }
        if (row == 0) {
            throw new RuntimeException("转账失败,请检查账户余额");
        }
    }

    @Override
    public void transferMoney(UserAccountEntity from, UserAccountEntity to, Double money) {

    }
}
