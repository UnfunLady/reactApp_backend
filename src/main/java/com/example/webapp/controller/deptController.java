package com.example.webapp.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.webapp.bean.*;
import com.example.webapp.mapper.covidInfoMapper;
import com.example.webapp.mapper.deptRedoMapper;
import com.example.webapp.mapper.employeSalaryMapper;
import com.example.webapp.service.depallService;
import com.example.webapp.service.deptService;
import com.example.webapp.service.employeService;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

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
    @Autowired
    deptRedoMapper deptRedoMapper;
    @Autowired
    covidInfoMapper covidInfoMapper;

    //    获取部门信息
    @LoginToken
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
            if (list != null && list.size() > 0) {
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
            if (list != null && list.size() > 0 && list1 != null && list1.size() > 0) {
                map1.put("code", 200);
                map1.put("salaryInfo", list);
                map1.put("DeptInfo", list1);
            } else {
                map1.put("code", 202);
                map1.put("msg", "该部门号不存在！");
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
            Depall isDepall = depallService.getBaseMapper().selectOne(new QueryWrapper<Depall>().eq("dname", depall.getDname()));
            if (isDepall != null) {
                map1.put("code", 202);
                map1.put("msg", "已有相同部门存在！");
            } else {
                boolean update = depallService.update(new UpdateWrapper<Depall>().set("dname", depall.getDname()).set("`explain`", depall.getExplain()).eq("dno", depall.getDno()));
                if (update) {
                    map1.put("code", 200);
                    map1.put("msg", "修改基本信息成功!");
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "修改基本信息失败!");
                }
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
                String publicPath = System.getProperty("user.dir") + "/src/main/resources/images/";
//               判断文件是否同名存在
                File isFile = new File("classpath:/images/", avatarName);
                if (isFile.exists()) {
//                如果存在相同文件 则修改文件名再存
                    avatarName = "uploadAvatarZengYu" + (int) (Math.random() * 114515) + "SAFE" + file.getOriginalFilename();
                    file.transferTo(new File(publicPath + avatarName));
//                执行修改操作
                    boolean update = depallService.update(new UpdateWrapper<Depall>().set("avatar", "http://127.0.0.1:8888images/" + avatarName).set("dname", map.get("dname"))
                            .set("`explain`", map.get("explain")).eq("dno", Integer.parseInt(map.get("dno").toString())));
                    if (update) {
                        map1.put("code", 200);
                        map1.put("msg", "修改成功");
                    }
                } else {

//                执行修改操作
                    Depall isDepall = depallService.getBaseMapper().selectOne(new QueryWrapper<Depall>().eq("dname", map.get("dname")));
                    if (isDepall != null) {
                        map1.put("code", 202);
                        map1.put("msg", "已有相同部门存在！");
                    } else {
                        boolean update = depallService.update(new UpdateWrapper<Depall>().set("avatar", "http://127.0.0.1:8888/images/" + avatarName).set("dname", map.get("dname"))
                                .set("`explain`", map.get("explain")).eq("dno", Integer.parseInt(map.get("dno").toString())));
                        if (update) {
                            map1.put("code", 200);
                            map1.put("msg", "修改成功");
                            file.transferTo(new File(publicPath + avatarName));
                        } else {
                            map1.put("code", 202);
                            map1.put("msg", "修改失败!");
                        }
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

    //添加小组
    @LoginToken
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
                            if (employe != null && employe.size() > 0) {
                                //  修改员工所属id
                                employe.get(0).setDeptno(dept2.getId());
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

    //    解散小组
    @LoginToken
    @PostMapping("/api/delGroup")
    public Map delGroup(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("id") == null || map.get("user") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
//            获取要删除的小组信息插入到备份表
            Dept dept = deptService.getBaseMapper().selectOne(new QueryWrapper<Dept>().eq("id", map.get("id")));
            if (dept == null) {
                map1.put("code", 202);
                map1.put("msg", "该小组信息不存在");
            } else {
                //        获取当前操作时间
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                String nowDate = dateFormat.format(date);
//                准备信息插入备份表
                DeptRedo deptRedo = new DeptRedo();
                deptRedo.setId(dept.getId());
                deptRedo.setDeptno(dept.getDeptno());
                deptRedo.setCount(dept.getCount());
                deptRedo.setLocation(dept.getLocation());
                deptRedo.setDeptname(dept.getDeptname());
                deptRedo.setCountCovid(dept.getCountCovid());
                deptRedo.setConfirmTime(nowDate);
                deptRedo.setWhichDone(map.get("user").toString());
                int insert = deptRedoMapper.insert(deptRedo);
                if (insert > 0) {
//                    查询小组剩下的员工
                    List<Employe> employeList = employeService.getBaseMapper().selectList(new QueryWrapper<Employe>().eq("deptno", map.get("id")));
                    if (employeList != null && employeList.size() > 0) {
//                  如果员工不为空 先删除完再删除小组 否则直接删除小组
                        int count = 0, successCount = 0;
                        for (Employe e : employeList) {
                            count++;
                            int delete = employeService.getBaseMapper().delete(new UpdateWrapper<Employe>().eq("employno", e.getEmployno()).eq("deptno", map.get("id")));
                            if (delete > 0) {
                                successCount++;
                            }
                        }
                        if (count == successCount) {
                            System.out.println(count);
                            System.out.println(successCount);
//                  如果员工都删除完了就删除小组
                            int delete = deptService.getBaseMapper().delete(new UpdateWrapper<Dept>().eq("id", map.get("id")));
                            if (delete > 0) {
                                map1.put("code", 200);
                                map1.put("msg", "删除小组成功！");
                            } else {
//                      败了则清空备份表数据
                                deptRedoMapper.delete(new UpdateWrapper<DeptRedo>().eq("id", deptRedo.getId()).eq("deptno", deptRedo.getDeptno()));
                                map1.put("code", 202);
                                map1.put("msg", "删除小组失败！");
                            }
                        } else {
//                           失败了则清空备份表数据
                            deptRedoMapper.delete(new UpdateWrapper<DeptRedo>().eq("id", deptRedo.getId()).eq("deptno", deptRedo.getDeptno()));
                            map1.put("code", 202);
                            map1.put("msg", "删除小组失败！");
                        }
                    } else {
//                    直接删除小组
                        int delete = deptService.getBaseMapper().delete(new UpdateWrapper<Dept>().eq("id", map.get("id")));
                        if (delete > 0) {
                            map1.put("code", 200);
                            map1.put("msg", "删除小组成功！");
                        } else {
                            deptRedoMapper.delete(new UpdateWrapper<DeptRedo>().eq("id", deptRedo.getId()).eq("deptno", deptRedo.getDeptno()));
                            map1.put("code", 202);
                            map1.put("msg", "删除小组失败！");
                        }
                    }
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "删除小组失败！");
                }

            }
        }
        return map1;
    }

    //    解散部门
    @LoginToken
    @PostMapping("/api/delDept")
    public Map delDept(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("dno") == null || map.get("children") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
//            转换格式
            List<Dept> childrenList = JSONObject.parseArray(JSONObject.toJSONString(map.get("children"))).toJavaList(Dept.class);
            Integer dno = Integer.parseInt(map.get("dno").toString());
            if (childrenList != null && childrenList.size() > 0) {
//              说明还有小组 先删除小组
//            遍历部门下的所有小组 搜索小组下的全部员工 删除掉后再删除小组 直到小组删除完毕再删除部门
                int dCount = 0, dSuccessCount = 0;
                for (Dept d : childrenList) {
                    dCount++;
                    List<Employe> employeList = employeService.getBaseMapper().selectList(new QueryWrapper<Employe>().eq("deptno", d.getId()));
                    if (employeList != null && employeList.size() > 0) {
                        int eCount = 0, eSuccessCount = 0;
//                    小组还有员工 先删完员工
                        for (Employe e : employeList) {
                            eCount++;
                            int deleteEmploye = employeService.getBaseMapper().delete(new UpdateWrapper<Employe>().eq("employno", e.getEmployno()).eq("deptno", e.getDeptno()));
                            System.out.println("deleteEmploye:" + deleteEmploye);
                            if (deleteEmploye > 0) {
                                eSuccessCount++;
                            }
                        }
                        if (eCount == eSuccessCount && eSuccessCount != 0) {
                            System.out.println(dCount);
                            System.out.println(eCount);
                            System.out.println(eSuccessCount);
//                        删除完了员工之后删除小组
                            int deleteDept = deptService.getBaseMapper().delete(new UpdateWrapper<Dept>().eq("id", d.getId()));
                            if (deleteDept > 0) {
//                       说明删除小组成功
                                dSuccessCount++;
                            } else {
                                map1.put("code", 202);
                                map1.put("msg", "删除失败！");
                            }
                        } else {
                            map1.put("code", 202);
                            map1.put("msg", "删除出错！");
                        }
                    } else {
//                    没有员工了
                        int deleteDept = deptService.getBaseMapper().delete(new UpdateWrapper<Dept>().eq("id", d.getId()));
                        if (deleteDept > 0) {
//                       说明删除小组成功
                            dSuccessCount++;
                        } else {
                            map1.put("code", 202);
                            map1.put("msg", "删除失败！");
                        }
                    }
                }
                if (dCount == dSuccessCount) {
//                说明全部小组删除完毕  执行删除部门
                    int deleteDepall = depallService.getBaseMapper().delete(new UpdateWrapper<Depall>().eq("dno", dno));
                    if (deleteDepall > 0) {
                        map1.put("code", 200);
                        map1.put("msg", "删除部门成功!");
                    } else {
                        map1.put("code", 202);
                        map1.put("msg", "删除部门出错!");
                    }
                }
            } else {
//                没有小组了 直接删除部门
                int deleteDepall = depallService.getBaseMapper().delete(new UpdateWrapper<Depall>().eq("dno", dno));
                if (deleteDepall > 0) {
                    map1.put("code", 200);
                    map1.put("msg", "删除部门成功!");
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "删除部门出错!");
                }
            }

        }

        return map1;
    }

    //    新增部门
    @LoginToken
    @PostMapping("/api/addDeptpartment")
    public Map addDeptpartment(@RequestParam Map map, @RequestParam("file") MultipartFile file) throws IOException {
        Map map1 = new HashMap();
        if ((map.get("dname") == null && map.get("dname").toString().length() == 0) || (map.get("explain") == null && map.get("explain").toString().length() == 0)) {
            map1.put("code", 201);
            map1.put("msg", "缺少重要参数");
        } else {
            if (!file.isEmpty()) {
//               获取到头像 然后将头像名字随机赋值 存到resources文件夹
                String avatarName = "uploadDeptAvatarZengYu" + (int) (Math.random() * 114514) + file.getOriginalFilename();
//                设置存放文件路径
                String publicPath = System.getProperty("user.dir") + "/src/main/resources/images/";
//               判断文件是否同名存在
                File isFile = new File("classpath:/images/", avatarName);
                if (isFile.exists()) {
//                如果存在相同文件 则修改文件名再存
                    avatarName = "uploadDeptAvatarZengYu" + (int) (Math.random() * 114515) + "SAFE" + file.getOriginalFilename();
//                执行新增操作
                    Depall depall = new Depall();
                    depall.setDname(map.get("dname").toString());
                    depall.setExplain(map.get("explain").toString());
                    depall.setAvatar("http://127.0.0.1:8888/images/" + avatarName);
//                    先判断有没有重复部门名
                    Depall isDepall = depallService.getBaseMapper().selectOne(new QueryWrapper<Depall>().eq("dname", map.get("dname")));
                    if (isDepall != null) {
                        map1.put("code", 202);
                        map1.put("msg", "已有相同部门存在！");
                    } else {
                        int insert = depallService.getBaseMapper().insert(depall);
                        if (insert > 0) {
                            map1.put("code", 200);
                            map1.put("msg", "添加部门成功！");
                            file.transferTo(new File(publicPath + avatarName));
                        } else {
                            map1.put("code", 202);
                            map1.put("msg", "添加部门失败！");
                        }
                    }
                } else {

//                执行新增操作
                    Depall depall = new Depall();
                    depall.setDname(map.get("dname").toString());
                    depall.setExplain(map.get("explain").toString());
                    depall.setAvatar("http://127.0.0.1:8888/images/" + avatarName);
                    Depall isDepall = depallService.getBaseMapper().selectOne(new QueryWrapper<Depall>().eq("dname", map.get("dname")));
                    if (isDepall != null) {
                        map1.put("code", 202);
                        map1.put("msg", "已有相同部门存在！");
                    } else {
                        int insert = depallService.getBaseMapper().insert(depall);
                        if (insert > 0) {
                            map1.put("code", 200);
                            map1.put("msg", "添加部门成功！");
                            file.transferTo(new File(publicPath + avatarName));
                        } else {
                            map1.put("code", 202);
                            map1.put("msg", "添加部门失败！");
                        }
                    }

                }
            } else {
                map1.put("code", 202);
                map1.put("msg", "缺少图片参数");
            }
        }

        return map1;
    }

    //    获取部门接种信息
    @LoginToken
    @GetMapping("/api/getCompanyEvilInfo")
    public Map getCompanyEvilInfo() {
        Map map = new HashMap();
        List<Map<String, String>> companyEvilInfo = depallService.getCompanyEvilInfo();
        if (companyEvilInfo != null && companyEvilInfo.size() > 0) {
            map.put("code", 200);
            map.put("deptInfo", companyEvilInfo);
        } else {
            map.put("code", 202);
            map.put("msg", "获取接种信息失败!");
        }
        return map;
    }

    //    获取部门接种获取相关员工信息
    @LoginToken
    @GetMapping("/api/getEmployeEvilInfo")
    public Map getEmployeEvilInfo(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("dno") == null || map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "获取信息失败!");
        } else {
            // 未接种完毕的员工信息
            Integer page = (Integer.parseInt(map.get("page").toString()) - 1) * Integer.parseInt(map.get("size").toString());
            List<Employe> employeList = employeService.getEmployeNoCovid(Integer.parseInt(map.get("dno").toString()), page, Integer.parseInt(map.get("size").toString()));
            if (employeList != null && employeList.size() > 0) {
                //获取具体接种信息
                List<CovidInfo> covidInfoList = covidInfoMapper.getEmployeDetailCovidInfo(Integer.parseInt(map.get("dno").toString()));
                if (covidInfoList != null && covidInfoList.size() > 0) {
                    map1.put("code", 200);
                    map1.put("employeInfo", employeList);
                    map1.put("employeCount", employeList.size());
                    map1.put("evilInfo", covidInfoList);
                    map1.put("evilCount", covidInfoList.size());
                } else {
                    map1.put("code", 200);
                    map1.put("employeCount", JSONObject.parse("[]"));
                    map1.put("employeCount", 0);
                    map1.put("evilInfo", JSONObject.parse("[]"));
                    map1.put("evilCount", 0);
                }
            } else {
                map1.put("code", 200);
                map1.put("employeInfo", null);
                map1.put("employeCount", JSONObject.parse("[]"));
                map1.put("evilInfo", JSONObject.parse("[]"));
                map1.put("evilCount", 0);
            }
        }

        return map1;
    }

    // 获取部门全部员工接种信息
    @LoginToken
    @GetMapping("/api/getAllEmployeEvilInfo")
    public Map getAllEmployeEvilInfo(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("dno") == null || map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "获取信息失败!");
        } else {
            Integer page = (Integer.parseInt(map.get("page").toString()) - 1) * Integer.parseInt(map.get("size").toString());
            List<AllEmoloyeEvilInfo> allEmoloyeEvilInfoList = covidInfoMapper.getAllEmployeEvilInfo(Integer.parseInt(map.get("dno").toString()), page, Integer.parseInt(map.get("size").toString()));
            if (allEmoloyeEvilInfoList != null && allEmoloyeEvilInfoList.size() > 0) {
                // 获取总数
                Integer allEmployeCount = covidInfoMapper.getAllEmployeCount(Integer.parseInt(map.get("dno").toString()));
                if (allEmployeCount > 0) {
                    map1.put("code", 200);
                    map1.put("allEmployeEvilInfo", allEmoloyeEvilInfoList);
                    map1.put("count", allEmployeCount);
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "获取员工信息失败!");
                }
            } else {
                map1.put("code", 202);
                map1.put("msg", "暂无任何员工信息!");
            }
        }
        return map1;
    }

    // 修改接种信息
    @LoginToken
    @PostMapping("/api/updateEmployeEvilInfo")
    public Map updateEmployeEvilInfo(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("depallid") == null || map.get("deptid") == null || map.get("employno") == null || map.get("firstInoculation") == null || map.get("secondInoculation") == null || map.get("threeInoculation") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数!");
        } else {
            CovidInfo covidInfo = new CovidInfo();
            covidInfo.setDepallid(Integer.parseInt(map.get("depallid").toString()));
            covidInfo.setDeptid(Integer.parseInt(map.get("deptid").toString()));
            covidInfo.setEmployid(Integer.parseInt(map.get("employno").toString()));
            covidInfo.setFirstInoculation(map.get("firstInoculation").toString());
            covidInfo.setSecondInoculation(map.get("secondInoculation").toString());
            covidInfo.setThreeInoculation(map.get("threeInoculation").toString());
            int update = covidInfoMapper.update(covidInfo, new UpdateWrapper<CovidInfo>().set("depallid", covidInfo.getDepallid())
                    .set("firstInoculation", covidInfo.getFirstInoculation()).set("secondInoculation", covidInfo.getSecondInoculation())
                    .set("threeInoculation", covidInfo.getThreeInoculation()).eq("deptid", covidInfo.getDeptid()).eq("employid", covidInfo.getEmployid()));
            if (update > 0) {
                map1.put("code", 200);
                map1.put("msg", "修改成功!");
            } else {
                map1.put("code", 202);
                map1.put("msg", "修改失败!");
            }
        }
        return map1;
    }

    // 查看删除的小组
    @LoginToken
    @GetMapping("/api/getRecoverGroup")
    public Map getRecoverGroup(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少参数！");
        } else {
            Page<DeptRedo> Page = new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
            Page<DeptRedo> deptRedoPage = deptRedoMapper.selectPage(Page, new QueryWrapper<DeptRedo>(null));
            if (deptRedoPage != null && deptRedoPage.getTotal() > 0) {
                List<DeptRedo> deptRedoList = deptRedoPage.getRecords();
                Integer count = deptRedoMapper.selectCount(new QueryWrapper<DeptRedo>(null));
                map1.put("code", 200);
                map1.put("results", deptRedoList);
                map1.put("count", count);
            } else {
                map1.put("code", 202);
                map1.put("msg", "暂无删除的员工");
            }
        }
        return map1;
    }

    //    撤回删除的小组
    @PostMapping("/api/recoverGroup")
    @LoginToken
    public Map recoverGroup(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("id") == null || map.get("deptno") == null || map.get("deptname") == null || map.get("location") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少参数！");
        } else {
            int delete = deptRedoMapper.delete(new UpdateWrapper<DeptRedo>().eq("id", Integer.parseInt(map.get("id").toString())).eq("deptno", Integer.parseInt(map.get("deptno").toString())));
            if (delete > 0) {
                Dept dept = new Dept();
                dept.setDeptno(Integer.parseInt(map.get("deptno").toString()));
                dept.setLocation(map.get("location").toString());
                dept.setDeptname(map.get("deptname").toString());
                dept.setId(Integer.parseInt(map.get("id").toString()));
                dept.setCount(0);
                dept.setCountCovid(0);
                int insert = deptService.getBaseMapper().insert(dept);
                if (insert > 0) {
                    map1.put("code", 200);
                    map1.put("msg", "恢复小组成功！");
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "恢复小组失败！");
                }
            }
        }
        return map1;
    }

}

