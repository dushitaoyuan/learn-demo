package com.ncs.single.mvc.service.impl;

import com.ncs.commons.utils.IdGenUtil;
import com.ncs.commons.utils.PasswordUtil;
import com.ncs.single.mvc.dao.UserDao;
import com.ncs.single.mvc.entity.AccountEntity;
import com.ncs.single.mvc.enums.AccountStatusEnum;
import com.ncs.single.mvc.enums.RegistTypeEnum;
import com.ncs.single.mvc.enums.RoleTypeEnum;
import com.ncs.single.mvc.vo.LoginUserVo;
import com.ncs.single.mvc.vo.RegistForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author dushitaoyuan
 * @desc 账户逻辑帮助
 * @date 2019/12/17
 */
@Service
public class AccountServiceHelper {
    @Autowired
    UserDao userDao;
    @Transactional(readOnly = true)
    public LoginUserVo buildLoginUserVo(AccountEntity accountEntity) {
        return new LoginUserVo(accountEntity, userDao.findById(accountEntity.getBindUser()));
    }
    public AccountEntity buildAccountEntity(RegistForm registForm) {
        AccountEntity accountEntity=new AccountEntity();
        accountEntity.setId(IdGenUtil.genLongId());
        RegistTypeEnum registType = RegistTypeEnum.type(registForm.getRegistType());
        switch (registType){
            case PHONE_REGIST: accountEntity.setPhone(registForm.getPhone());break;
            case EMAIL_REGIST: accountEntity.setEmail(registForm.getEmail());break;
            case USERNAME_REGIST: accountEntity.setUsername(registForm.getUsername());break;
        }
        accountEntity.setPassword(PasswordUtil.passwordEncode(registForm.getPassword()));
        accountEntity.setStatus(AccountStatusEnum.NORMAL.code);
        accountEntity.setType(RoleTypeEnum.COMMON.code);
        return accountEntity;
    }
}
