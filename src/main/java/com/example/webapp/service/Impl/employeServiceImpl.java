package com.example.webapp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webapp.bean.Employe;
import com.example.webapp.mapper.employeMapper;
import com.example.webapp.service.employeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class employeServiceImpl extends ServiceImpl<employeMapper, Employe> implements employeService {
    @Resource
    employeMapper employeMapper;

    @Override
    public Integer getCount() {
        return employeMapper.getCount();
    }

    @Override
    public Integer getSalaryAVG() {
        return employeMapper.getSalaryAVG();
    }

    @Override
    public Integer getManCount() {
        return employeMapper.getManCount();
    }

    @Override
    public List<Map<String, String>> getEmployee(Integer deptId, Integer page, Integer size) {
        return employeMapper.getEmployee(deptId, page, size);
    }

    @Override
    public List<Map<String,String>> getEmoloyeByKeyWord(String keyword, Integer page, Integer size) {
        return employeMapper.getEmoloyeByKeyWord(keyword, page, size);
    }

    @Override
    public List<Map<String, String>> getEmployeDistinct() {
        return employeMapper.getEmployeDistinct();
    }

    @Override
    public Integer getKeyWordSearchCount(String keyword) {
        return employeMapper.getKeyWordSearchCount(keyword);
    }

    @Override
    public List<Employe> getEmployeNoCovid(Integer dno, Integer page, Integer size) {
        return employeMapper.getEmployeNoCovid(dno,page,size);
    }

    @Override
    public List<Map<String, String>> getEmployeInfo(Integer employeno) {
        return employeMapper.getEmployeInfo(employeno);
    }
}
