//package com.erban.admin;
//
//import com.erban.admin.main.dto.UserRoomDTO;
//import com.erban.admin.main.service.room.StatRoomConsumeService;
//import com.erban.admin.web.task.UserRoomTask;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import java.util.List;
//
///**
// * @Description:
// * @Author: alwyn
// * @Date: 2018/11/9 17:31
// */
//@RunWith(SpringJUnit4ClassRunner.class) //使用junit4进行测试
//@ContextConfiguration(locations = {"classpath*:application-context-web.xml"})
//public class UserRoomConsumeTest {
//    @Autowired
//    private UserRoomTask userRoomTask;
//    @Autowired
//    private StatRoomConsumeService roomConsumeService;
//
//    @Test
//    public void countListTest() {
//        roomConsumeService.countRoomConsume(null);
//    }
//
//    @Test
//    public void test() {
//        userRoomTask.countUserConsume();
//    }
//
//    @Test
//    public void addRoomTest() {
//        roomConsumeService.addRoom(5914521L);
//    }
//
//    @Test
//    public void listRoomTest() {
//
//    }
//}
