package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface usersMapper extends BaseMapper<Users> {
    @Select("select * from users where username=#{username} and password=#{password}")
    Users getUserByNameWord(String username, String password);
}
