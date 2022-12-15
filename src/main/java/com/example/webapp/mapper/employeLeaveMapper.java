package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.EmployeLeave;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
@Mapper
public interface employeLeaveMapper extends BaseMapper<EmployeLeave> {
    //    获取员工所属部门信息
    @MapKey("")
    List<Map<String, String>> getDepallByEmployno(Integer employeno);

    //    根据部门号获取员工所属小组
    @MapKey("")
    List<Map<String, String>> getDeptByEmploynoAndDno(Integer employeno, Integer dno);

//    获取员工信息
@MapKey("")
    List<Map<String,String>> getEmployeName(Integer employeno, Integer deptId);
}
