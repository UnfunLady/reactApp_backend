package com.example.webapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.webapp.bean.Users;
import com.example.webapp.mapper.usersMapper;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class userController {
    @Autowired
    usersMapper usersMapper;
    //    修改密码
    @LoginToken
    @PostMapping("/api/editPassword")
    public Map editPassword(@RequestParam Map map) {
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
}
