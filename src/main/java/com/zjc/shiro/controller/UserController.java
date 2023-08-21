package com.zjc.shiro.controller;

import com.zjc.shiro.service.UserService;
import com.zjc.shiro.vo.UserRegisterVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@Transactional(rollbackFor={Exception.class})
@RestController()
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册，该接口应该配置为白名单接口
     */
    @PostMapping("/register")
    public boolean register(@Validated UserRegisterVo userRegisterVo) {
        return userService.register(userRegisterVo);
    }

}
