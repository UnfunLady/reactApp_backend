package com.example.webapp.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.webapp.bean.Employe;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface employeMapper extends BaseMapper<Employe> {
    //    获取部门下全部员工信息
    List<Employe> getEmployee(Integer deptId, Integer page, Integer size);

    //    关键字获取员工信息
    List<Employe> getEmoloyeByKeyWord(String keyword, Integer page, Integer size);

    //    关键字获取时要获取的总员工数量
    Integer getKeyWordSearchCount(String keyword);

    //    分页获取未接种员工信息
    List<Employe> getEmployeNoCovid(Integer dno, Integer page, Integer size);


}
