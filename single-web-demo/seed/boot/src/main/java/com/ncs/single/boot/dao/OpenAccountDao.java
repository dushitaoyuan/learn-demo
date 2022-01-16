package com.ncs.single.boot.dao;

import com.ncs.single.boot.entity.OpenAccountEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * api权限设计-开放账户表 Mapper 接口
 * </p>
 *
 * @author NCS
 * @since 2019-12-20
 */
@Mapper
public interface OpenAccountDao extends BaseMapper<OpenAccountEntity> {

}
