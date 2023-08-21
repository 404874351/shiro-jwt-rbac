package com.zjc.shiro.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginVo {
    /**
     * 用户名
     */
    private String username;
    /**
     * 密码
     */
    private String password;
}
