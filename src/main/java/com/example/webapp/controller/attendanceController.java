package com.example.webapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.webapp.bean.DeptRedo;
import com.example.webapp.bean.EmployeLeave;
import com.example.webapp.mapper.employeLeaveMapper;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class attendanceController {
    @Autowired
    employeLeaveMapper employeLeaveMapper;
    @LoginToken
    @GetMapping("/api/getEmployeLeaveByPage")
    public Map getEmployeLeavePage(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少请求参数");
        } else {
            Page<EmployeLeave> Page = new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
            Page<EmployeLeave> deptRedoPage = employeLeaveMapper.selectPage(Page, new QueryWrapper<>(null));
            List<EmployeLeave> employeLeaves = deptRedoPage.getRecords();
            Integer selectCount = employeLeaveMapper.selectCount(new QueryWrapper<>(null));
            if (employeLeaves != null && employeLeaves.size() > 0) {
                map1.put("code", 200);
                map1.put("employeLeaveData", employeLeaves);
                map1.put("count",selectCount);
            } else {
                map1.put("code", 202);
                map1.put("msg", "暂无员工请假申请或出错");
            }
        }
        return map1;
    }
}
