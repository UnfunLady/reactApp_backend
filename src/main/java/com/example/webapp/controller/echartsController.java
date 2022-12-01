package com.example.webapp.controller;

import com.example.webapp.mapper.echartsMapper;
import com.example.webapp.service.depallService;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class echartsController {

    @Autowired
    echartsMapper echartsMapper;

    // 首页第一张图echarts各部门总人数
    @LoginToken
    @GetMapping("/api/deptTotal")
    public Map deptTotal() {
        Map map = new HashMap<>();
        List<Map<String, String>> oneChartsData = echartsMapper.getOneCharts();
        if (oneChartsData != null && oneChartsData.size() > 0) {
            map.put("code", 200);
            map.put("deptInfo", oneChartsData);
            map.put("title", "各部门总人数");
        } else {
            map.put("code", 202);
            map.put("msg", "获取图表信息失败!");
        }
        return map;
    }

    // 第二张图 获取部门细节 平均工资 总人数 小组数
    @LoginToken
    @GetMapping("/api/deptDetail")
    public Map deptDetail() {
        Map map = new HashMap<>();
        List<Map<String, String>> twoChartsData = echartsMapper.getTwoCharts();
        if (twoChartsData != null && twoChartsData.size() > 0) {
            map.put("code", 200);
            map.put("deptDetailInfo", twoChartsData);
            map.put("title", "各部门总人数");
        } else {
            map.put("code", 202);
            map.put("msg", "获取图表信息失败!");
        }
        return map;
    }

}

