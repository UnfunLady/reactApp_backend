package com.example.webapp.bean;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("eusers")
public class Eusers {
    private String username;
    private String password;
    private String islock;
    private String level;
    private String avatar;
    private String nickname;
}
