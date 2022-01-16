package com.ncs.sprinbase.core.service.impl;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ncs.sprinbase.core.dao.SimpleUserDao;
import com.ncs.sprinbase.core.dto.SimpleUserDTO;
import com.ncs.sprinbase.core.entity.SimpleUserEntity;
import com.ncs.sprinbase.core.service.SimpleUserService;
import com.ncs.sprinbase.core.utils.CBeanMapper;

/**
 * @author dushitaoyuan
 * @date 2019/12/27
 */
@Service
@Transactional
public class SimpleUserServiceImpl implements SimpleUserService {
    @Autowired
    SimpleUserDao simpleUserDao;

    @Override
    public SimpleUserDTO findById(Long id) {
        SimpleUserEntity user = simpleUserDao.findById(id);

        if (Objects.nonNull(user)) {
            return CBeanMapper.map(user, SimpleUserDTO.class);
        }

        return null;
    }

    @Override
    public SimpleUserDTO findByUserName(String username) {
        SimpleUserEntity user = simpleUserDao.findByUserName(username);

        if (Objects.nonNull(user)) {
            return CBeanMapper.map(user, SimpleUserDTO.class);
        }

        return null;
    }
}

