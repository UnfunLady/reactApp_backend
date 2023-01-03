package com.example.webapp.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.webapp.bean.Eusers;
import com.example.webapp.bean.Users;

import java.util.Date;

public class SignToken {
    //过期时间设置
    private static final long EXPIRE_TIME = 60 * 60 * 1000;
    private static final String SING = "ZengYu";  //秘钥
    private static final String SING1 = "YuanGong";  //秘钥
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
//    验证token
    public static DecodedJWT verify(String token) {
        return JWT.require(Algorithm.HMAC256(SING)).build().verify(token);
    }

    //员工token
    public static String getTokenEmploye(Eusers euser) {
        String[] claims = {euser.getUsername(), euser.getPassword()};
        String token = "";
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        token = JWT.create().withAudience(euser.getUsername())
                .withAudience(euser.getPassword())
                .withExpiresAt(date)
                .withArrayClaim("userInfo", claims)
                .sign(Algorithm.HMAC256(SING1));
        return token;
    }

    public static DecodedJWT verifyE(String token) {
        return JWT.require(Algorithm.HMAC256(SING1)).build().verify(token);
    }
}
