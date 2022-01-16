package com.ncs.single.mvc.dao;

import com.ncs.single.mvc.entity.AccountEntity;
import com.ncs.single.mvc.entity.UserEntity;
import org.apache.ibatis.annotations.Param;

/**
 * @author dushitaoyuan
 * @desc demo测试
 * @date 2019/12/17
 */
public interface UserDao {
    UserEntity findById(@Param("id") Long id);
}
