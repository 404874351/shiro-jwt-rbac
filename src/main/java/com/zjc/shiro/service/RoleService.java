package com.zjc.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.shiro.entity.Role;

import java.util.List;

public interface RoleService extends IService<Role> {

    List<Role> listByUserId(Integer userId);

}
