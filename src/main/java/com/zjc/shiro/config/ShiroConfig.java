package com.zjc.shiro.config;

import com.zjc.shiro.security.filter.JwtFilter;
import com.zjc.shiro.security.filter.RbacFilter;
import com.zjc.shiro.security.realm.JwtRealm;
import com.zjc.shiro.service.PermissionService;
import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.mgt.DefaultSubjectDAO;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    /**
     * 路径权限拦截白名单
     */
    public static final String[] URL_WHITE_LIST = {
            // 登录接口
            "/login/**",
            // 静态资源
            "/*.html",
            "/*.js",
            "/*.css",
    };

    @Autowired
    private JwtRealm jwtRealm;

    @Autowired
    private PermissionService permissionService;

    @Bean
    public DefaultWebSecurityManager defaultWebSecurityManager() {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        // 配置自定义 realm
        ArrayList<Realm> realms = new ArrayList<>();
        realms.add(jwtRealm);
        securityManager.setRealms(realms);

        // 关闭 session
        DefaultSubjectDAO subjectDAO = new DefaultSubjectDAO();
        DefaultSessionStorageEvaluator sessionStorageEvaluator = new DefaultSessionStorageEvaluator();
        sessionStorageEvaluator.setSessionStorageEnabled(false);
        subjectDAO.setSessionStorageEvaluator(sessionStorageEvaluator);
        securityManager.setSubjectDAO(subjectDAO);
        // 关闭 rememberMe
        securityManager.setRememberMeManager(null);

        return securityManager;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
        ShiroFilterFactoryBean filterFactoryBean = new ShiroFilterFactoryBean();
        // 配置安全管理器
        filterFactoryBean.setSecurityManager(securityManager);

        // 添加自定义过滤器
        // 此处只能使用 new 而不能通过容器注入，否则自带的 anon 会失效
        Map<String, Filter> filters = new LinkedHashMap<>();
        filters.put("jwt-authentication", new JwtFilter());
        filters.put("rbac-authorization", new RbacFilter(permissionService));
        filterFactoryBean.setFilters(filters);

        // 配置拦截路径
        Map<String, String> filterMap = new LinkedHashMap<>();
        // 白名单不过滤
        for (String url : URL_WHITE_LIST) {
            filterMap.put(url, "anon");
        }
        // 其余路径，全部拦截
        // 先认证，后授权，多个过滤器用逗号分隔
        filterMap.put("/**", "jwt-authentication,rbac-authorization");

        filterFactoryBean.setFilterChainDefinitionMap(filterMap);

        return filterFactoryBean;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator = new DefaultAdvisorAutoProxyCreator();
        // shiro 默认会在 @Controller 方法上配置 @RequiresRole @RequiresAuthentication 等认证与权限控制
        // 这导致了如果在没有配置任何认证和权限逻辑时，所有接口报404
        // 所以，在这里设置 setUsePrefix(true) 来解决
        defaultAdvisorAutoProxyCreator.setUsePrefix(true);
        return defaultAdvisorAutoProxyCreator;
    }


}
