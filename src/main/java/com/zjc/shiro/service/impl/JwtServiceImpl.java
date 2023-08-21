package com.zjc.shiro.service.impl;

import com.zjc.shiro.service.JwtService;
import com.zjc.shiro.utils.RedisUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class JwtServiceImpl implements JwtService {

    @Value("${auth.jwt.max-age}")
    private long maxAge;

    @Value("${auth.jwt.secret}")
    private String secret;

    @Autowired
    private RedisUtils redisUtils;

    @Override
    public long getMaxAge() {
        return maxAge;
    }

    public static final String REDIS_TOKEN_KEY = "token";
    public static final String REDIS_AUTHORITY_KEY = "authority";

    /**
     * 创建token，携带username和自定义数据
     * @param username 用户名
     * @param map 自定义数据
     * @return
     */
    @Override
    public String createToken(String username, Map<String, Object> map) {
        Date currentDate = new Date();
        Date expireDate = new Date(currentDate.getTime() + 1000 * maxAge);
        String jwt = Jwts.builder()
                .setClaims(map)
                .setHeaderParam("typ", "JWT")
                .setSubject(username)
                .setIssuedAt(currentDate)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        return jwt;
    }

    /**
     * 解析token
     * @param jwt token
     * @return
     */
    @Override
    public Claims parseToken(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims;
        } catch (Exception e) {
            log.error(e.toString());
            return null;
        }
    }

    /**
     * token过期判断
     * @param claims
     * @return
     */
    @Override
    public boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    /**
     * 获取token缓存
     * @param username 用户名
     * @return token
     */
    @Override
    public String getTokenInRedis(String username) {
        return (String) redisUtils.get(username + ":" + REDIS_TOKEN_KEY);

    }

    @Override
    public List<String> getAuthorityInRedis(String username) {
        List<Object> list = redisUtils.lGet(username + ":" + REDIS_AUTHORITY_KEY, 0, -1);
        return list.stream().map(obj -> {
            return obj.toString();
        }).collect(Collectors.toList());
    }

    @Override
    public boolean setTokenAndAuthorityInRedis(String username, String token, List<Object> authorities, long expire) {
        // 每次仅保留最新内容，清空之前的内容
        this.delTokenAndAuthorityInRedis(username);

        if(!redisUtils.set(username + ":" + REDIS_TOKEN_KEY, token, expire)) {
            return false;
        }

        return redisUtils.lSet(username + ":" + REDIS_AUTHORITY_KEY, authorities, expire);
    }

    /**
     * 删除用户token和权限列表
     * @param username 用户名
     */
    @Override
    public void delTokenAndAuthorityInRedis(String username) {
        redisUtils.del(username + ":" + REDIS_TOKEN_KEY);
        redisUtils.del(username + ":" + REDIS_AUTHORITY_KEY);
    }

}
