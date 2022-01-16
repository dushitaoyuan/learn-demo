package com.ncs.single.boot.service.impl;

import com.ncs.single.boot.entity.OpenAccountEntity;
import com.ncs.single.boot.dao.OpenAccountDao;
import com.ncs.single.boot.service.OpenAccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * api权限设计-开放账户表 服务实现类
 * </p>
 *
 * @author NCS
 * @since 2019-12-20
 */
@Service
@Transactional
public class OpenAccountServiceImpl extends ServiceImpl<OpenAccountDao, OpenAccountEntity> implements OpenAccountService {

}
