//package com.example.webapp.controller;
//
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//public class hello {
//    //    先找controller 再找静态资源
////    @RequestMapping("/logo.jpg")
////    public String hello() {
////        return "Aaa";
////    }
//
//    //pathvariable路径变量  Map会自动把所有的变量存进去
////    RequestHeader获取请求头信息
////    RequestParam获取请求参数 例如？age=17
//    @GetMapping("/car/{id}/owner/{username}")
//    public Map<String, Object> getCar(@PathVariable("id") Integer id,
//                                      @PathVariable("username") String username,
//                                      @PathVariable Map<String, String> PV,
//                                      @RequestHeader("user-agent") String userAgent,
//                                      @RequestHeader Map<String, String> headers,
//                                      //    http://localhost:8888/car/1/owner/unfunlady?age=18&job=teacher&job=hha
//                                      @RequestParam("age") Integer age,
//                                      @RequestParam("job") List<String> job,
//                                      @RequestParam Map<String, String> params
//    ) {
//        System.out.println(id);
//        System.out.println(userAgent);
//        System.out.println(age);
//        System.out.println(job);
//        System.out.println(params);
//        Map<String, Object> map = new HashMap<>();
//        map.put("code", 200);
//        map.put("id", id);
//        map.put("username", username);
//        map.put("PV", PV);
//        map.put("userAgent", userAgent);
//        map.put("User-AgentMap", headers);
//        return map;
//    }
////获取post请求的请求体数据
//    @PostMapping("/postMe")
//    public Map postMethod(@RequestBody String content) {
//        Map<String, Object> map = new HashMap<>();
//        map.put("body", content);
//        /*
//        * {
//    "body": "id=2&user=admin"
//            }
//        * */
//        return map;
//
//    }
//
//
//}
