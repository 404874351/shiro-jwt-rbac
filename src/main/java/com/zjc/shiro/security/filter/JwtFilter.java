package com.zjc.shiro.security.filter;

import cn.hutool.core.util.StrUtil;
import com.zjc.shiro.enums.StateCodeMsg;
import com.zjc.shiro.exception.TokenException;
import com.zjc.shiro.security.token.JwtToken;
import com.zjc.shiro.vo.ResponseResult;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtFilter extends AuthenticatingFilter {

    /**
     * header.Authorization 中存放的 token 前缀
     */
    public static final String TOKEN_PREFIX = "Bearer";
    /**
     * 匿名访问所用的临时token
     */
    public static final String ANONYMOUS_TOKEN = "anon";

    /**
     * 通过 jwt 完成身份认证
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        // 执行登录认证逻辑，移交 realm
        try {
            return this.executeLogin(request, response);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        return false;
    }


    /**
     * 身份认证失败
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException exception, ServletRequest request, ServletResponse response) {
        // 保存返回结果
        ResponseResult res;
        // 获取token异常状态
        TokenException tokenException = (TokenException) exception;
        StateCodeMsg tokenState = tokenException.getState();
        if(tokenState != null) {
            res = ResponseResult.fail(tokenState, null);
        } else {
            res = ResponseResult.fail(StateCodeMsg.AUTHENTICATION_FAILED, null);
        }
        try {
            ResponseResult.outputSuccessResult((HttpServletResponse) response, res);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return false;
    }

    @Override
    protected AuthenticationToken createToken(ServletRequest request, ServletResponse response) throws Exception {
        // 从 header 中获取认证信息
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        String jwt = this.getJwtFromAuthorization(httpServletRequest.getHeader("Authorization"));
        // 匿名访问，也要给一个临时token
        if (StrUtil.isEmpty(jwt)) {
            jwt = ANONYMOUS_TOKEN;
        }
        return new JwtToken(jwt);
    }

    /**
     * 通过请求头的 Authorization 获取 token
     */
    private String getJwtFromAuthorization(String authorization) {
        if(authorization == null) {
            return null;
        }
        // 去除固定前缀和空白符
        String jwt = authorization.replaceAll(TOKEN_PREFIX, "");
        jwt = jwt.replaceAll("\\s*", "");
        return jwt;
    }
}
