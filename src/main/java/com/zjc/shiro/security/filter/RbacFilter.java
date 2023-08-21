package com.zjc.shiro.security.filter;

import cn.hutool.core.collection.CollUtil;
import com.zjc.shiro.dto.PermissionRoleDto;
import com.zjc.shiro.dto.UserRoleDto;
import com.zjc.shiro.entity.BaseEntity;
import com.zjc.shiro.entity.Permission;
import com.zjc.shiro.enums.StateCodeMsg;
import com.zjc.shiro.service.PermissionService;
import com.zjc.shiro.vo.ResponseResult;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.web.filter.PathMatchingFilter;
import org.springframework.util.AntPathMatcher;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RbacFilter extends PathMatchingFilter {
    /**
     * 全局权限信息列表
     */
    public static List<PermissionRoleDto> permissionRoleDtoList;

    /**
     * 路径匹配器
     */
    public static AntPathMatcher antPathMatcher = new AntPathMatcher();

    private PermissionService permissionService;

    public RbacFilter(PermissionService permissionService) {
        this.permissionService = permissionService;
        if (permissionRoleDtoList == null) {
            this.loadDataSource();
        }
    }

    /**
     * 加载权限信息
     */
    public void loadDataSource() {
        permissionRoleDtoList = permissionService.permissionRoleDtoList();
    }

    /**
     * 清空权限信息，在权限被修改后调用
     */
    public static void clearDataSource() {
        permissionRoleDtoList = null;
    }

    /**
     * 资源授权
     */
    @Override
    protected boolean onPreHandle(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        // 获取用户权限列表
        UserRoleDto user = (UserRoleDto) SecurityUtils.getSubject().getPrincipal();
        List<String> userAuthorities = user.getRoleList();
        // 获取请求url
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String url = httpServletRequest.getRequestURI();

        // 检查每一个接口权限，寻找与当前请求路径匹配的对象
        for (PermissionRoleDto permissionRoleDto : permissionRoleDtoList) {
            if (antPathMatcher.match(permissionRoleDto.getUrl(), url)) {
                // 本项目权限配置中的*全部指向id，对于误匹配page等情况，需要单独处理
                if (antPathMatcher.extractPathWithinPattern(permissionRoleDto.getUrl(), url).matches("[\\D]+")) {
                    continue;
                }

                // 可支持匿名访问，则放行
                if (permissionRoleDto.getAnonymous() == Permission.ANONYMOUS_ENABLE) {
                    return true;
                }

                // 接口匹配成功，且不允许匿名访问，获取对应的授权角色列表
                List<String> urlAuthorities = permissionRoleDto.getRoleList().stream().map(item -> {
                    return item.getCode();
                }).collect(Collectors.toList());
                // 用户无任何授权，接口不开放任何角色，或已被禁用，则拒绝访问
                if (CollUtil.isEmpty(userAuthorities)
                        || CollUtil.isEmpty(urlAuthorities)
                        || permissionRoleDto.getDeleted() == BaseEntity.ENTITY_DEACTIVATED) {
                    onAccessDenied(response);
                    return false;
                }

                // 检查请求路径允许的每一个角色，如与用户授权的角色之一匹配，则通过授权
                for (String urlAuthority : urlAuthorities) {
                    if (userAuthorities.contains(urlAuthority)) {
                        return true;
                    }
                }

                // 没有授权，则拒绝访问
                onAccessDenied(response);
                return false;
            }
        }

        // 如没有在循环中退出，则说明当前接口无权限配置，一律放行
        // 如具体业务安全性要求较高，也可一律拒绝
        return true;
    }

    /**
     * 访问无权限回调
     */
    private void onAccessDenied(ServletResponse response) {
        try {
            ResponseResult res = ResponseResult.fail(StateCodeMsg.ACCESS_DENIED, null);
            ResponseResult.outputSuccessResult((HttpServletResponse) response, res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
