package com.example.webapp.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.webapp.bean.Depall;
import com.example.webapp.mapper.depallMapper;
import com.example.webapp.service.depallService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Service
public class depallServiceImpl extends ServiceImpl<depallMapper, Depall> implements depallService {
    @Resource
    depallMapper depallMapper;

    @Override
    public Integer getCount() {
        return depallMapper.getCount();
    }

    @Override
    public List<Depall> getAllDepall() {
        return depallMapper.getAllDepall();
    }

    @Override
    public List<Map<String, String>> getCompanyEvilInfo() {
        return depallMapper.getCompanyEvilInfo();
    }

}
