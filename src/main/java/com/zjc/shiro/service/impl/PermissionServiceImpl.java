package com.zjc.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.shiro.dto.PermissionRoleDto;
import com.zjc.shiro.entity.Permission;
import com.zjc.shiro.mapper.PermissionMapper;
import com.zjc.shiro.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {

    @Autowired
    private PermissionMapper permissionMapper;

    @Override
    public List<PermissionRoleDto> permissionRoleDtoList() {
        return permissionMapper.permissionRoleDtoList();
    }
}
