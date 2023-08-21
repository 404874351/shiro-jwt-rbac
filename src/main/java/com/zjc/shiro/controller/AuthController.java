package com.zjc.shiro.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.SecureUtil;
import com.zjc.shiro.dto.UserRoleDto;
import com.zjc.shiro.entity.Role;
import com.zjc.shiro.entity.User;
import com.zjc.shiro.enums.StateCodeMsg;
import com.zjc.shiro.exception.LoginException;
import com.zjc.shiro.service.JwtService;
import com.zjc.shiro.service.RoleService;
import com.zjc.shiro.service.UserService;
import com.zjc.shiro.vo.LoginVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Transactional(rollbackFor={Exception.class})
@RestController()
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private JwtService jwtService;

    /**
     * 用户名密码登录
     */
    @PostMapping("/login")
    public Object loginByPassword(@Validated LoginVo loginVo) {
        // 检查用户名是否为空
        if (StrUtil.isEmpty(loginVo.getUsername())) {
            throw new LoginException(StateCodeMsg.USERNAME_NULL);
        }
        User user = userService.getByUsername(loginVo.getUsername());
        // 检查密码
        if (user == null || !user.getPassword().equals(SecureUtil.md5(loginVo.getPassword()))) {
            throw new LoginException(StateCodeMsg.USERNAME_OR_PASSWORD_ERROR);
        }
        // 检查用户是否停用
        if (user.isEntityDeactivated()) {
            throw new LoginException(StateCodeMsg.USER_DEACTIVATED);
        }

        // 认证通过，完成后续处理
        return this.onLoginSuccess(user);
    }

    /**
     * 其他登录方式，可自由添加
     */
    @PostMapping("/login/other")
    public Object loginByOther() {
        return true;
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public boolean logout() {
        // 获取用户认证信息
        UserRoleDto user = (UserRoleDto) SecurityUtils.getSubject().getPrincipal();

        // 完成退出登录处理
        return this.onLogoutSuccess(user.getUsername());
    }

    /**
     * 登录成功回调，保存登录态到缓存
     */
    private Object onLoginSuccess(User user) {
        // 获取用户的权限列表
        String username = user.getUsername();
        List<Role> roles = roleService.listByUserId(user.getId());
        List<Object> authorities = roles.stream().map(item -> {
            return item.getCode();
        }).collect(Collectors.toList());

        // 创建token，添加自定义载荷，暂定id
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        String token = jwtService.createToken(username, map);

        // redis存储token和权限列表
        boolean redisRes = jwtService.setTokenAndAuthorityInRedis(username, token, authorities, jwtService.getMaxAge());
        if(!redisRes) {
            // redis清空出错，则清空缓存，返回错误信息
            jwtService.delTokenAndAuthorityInRedis(username);
            throw new LoginException(StateCodeMsg.LOGIN_FAIL);
        }

        // 返回token到客户端，作为登录凭证
        Map<String, Object> data = new HashMap<>();
        data.put("token", token);
        return data;
    }

    /**
     * 退出登录回调，删除缓存中的登录态
     */
    private boolean onLogoutSuccess(String username) {
        // 清空 redis 用户缓存，防止 token 误用
        jwtService.delTokenAndAuthorityInRedis(username);

        return true;
    }

}
