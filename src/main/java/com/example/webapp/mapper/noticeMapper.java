package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Notice;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface noticeMapper extends BaseMapper<Notice> {
    @MapKey("")
    List<Map<String, String>> getNoticeBykeyWordPage(String keyword, Integer limit, Integer size);

    Integer getNoticeCountByKeyWord(String keyword);

    //获取员工部门专属公告
    @MapKey("")
    List<Map<String, String>> getNoticeEmploye(Integer employeno, Integer limit, Integer size);

    //    获取专属公告总数
    Integer getSpecialNoticeCount(Integer employeno);

    //    获取今日公告
    @MapKey("")
    List<Map<String, String>> getTodayNoticeEmploye(Integer employeno);


}
