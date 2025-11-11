//package com.erban.admin;
//
//import com.erban.admin.main.service.user.MakeFriendRecomService;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
///**
// * @Author: alwyn
// * @Description:
// * @Date: 2018/11/11 001112:46
// */
//@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
//@ContextConfiguration(locations = {"classpath*:application-context-web.xml"})
//public class MakeFriendTest {
//
//    @Autowired
//    private MakeFriendRecomService makeFriendRecomService;
//
//    @Test
//    public void listTest() {
//        makeFriendRecomService.getList(12L, null, null);
//    }
//
//    @Test
//    public void addUserTest() {
//        makeFriendRecomService.saveRecomUser(null, 12L, 123L);
//    }
//
//    @Test
//    public void delUserTest() {
//        makeFriendRecomService.delUser(12L);
//    }
//
//    @Test
//    public void addSourceTest() {
//        makeFriendRecomService.addSource(12L, 129L);
//    }
//
//    @Test
//    public void initSourceTest() {
//        makeFriendRecomService.initCharmRecomSource();
//    }
//}
