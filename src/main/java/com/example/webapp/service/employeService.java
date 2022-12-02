package com.example.webapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.webapp.bean.Employe;

import java.util.List;
import java.util.Map;

public interface employeService extends IService<Employe> {
    Integer getCount();
    Integer getSalaryAVG();
    Integer getManCount();
    List<Map<String, String>> getEmployee(Integer deptId, Integer page, Integer size);
    List<Employe> getEmoloyeByKeyWord(String keyword, Integer page, Integer size);
    Integer getKeyWordSearchCount(String keyword);
    List<Employe> getEmployeNoCovid(Integer dno, Integer page, Integer size);
}
