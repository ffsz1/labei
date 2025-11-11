//package com.juxiao.xchat.api.controller;
//
//import io.swagger.annotations.Api;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestMethod;
//
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
//@Controller
//@RequestMapping("/")
//@Api(description = "其他接口", tags = "其他")
//public class IndexController {
//
//    @RequestMapping(value = "/", method = {RequestMethod.GET, RequestMethod.POST})
//    public void home(HttpServletResponse response) throws IOException {
//        response.sendRedirect("https://www.pinjin12.com/index.html");
//    }
//
//    @RequestMapping(value = "/index", method = {RequestMethod.GET, RequestMethod.POST})
//    public void index(HttpServletResponse response) throws IOException {
//        response.sendRedirect("https://www.pinjin12.com/index.html");
//    }
//}