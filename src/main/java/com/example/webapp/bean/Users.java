package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("users")
public class Users {
    private String username;
    private String password;
    private String isLock;
    private String level;
    private String avatar;
    private String nickname;
    private String token;
}
