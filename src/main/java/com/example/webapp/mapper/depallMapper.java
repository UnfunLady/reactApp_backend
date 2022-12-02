package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Depall;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface depallMapper extends BaseMapper<Depall> {
    //    获取部门总数
    @Select("SELECT count(d.dno)as companyDeptCount from depall d")
    Integer getCount();

    @Select("SELECT dno,dname,d.explain,avatar,count,groupCount,isAllcovid,noCovid FROM depall d")
    List<Depall> getAllDepall();

    //    获取部门接种信息
    @MapKey("dno")
    List<Map<String, String>> getCompanyEvilInfo();
}
