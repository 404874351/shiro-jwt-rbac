package com.zjc.shiro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjc.shiro.entity.User;
import com.zjc.shiro.vo.UserRegisterVo;


public interface UserService extends IService<User> {

    User getByUsername(String username);

    boolean register(UserRegisterVo userRegisterVo);

}
