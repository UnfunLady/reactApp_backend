package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Depall;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface depallMapper extends BaseMapper<Depall> {
    @Select("SELECT dno,dname,d.explain,avatar,count,groupCount,isAllcovid,noCovid FROM depall d")
    List<Depall>  getAllDepall();
}
