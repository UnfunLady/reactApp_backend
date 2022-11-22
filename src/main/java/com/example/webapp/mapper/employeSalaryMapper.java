package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.EmployeSalary;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;
@Component
@Mapper
public interface employeSalaryMapper extends BaseMapper<EmployeSalary> {

}
