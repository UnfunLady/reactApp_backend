package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.EmployeSalary;
import com.example.webapp.bean.EmployeSalaryDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Mapper
public interface employeSalaryDetailMapper extends BaseMapper<EmployeSalaryDetail> {
    List<EmployeSalaryDetail> getEmployeSalaryByPage(Integer deptid, Integer page, Integer size);
//    获取补贴信息
    List<EmployeSalary> getDetail(Integer deptid);
//    获取员工总数量
    Integer getCount(Integer deptid);
}
