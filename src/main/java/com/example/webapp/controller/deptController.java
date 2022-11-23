package com.example.webapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.example.webapp.bean.Depall;
import com.example.webapp.bean.Dept;
import com.example.webapp.bean.Employe;
import com.example.webapp.bean.EmployeSalary;
import com.example.webapp.mapper.employeSalaryMapper;
import com.example.webapp.service.depallService;
import com.example.webapp.service.deptService;
import com.example.webapp.service.employeService;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    @Autowired
    employeService employeService;

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

    //  获取团队整体薪资
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

    //    修改部门信息 （无头像）  obj参数｛｝
    @LoginToken
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

    //   修改部门信息(有头像)
    @LoginToken
    @PostMapping("/api/editDeptR")
    public Map editDeptR(@RequestParam Map map, @RequestParam("file") MultipartFile file) throws IOException {
        Map map1 = new HashMap();
        if (map.get("dno") == null || map.get("dname") == null || map.get("explain") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            if (!file.isEmpty()) {
//               获取到头像 然后将头像名字随机赋值 存到resources文件夹
                String avatarName = "uploadAvatarZengYu" + (int) (Math.random() * 114514) + file.getOriginalFilename();
//                设置存放文件路径
                String publicPath = System.getProperty("user.dir") + "/src/main/resources/public/";
//               判断文件是否同名存在
                File isFile = new File("classpath:/public/", avatarName);
                if (isFile.exists()) {
//                如果存在相同文件 则修改文件名再存
                    avatarName = "uploadAvatarZengYu" + (int) (Math.random() * 114515) + "SAFE" + file.getOriginalFilename();
                    file.transferTo(new File(publicPath + avatarName));
//                执行修改操作
                    boolean update = depallService.update(new UpdateWrapper<Depall>().set("avatar", publicPath + avatarName).set("dname", map.get("dname"))
                            .set("`explain`", map.get("explain")).eq("dno", Integer.parseInt(map.get("dno").toString())));
                    if (update) {
                        map1.put("code", 200);
                        map1.put("msg", "修改成功");
                    }
                } else {
                    file.transferTo(new File(publicPath + avatarName));
//                执行修改操作
                    boolean update = depallService.update(new UpdateWrapper<Depall>().set("avatar", publicPath + avatarName).set("dname", map.get("dname"))
                            .set("`explain`", map.get("explain")).eq("dno", Integer.parseInt(map.get("dno").toString())));
                    if (update) {
                        map1.put("code", 200);
                        map1.put("msg", "修改成功");
                    }
                }
            } else {
                map1.put("code", 202);
                map1.put("msg", "缺少图片参数");
            }

        }


        return map1;
    }

    //    修改小组信息
    @LoginToken
    @PostMapping("/api/editGroupInfo")
    public Map editGroupInfo(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("id") == null || map.get("deptname") == null || map.get("updateName") == null || map.get("location") == null || map.get("count") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            if (Boolean.parseBoolean(map.get("updateName").toString())) {
                Dept dept = deptService.getBaseMapper().selectOne(new QueryWrapper<Dept>().eq("deptname", map.get("deptname")));
                if (dept != null) {
                    map1.put("code", 202);
                    map1.put("msg", "修改小组信息失败:已经有相同名字的小组了！");
                } else {
                    Boolean update = deptService.update(new UpdateWrapper<Dept>().set("deptname", map.get("deptname")).set("location", map.get("location")).set("count", map.get("count")).eq("id", map.get("id")));
                    if (update) {
                        map1.put("code", 200);
                        map1.put("msg", "修改小组信息成功");
                    } else {
                        map1.put("code", 202);
                        map1.put("msg", "修改小组信息失败");
                    }
                }
            } else {
                Boolean update = deptService.update(new UpdateWrapper<Dept>().set("deptname", map.get("deptname")).set("location", map.get("location")).set("count", map.get("count")).eq("id", map.get("id")));
                if (update) {
                    map1.put("code", 200);
                    map1.put("msg", "修改小组信息成功");
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "修改小组信息失败");
                }
            }
        }

        return map1;
    }

    @PostMapping("/api/addGroup")
    public Map addGroup(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("addForm") == null || map.get("deptno") == null || map.get("deptname") == null || map.get("location") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            Dept dept = deptService.getBaseMapper().selectOne(new QueryWrapper<Dept>().eq("deptname", map.get("deptname")));
            if (dept != null) {
                map1.put("code", 202);
                map1.put("msg", "已有相同名字小组存在！");
            } else {
//                新增小组
                Dept dept1 = new Dept();
                dept1.setDeptno(Integer.parseInt(map.get("deptno").toString()));
                dept1.setDeptname(map.get("deptname").toString());
                dept1.setLocation(map.get("location").toString());
                int insert = deptService.getBaseMapper().insert(dept1);
                if (insert > 0) {
//                    插入成功后 获取插入的的id
                    Dept dept2 = deptService.getBaseMapper().selectOne(new QueryWrapper<Dept>().eq("deptname", map.get("deptname")));
                    if (dept2 != null) {
//            遍历员工数组 拿到员工信息 存放到小组里面
                        List employeList = (List) map.get("addForm");
                        int count = 0, successCount = 0;
                        for (Object i : employeList) {
                            count++;
                            List<Employe> employe = employeService.getBaseMapper().selectList(new QueryWrapper<Employe>().eq("employno", i));
//                        修改员工所属id
                            employe.get(0).setDeptno(dept2.getId());
                            if (employe != null) {
//                      插入员工到小组
                                int insert1 = employeService.getBaseMapper().insert(employe.get(0));
                                if (insert1 > 0) {
                                    successCount++;
                                } else {
                                    map1.put("code", 202);
                                    map1.put("msg", "添加小组员工失败！");
                                }
                            } else {
                                map1.put("code", 202);
                                map1.put("msg", "添加小组失败！");
                            }
                        }
                        //  如果插入所有员工都成功了
                        if (count == successCount) {
                            map1.put("code", 200);
                            map1.put("msg", "添加小组成功！");
                        } else {
                            System.out.println(count);
                            System.out.println(successCount);
                            map1.put("code", 202);
                            map1.put("msg", "添加小组失败！");
                        }
                    }
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "添加小组失败！");
                }
            }
        }
        return map1;
    }
}

