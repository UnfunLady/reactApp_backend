package com.example.webapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.webapp.bean.Depall;
import com.example.webapp.bean.Dept;
import com.example.webapp.bean.EmployeSalary;
import com.example.webapp.mapper.employeSalaryMapper;
import com.example.webapp.service.depallService;
import com.example.webapp.service.deptService;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class deptController {
    @Autowired
    deptService deptService;
    @Autowired
    depallService depallService;
    @Autowired
    employeSalaryMapper employeSalaryMapper;

    //    获取部门信息
    @GetMapping("/api/deptInfo")
    public Map getDeptInfo() {
        Map map = new HashMap<>();
        //        获取全部部门
        List<Depall> list = new ArrayList<>();
        list = depallService.getAllDepall();
        if (list != null) {
            //        获取全部小组
            List<Dept> list1 = new ArrayList<>();
            list1 = deptService.getBaseMapper().selectList(null);
            if (list1 != null) {
                map.put("code", 200);
                map.put("msg", "操作成功！");
                map.put("deptInfo", list);
                map.put("groupInfo", list1);
            } else {
                map.put("code", 202);
                map.put("msg", "操作失败！");
            }
        } else {
            map.put("code", 202);
            map.put("msg", "操作失败！");
        }

        return map;
    }

    // 根据部门号查找部门下的全部团队
    @LoginToken
    @GetMapping("/api/getDeptByDno")
    public Map getDeptByDno(@RequestParam Map<String, String> params) {
        Map map = new HashMap<>();
        if (params.get("dno") != null) {
            List<Dept> list = deptService.getBaseMapper().selectList(new QueryWrapper<Dept>().eq("deptno", params.get("dno")));
            if (list != null) {
                map.put("code", 200);
                map.put("msg", "操作成功！");
                map.put("groupInfo", list);
            }
        } else {
            map.put("code", 202);
            map.put("msg", "缺少部门号！");
        }
        return map;
    }

    //获取团队整体薪资
    @LoginToken
    @GetMapping("/api/getSalaryInfo")
    public Map getSalaryInfo(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("dno") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少部门号参数！");
        } else {
//            获取团队下的薪资信息以及团队的小组信息
            List<EmployeSalary> list = employeSalaryMapper.selectList(new QueryWrapper<EmployeSalary>().eq("deptno", map.get("dno")));
            List<Dept> list1 = deptService.getBaseMapper().selectList(new QueryWrapper<Dept>().eq("deptno", map.get("dno")));
            if (list != null && list1 != null) {
                map1.put("code", 200);
                map1.put("salaryInfo", list);
                map1.put("DeptInfo", list1);
            } else {
                map1.put("code", 202);
                map1.put("msg", "获取薪资信息失败！");
            }

        }

        return map1;
    }

    // 修改薪资信息
    @LoginToken
    @PostMapping("/api/updateSalaryInfo")
    public Map updateSalaryInfo(@RequestBody Map map) {
        Map map1 = new HashMap();
//        如果editIsuse为空且修改id为0则说明只修改了绩效
        if ("".equals(map.get("editIsuse")) || map.get("editIsuse") == null || map.get("editIsuseId") == null || Integer.parseInt(map.get("editIsuseId").toString()) == 0) {
//            只修改performance  判断参数是否有
            if (map.get("performance") == null || map.get("performanceId") == null || map.get("performance") instanceof Integer == false || Integer.parseInt(map.get("performance").toString()) > 100
                    || Integer.parseInt(map.get("performance").toString()) < 0) {
                map1.put("code", 201);
                map1.put("msg", "关键信息缺失或错误!");
            } else {
                EmployeSalary employeSalary = new EmployeSalary();
                employeSalary.setPerformance(Integer.parseInt(map.get("performance").toString()));
                int update = employeSalaryMapper.update(employeSalary, new UpdateWrapper<EmployeSalary>().eq("deptid", map.get("performanceId")));
                if (update > 0) {
                    map1.put("code", 200);
                    map1.put("msg", "修改绩效成功!");
                } else {
                    map1.put("code", 201);
                    map1.put("msg", "修改绩效失败!");
                }
            }
        } else {
//            否则修改部门的isuse  判断传入的值是否为true或false
            if (("true".equalsIgnoreCase(map.get("editIsuse").toString()) || "false".equalsIgnoreCase(map.get("editIsuse").toString())) && map.get("editIsuseId") != null) {
                EmployeSalary employeSalary = new EmployeSalary();
                employeSalary.setIsuse(map.get("editIsuse").toString());
                employeSalary.setDeptid(Integer.parseInt(map.get("editIsuseId").toString()));
                int update = employeSalaryMapper.update(employeSalary, new UpdateWrapper<EmployeSalary>().eq("deptid", map.get("editIsuseId")));
                if (update > 0) {
                    map1.put("code", 200);
                    map1.put("msg", "修改薪资信息成功!");
                } else {
                    map1.put("code", 201);
                    map1.put("msg", "修改薪资信息失败!");
                }
            } else {
                map1.put("code", 201);
                map1.put("msg", "参数值错误！");

            }
        }
        return map1;
    }

    //    修改部门信息 （无头像）
    @PostMapping("/api/editDeptNoAvatar")
    public Map editDeptNoAvatar(@RequestBody Map<String, Depall> map) {
        Map map1 = new HashMap();
        if (map.get("editDeptData") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            Depall depall = map.get("editDeptData");
            boolean update = depallService.update(new UpdateWrapper<Depall>().set("dname", depall.getDname()).set("`explain`", depall.getExplain()).eq("dno", depall.getDno()));
            if (update) {
                map1.put("code", 200);
                map1.put("msg", "修改基本信息成功!");
            } else {
                map1.put("code", 202);
                map1.put("msg", "修改基本信息失败!");
            }
        }


        return map1;
    }
}

