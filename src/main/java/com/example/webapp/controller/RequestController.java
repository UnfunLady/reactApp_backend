//package com.example.webapp.controller;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.example.webapp.bean.user;
//import com.example.webapp.mapper.userMapper;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//import com.example.webapp.service.userService;
//
//import javax.servlet.http.HttpServletRequest;
//
//import java.io.File;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//
//@Slf4j
//@Controller
//public class RequestController {
//
//    @GetMapping("/goto")
//    public String goToPage(HttpServletRequest request) {
//        request.setAttribute("msg", "success!!");
//        request.setAttribute("code", 200);
////        转发到项目下
//        return "forward:/success";
//    }
//
//    @ResponseBody
//    @GetMapping("/success")
//    public Map success(@RequestAttribute("msg") String msg, HttpServletRequest request) {
//        Object obj = request.getAttribute("code");
//        System.out.println("code:" + obj);
//        System.out.println("msg:" + msg);
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", obj);
//        map.put("msg", msg);
//        return map;
//    }
//
//    //    分号分来就是矩阵变量
//    @GetMapping("/goto/sell")
////    默认禁用矩阵变量
//
//    public Map juzheng(@MatrixVariable("low") Integer low, @MatrixVariable("brand") List<String> brand) {
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("low", low);
//        map.put("brand", brand);
//        return map;
//    }
//
//    @ResponseBody
//    @PostMapping("/uploadImg")
//    @CrossOrigin
//    public String uploadImg(@RequestParam("file") MultipartFile file) throws IOException {
//        System.out.println(file);
//        if (!file.isEmpty()) {
//            log.info(file.getOriginalFilename());
//            file.transferTo(new File("C:\\Users\\Administrator\\Desktop\\test\\webapp\\src\\main\\java\\com\\example\\webapp\\images\\" + file.getOriginalFilename()));
//        }
//        return "main";
//    }
//
//    @ResponseBody
//    @PostMapping("/uploadf")
//    public String uploadImg1(@RequestBody String content) throws IOException {
//        System.out.println(content);
//        return "main";
//    }
//
//    @Autowired
//    userService userService;
//
//    @GetMapping("/getUser")
//    @ResponseBody
//    @CrossOrigin
//    public List<user> getUser(@RequestParam("id") Integer id,
//                              @RequestParam(value = "page", defaultValue = "1") Integer page,
//                              @RequestParam("size") Integer size) {
//
//        List<user> list = userService.list();
//        user users = new user("unfun", 3, 20);
////        List<user> listUser=new ArrayList<>();
////        user users1=new user("unfun1",4,20);
////        user users2=new user("unfun2",5,20);
////        listUser.add(users1);
////        listUser.add(users2);
////        boolean b = userService.saveBatch(listUser);
////        System.out.println(b);
////        分页
//        Page<user> userPage = new Page<>(page, size);
//        Page<user> page1 = userService.page(userPage, null);
//        for (user u : page1.getRecords()) {
//            System.out.println(u + "ni");
//        }
//        System.out.println(page1.hasNext());
//        System.out.println(page1.getTotal());
//        return list;
//    }
//
//    @ResponseBody
//    @PostMapping("/delUser")
//    public boolean delUser(@RequestParam("id") Integer id) {
//
//        return userService.removeById(id);
//    }
//
//}
//
