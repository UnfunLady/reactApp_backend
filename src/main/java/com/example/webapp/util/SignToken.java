package com.example.webapp.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.webapp.bean.Users;

import java.util.Date;

public class SignToken {
    //过期时间设置
    private static final long EXPIRE_TIME = 60 * 60 * 1000;
    private static final String SING = "ZengYu";  //秘钥
    //    生成token
    public static String getToken(Users user) {
        String[] claims = {user.getUsername(), user.getPassword()};
        String token = "";
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        token = JWT.create().withAudience(user.getUsername())
                .withAudience(user.getPassword())
                .withExpiresAt(date)
                .withArrayClaim("userInfo", claims)
                .sign(Algorithm.HMAC256(SING));
        return token;
    }

    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }
}
