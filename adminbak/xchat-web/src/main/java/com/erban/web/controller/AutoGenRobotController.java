package com.erban.web.controller;

import com.erban.main.service.AutoGenRobotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by liuguofu on 2017/8/10.
 */
@Controller
@RequestMapping("/gen")
public class AutoGenRobotController {

    @Autowired
    private AutoGenRobotService autoGenRobotService;

    @RequestMapping(value = "name", method = RequestMethod.GET)
    public void saveOrUpdateAuctionRival() throws Exception {
        File file = new File("E:\\robots\\name.txt");//Text文件
        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "GBK"));//构造一个BufferedReader类来读取文件
        String name = null;
        while ((name = br.readLine()) != null) {//使用readLine方法，一次读一行
            autoGenRobotService.saveName(name);
        }
        br.close();
        File filePic = new File("E:\\robots\\avatar");//Text文件
        autoGenRobotService.uploadAvatar(filePic);
    }

    @RequestMapping(value = "avatar", method = RequestMethod.GET)
    public void genAvatar() throws Exception {
        File filePic = new File("E:\\avatar");//Text文件
        autoGenRobotService.uploadAvatar(filePic);
    }

    @RequestMapping(value = "acc", method = RequestMethod.GET)
    public void batchGenRobAccount() throws Exception {
        autoGenRobotService.batchGenRobAccount();
    }

    public static void main(String args[]){
        String str="";
        int a=68;
        int limit1=1320;
        int page=20;
        int total=5000;
        for(int i=0;i<220;i++){

            str=str+"INSERT INTO room_robot_group(robot_uid,group_no)  " +
                    "SELECT uid,"+a+" FROM users WHERE def_user=3 ORDER BY uid ASC LIMIT "+ limit1+",20; \n";
            limit1+=20;
            a++;
        }
        System.out.println(str);

    }




}
