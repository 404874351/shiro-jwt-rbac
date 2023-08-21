package com.zjc.shiro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjc.shiro.entity.Role;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> selectListByUserId(Integer userId);


}
