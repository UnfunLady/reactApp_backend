package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Dept;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface deptMapper extends BaseMapper<Dept> {
}
