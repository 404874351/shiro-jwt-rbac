package com.zjc.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.shiro.dto.PermissionRoleDto;
import com.zjc.shiro.entity.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PermissionMapper extends BaseMapper<Permission> {

    List<PermissionRoleDto> permissionRoleDtoList();
}
