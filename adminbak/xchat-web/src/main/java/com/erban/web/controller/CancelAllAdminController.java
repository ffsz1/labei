package com.erban.web.controller;

import com.erban.main.service.ErBanNetEaseService;
import com.google.gson.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by liuguofu on 2017/9/15.
 */
@Controller
@RequestMapping("/cancel")
public class CancelAllAdminController {

    static Gson gson=new Gson();
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;
    @RequestMapping(value = "/admin", method = RequestMethod.GET)
    @ResponseBody
    public void cancelAdmin() throws Exception{
        File file = new File("D:\\room.txt");//Text文件
        BufferedReader br = new BufferedReader(new FileReader(file));//构造一个BufferedReader类来读取文件
        String name = null;
        while ((name = br.readLine()) != null) {//使用readLine方法，一次读一行
            String array[]=name.split(",");
            Date date=new Date();
            JsonObject jsonObject=new JsonObject();
            JsonParser jsonParser=new JsonParser();
            String result=erBanNetEaseService.getMembersByPage(Long.valueOf(array[1]), 0,date.getTime() , 100);;
            jsonParser.parse(result);
            JsonArray jsonArray=jsonObject.getAsJsonObject("desc").get("data").getAsJsonArray();

            erBanNetEaseService.setChatRoomMemberRole( Long.valueOf(array[1]), array[0],"" , 1,  "false");
        }
        br.close();

    }
    public static void main(String args[]) throws Exception{
        ErBanNetEaseService erBanNetEaseService=new ErBanNetEaseService();
        JsonParser jsonParser=new JsonParser();
        long roomId=11163093;
        Date date=new Date();
        String result=erBanNetEaseService.getMembersByPage(roomId, 0, date.getTime(), 100);;
        JsonElement jsonElement=jsonParser.parse(result);
        JsonObject jsonObject=jsonElement.getAsJsonObject();
        JsonElement jsonElement2= jsonObject.get("desc");
        JsonObject jsonObject2=jsonElement2.getAsJsonObject();
        JsonArray jsonArray=jsonObject2.get("data").getAsJsonArray();
        Iterator it= jsonArray.iterator();
        while(it.hasNext()){
           Map map= gson.fromJson(it.next().toString(), Map.class);
            Long accid=Long.valueOf(map.get("accid").toString());
            String type=map.get("type").toString();
            if(type.equals("CREATOR")){
                continue;
            }
            System.out.println(accid);
        }
        jsonArray.size();
    }

}
