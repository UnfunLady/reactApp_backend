package com.example.webapp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webapp.bean.Users;
import com.example.webapp.mapper.usersMapper;
import com.example.webapp.service.usersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


@Service
public class userServiceImpl extends ServiceImpl<usersMapper, Users> implements usersService {
    @Resource
    private usersMapper usersMapper;
    @Override
    public Users getUserByNameWord(String username, String password) {
        return usersMapper.getUserByNameWord(username, password);
    }
}
