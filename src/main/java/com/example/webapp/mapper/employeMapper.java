package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Employe;
import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

@Mapper
public interface employeMapper extends BaseMapper<Employe> {
    //    获取员工总数
    @Select("SELECT count(DISTINCT(e.employno)) as companyEmployeCount  from employee e")
    Integer getCount();
    // 获取平均薪资
    @Select("select round(avg(e.employsalary),0) as companyAvgSalary from employee e")
    Integer getSalaryAVG();

    // 获取男性人数
    @Select("select count(DISTINCT(e.employno))as BoyGrilsPercentage from employee e WHERE e.employsex='男'")
    Integer getManCount();
    //    获取部门下全部员工信息
    @MapKey("employno")
    List<Map<String, String>> getEmployee(Integer deptId, Integer page, Integer size);

    //    关键字获取员工信息
    List<Employe> getEmoloyeByKeyWord(String keyword, Integer page, Integer size);

    //    关键字获取时要获取的总员工数量
    Integer getKeyWordSearchCount(String keyword);

    //    分页获取未接种员工信息
    List<Employe> getEmployeNoCovid(Integer dno, Integer page, Integer size);


}
