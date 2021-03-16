package com.taoyuanx.demo.service.impl;

import com.taoyuanx.demo.entity.UserEntity;
import com.taoyuanx.demo.mapper.UserMapper;
import com.taoyuanx.demo.service.UserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author taoyuan
 * @since 2020-09-08
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, UserEntity> implements UserService {

}
