package com.example.webapp.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.webapp.bean.*;
import com.example.webapp.mapper.clockMapper;
import com.example.webapp.mapper.depallTreeMapper;
import com.example.webapp.mapper.employeLeaveMapper;
import com.example.webapp.mapper.noticeMapper;
import com.example.webapp.service.depallService;
import com.example.webapp.service.deptService;
import com.example.webapp.service.employeService;
import com.example.webapp.util.LoginEmployeToken;
import com.example.webapp.util.LoginToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.*;

@RestController
public class attendanceController {
    @Autowired
    employeLeaveMapper employeLeaveMapper;
    @Autowired
    depallService depallService;
    @Autowired
    deptService deptService;
    @Autowired
    employeService employeService;
    @Autowired
    clockMapper clockMapper;
    @Autowired
    depallTreeMapper depallTreeMapper;
    @Autowired
    noticeMapper noticeMapper;

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

    //请假申请
    @LoginEmployeToken
    @PostMapping("/api/reqAddLeave")
    public Map addLeave(@RequestBody Map map1) {
        Map map = new HashMap<>();
        if (map1.get("deptid") == null || map1.get("dno") == null || map1.get("employename") == null || map1.get("employeno") == null ||
                map1.get("leaveLong") == null || map1.get("leaveWhen") == null || map1.get("whyLeave") == null || map1.get("postTime") == null) {
            map.put("code", 202);
            map.put("msg", "缺少请求参数");
        } else {
            EmployeLeave employeLeave = new EmployeLeave();
            employeLeave.setDeptid(Integer.parseInt(map1.get("deptid").toString()));
            employeLeave.setDno(Integer.parseInt(map1.get("dno").toString()));
            employeLeave.setEmployeno(Integer.parseInt(map1.get("employeno").toString()));
            employeLeave.setLeaveLong(map1.get("leaveLong").toString());
            employeLeave.setLeaveWhen(map1.get("leaveWhen").toString());
            employeLeave.setWhyLeave(map1.get("whyLeave").toString());
            employeLeave.setEmployename(map1.get("employename").toString());
            employeLeave.setPostTime(map1.get("postTime").toString());
            int insert = employeLeaveMapper.insert(employeLeave);
            if (insert > 0) {
                map.put("code", 200);
                map.put("msg", "申请成功");
            } else {
                map.put("code", 202);
                map.put("msg", "申请失败");
            }
        }
        return map;
    }

    //    员工获取请假申请
    @LoginEmployeToken
    @PostMapping("/api/getEmployeLeaveInfo")
    public Map getEmployeLeaveInfo(@RequestBody Map map1) {
        Map map = new HashMap<>();
        if (map1.get("employeno") == null) {
            map.put("code", 202);
            map.put("msg", "缺少请求参数");
        } else {
            List<EmployeLeave> employeLeaveListInfo = employeLeaveMapper.selectList(new QueryWrapper<EmployeLeave>(null).eq("employeno", Integer.parseInt(map1.get("employeno").toString())));
            if (employeLeaveListInfo != null && employeLeaveListInfo.size() > 0) {
                map.put("code", 200);
                map.put("employeLeaveListInfo", employeLeaveListInfo);
                map.put("count", employeLeaveListInfo.size());
            } else {
                map.put("code", 200);
                map.put("employeLeaveListInfo", null);
                map.put("count", 0);
            }
        }
        return map;
    }

    //判断员工是否打了卡
    @LoginEmployeToken
    @PostMapping("/api/clockMorning")
    public Map clockMorningInfo(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("employeno") == null || map.get("clockTime") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少请求参数");
        } else {
            List<ClockInfo> clockMorning = clockMapper.isClockMorning(Integer.parseInt(map.get("employeno").toString()), map.get("clockTime").toString());
            List<ClockInfo> clockAfter = clockMapper.isClockAfter(Integer.parseInt(map.get("employeno").toString()), map.get("clockTime").toString());
            if (clockMorning != null && clockMorning.size() > 0) {
                map1.put("code", 200);
                map1.put("clockMorning", true);
                if (clockAfter != null && clockAfter.size() > 0) {
                    map1.put("clockAfter", true);
                } else {
                    map1.put("clockAfter", false);
                }
            } else {
                map1.put("code", 200);
                map1.put("clockMorning", false);
                if (clockAfter != null && clockAfter.size() > 0) {
                    map1.put("clockAfter", true);
                } else {
                    map1.put("clockAfter", false);
                }
            }
        }
        return map1;
    }

    //    添加打卡信息
    @LoginEmployeToken
    @PostMapping("/api/saveclock")
    public Map saveClock(@RequestBody List<ClockInfo> clockInfoList) {
        Map map1 = new HashMap();
        int count = 0, successCount = 0;
        for (ClockInfo i : clockInfoList) {
            count++;
            int insert = clockMapper.insert(i);
            if (insert > 0) {
                successCount++;
            }
        }
        if (successCount == count) {
            map1.put("code", 200);
            map1.put("msg", "打卡成功!");
        } else {
            map1.put("code", 202);
            map1.put("msg", "打卡失败!");
        }
        return map1;
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
            Page<EmployeLeave> deptRedoPage = employeLeaveMapper.selectPage(Page, new QueryWrapper<EmployeLeave>().orderByDesc("leaveWhen"));
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
    public Map clockInfo(@RequestParam Map map1) {

        Map map = new HashMap();
        if (map1.get("page") == null || map1.get("size") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            int page = (Integer.parseInt(map1.get("page").toString()) - 1) * Integer.parseInt(map1.get("size").toString());
            //        全部打卡情况
            List<Map<String, String>> todayAllInfo = clockMapper.getTodayAllInfo(page, Integer.parseInt(map1.get("size").toString()));
            //        上下班打卡情况
            List<Map<String, String>> todayMorningInfo = clockMapper.getTodayMorningInfo();
//            上班人数
            List<Map<String, String>> todayAfterInfo = clockMapper.getTodayAfterInfo();
            int delayCount = clockMapper.getClockDelayCount();
            int todayMorningCount = clockMapper.getTodayClockMorningCount();
            int deptCount = depallService.count();
            int allClockCount = clockMapper.getTodayClockCount();
            NumberFormat numberFormat = NumberFormat.getInstance();
            numberFormat.setMaximumFractionDigits(2);
            String Percentage = "";
            if (delayCount > 0 && todayMorningCount > 0) {
                Percentage = numberFormat.format((float) delayCount / (float) todayMorningCount * 100) + "%";
            } else {
                Percentage = "0%";
            }
            map.put("code", 200);
            map.put("todayAllInfo", todayAllInfo);
            map.put("AllClockCount", allClockCount);
            map.put("DeptMorningCount", todayMorningInfo.size());
            map.put("todayMorningInfo", todayMorningInfo);
            map.put("todayAfterInfo", todayAfterInfo);
            map.put("DeptAfterCount", todayAfterInfo.size());
            map.put("AllDeptCount", deptCount);
            map.put("delayCount", delayCount);
            map.put("delayPercentage", Percentage);
        }

        return map;
    }

    //获取基本信息
    @LoginToken
    @GetMapping("/api/getBaseInfo")
    public Map baseInfo() {
        Map map = new HashMap();
        //        部门数量
        int depallCount = depallService.count();
//        小组数量
        int deptCount = deptService.count();
//        员工总数
        int employeCount = employeService.getCount();
//        本月打卡人数
        int monthClockCount = clockMapper.getMonthClockCount();
//        本月迟到人数
        int monthClockDelayCount = clockMapper.getMonthClockDelayCount();
        NumberFormat numberFormat = NumberFormat.getInstance();
        numberFormat.setMaximumFractionDigits(2);
        String Percentage = numberFormat.format((float) monthClockDelayCount / (float) monthClockCount * 100) + "%";
        map.put("code", 200);
        map.put("depallCount", depallCount);
        map.put("deptCount", deptCount);
        map.put("employeCount", employeCount);
        map.put("monthClockCount", monthClockCount);
        map.put("monthClockDelayCount", monthClockDelayCount);
        map.put("monthClockPercentage", Percentage);
        return map;
    }

    //    条形图数据
    @LoginToken
    @GetMapping("/api/getBaseChartsInfo")
    public Map baseChartsInfo() {
        Map map = new HashMap();
//        本月打卡人数
        int monthClockCount = clockMapper.getMonthClockCount();
//        本月迟到人数
        int monthClockDelayCount = clockMapper.getMonthClockDelayCount();
//    本周打卡人数
        Integer weekClockCount = clockMapper.getWeekClockCount();
        //    本周打卡迟到人数
        Integer weekClockDelayCount = clockMapper.getWeekClockDelayCount();
        //    本日打卡人数
        Integer todayClockCount = clockMapper.getTodayClockCount();
        //    本日打卡迟到人数
        Integer todayClockDelayCount = clockMapper.getTodayClockDelayCount();
        List dataList = new ArrayList();
        dataList.add(todayClockCount);
        dataList.add(todayClockDelayCount);
        dataList.add(monthClockCount);
        dataList.add(monthClockDelayCount);
        dataList.add(weekClockCount);
        dataList.add(weekClockDelayCount);

        List titleList = new ArrayList();
        titleList.add("日打卡数");
        titleList.add("日迟到数");
        titleList.add("月打卡数");
        titleList.add("月迟到数");
        titleList.add("周打卡数");
        titleList.add("周迟到数");

        map.put("code", 200);
        map.put("dataList", dataList);
        map.put("titleList", titleList);

        return map;
    }

    //    获取今日打卡员工信息
    @LoginToken
    @GetMapping("/api/getTodayClockEmployeInfo")
    public Map getTodayClockEmployeInfo(@RequestParam Map map1) {
        Map map = new HashMap();
        if (map1.get("dno") == null || map1.get("page") == null || map1.get("size") == null || map1.get("type") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            if ("morning".equals(map1.get("type"))) {
                int page = (Integer.parseInt(map1.get("page").toString()) - 1) * Integer.parseInt(map1.get("size").toString());
                List<Map<String, String>> todayClockEmployeInfo = clockMapper.getTodayClockEmployeInfoPage(Integer.parseInt(map1.get("dno").toString()), "上午", page, Integer.parseInt(map1.get("size").toString()));
                map.put("code", 200);
                map.put("todayClockEmployeInfo", todayClockEmployeInfo);
            } else {
                int page = (Integer.parseInt(map1.get("page").toString()) - 1) * Integer.parseInt(map1.get("size").toString());
                List<Map<String, String>> todayClockEmployeInfo = clockMapper.getTodayClockEmployeInfoPage(Integer.parseInt(map1.get("dno").toString()), "下午", page, Integer.parseInt(map1.get("size").toString()));
                map.put("code", 200);
                map.put("todayClockEmployeInfo", todayClockEmployeInfo);
            }
        }
        return map;
    }

    //今日正常打卡信息
    @LoginToken
    @GetMapping("/api/getTodayClockNormalInfo")
    public Map getTodayClockNormalInfo(@RequestParam Map map1) {
        Map map = new HashMap();
        if (map1.get("page") == null || map1.get("size") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            int page = (Integer.parseInt(map1.get("page").toString()) - 1) * Integer.parseInt(map1.get("size").toString());
            List<Map<String, String>> todayNormalClockInfo = clockMapper.getTodayNormalClockInfo(page, Integer.parseInt(map1.get("size").toString()));
            int normalCount = clockMapper.getTodayNormalCount();
            map.put("code", 200);
            map.put("todayNormalClockInfo", todayNormalClockInfo);
            map.put("normalCount", normalCount);
        }
        return map;
    }

    //今日迟到打卡信息
    @LoginToken
    @GetMapping("/api/getTodayClockDelayInfo")
    public Map getTodayClockDelayInfo(@RequestParam Map map1) {
        Map map = new HashMap();
        if (map1.get("page") == null || map1.get("size") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            int page = (Integer.parseInt(map1.get("page").toString()) - 1) * Integer.parseInt(map1.get("size").toString());
            List<Map<String, String>> todayDelayClockInfo = clockMapper.getTodayDelayClockInfo(page, Integer.parseInt(map1.get("size").toString()));
            int delaycount = clockMapper.getClockDelayCount();
            map.put("code", 200);
            map.put("todayDelayClockInfo", todayDelayClockInfo);
            map.put("delaycount", delaycount);
        }
        return map;
    }

    //    今日请假信息
    @LoginToken
    @GetMapping("/api/getTodayLeaveInfo")
    public Map getTodayLeaveInfo(@RequestParam Map map1) {
        Map map = new HashMap();
        if (map1.get("page") == null || map1.get("size") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            int page = (Integer.parseInt(map1.get("page").toString()) - 1) * Integer.parseInt(map1.get("size").toString());
            List<Map<String, String>> todayLeaveInfo = clockMapper.getTodayLeaveInfo(page, Integer.parseInt(map1.get("size").toString()));
            int leaveCount = clockMapper.getTodayLeaveCount();
            map.put("code", 200);
            map.put("todayLeaveInfo", todayLeaveInfo);
            map.put("leaveCount", leaveCount);
        }
        return map;
    }

    //获取打卡信息左侧树结构的数据
    @LoginToken
    @GetMapping("/api/getTreeInfo")
    public Map getTreeInfo(@RequestParam Map map1) {
        Map map = new HashMap();
        List<Map<String, String>> depallTreeList = depallTreeMapper.getAllDepall();
        if (depallTreeList != null && depallTreeList.size() > 0) {
            // 循环遍历
            for (Map m : depallTreeList) {
                List<Map<String, String>> deptList = depallTreeMapper.getChildrenDeptInfo(Integer.parseInt(m.get("key").toString()));
                m.put("children", deptList);
            }
            map.put("code", 200);
            map.put("depallTreeList", depallTreeList);

        } else {
            map.put("code", 202);
            map.put("msg", "暂无部门信息");
        }
        return map;
    }

    //    获取指定打卡数据
    @LoginToken
    @PostMapping("/api/getClockInfoTree")
    public Map getClockInfoByTree(@RequestBody Map map1) {
        Map map = new HashMap();
        if (map1.get("dno") == null || map1.get("isDepall") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            if (Boolean.parseBoolean(map1.get("isDepall").toString())) {
//                获取部门全部数据
                List<ClockInfo> depallClockInfo = clockMapper.selectList(new QueryWrapper<ClockInfo>().eq("dno", map1.get("dno")).orderByDesc("clockTime"));
                map.put("code", 200);
                map.put("ClockInfo", depallClockInfo);
                map.put("count", depallClockInfo.size());
            } else {
                List<ClockInfo> deptClockInfo = clockMapper.selectList(new QueryWrapper<ClockInfo>().eq("dno", map1.get("dno")).eq("deptid", map1.get("deptId")).orderByDesc("clockTime"));
                map.put("code", 200);
                map.put("ClockInfo", deptClockInfo);
                map.put("count", deptClockInfo.size());
            }
        }
        return map;
    }

    //    修改打卡数据
    @LoginToken
    @PostMapping("/api/editClockInfo")
    public Map editClockInfo(@RequestBody Map map1) {
        Map map = new HashMap();
        if (map1.get("dno") == null || map1.get("deptId") == null || map1.get("employeno") == null ||
                map1.get("clockTime") == null || map1.get("originClockTime") == null || map1.get("type") == null
                || map1.get("employename") == null || map1.get("employeno") == null) {
            map.put("code", 202);
            map.put("msg", "缺少重要参数");
        } else {
            ClockInfo clockInfo = new ClockInfo();
            clockInfo.setEmployename(map1.get("employename").toString());
            clockInfo.setEmployeno(Integer.parseInt(map1.get("employeno").toString()));
            clockInfo.setClockTime(map1.get("clockTime").toString());
            clockInfo.setDno(Integer.parseInt(map1.get("dno").toString()));
            clockInfo.setDeptid(Integer.parseInt(map1.get("deptId").toString()));
            clockInfo.setType(map1.get("type").toString());
            int update = clockMapper.update(clockInfo, new UpdateWrapper<ClockInfo>().eq("clockTime", map1.get("originClockTime"))
                    .eq("type", clockInfo.getType()).eq("dno", clockInfo.getDno()).eq("employeno", clockInfo.getEmployeno()).eq("deptid", clockInfo.getDeptid()));
            if (update > 0) {
                map.put("code", 200);
                map.put("msg", "修改打卡信息成功");
            } else {
                map.put("code", 202);
                map.put("msg", "修改打卡信息失败");
            }
        }
        return map;
    }

    //   删除图片
    @LoginToken
    @PostMapping("/api/deleteNoticeImage")
    public Map deleteNoticeImage(@RequestBody Map<String, List<String>> list, HttpServletRequest request) throws IOException {
        Map map1 = new HashMap();
//        设置存放文件路径
        String publicPath = System.getProperty("user.dir") + "/src/main/resources/images/Notice/";
        int count = 0, deleteCount = 0;
        for (String i : list.get("deleteList")) {
            count++;
            String originFileName = i.split(request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort() + "/images/Notice/")[1];
            File file = new File(publicPath + originFileName);
            if (file.exists()) {
                boolean delete = file.delete();
                if (delete) {
                    deleteCount++;
                }
            }
        }
        if (count == deleteCount) {
            map1.put("code", 200);
            map1.put("msg", "删除多余图片成功!");
        } else {
            map1.put("code", 202);
            map1.put("msg", "删除多余图片失败!有可能是因为所删图片不存在！");

        }
        return map1;
    }


    //    公告上传图片api
//    @LoginToken
    @PostMapping("/api/uploadImgNotice")
    public Map editDeptR(@RequestParam Map map, @RequestParam("file") MultipartFile file, HttpServletRequest request) throws IOException {
        Map map1 = new HashMap();
        if (!file.isEmpty()) {
//               获取到头像 然后将头像名字随机赋值 存到resources文件夹
            String avatarName = "uploadNoticeZengYu" + (int) (Math.random() * 114514) + file.getOriginalFilename();
//                设置存放文件路径
            String publicPath = System.getProperty("user.dir") + "/src/main/resources/images/Notice/";
//               判断文件是否同名存在
            File isFile = new File("classpath:/images/Notice/", avatarName);
            if (isFile.exists()) {
//                如果存在相同文件 则修改文件名再存
                avatarName = "uploadNoticeZengYu" + (int) (Math.random() * 114515) + "SAFE" + file.getOriginalFilename();
                file.transferTo(new File(publicPath + avatarName));
                map1.put("code", 200);
                map1.put("msg", "success");
                map1.put("url", request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort() + "/images/Notice/" + avatarName);
            } else {
//                执行插入图片操作
                file.transferTo(new File(publicPath + avatarName));
                map1.put("code", 200);
                map1.put("msg", "success");
                map1.put("url", request.getScheme() + "://" + request.getServerName() + ':' + request.getServerPort() + "/images/Notice/" + avatarName);
            }
        } else {
            map1.put("code", 202);
            map1.put("msg", "缺少图片参数");
        }

        return map1;
    }

    //获取公告信息
    @LoginToken
    @GetMapping("/api/getAllNoticeInfo")
    public Map getAllNoticeInfo(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("page") == null || map.get("size") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            Page<Notice> page = new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
            Page<Notice> noticePage = noticeMapper.selectPage(page, new QueryWrapper<Notice>().orderByDesc("postTime"));
            Integer allCount = noticeMapper.selectCount(new QueryWrapper<>(null));
            map1.put("code", 200);
            map1.put("noticeInfo", noticePage.getRecords());
            map1.put("count", allCount);
        }
        return map1;
    }


    //    添加公告
    @LoginToken
    @PostMapping("/api/addNotice")
    public Map addNotice(@RequestBody Map list, HttpServletRequest request) throws IOException {
        Map map1 = new HashMap();
        if (list.get("isAll") == null || list.get("thyme") == null || list.get("content") == null
                || list.get("startTime") == null || list.get("endTime") == null || list.get("postMan") == null || list.get("postTime") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            if (Boolean.parseBoolean(list.get("isAll").toString())) {
//全部公告
                Notice notice = new Notice();
                notice.setContent(list.get("content").toString());
                notice.setIsAll("true");
                notice.setThyme(list.get("thyme").toString());
                notice.setStartTime(list.get("startTime").toString());
                notice.setEndTime(list.get("endTime").toString());
                notice.setPostMan(list.get("postMan").toString());
                notice.setPostTime(list.get("postTime").toString());
                try {
                    int insert = noticeMapper.insert(notice);
                    if (insert > 0) {
                        map1.put("code", 200);
                        map1.put("msg", "添加公告成功!");
                    }
                } catch (Exception e) {
                    map1.put("code", 202);
                    map1.put("msg", "添加公告失败!");
                    map1.put("Exception", e.toString());
                }


            } else {
                JSONArray array = JSON.parseArray(JSON.toJSONString(list.get("specialArray")));
                int count = 0, successCount = 0;
                for (Object i : array) {
                    count++;
                    Map selectMap = (Map) i;
//           支线插入操作
                    Notice notice = new Notice();
                    notice.setContent(list.get("content").toString());
                    notice.setIsAll("false");
                    notice.setThyme(list.get("thyme").toString());
                    notice.setSpecialDno(Integer.parseInt(selectMap.get("dno").toString()));
                    notice.setSpecialDeptId(Integer.parseInt(selectMap.get("value").toString()));
                    notice.setStartTime(list.get("startTime").toString());
                    notice.setEndTime(list.get("endTime").toString());
                    notice.setPostMan(list.get("postMan").toString());
                    notice.setPostTime(list.get("postTime").toString());
                    int insert = noticeMapper.insert(notice);
                    if (insert > 0) {
                        successCount++;
                    }
                }
                if (count == successCount) {
                    map1.put("code", 200);
                    map1.put("msg", "添加公告成功!");
                } else {
                    map1.put("code", 202);
                    map1.put("msg", "部分部门公告添加失败!");
                }
            }

        }

        return map1;
    }

    //    删除公告
    @LoginToken
    @PostMapping("/api/deleteNotice")
    public Map deleteNoticeInfo(@RequestBody Map map) {
        Map map1 = new HashMap();
        if (map.get("id") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            int delete = noticeMapper.delete(new UpdateWrapper<Notice>().eq("id", Integer.parseInt(map.get("id").toString())));
            if (delete > 0) {
                map1.put("code", 200);
                map1.put("msg", "删除公告成功!");
            } else {
                map1.put("code", 202);
                map1.put("msg", "删除公告失败!");
            }
        }
        return map1;
    }

    //    关键字查找公告分页
    @LoginToken
    @GetMapping("/api/getNoticeByKeyWord")
    public Map getNoticeByKeyWord(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("page") == null || map.get("size") == null || map.get("keyWord") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            int limit = (Integer.parseInt(map.get("page").toString()) - 1) * Integer.parseInt(map.get("size").toString());
            List<Map<String, String>> noticeInfo = noticeMapper.getNoticeBykeyWordPage(map.get("keyWord").toString(), limit, Integer.parseInt(map.get("size").toString()));
            Integer count = noticeMapper.getNoticeCountByKeyWord(map.get("keyWord").toString());
            map1.put("code", 200);
            map1.put("noticeInfo", noticeInfo);
            map1.put("count", count);

        }
        return map1;
    }


    //    分页获取公告 ---员工
    @LoginEmployeToken
    @GetMapping("/api/getNoticeEmploye")
    public Map getNoticeEmploye(@RequestParam Map map) {
        Map map1 = new HashMap();
        if (map.get("page") == null || map.get("size") == null || map.get("employeno") == null) {
            map1.put("code", 202);
            map1.put("msg", "缺少重要参数");
        } else {
            Page<Notice> page = new Page<>(Integer.parseInt(map.get("page").toString()), Integer.parseInt(map.get("size").toString()));
            Page<Notice> noticeAllInfo = noticeMapper.selectPage(page, new QueryWrapper<Notice>().orderByDesc("postTime").eq("isAll", "true"));
            int limit = (Integer.parseInt(map.get("page").toString()) - 1) * Integer.parseInt(map.get("size").toString());
            List<Map<String, String>> noticeEmploye = noticeMapper.getNoticeEmploye(Integer.parseInt(map.get("employeno").toString()), limit, Integer.parseInt(map.get("size").toString()));
            List<Map<String, String>> todayNoticeInfo = noticeMapper.getTodayNoticeEmploye(Integer.parseInt(map.get("employeno").toString()));
            Integer allNoticeCount = noticeMapper.selectCount(new QueryWrapper<Notice>().eq("isAll", "true"));
            Integer specialNoticeCount = noticeMapper.getSpecialNoticeCount(Integer.parseInt(map.get("employeno").toString()));

            map1.put("code", 200);
            map1.put("allNotice", noticeAllInfo.getRecords());
            map1.put("specialNotice", noticeEmploye);
            map1.put("todayNoticeInfo", todayNoticeInfo);
            map1.put("allNoticeCount", allNoticeCount);
            map1.put("specialNoticeCount", specialNoticeCount);
            map1.put("todayNoticeInfoCount", todayNoticeInfo.size());
        }
        return map1;
    }

}