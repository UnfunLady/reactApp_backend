package com.example.webapp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Eusers;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

@Component
@Mapper
public interface euserMapper extends BaseMapper<Eusers> {
    @Select("select * from eusers where username=#{username} and password=#{password} and level ='2'")
    Eusers getUserByNameWord(String username, String password);
}
