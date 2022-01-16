package com.ncs.single.mvc.dao;

import com.ncs.single.mvc.dto.AccountDTO;
import com.ncs.single.mvc.entity.AccountEntity;
import com.ncs.single.mvc.vo.AccountQueryVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author dushitaoyuan
 * @desc demo测试
 * @date 2019/12/17
 */
public interface AccountDao {
    AccountEntity findByPhone(@Param("phone") String phone);
    AccountEntity findByEmail(@Param("email") String email);
    AccountEntity findByUsername(@Param("username") String username);

    void saveAccount(AccountEntity accountEntity);
    void updateAccountStatus(@Param("id") Long id,@Param("accountStatus")Integer accountStatus);

    List<AccountEntity> selectQuery(AccountQueryVo query);

    AccountEntity getById(@Param("id")Long id);
    //更新
    void update(AccountEntity accountEntity);
    void delete(@Param("id")Long id);
}
