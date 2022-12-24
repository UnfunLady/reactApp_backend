package com.example.webapp.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Eusers;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface euserMapper extends BaseMapper<Eusers> {
    @Select("select * from eusers where username=#{username} and password=#{password} and level ='2'")
    Eusers getUserByNameWord(String username, String password);

    //    关键字查找账号信息
    @MapKey("")
    List<Map<String, String>> getEusersInfoByKeyWord(String keyword, String type, Integer page, Integer size);

//    获取关键字查找的数量

    Integer getEusersInfoByKeyWordCount(String keyword, String type, Integer page, Integer size);

}
