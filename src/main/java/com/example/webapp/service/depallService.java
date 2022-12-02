package com.example.webapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.webapp.bean.Depall;
import java.util.List;
import java.util.Map;

public interface depallService extends IService<Depall> {
    Integer getCount();
    List<Depall> getAllDepall();
    List<Map<String, String>> getCompanyEvilInfo();
}
