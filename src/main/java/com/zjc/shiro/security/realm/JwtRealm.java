package com.zjc.shiro.security.realm;

import cn.hutool.core.util.StrUtil;
import com.zjc.shiro.dto.UserRoleDto;
import com.zjc.shiro.enums.StateCodeMsg;
import com.zjc.shiro.exception.TokenException;
import com.zjc.shiro.security.filter.JwtFilter;
import com.zjc.shiro.security.token.JwtToken;
import com.zjc.shiro.service.JwtService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class JwtRealm extends AuthorizingRealm {

    @Autowired
    private JwtService jwtService;

    /**
     * 指定生效范围
     */
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;
    }

    /**
     * 统一将授权逻辑放到过滤器，此处不再使用
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    /**
     * 使用 jwt 完成用户认证，获取用户信息
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 获取jwt
        String jwt = (String) authenticationToken.getCredentials();
        // 匿名访问，交由后续处理，因为某些路径可动态配置匿名访问
        if(StrUtil.isEmpty(jwt) || jwt.equals(JwtFilter.ANONYMOUS_TOKEN)) {
            return new SimpleAuthenticationInfo(new UserRoleDto(), jwt, getName());
        }
        // 实名访问，检查token是否非法或过期
        Claims claims = jwtService.parseToken(jwt);
        if(claims == null) {
            throw new TokenException(StateCodeMsg.TOKEN_ILLEGAL);
        }

        // 此时，token存在且可以被解析，需要结合缓存判定用户身份
        // 从redis获取 token 和 authorities
        String username = claims.getSubject();
        String jwtInRedis = jwtService.getTokenInRedis(username);
        // 判定缓存的token是否为空或不一致
        if(jwtInRedis == null) {
            throw new TokenException(StateCodeMsg.TOKEN_INVALID);
        }
        if(!jwtInRedis.equals(jwt)) {
            throw new TokenException(StateCodeMsg.TOKEN_ILLEGAL);
        }

        // 构造用户认证信息，保存到 principal
        UserRoleDto user = new UserRoleDto();
        user.setId(claims.get("id", Integer.class));
        user.setUsername(username);
        user.setRoleList(jwtService.getAuthorityInRedis(username));

        return new SimpleAuthenticationInfo(user, jwt, getName());
    }


}
