package com.example.webapp.controller;

import com.example.webapp.bean.Users;
import com.example.webapp.service.usersService;
import com.example.webapp.util.LoginToken;
import com.example.webapp.util.SignToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

@RestController
public class userLoginController {
    @Autowired
    usersService usersService;
    @PostMapping("/api/login")
    public Map loginController(@RequestParam Map<String, String> loginData) {
        Map<String, Object> map = new HashMap<>();
        Users users = usersService.getUserByNameWord(loginData.get("username"), loginData.get("password"));
        if (users != null && loginData.get("username") != "" && loginData.get("password") != "") {
            String token = SignToken.getToken(users);
            map.put("code", 200);
            map.put("msg", "账号密码验证成功！");
            map.put("Info", users);
            map.put("token", token);
            return map;
        } else {
            map.put("code", 201);
            map.put("msg", "账号密码验证失败！");
            return map;
        }
    }

//    上传头像
//    @PostMapping("/uploadImg")
//    @CrossOrigin
//    public String uploadImg(@RequestParam("file") MultipartFile file,@RequestParam("baseUrl")String baseUrl) throws IOException {
//        BASE64Decoder decoder = new BASE64Decoder();
//       File tempFile = new File("C:\\Users\\Administrator\\Desktop\\test\\webapp\\src\\main\\java\\com\\example\\webapp\\images\\"+"3.jpg");
//        OutputStream out;
//        out = new FileOutputStream(tempFile);
//        // Base64解码
//        byte[] b = decoder.decodeBuffer(baseUrl);
//        for (int i = 0; i < b.length; ++i) {
//            if (b[i] < 0) {// 调整异常数据
//                b[i] += 256;
//            }
//        }
//        out.write(b);
//        out.write(decoder.decodeBuffer(baseUrl));
//        out.flush();
//        out.close();
//        return "main";
//    }



    @LoginToken
    @GetMapping("/testJwt")
    public String testJWT() {

        return "trueToken";
    }
}
