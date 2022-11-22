package com.example.webapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.webapp.bean.Users;

public interface usersService extends IService<Users> {
    Users getUserByNameWord(String username, String password);
}
