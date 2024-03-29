package com.example.webapp.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.webapp.bean.*;
import com.example.webapp.mapper.employeReDoMapper;
import com.example.webapp.mapper.employeSalaryDetailMapper;
import com.example.webapp.mapper.euserMapper;
import com.example.webapp.service.deptService;
import com.example.webapp.service.employeService;
import com.example.webapp.util.LoginEmployeToken;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@RestController
public class employeController {
    @Autowired
    deptService deptService;
    @Autowired
    employeService employeService;
    @Autowired
    employeReDoMapper employeReDoMapper;
    @Autowired
    employeSalaryDetailMapper employeSalaryDetailMapper;
    @Autowired
    euserMapper euserMapper;

    // 根据部门团队号查询 部门团队下的所有成员
    @LoginToken
    @GetMapping("/api/getEmployee")
    public Map getEmploye(@RequestParam Map<String, String> params) {
        Map map = new HashMap<>();
        if (params.get("deptId") != null && params.get("page") != null && params.get("size") != null) {
            if (Integer.parseInt(params.get("page")) < 1) {
                map.put("code", 202);
                map.put("msg", "参数出错！");
            } else {
                List<Map<String, String>> list = employeService.getEmployee(Integer.parseInt(params.get("deptId")), (Integer.parseInt(params.get("page")) - 1) * Integer.parseInt(params.get("size")),
                        Integer.parseInt(params.get("size")));
                if (list != null && list.size() > 0) {
                    Dept dept = deptService.getBaseMapper().selectOne(new QueryWrapper<Dept>().select("count").eq("id", params.get("deptId")));
                    if (dept != null) {
                        map.put("code", 200);
                        map.put("msg", "操作成功！");
                        map.put("employeInfo", list);
                        map.put("count", dept.getCount());
                    } else {
                        map.put("code", 202);
                        map.put("msg", "该部门暂无员工！");
                    }
                } else {
                    map.put("code", 202);
                    map.put("msg", "该部门暂无员工！");
                }
            }

        } else {
            map.put("code", 202);
            map.put("msg", "缺少部门号或页码或者查询数量！");
        }
        return map;
    }

    //增加或修改员工信息
    @LoginToken
    @PostMapping("/api/addOrUpdateEmploy")
    public Map addOrUpdateEmploye(@RequestBody Map map) {
        Map map1 = new HashMap<>();
//        判断是否有参数
        if (map.get("isUpdate") != null && map.get("changeGroup") != null) {
            if (Boolean.parseBoolean(map.get("isUpdate").toString())) {
                //判断是否移动员工
                if (Boolean.parseBoolean(map.get("changeGroup").toString())) {
                    Employe employe = employeService.getBaseMapper().selectOne(new QueryWrapper<Employe>().eq("employno", map.get("employno")).eq("deptno", Integer.parseInt(map.get("oldDeptno").toString())));
                    if (employe != null) {
                        map1.put("code", 202);
                        map1.put("msg", "该小组已有相同成员 轻移至其他小组");
                    } else {
                        if (map.get("deptno") == null || map.get("employno") == null || map.get("employemail") == null || map.get("employidcard") == null ||
                                map.get("employname") == null || map.get("employphone") == null || map.get("entryDate") == null ||
                                map.get("employsex") == null || map.get("employaddress") == null || map.get("employage") == null ||
                                map.get("employsalary") == null) {
                            map1.put("code", 202);
                            map1.put("msg", "缺少员工信息参数");
                        } else {
                            Employe upEmploye = new Employe();
                            upEmploye.setDeptno(Integer.parseInt(map.get("deptno").toString()));
                            upEmploye.setEmployemail(map.get("employemail").toString());
                            upEmploye.setEmployidcard(map.get("employidcard").toString());
                            upEmploye.setEmployname(map.get("employname").toString());
                            upEmploye.setEmployphone(map.get("employphone").toString());
                            upEmploye.setEntryDate(map.get("entryDate").toString());
                            upEmploye.setEmploysex(map.get("employsex").toString());
                            upEmploye.setEmployaddress(map.get("employaddress").toString());
                            upEmploye.setEmployage(map.get("employage").toString());
                            upEmploye.setEmploysalary(map.get("employsalary").toString());
                            boolean update = employeService.update(upEmploye, new UpdateWrapper<Employe>().
                                    eq("deptno", Integer.parseInt(map.get("oldDeptno").toString())).eq("employno", map.get("employno").toString()));
                            if (update) {
                                map1.put("code", 200);
                                map1.put("msg", "修改员工成功！");
                            } else {
                                map1.put("code", 202);
                                map1.put("msg", "修改员工失败！");
                            }
                        }
                    }
                } else {
//                正常修改员工
                    Employe upEmploye = new Employe();
                    upEmploye.setDeptno(Integer.parseInt(map.get("deptno").toString()));
                    upEmploye.setEmployemail(map.get("employemail").toString());
                    upEmploye.setEmployidcard(map.get("employidcard").toString());
                    upEmploye.setEmployname(map.get("employname").toString());
                    upEmploye.setEmployphone(map.get("employphone").toString());
                    upEmploye.setEntryDate(map.get("entryDate").toString());
                    upEmploye.setEmploysex(map.get("employsex").toString());
                    upEmploye.setEmployaddress(map.get("employaddress").toString());
                    upEmploye.setEmployage(map.get("employage").toString());
                    upEmploye.setEmploysalary(map.get("employsalary").toString());
                    boolean update = employeService.update(upEmploye, new UpdateWrapper<Employe>().
                            eq("deptno", Integer.parseInt(map.get("oldDeptno").toString())).eq("employno", map.get("employno").toString()));
                    if (update) {
                        map1.put("code", 200);
                        map1.put("msg", "修改员工成功！");
                    } else {
                        map1.put("code", 202);
                        map1.put("msg", "修改员工失败！");
                    }
                }
            } else {
//            添加员工信息
                if (map.get("deptno") == null || map.get("employemail") == null || map.get("employidcard") == null ||
                        map.get("employname") == null || map.get("employphone") == null || map.get("entryDate") == null ||
                        map.get("employsex") == null || map.get("employaddress") == null || map.get("employage") == null ||
                        map.get("employsalary") == null) {
                    map1.put("code", 202);
                    map1.put("msg", "缺少员工信息参数");
                } else {
                    Employe employe = new Employe();
                    employe.setDeptno(Integer.parseInt(map.get("deptno").toString()));
                    employe.setEmployemail(map.get("employemail").toString());
                    employe.setEmployidcard(map.get("employidcard").toString());
                    employe.setEmployname(map.get("employname").toString());
                    employe.setEmployphone(map.get("employphone").toString());
                    employe.setEntryDate(map.get("entryDate").toString());
                    employe.setEmploysex(map.get("employsex").toString());
                    employe.setEmployaddress(map.get("employaddress").toString());
                    employe.setEmployage(map.get("employage").toString());
                    employe.setEmploysalary(map.get("employsalary").toString());
                    int insert = employeService.getBaseMapper().insert(employe);
                    if (insert > 0) {
                        //插入新账号到账号表
                        Employe employe1 = employeService.getBaseMapper().selectOne(new QueryWrapper<Employe>().eq("employname", employe.getEmployname()).eq("deptno", employe.getDeptno()).eq("employidcard", employe.getEmployidcard()).eq("entryDate", employe.getEntryDate()));
                        if (employe1 != null) {
                            Eusers eusers = new Eusers();
                            eusers.setUsername(employe1.getEmployno().toString());

                            int insert1 = euserMapper.insert(eusers);
                            if (insert1 > 0) {
                                map1.put("code", 200);
                                map1.put("msg", "添加员工成功！");
                            }
                        } else {
                            map1.put("code", 202);
                            map1.put("msg", "添加员工账号失败请联系管理员！");
                        }

                    } else {
                        map1.put("code", 202);
                        map1.put("msg", "添加员工失败！");
                    }
                }
            }
        } else {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        }


        return map1;
    }

    //    删除员工信息并存入备份表
    @LoginToken
    @PostMapping("/api/deleteEmploy")
    public Map deleteEmploye(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("deptno") != null && map.get("employno") != null && map.get("user") != null) {
//        获取当前操作时间
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = new Date();
            String nowDate = dateFormat.format(date);
//          查询该员工信息
            Employe employe = employeService.getOne(new QueryWrapper<Employe>().eq("employno", map.get("employno"))
                    .eq("deptno", map.get("deptno")));
//            获取员工部门号
            Dept dept = deptService.getBaseMapper().selectOne(new QueryWrapper<Dept>().select("deptno").eq("id", map.get("deptno")));
            if (employe != null && dept.getDeptno() != null) {
//             如果有该员工则 插入备份表
                EmployeRedo employeRedo = new EmployeRedo();
                employeRedo.setDeptno(employe.getDeptno());
                employeRedo.setConfirmTime(nowDate);
                employeRedo.setDno(dept.getDeptno());
                employeRedo.setWhichDone(map.get("user").toString());
                employeRedo.setEmployaddress(employe.getEmployaddress());
                employeRedo.setEmployage(employe.getEmployage());
                employeRedo.setEmployemail(employe.getEmployemail());
                employeRedo.setEmployidcard(employe.getEmployidcard());
                employeRedo.setEmployname(employe.getEmployname());
                employeRedo.setEmployno(employe.getEmployno());
                employeRedo.setEmployphone(employe.getEmployphone());
                employeRedo.setEmploysalary(employe.getEmploysalary());
                employeRedo.setEmploysex(employe.getEmploysex());
                employeRedo.setEntryDate(employe.getEntryDate());
                employeRedo.setIsuse(employe.getIsuse());
                int insert = employeReDoMapper.insert(employeRedo);
                if (insert > 0) {
//                   插入备份表后执行删除操作
                    boolean remove = employeService.remove(new UpdateWrapper<Employe>().eq("employno", map.get("employno")).eq("deptno", map.get("deptno")));
                    if (remove) {
                        map1.put("code", 200);
                        map1.put("msg", "删除成功");
                    } else {
                        //删除失败则清除刚插入的备份表信息
                        employeReDoMapper.delete(new UpdateWrapper<EmployeRedo>().eq("employno", map.get("employno")).eq("deptno", map.get("deptno")));
                        map1.put("code", 202);
                        map1.put("msg", "删除失败!");
                    }
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "删除失败:备份信息失败");
                }
            } else {
                map1.put("code", 202);
                map1.put("msg", "删除失败:无该员工信息");
            }
        } else {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        }


        return map1;
    }

    //    关键字查找员工
    @LoginToken
    @GetMapping("/api/searchEmploy")
    public Map searchEmploye(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("keyword") == null || map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数!");
        } else {
            //根据关键字查找人
            List<Map<String, String>> emoloyes = employeService.getEmoloyeByKeyWord(map.get("keyword").toString(), (Integer.parseInt(map.get("page").toString()) - 1) * Integer.parseInt(map.get("size").toString()), Integer.parseInt(map.get("size").toString()));
            //获取一共有几个
            Integer count = employeService.getKeyWordSearchCount(map.get("keyword").toString());
            if (emoloyes != null && emoloyes.size() > 0 && count != 0) {
                map1.put("code", 200);
                map1.put("msg", "查找成功!");
                map1.put("employeInfo", emoloyes);
                map1.put("count", count);
            } else {
                map1.put("code", 202);
                map1.put("msg", "暂无所查员工信息!");
            }
        }
        return map1;
    }

    //获取员工工资明细
    @LoginToken
    @GetMapping("/api/getSaralyDetailInfo")
    public Map getSaralyDetailInfo(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("deptid") == null || map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少关键参数");
        } else {
            if (Integer.parseInt(map.get("page").toString()) < 1) {
                map1.put("code", 202);
                map1.put("msg", "参数出错！");
            } else {
                Integer page = ((Integer.parseInt(map.get("page").toString()) - 1) * Integer.parseInt(map.get("size").toString()));
//                获取员工信息
                List<EmployeSalaryDetail> list = employeSalaryDetailMapper.getEmployeSalaryByPage(Integer.parseInt(map.get("deptid").toString()), page, Integer.parseInt(map.get("size").toString()));
//                获取补贴信息
                List<EmployeSalary> detailList = employeSalaryDetailMapper.getDetail(Integer.parseInt(map.get("deptid").toString()));
//             获取部门员工数
                Integer count = employeSalaryDetailMapper.getCount(Integer.parseInt(map.get("deptid").toString()));
                if (list != null && list.size() > 0 && detailList != null && detailList.size() > 0 && count > 0) {
                    map1.put("code", 200);
                    map1.put("msg", "查询成功！");
                    map1.put("detailInfo", list);
                    map1.put("subDetail", detailList.get(0));
                    map1.put("count", count);
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "查询信息失败！");
                }
            }
        }

        return map1;

    }

    //修改员工工资明细
    @LoginToken
    @PostMapping("/api/updateSalaryDetail")
    public Map updateSalaryDetail(@RequestBody ArrayList<EmployeSalaryDetail> list) {
        Map map1 = new HashMap();
        if (list.size() == 0 || list.get(0) == null) {
            map1.put("code", 202);
            map1.put("msg", "参数异常！");
        } else {
            EmployeSalaryDetail employeSalaryDetail = new EmployeSalaryDetail();
//            统计几个人
            int count = 0;
//            统计成功次数
            int successCount = 0;
            for (EmployeSalaryDetail i : list) {
                count++;
                int update = employeSalaryDetailMapper.update(i, new UpdateWrapper<EmployeSalaryDetail>().eq("deptno", i.getDeptno()).eq("employno", i.getEmployno()));
                if (update > 0) {
                    successCount++;
                }
//                如果次数相等说明全部修改成功
                if (count == successCount) {
                    map1.put("code", 200);
                    map1.put("msg", "修改员工信息成功！");
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "修改员工信息失败！");
                }
            }
        }

        return map1;
    }

    //    获取全部员工信息
    @LoginToken
    @GetMapping("/api/getAllEmploye")
    public Map getAllEmploye() {
        Map map = new HashMap();
        List<Map<String, String>> list = employeService.getEmployeDistinct();
        if (list != null && list.size() > 0) {
            map.put("code", 200);
            map.put("employeInfo", list);
            map.put("msg", "获取员工成功！");
        } else {
            map.put("code", 202);
            map.put("msg", "获取员工失败！");
        }
        return map;
    }

    //查看删除的员工信息
    @LoginToken
    @GetMapping("/api/getDeletedEmploye")
    public Map getDeletedEmploye(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少参数！");
        } else {
            Page<EmployeRedo> userPage = new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
            Page<EmployeRedo> employeRedoPage = employeReDoMapper.selectPage(userPage, new QueryWrapper<EmployeRedo>(null));
            if (employeRedoPage != null && employeRedoPage.getTotal() > 0) {
                List<EmployeRedo> EmployeRedoList = employeRedoPage.getRecords();
                Integer count = employeReDoMapper.selectCount(new QueryWrapper<EmployeRedo>(null));
                map1.put("code", 200);
                map1.put("results", EmployeRedoList);
                map1.put("count", count);
            } else {
                map1.put("code", 202);
                map1.put("msg", "暂无删除的员工");
            }

        }


        return map1;
    }

    // 撤回删除的员工操作
    @LoginToken
    @PostMapping("/api/rebackEmploye")
    public Map rebackEmploye(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("deptno") == null || map.get("employno") == null || map.get("employname") == null || map.get("employage") == null
                || map.get("employsex") == null || map.get("employidcard") == null || map.get("employphone") == null || map.get("employemail") == null
                || map.get("entryDate") == null || map.get("employsalary") == null || map.get("employaddress") == null || map.get("isuse") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少参数！");
        } else {
            Employe employe = new Employe();
            employe.setDeptno(Integer.parseInt(map.get("deptno").toString()));
            employe.setIsuse(map.get("isuse").toString());
            employe.setEntryDate(map.get("entryDate").toString());
            employe.setEmploysex(map.get("employsex").toString());
            employe.setEmploysalary(map.get("employsalary").toString());
            employe.setEmployphone(map.get("employphone").toString());
            employe.setEmployno(Integer.parseInt(map.get("employno").toString()));
            employe.setEmployname(map.get("employname").toString());
            employe.setEmployidcard(map.get("employidcard").toString());
            employe.setEmployemail(map.get("employemail").toString());
            employe.setEmployage(map.get("employage").toString());
            employe.setEmployaddress(map.get("employaddress").toString());
//            删除备份表数据
            int delete = employeReDoMapper.delete(new QueryWrapper<EmployeRedo>()
                    .eq("employno", Integer.parseInt(map.get("employno").toString())).eq("deptno", Integer.parseInt(map.get("deptno").toString())));
            if (delete > 0) {
//                恢复员工数据
                int insert = employeService.getBaseMapper().insert(employe);
                if (insert > 0) {
                    map1.put("code", 200);
                    map1.put("msg", "恢复成功！");
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "恢复失败！");
                }
            } else {
                map1.put("code", 202);
                map1.put("msg", "恢复失败！");
            }
        }
        return map1;
    }


    //    获取打卡的员工信息
    @LoginEmployeToken
    @PostMapping("/api/getEmployeInfo")
    public Map getEmployeInfo(@RequestBody Map map1) {
        Map map = new HashMap();
        if (map1.get("employeno") == null) {
            map.put("code", 202);
            map.put("msg", "缺少参数！");
        } else {
            List<Map<String, String>> employeInfo = employeService.getEmployeInfo(Integer.parseInt(map1.get("employeno").toString()));
            if (employeInfo != null && employeInfo.size() > 0) {
                map.put("code", 200);
                map.put("employeInfo", employeInfo);
            } else {
                map.put("code", 202);
                map.put("msg", "获取信息失败！");
            }
        }

        return map;
    }

    //    修改密码
    @LoginEmployeToken
    @PostMapping("/api/updateEuserPassword")
    public Map updateEuserPassword(@RequestBody Map map1) {
        Map map = new HashMap();
        if (map1.get("nowPassword") == null || map1.get("newPassword") == null || map1.get("user") == null) {
            map.put("code", 202);
            map.put("msg", "修改密码失败：缺少重要参数");
        } else {
            String nowPassword = map1.get("nowPassword").toString();
            String newPassword = map1.get("newPassword").toString();
            String username = map1.get("user").toString();
            if (newPassword.equals(nowPassword)) {
                map.put("code", 202);
                map.put("msg", "新旧密码不能相同！");
            } else {
//                判断原密码是否正确
                Eusers euser = euserMapper.selectOne(new QueryWrapper<Eusers>().eq("username", username));
                if (euser.getPassword().equals(nowPassword)) {
//                  原密码正确
                    euser.setPassword(newPassword);
                    int update = euserMapper.update(euser, new UpdateWrapper<Eusers>().eq("username", username));
                    if (update > 0) {
                        map.put("code", 200);
                        map.put("msg", "修改密码成功！");
                    }
                } else {
                    map.put("code", 202);
                    map.put("msg", "旧密码错误！");
                }
            }
        }

        return map;
    }


    //    获取全部员工账号分页
    @LoginToken
    @PostMapping("/api/getEuses")
    public Map getEusesPage(@RequestBody Map map1) {
        Map map = new HashMap();
        if (map1.get("page") == null || map1.get("size") == null) {
            map.put("code", 202);
            map.put("msg", "缺少参数！");
        } else {
            Page<Eusers> ePage = new Page<>(Integer.parseInt(map1.get("page").toString()), Integer.parseInt(map1.get("size").toString()));
            Page<Eusers> eusersPage = euserMapper.selectPage(ePage, new QueryWrapper<Eusers>(null));
            if (eusersPage != null && eusersPage.getTotal() > 0) {
                List<Eusers> eusersList = eusersPage.getRecords();
                Integer count = euserMapper.selectCount(new QueryWrapper<Eusers>(null));
                map.put("code", 200);
                map.put("eusersList", eusersList);
                map.put("count", count);
            } else {
                map.put("code", 202);
                map.put("msg", "暂无员工账号");
            }
        }
        return map;
    }

    //关键字查找
    @LoginToken
    @PostMapping("/api/keyWordSearch")
    public Map keyWordSearch(@RequestBody Map map1) {
        Map map = new HashMap();
        int page = (Integer.parseInt(map1.get("page").toString()) - 1) * Integer.parseInt(map1.get("size").toString());
        List<Map<String, String>> keyWordList = euserMapper.getEusersInfoByKeyWord(map1.get("keyword").toString(), map1.get("type").toString(), page, Integer.parseInt(map1.get("size").toString()));
        Integer count = euserMapper.getEusersInfoByKeyWordCount(map1.get("keyword").toString(), map1.get("type").toString(), page, Integer.parseInt(map1.get("size").toString()));
        map.put("code", 200);
        map.put("eusersList", keyWordList);
        map.put("count", count);

        return map;
    }

    //重置账号
    @LoginToken
    @PostMapping("/api/resetEuser")
    public Map resetEuser(@RequestBody Map map1) {
        Map map = new HashMap();
        if (map1.get("username") == null) {
            map.put("code", 202);
            map.put("msg", "缺少参数！");
        } else {
            Eusers eusers = new Eusers();
            eusers.setIslock("0");
            eusers.setPassword("88888888");
            int update = euserMapper.update(eusers, new UpdateWrapper<Eusers>().eq("username", map1.get("username")));
            if (update > 0) {
                map.put("code", 200);
                map.put("msg", "重置成功");
            } else {
                map.put("code", 202);
                map.put("msg", "重置失败");
            }

        }

        return map;
    }

    //修改账号
    @LoginToken
    @PostMapping("/api/updateEuser")
    public Map blockOrFreeEuser(@RequestBody Map map1) {
        Map map = new HashMap();
        Eusers eusers = new Eusers();
        if (map1.get("username") == null || map1.get("type") == null || map1.get("password") == null) {
            map.put("code", 202);
            map.put("msg", "缺少参数！");
        } else {
            eusers.setPassword(map1.get("password").toString());
            if ("block".equals(map1.get("type"))) {
                eusers.setIslock("1");
                int update = euserMapper.update(eusers, new UpdateWrapper<Eusers>().eq("username", map1.get("username")));
                if (update > 0) {
                    map.put("code", 200);
                    map.put("msg", "操作成功！");
                } else {
                    map.put("code", 202);
                    map.put("msg", "操作失败！");
                }
            } else {
                eusers.setIslock("0");
                int update = euserMapper.update(eusers, new UpdateWrapper<Eusers>().eq("username", map1.get("username")));
                if (update > 0) {
                    map.put("code", 200);
                    map.put("msg", "操作成功！");
                } else {
                    map.put("code", 202);
                    map.put("msg", "操作失败！");
                }
            }

        }
        return map;
    }

}

