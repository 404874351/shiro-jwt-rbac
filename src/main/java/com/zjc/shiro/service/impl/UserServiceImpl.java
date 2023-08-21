package com.zjc.shiro.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjc.shiro.entity.Role;
import com.zjc.shiro.entity.User;
import com.zjc.shiro.mapper.UserMapper;
import com.zjc.shiro.service.UserService;
import com.zjc.shiro.vo.UserRegisterVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 通过用户名获取用户
     * @param username
     * @return
     */
    @Override
    public User getByUsername(String username) {
        return this.getOne(new QueryWrapper<User>().eq("username", username));
    }

    /**
     * 用户注册，默认分配普通用户角色
     * @param userRegisterVo
     * @return
     */
    @Override
    public boolean register(UserRegisterVo userRegisterVo) {
        // 添加用户，密码加密
        User user = new User();
        BeanUtil.copyProperties(userRegisterVo, user);
        user.setUsername(user.getPhone());
        user.setPassword(SecureUtil.md5(userRegisterVo.getPassword()));
        boolean saveUserRes = this.save(user);
        // 绑定角色
        boolean bindRoleRes = userMapper.bindUserRole(user.getId(), Role.USER_ID);

        return saveUserRes && bindRoleRes;
    }

}
