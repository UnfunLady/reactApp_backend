package com.example.webapp.mapper;


import org.apache.ibatis.annotations.Mapper;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Mapper
@Component
public interface depallTreeMapper  {
    //    获取全部部门
    @Select("SELECT d.dno as `key`,d.dname as title,d.`explain`,d.avatar,d.count,d.groupCount FROM depall d")
    List<Map<String, String>> getAllDepall();

    @Select("SELECT d.id as `key`,d.deptname as title,d.deptno,d.count,d.location FROM dept d WHERE d.deptno=#{deptno}")
    List<Map<String, String>> getChildrenDeptInfo(Integer deptno);
}
