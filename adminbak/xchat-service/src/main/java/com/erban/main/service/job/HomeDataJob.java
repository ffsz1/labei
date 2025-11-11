package com.erban.main.service.job;


import com.erban.main.mybatismapper.UsersMapper;
import com.erban.main.service.*;
import com.erban.main.service.user.UsersService;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.oauth2.service.service.JedisService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.reflect.Type;
import java.util.*;

public class HomeDataJob implements Job {

    private static final Logger LOGGER = LoggerFactory.getLogger(HomeDataJob.class);
    private Type type = new TypeToken<List<String>>() {}.getType();
    private static List<String> notRunRobot = null;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UsersMapper usersMapper;
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    @Autowired
    private HomeService homeService;
    @Autowired
    private UsersService usersService;



    private Gson gson=new Gson();
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        LOGGER.info("HomeDataJob 开始刷新首页数据.....");
        homeService.doHomeDataJob();
        LOGGER.info("HomeDataJob 刷新首页数据完成.....");
    }



    /**
     *
     * @param a
     * @param low
     * @param high
     */
    public void sort(int[] a,int low,int high){
        int start = low;
        int end = high;
        int key = a[low];


        while(end>start){
            //从后往前比较
            while(end>start&&a[end]>=key)  //如果没有比关键值小的，比较下一个，直到有比关键值小的交换位置，然后又从前往后比较
                end--;
            if(a[end]<=key){
                int temp = a[end];
                a[end] = a[start];
                a[start] = temp;
            }
            //从前往后比较
            while(end>start&&a[start]<=key)//如果没有比关键值大的，比较下一个，直到有比关键值大的交换位置
                start++;
            if(a[start]>=key){
                int temp = a[start];
                a[start] = a[end];
                a[end] = temp;
            }
            //此时第一次循环比较结束，关键值的位置已经确定了。左边的值都比关键值小，右边的值都比关键值大，但是两边的顺序还有可能是不一样的，进行下面的递归调用
        }
        //递归
        if(start>low) sort(a,low,start-1);//左边序列。第一个索引位置到关键值索引-1
        if(end<high) sort(a,end+1,high);//右边序列。从关键值索引+1到最后一个
    }
}
