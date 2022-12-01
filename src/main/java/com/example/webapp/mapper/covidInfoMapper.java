package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.AllEmoloyeEvilInfo;
import com.example.webapp.bean.CovidInfo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface covidInfoMapper extends BaseMapper<CovidInfo> {

    // 员工具体接种信息
    List<CovidInfo> getEmployeDetailCovidInfo(Integer dno);

    //获取全部员工信息 分页
    List<AllEmoloyeEvilInfo> getAllEmployeEvilInfo(Integer dno, Integer page, Integer size);
    //获取全部员工总数
    Integer getAllEmployeCount(Integer dno);
}
