package com.taoyuanx.demo.mapper;

import com.taoyuanx.demo.entity.PermissionEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author taoyuan
 * @since 2020-09-08
 */
public interface PermissionMapper extends BaseMapper<PermissionEntity> {

    List<PermissionEntity> listPermissionByUserId(Long id);
}
