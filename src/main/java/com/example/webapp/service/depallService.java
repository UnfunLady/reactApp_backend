package com.example.webapp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.webapp.bean.Depall;
import java.util.List;
public interface depallService extends IService<Depall> {
    List<Depall> getAllDepall();
}
