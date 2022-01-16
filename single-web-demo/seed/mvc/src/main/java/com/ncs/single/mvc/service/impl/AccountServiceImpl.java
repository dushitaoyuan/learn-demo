package com.ncs.single.mvc.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.ncs.commons.bean.CBeanMapper;
import com.ncs.commons.utils.PasswordUtil;
import com.ncs.single.mvc.commons.SessionConstants;
import com.ncs.single.mvc.dao.AccountDao;
import com.ncs.single.mvc.dto.AccountDTO;
import com.ncs.single.mvc.dto.PageDTO;
import com.ncs.single.mvc.entity.AccountEntity;
import com.ncs.single.mvc.enums.AccountStatusEnum;
import com.ncs.single.mvc.enums.LoginTypeEnum;
import com.ncs.single.mvc.enums.RegistTypeEnum;
import com.ncs.single.mvc.exception.ServiceException;
import com.ncs.single.mvc.service.AccountService;
import com.ncs.single.mvc.utils.SessionUserUtil;
import com.ncs.single.mvc.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author dushitaoyuan
 * @desc 账户实现
 * @date 2019/12/17
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
    @Autowired
    AccountDao accountDao;

    @Autowired
    AccountServiceHelper accountServiceHelper;

    @Override
    public LoginUserVo login(LoginForm loginForm) {

        if (!LoginTypeEnum.supportLogin(loginForm.getLoginType())) {
            throw new ServiceException(1, "登录方式不支持");
        }
        //验证码 session中或redis中
        String vafyCode = SessionUserUtil.getAttribute(SessionConstants.LOGIN_VAFY_CODE,SessionUserUtil.getCurrentRequest());
        if (StringUtils.isEmpty(loginForm.getVafyCode())||(StringUtils.isNotEmpty(loginForm.getVafyCode()) && vafyCode.equalsIgnoreCase(loginForm.getVafyCode()))) {
            throw new ServiceException(2, "验证码不正确");
        }

        LoginTypeEnum loginType = LoginTypeEnum.type(loginForm.getLoginType());
        AccountEntity accountEntity = null;
        switch (loginType) {
            case PHONE_LOGIN: {
                accountEntity = accountDao.findByPhone(loginForm.getPhone());
            }
            break;
            case EMAIL_LOGIN: {
                accountEntity = accountDao.findByEmail(loginForm.getEmail());
            }
            break;
            case USERNAME_LOGIN: {
                accountEntity = accountDao.findByUsername(loginForm.getUsername());
            }
            break;
        }
        if (Objects.isNull(accountEntity)) {
            throw new ServiceException(3, "账户不存在");
        }
        if (accountEntity.getStatus().equals(AccountStatusEnum.DELETED)) {
            throw new ServiceException(3, "账户不存在");
        }
        if (accountEntity.getStatus().equals(AccountStatusEnum.LOCKED)) {
            throw new ServiceException(4, "账户已锁定,请申诉解锁账户");
        }
        if (accountEntity.getStatus().equals(AccountStatusEnum.ABNORMAL)) {
            throw new ServiceException(5, "账户处于异常状态,请联系管理员解除异常状态");
        }
        if (Objects.nonNull(loginForm.getPassword()) && PasswordUtil.passwordEqual(accountEntity.getPassword(), loginForm.getPassword())) {
            throw new ServiceException(6, "账户密码不匹配");
        }
        // account 转 LoginUserVo
        return accountServiceHelper.buildLoginUserVo(accountEntity);
    }


    @Override
    public void regist(RegistForm registForm) {

        if (!RegistTypeEnum.supportRegist(registForm.getRegistType())) {
            throw new ServiceException(1, "登录方式不支持");
        }
        //验证码 session中或redis中
        String vafyCode = SessionUserUtil.getAttribute(SessionConstants.REGIST_VAFY_CODE,SessionUserUtil.getCurrentRequest());
        if (StringUtils.isEmpty(registForm.getVafyCode())||(StringUtils.isNotEmpty(registForm.getVafyCode()) && !vafyCode.equalsIgnoreCase(registForm.getVafyCode()))) {
            throw new ServiceException(2, "验证码不正确");
        }
        if (StringUtils.equals(registForm.getPassword(), registForm.getConfirmPassword())) {
            throw new ServiceException(3, "密码和确认密码不一致");
        }
        RegistTypeEnum registType = RegistTypeEnum.type(registForm.getRegistType());
        AccountEntity accountEntity = null;
        switch (registType) {
            case PHONE_REGIST: {
                accountEntity = accountDao.findByPhone(registForm.getPhone());
            }
            break;
            case EMAIL_REGIST: {
                accountEntity = accountDao.findByEmail(registForm.getEmail());
            }
            break;
            case USERNAME_REGIST: {
                accountEntity = accountDao.findByUsername(registForm.getUsername());
            }
            break;
        }
        if (Objects.nonNull(accountEntity)) {
            AccountStatusEnum accountStatusEnum = AccountStatusEnum.type(accountEntity.getStatus());
            if (accountStatusEnum.equals(AccountStatusEnum.DELETED)) {
                //激活账户
                accountDao.updateAccountStatus(accountEntity.getId(), AccountStatusEnum.NORMAL.code);
                return;
            }
            throw new ServiceException(4, "账户已存在");
        }
        accountEntity = accountServiceHelper.buildAccountEntity(registForm);
        accountDao.saveAccount(accountEntity);
    }

    @Override
    public PageDTO<AccountDTO> pageList(PageVo<AccountQueryVo> pageVo) {
        PageHelper.startPage(pageVo.getPageNum(), pageVo.getPageSize());
        List<AccountEntity> list = accountDao.selectQuery(pageVo.getQuery());
        ;
        return PageDTO.changeListType(CBeanMapper.mapList(list, AccountDTO.class), (Page) list);
    }

    @Override
    public List<AccountDTO> list(AccountQueryVo queryVo) {
        List<AccountEntity> list = accountDao.selectQuery(queryVo);
        return CBeanMapper.mapList(list, AccountDTO.class);
    }

    @Override
    public Optional<AccountDTO> getById(Long id) {
        AccountEntity accountEntity = accountDao.getById(id);
        return Objects.nonNull(accountEntity)?Optional.of(CBeanMapper.map(accountEntity,AccountDTO.class)):Optional.empty();
    }

    @Override
    public void update(AccountDTO accountDTO) {
        Optional<AccountDTO> existAccount = getById(accountDTO.getId());
        existAccount.orElseThrow(()->{
            return new ServiceException("账户不存在,无法更新");
        });
        accountDao.update(accountDTO);
    }

    @Override
    public void save(AccountDTO accountDTO) {
        accountDao.saveAccount(CBeanMapper.map(accountDTO,AccountEntity.class));
    }

    @Override
    public void delete(Long id) {
        accountDao.delete(id);
    }

    @Override
    public boolean exist(RegistForm registForm) {
        RegistTypeEnum registType = RegistTypeEnum.type(registForm.getRegistType());
        AccountEntity accountEntity = null;
        switch (registType) {
            case PHONE_REGIST: {
                accountEntity = accountDao.findByPhone(registForm.getPhone());
            }
            break;
            case EMAIL_REGIST: {
                accountEntity = accountDao.findByEmail(registForm.getEmail());
            }
            break;
            case USERNAME_REGIST: {
                accountEntity = accountDao.findByUsername(registForm.getUsername());
            }
            break;
        }
        if (Objects.nonNull(accountEntity)&&!AccountStatusEnum.DELETED.equals(accountEntity.getStatus())) {
            return  true;
        }
        return false;
    }


}
