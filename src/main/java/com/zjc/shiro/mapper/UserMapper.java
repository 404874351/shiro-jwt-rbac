package com.zjc.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.shiro.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserMapper extends BaseMapper<User> {

    boolean bindUserRole(Integer userId, Integer roleId);
}
