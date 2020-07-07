package com.taoyuanx.demo.business.service;

import com.baomidou.dynamic.datasource.annotation.DS;
import com.taoyuanx.demo.business.dao.UserAccountDao;
import com.taoyuanx.demo.business.entity.UserAccountEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2020/7/7
 */
@Component
public class TransferHelperService {
    @Autowired
    UserAccountDao userAccountDao;

    @DS("money_a")
    public void updateMoneyForA(UserAccountEntity userAccountEntity, Double money) {
        UserAccountEntity account = userAccountDao.selectById(userAccountEntity.getId());
        if (Objects.isNull(account)) {
            throw new RuntimeException("转账失败,账户不存在");
        }
        int row = 0;
        if (money > 0) {
            row = userAccountDao.addMoney(userAccountEntity.getId(), money);
        } else if (money < 0) {
            row = userAccountDao.subMoney(userAccountEntity.getId(), -money);
        }
        if (row == 0) {
            throw new RuntimeException("转账失败,请检查账户余额");
        }

    }

    @DS("money_b")
    public void updateMoneyForB(UserAccountEntity userAccountEntity, Double money) {
        UserAccountEntity account = userAccountDao.selectById(userAccountEntity.getId());
        if (Objects.isNull(account)) {
            throw new RuntimeException("转账失败,账户不存在");
        }
        int row = 0;
        if (money > 0) {
            row = userAccountDao.addMoney(userAccountEntity.getId(), money);
        } else if (money < 0) {
            row = userAccountDao.subMoney(userAccountEntity.getId(), -money);
        }
        if (row == 0) {
            throw new RuntimeException("转账失败,请检查账户余额");
        }
    }

    @DS("money_a")
    public UserAccountEntity getByIdA(Long accountId) {

        return userAccountDao.selectById(accountId);

    }

    @DS("money_b")
    public UserAccountEntity getByIdB(Long accountId) {
        return userAccountDao.selectById(accountId);

    }


}
