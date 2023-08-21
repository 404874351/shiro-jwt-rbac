package com.zjc.shiro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.shiro.entity.Role;
import com.zjc.shiro.mapper.RoleMapper;
import com.zjc.shiro.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleMapper;

    /**
     * 获取用户对应的角色列表
     * @param userId
     * @return
     */
    @Override
    public List<Role> listByUserId(Integer userId) {
        return roleMapper.selectListByUserId(userId);
    }

}
