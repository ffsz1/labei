package com.erban.admin.web.test;

import com.erban.admin.main.mapper.AdminMenuMapper;
import com.erban.admin.main.mapper.OfficialGoldRecordMapper;
import com.erban.admin.main.model.AdminMenu;
import com.erban.admin.main.model.OfficialGoldRecord;
import com.erban.admin.main.service.record.UserService;
import com.erban.admin.main.service.system.AdminMenuService;

import com.erban.main.model.BillRecord;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.BillRecordMapper;
import com.erban.main.mybatismapper.UsersMapper;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.Date;
import java.util.List;

public class TestMain {


    public static void main(String[] args) {
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("G:\\xchat\\server\\branches\\xchat-oauth2\\xchat-admin-web\\src\\main\\resources\\application-context-web.xml");
        AdminMenuService adminMenuService = applicationContext.getBean(AdminMenuService.class);
//        UserPacketRecordMapper userPacketRecord = applicationContext.getBean(UserPacketRecordMapper.class);
//        userPacketRecord.selectByPrimaryKey("3342");
//
        AdminMenuMapper adminMenuMapper = applicationContext.getBean(AdminMenuMapper.class);
        AdminMenu adminMenu = adminMenuMapper.selectByPrimaryKey(2);
        BillRecordMapper billRecordMapper = applicationContext.getBean(BillRecordMapper.class);
        billRecordMapper.selectByPrimaryKey("3sdfdsf");
        UsersMapper usersMapper = applicationContext.getBean(UsersMapper.class);
//        UserService userService = (UserService) applicationContext.getBean("userService2");
        usersMapper.selectByPrimaryKey(898260L);
//        List<Users> list = userService.getUserList("898260");
//
//        System.out.println(list.size());

    }
}
