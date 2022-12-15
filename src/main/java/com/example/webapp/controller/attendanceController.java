package com.example.webapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.webapp.bean.Depall;
import com.example.webapp.bean.Dept;
import com.example.webapp.bean.EmployeLeave;
import com.example.webapp.mapper.clockMapper;
import com.example.webapp.mapper.employeLeaveMapper;
import com.example.webapp.util.LoginEmployeToken;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class attendanceController {
    @Autowired
    employeLeaveMapper employeLeaveMapper;

    @Autowired
    clockMapper clockMapper;

    //员工获取属于自己的部门
//    获取部门信息
    @LoginEmployeToken
    @PostMapping("/api/deptInfoE")
    public Map getDepallEmploye(@RequestBody Map map1) {
        Map map = new HashMap<>();
        if (map1.get("employeno") == null) {
            map.put("code", 202);
            map.put("msg", "缺少请求参数");
        } else {
            List<Map<String, String>> deptInfo = employeLeaveMapper.getDepallByEmployno(Integer.parseInt(map1.get("employeno").toString()));
            if (deptInfo != null && deptInfo.size() > 0) {
                map.put("code", 200);
                map.put("deptInfo", deptInfo);
            } else {
                map.put("code", 202);
                map.put("msg", "获取所在部门信息失败");
            }
        }
        return map;
    }

    //    获取小组信息
    @LoginEmployeToken
    @PostMapping("/api/getGroupE")
    public Map getDeptEmploye(@RequestBody Map map1) {
        Map map = new HashMap<>();
        if (map1.get("employeno") == null || map1.get("dno") == null) {
            map.put("code", 202);
            map.put("msg", "缺少请求参数");
        } else {
            List<Map<String, String>> groupInfo = employeLeaveMapper.getDeptByEmploynoAndDno(Integer.parseInt(map1.get("employeno").toString()), Integer.parseInt(map1.get("dno").toString()));
            if (groupInfo != null && groupInfo.size() > 0) {
                map.put("code", 200);
                map.put("groupInfo", groupInfo);
            } else {
                map.put("code", 202);
                map.put("msg", "获取小组信息失败");
            }
        }
        return map;
    }

    //    获取员工姓名
    @LoginEmployeToken
    @PostMapping("/api/reqGetEmployName")
    public Map getEmployeName(@RequestBody Map map1) {
        Map map = new HashMap<>();
        if (map1.get("employeno") == null || map1.get("deptId") == null) {
            map.put("code", 202);
            map.put("msg", "缺少请求参数");
        } else {
            List<Map<String, String>> employeInfo = employeLeaveMapper.getEmployeName(Integer.parseInt(map1.get("employeno").toString()), Integer.parseInt(map1.get("deptId").toString()));
            if (employeInfo != null && employeInfo.size() > 0) {
                map.put("code", 200);
                map.put("employeInfo", employeInfo);
            } else {
                map.put("code", 202);
                map.put("msg", "获取员工信息失败");
            }
        }
        return map;
    }


    //    获取请假申请
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
                map1.put("count", selectCount);
            } else {
                map1.put("code", 202);
                map1.put("msg", "暂无员工请假申请或出错");
            }
        }
        return map1;
    }

    //    审批员工请假
    @LoginToken
    @PostMapping("/api/verfiyLeave")
    public Map verfiyLeave(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("reply") == null || map.get("verfiyState") == null || map.get("whichVerfiy") == null || map.get("leaveNumber") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少请求参数");
        } else {
            EmployeLeave employeLeave = new EmployeLeave();
            employeLeave.setReply(map.get("reply").toString());
            employeLeave.setVerfiyState(map.get("verfiyState").toString());
            employeLeave.setWhichVerfiy(map.get("whichVerfiy").toString());
            employeLeave.setLeaveNumber(Integer.parseInt(map.get("leaveNumber").toString()));
            int update = employeLeaveMapper.update(employeLeave, new UpdateWrapper<EmployeLeave>().eq("leaveNumber", employeLeave.getLeaveNumber()));
            if (update > 0) {
                map1.put("code", 200);
                map1.put("msg", "审批成功");
            } else {
                map1.put("code", 202);
                map1.put("msg", "审批失败");
            }
        }
        return map1;
    }

    //   打卡情况
    @LoginToken
    @GetMapping("/api/getClockInfo")
    public Map clockInfo() {
        Map map = new HashMap();
        List<Map<String, String>> todayAllInfo = clockMapper.getTodayAllInfo();
//        上下班打卡情况
        List<Map<String, String>> todayInfoGroupDepall = clockMapper.getTodayInfoGroupDepall();
        if (todayAllInfo != null && todayAllInfo.size() > 0 && todayInfoGroupDepall != null && todayInfoGroupDepall.size() > 0) {
            map.put("code", 200);
            map.put("todayAllInfo", todayAllInfo);
            map.put("todayInfoGroupDepall", todayInfoGroupDepall);
        } else {
            map.put("code", 202);
            map.put("msg", "暂无今日打卡信息");
        }
        return map;
    }


}
