package com.zjc.shiro.dto;

import lombok.Data;

import java.util.List;

/**
 * 用户与关联角色列表，用于授权
 */
@Data
public class UserRoleDto {
    /**
     * 用户 id
     */
    private Integer id;
    /**
     * 用户名
     */
    private String username;
    /**
     * 用户对应的角色代码列表
     */
    private List<String> roleList;
}
