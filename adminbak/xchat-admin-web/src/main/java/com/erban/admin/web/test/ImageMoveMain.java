package com.erban.admin.web.test;

import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.oauth2.service.service.UserService;
import org.apache.xbean.spring.context.FileSystemXmlApplicationContext;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class ImageMoveMain {


    public static void main(String[] args) {
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext("G:\\xchat\\server\\branches\\xchat-oauth2\\xchat-web\\src\\main\\resources\\application-context-web.xml");

        UsersService usersService = (UsersService) applicationContext.getBean(UserService.class);
        UsersMapper usersMapper = applicationContext.getBean(UsersMapper.class);

        UsersExample example = new UsersExample();
        example.setOrderByClause("order by uid asc");
        PageHelper.startPage(1, 2);
        List<Users> list = usersMapper.selectByExample(example);
        PageInfo<Users> pageInfo = new PageInfo<>(list);
        System.out.println(pageInfo.getTotal());
        for (Users users : pageInfo.getList()) {

        }
    }
}
