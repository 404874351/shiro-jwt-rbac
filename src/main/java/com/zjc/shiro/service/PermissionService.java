package com.zjc.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.shiro.dto.PermissionRoleDto;
import com.zjc.shiro.entity.Permission;

import java.util.List;

public interface PermissionService extends IService<Permission> {

    List<PermissionRoleDto> permissionRoleDtoList();


}
