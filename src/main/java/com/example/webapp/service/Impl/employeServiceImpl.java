package com.example.webapp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webapp.bean.Employe;
import com.example.webapp.mapper.employeMapper;
import com.example.webapp.service.employeService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class employeServiceImpl extends ServiceImpl<employeMapper, Employe> implements employeService {
    @Resource
    employeMapper employeMapper;

    @Override
    public List<Employe> getEmployee(Integer deptId, Integer page, Integer size) {
        return employeMapper.getEmployee(deptId, page, size);
    }

    @Override
    public List<Employe> getEmoloyeByKeyWord(String keyword, Integer page, Integer size) {
        return employeMapper.getEmoloyeByKeyWord(keyword, page, size);
    }

    @Override
    public Integer getKeyWordSearchCount(String keyword) {
        return employeMapper.getKeyWordSearchCount(keyword);
    }
}
