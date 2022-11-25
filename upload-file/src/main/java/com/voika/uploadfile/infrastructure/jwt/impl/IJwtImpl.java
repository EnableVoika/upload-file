package com.voika.uploadfile.infrastructure.jwt.impl;

import cn.hutool.json.JSONUtil;
import com.voika.uploadfile.infrastructure.jwt.IJwt;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Map;

public class IJwtImpl implements IJwt {

    /**
     * 密钥
     */
//    @Value("${my-config.jwt.secrity-key}")
    private String secret;

    /**
     * 有效期，单位秒
     * - 默认2周
     */
//    @Value("${my-config.jwt.default-expir-time}")
    private long defaultExpirTime;

    {
        System.out.println(secret);
        System.out.println(defaultExpirTime);

    }

    public IJwtImpl() {
    }

    public IJwtImpl(String secret, long defaultExpirTime) {
        this.secret = secret;
        this.defaultExpirTime = defaultExpirTime;
    }




    /**
     * 解析token
     * 从token中获取claim
     * 解析失败这个方法会抛出异常
     *
     * @return
     */
    @Override
    public Claims parse(String token) {
        return Jwts.parser()
                .setSigningKey(this.secret.getBytes())
                .parseClaimsJws(token)
                .getBody();
    }

    public Claims parse(String token, Class clazz) {
        return null;
    }

    /**
     * 获取token到期的时间时间
     * token几号、多久到期
     *
     * @param token token
     * @return 过期的那一刻
     */
    @Override
    public Date getExpir(String token) {
        return Jwts.parser()
                .setSigningKey(this.secret.getBytes())
                .parseClaimsJws(token)
                .getBody().getExpiration();
    }

    /**
     * 判断token是否过期
     *
     * @param token token
     * @return 已过期返回true，未过期返回false
     */
    @Override
    public Boolean isExpired(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(this.secret.getBytes())
                    .parseClaimsJws(token)
                    .getBody().getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            return true;
        }
        return false;
    }

    /**
     * 计算token的过期时间
     * 用于生成token时确定截止日期,作为参数传给创建token的方法
     *
     * @return 过期时间
     */
    private Date calculationExpirTime(long expire) {
        return new Date(System.currentTimeMillis() + expire * 1000);
    }

    /**
     * 为指定用户生成token
     *
     * @param claims 用户信息
     * @return token
     */
    @Override
    public String generateToken(Map<String, Object> claims) {
//        return generateToken(claims, Long.valueOf(defaultExpirTime));
        return generateToken(claims, defaultExpirTime);
    }

    @Override
    public String generateToken(Map<String, Object> claims, long expire) {
        Date expirationTime = this.calculationExpirTime(expire);
        byte[] keyBytes = secret.getBytes();
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);
        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(expirationTime)
                // 可以改成自定义的算法
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * @param claims json对象
     * @param expire 过期时间
     * @return
     */
    @Override
    public String generateToken(String claims, long expire) {
        Map map = JSONUtil.toBean(claims, Map.class);
        return generateToken(map, expire);
    }

    /**
     * @param claims json对象
     * @return
     */
    @Override
    public String generateToken(String claims) {
        Map map = JSONUtil.toBean(claims, Map.class);
        return generateToken(map);
    }

    @Override
    public String generateToken(Object obj) {
//        return generateToken(obj, Long.valueOf(defaultExpirTime));
        return generateToken(obj, defaultExpirTime);
    }

    @Override
    public String generateToken(Object obj, long expir) {
        String json = JSONUtil.toJsonStr(obj);
        Map jsonMap = JSONUtil.toBean(json, Map.class);
        return generateToken(jsonMap, expir);
    }

    /**
     * 判断token是否非法
     *
     * @param token token
     * @return 未过期返回true，否则返回false
     */
    @Override
    public void validateToken(String token) {
        Jwts.parser().setSigningKey(this.secret.getBytes()).parseClaimsJws(token);
    }

}
