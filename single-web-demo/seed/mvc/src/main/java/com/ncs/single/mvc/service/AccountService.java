package com.ncs.single.mvc.service;

import com.ncs.single.mvc.dto.AccountDTO;
import com.ncs.single.mvc.dto.PageDTO;
import com.ncs.single.mvc.entity.AccountEntity;
import com.ncs.single.mvc.vo.*;

import java.util.List;
import java.util.Optional;

/**
 * @author dushitaoyuan
 * @desc 账户逻辑层
 * @date 2019/12/17
 */
public interface AccountService {
    //登录
    LoginUserVo login(LoginForm loginForm);
    //注册
    void regist(RegistForm registForm);

    //账户增删改查
    PageDTO<AccountDTO> pageList(PageVo<AccountQueryVo> accountQueryVoPageVo);

    List<AccountDTO> list(AccountQueryVo queryVo);

    Optional<AccountDTO> getById(Long appSysId);

    void update(AccountDTO accountDTO);

    void save(AccountDTO accountDTO);

    void delete(Long id);

    boolean exist(RegistForm registForm);
}
