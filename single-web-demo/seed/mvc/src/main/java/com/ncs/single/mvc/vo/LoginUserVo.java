package com.ncs.single.mvc.vo;

import com.ncs.single.mvc.entity.AccountEntity;
import com.ncs.single.mvc.entity.UserEntity;
import com.ncs.single.mvc.enums.RoleTypeEnum;
import lombok.Data;

import java.util.Objects;

/**
 * @author dushitaoyuan
 * @desc 登录对象封装
 * @date 2019/12/17
 */
@Data
public class LoginUserVo {
    /**
     * 账户id，用户id
     */
    private Long accountId;
    private Long userId;

    private String name;

    private RoleTypeEnum role;

    public LoginUserVo(AccountEntity accountEntity, UserEntity userEntity){
        if(Objects.nonNull(accountEntity)){
            accountId=accountEntity.getId();
            role= RoleTypeEnum.roleType(accountEntity.getType());
        }
        if(Objects.nonNull(userEntity)){
            userId=userEntity.getId();
            name=userEntity.getName();
        }
    }
}
