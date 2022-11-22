package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.user;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface userMapper extends BaseMapper<user> {
//    @Select("select * from user where id=#{id}")
    public user getUser(Integer id);
}
