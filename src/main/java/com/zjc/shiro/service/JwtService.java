package com.zjc.shiro.service;


import io.jsonwebtoken.Claims;

import java.util.List;
import java.util.Map;

public interface JwtService {

    long getMaxAge();

    String createToken(String username, Map<String, Object> map);

    Claims parseToken(String jwt);

    boolean isTokenExpired(Claims claims);

    String getTokenInRedis(String username);

    List<String> getAuthorityInRedis(String username);

    boolean setTokenAndAuthorityInRedis(String username, String token, List<Object> authorities, long expire);

    void delTokenAndAuthorityInRedis(String username);

}
