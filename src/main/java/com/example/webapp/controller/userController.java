package com.example.webapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.webapp.bean.Depall;
import com.example.webapp.bean.Eusers;
import com.example.webapp.bean.Users;
import com.example.webapp.mapper.euserMapper;
import com.example.webapp.mapper.usersMapper;
import com.example.webapp.service.usersService;
import com.example.webapp.util.LoginToken;
import com.example.webapp.util.SignToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class userController {
    @Autowired
    usersMapper usersMapper;
    @Autowired
    usersService usersService;
    @Autowired
    euserMapper euserMapper;

    //    修改密码
    @LoginToken
    @PostMapping("/api/editPassword")
    public Map editPassword(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("nowPassword") == null || map.get("newPassword") == null || map.get("user") == null) {
            map1.put("code", 202);
            map1.put("msg", "修改密码失败：缺少重要参数");
        } else {
            String nowPassword = map.get("nowPassword").toString();
            String newPassword = map.get("newPassword").toString();
            String username = map.get("user").toString();
            if (newPassword.equals(nowPassword)) {
                map1.put("code", 202);
                map1.put("msg", "新旧密码不能相同！");
            } else {
//                判断原密码是否正确
                Users user = usersMapper.selectOne(new QueryWrapper<Users>().eq("username", username));
                if (user.getPassword().equals(nowPassword)) {
//                  原密码正确
                    user.setPassword(newPassword);
                    int update = usersMapper.update(user, new UpdateWrapper<Users>().eq("username", username));
                    if (update > 0) {
                        map1.put("code", 200);
                        map1.put("msg", "修改密码成功！");
                    }
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "旧密码错误！");
                }
            }
        }
        return map1;
    }

    //    修改信息 有头像
    @LoginToken
    @PostMapping("/api/editUserInfo")
    public Map editUserInfo(@RequestParam Map map, @RequestParam("file") MultipartFile file) throws IOException {
        Map map1 = new HashMap();
        if (map.get("nickname") == null || map.get("username") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            if (!file.isEmpty()) {
                String avatarName = "uploadUserAvatarZengYu" + (int) (Math.random() * 114514) + file.getOriginalFilename();
                // 设置存放文件路径
                String publicPath = System.getProperty("user.dir") + "/src/main/resources/images/Avatar/";
//               判断文件是否同名存在
                File isFile = new File("classpath:/images/Avatar/", avatarName);
                if (isFile.exists()) {
//                如果存在相同文件 则修改文件名再存
                    avatarName = "uploadUserAvatarZengYu" + (int) (Math.random() * 114515) + "SAFE" + file.getOriginalFilename();
//                执行修改操作
                    Users users = new Users();
                    users.setAvatar("http://127.0.0.1:8888/images/Avatar/" + avatarName);
                    users.setNickname(map.get("nickname").toString());
                    int update = usersService.getBaseMapper().update(users, new UpdateWrapper<Users>().eq("username", map.get("username")));
                    if (update > 0) {
                        map1.put("code", 200);
                        map1.put("msg", "修改用户信息成功！");
                        file.transferTo(new File(publicPath + avatarName));
                    } else {
                        map1.put("code", 202);
                        map1.put("msg", "修改用户信息失败!");
                    }
                } else {
                    Users users = new Users();
                    users.setAvatar("http://127.0.0.1:8888/images/Avatar/" + avatarName);
                    users.setNickname(map.get("nickname").toString());
                    int update = usersService.getBaseMapper().update(users, new UpdateWrapper<Users>().eq("username", map.get("username")));
                    if (update > 0) {
                        map1.put("code", 200);
                        map1.put("msg", "修改用户信息成功！");
                        file.transferTo(new File(publicPath + avatarName));
                    } else {
                        map1.put("code", 202);
                        map1.put("msg", "修改用户信息失败!");
                    }
                }

            } else {
                map1.put("code", 202);
                map1.put("msg", "缺少头像参数");
            }
        }
        return map1;
    }

    //    修改信息 无头像
    @LoginToken
    @PostMapping("/api/editUserInfoNoAvatar")
    public Map editUserInfoNoAvatar(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("nickname") == null || map.get("username") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            Users users = new Users();
            users.setNickname(map.get("nickname").toString());
            int update = usersService.getBaseMapper().update(users, new UpdateWrapper<Users>().eq("username", map.get("username")));
            if (update > 0) {
                map1.put("code", 200);
                map1.put("msg", "修改用户信息成功！");
            } else {
                map1.put("code", 202);
                map1.put("msg", "修改用户信息失败!");
            }
        }
        return map1;
    }

    //登陆
    @PostMapping("/api/login")
    public Map loginController(@RequestBody Map loginData) {
        Map<String, Object> map = new HashMap<>();
        if (loginData.get("username") == null || loginData.get("password") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            Users users = usersService.getUserByNameWord(loginData.get("username").toString(), loginData.get("password").toString());
            Eusers eusers = euserMapper.getUserByNameWord(loginData.get("username").toString(), loginData.get("password").toString());
            if (users != null) {
                String token = SignToken.getToken(users);
                map.put("code", 200);
                map.put("msg", "账号密码验证成功！");
                map.put("Info", users);
                map.put("token", token);
            } else {
                if (eusers != null) {
                    if ("0".equals(eusers.getIslock())) {
                        String token = SignToken.getTokenEmploye(eusers);
                        map.put("code", 200);
                        map.put("msg", "账号密码验证成功！");
                        map.put("Info", eusers);
                        map.put("token", token);
                    } else {
                        map.put("code", 201);
                        map.put("msg", "账号被封禁 请联系管理员！");
                    }

                } else {
                    map.put("code", 201);
                    map.put("msg", "账号密码错误！");
                }
            }
        }
        return map;
    }



}
