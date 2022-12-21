package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.ClockInfo;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@Mapper
public interface clockMapper extends BaseMapper<ClockInfo> {
    //    获取今日全员打卡情况
    @Select("SELECT * FROM clockemploye WHERE TO_DAYS(clocktime) =TO_DAYS(NOW()) limit #{page},#{size}")
    List<Map<String, String>> getTodayAllInfo(Integer page, Integer size);

    //    获取今日上班打卡信息
    @MapKey("")
    List<Map<String, String>> getTodayMorningInfo();
    //    获取今日迟到人数
    Integer getClockDelayCount();
    //    获取今日下班打卡信息
    @MapKey("")
    List<Map<String, String>> getTodayAfterInfo();
    //    获取本月总打卡次数
    @MapKey("")
    Integer getMonthClockCount();
    //    本月上午迟到人数
    Integer getMonthClockDelayCount();
    //    获取本周打卡次数
    Integer getWeekClockCount();
    //    获取本周迟到次数
    Integer getWeekClockDelayCount();
    //    获取本日打卡次数
    Integer getTodayClockCount();
    //    获取本日迟到次数
    Integer getTodayClockDelayCount();

    //   今日打卡部门上下班是否全勤情况
    @MapKey("")
    List<Map<String, String>> getTodayAllClockInfo();


    //    ----------------------------
//    员工今日是否打上下班卡
    List<ClockInfo> isClockMorning(Integer employeno, String clockTime);

    List<ClockInfo> isClockAfter(Integer employeno, String clockTime);

}
