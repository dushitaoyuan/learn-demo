package com.ncs.sprinbase.core.service;

import com.ncs.sprinbase.core.dto.SimpleUserDTO;

/**
 * @author dushitaoyuan
 * @desc 账户逻辑层
 * @date 2019/12/17
 */
public interface SimpleUserService {
    SimpleUserDTO findById(Long id);

    SimpleUserDTO findByUserName(String username);
}


