package com.ncs.sprinbase.core.dao;

import org.apache.ibatis.annotations.Param;

import com.ncs.sprinbase.core.entity.SimpleUserEntity;

/**
 * @author dushitaoyuan
 * @desc demo
 * @date 2019/12/17
 */
public interface SimpleUserDao {
    SimpleUserEntity findById(@Param("id") Long id);

    SimpleUserEntity findByUserName(@Param("username") String username);
}


