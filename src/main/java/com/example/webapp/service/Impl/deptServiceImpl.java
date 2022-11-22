package com.example.webapp.service.Impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webapp.bean.Dept;
import com.example.webapp.mapper.deptMapper;
import com.example.webapp.service.deptService;
import org.springframework.stereotype.Service;

@Service
public class deptServiceImpl extends ServiceImpl<deptMapper, Dept> implements deptService {
}
