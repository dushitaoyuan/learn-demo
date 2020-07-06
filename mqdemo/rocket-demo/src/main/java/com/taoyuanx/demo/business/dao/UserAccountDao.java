package com.taoyuanx.demo.business.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.taoyuanx.demo.business.entity.UserAccountEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

/**
 * @author dushitaoyuan
 * @date 2020/7/6
 */
public interface UserAccountDao extends BaseMapper<UserAccountEntity> {
    @Update("update user_account set money=money+#{money} where id=#{id} ")
    int addMoney(@Param("id") Long id, @Param("money") Double money);

    @Update("update user_account set money=money-#{money} where id=#{id} and money>#{money} ")
    int subMoney(@Param("id") Long id, @Param("money") Double money);
}
