package com.taoyuanx.demo.service;

import com.taoyuanx.demo.dao.UserDao;
import com.taoyuanx.demo.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @date 2020/8/11
 */
@Service
public class UserService {
    @Autowired
    UserDao userDao;

    @Cacheable(cacheNames = "user", key = "#id", unless = "#result == null")
    @Transactional(readOnly = true)
    public UserEntity getById(Long id) {
        return userDao.selectById(id);
    }

    public void save(UserEntity user) {
        userDao.insert(user);
    }

    @CacheEvict(cacheNames = "user", key = "#user.id")
    public void update(UserEntity user) {
        userDao.updateById(user);
    }
}
