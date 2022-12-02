package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface deptMapper extends BaseMapper<Dept> {
//    获取小组总数
    @Select("select count(d.id)AS companyGroupCount from dept d")
    Integer getCount();
}
