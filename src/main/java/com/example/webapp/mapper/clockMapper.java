package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.ClockInfo;
import com.example.webapp.bean.Employe;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface clockMapper extends BaseMapper<ClockInfo> {
//    获取今日全员打卡情况
    @Select("SELECT * FROM clockemploye WHERE TO_DAYS(clocktime) =TO_DAYS(NOW())")
    List<Map<String, String>> getTodayAllInfo();

    @MapKey("")
//    获取今日部门打卡信息
    List<Map<String, String>> getTodayInfoGroupDepall();
}
