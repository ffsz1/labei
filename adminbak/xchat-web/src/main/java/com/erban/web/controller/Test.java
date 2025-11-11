package com.erban.web.controller;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.Users;
import com.google.gson.Gson;
import com.google.gson.internal.LinkedTreeMap;
import com.xchat.common.UUIDUitl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by liuguofu on 2017/6/6.
 */
public class Test {

    public static void main(String args[]){
//        String str="{\"id\":\"evt_400170707144522227491903\",\"created\":1499409921,\"livemode\":false,\"type\":\"charge.succeeded\",\"data\":{\"object\":{\"id\":\"ch_D480SS1qD4aTWLCKqH9mn1K4\",\"object\":\"charge\",\"created\":1499409913,\"livemode\":false,\"paid\":true,\"refunded\":false,\"reversed\":false,\"app\":\"app_KaPO844ej1G8T40a\",\"channel\":\"alipay\",\"order_no\":\"7b53855ad08b48a8a2ad218924dcc531\",\"client_ip\":\"192.168.0.100\",\"amount\":600,\"amount_settle\":600,\"currency\":\"cny\",\"subject\":\"600金币金币充值\",\"body\":\"600金币金币充值\",\"extra\":{\"rn_check\":\"T\",\"buyer_account\":\"alipay_account\"},\"time_paid\":1499409921,\"time_expire\":1499496313,\"time_settle\":null,\"transaction_no\":\"2017070758155903\",\"refunds\":{\"object\":\"list\",\"url\":\"/v1/charges/ch_D480SS1qD4aTWLCKqH9mn1K4/refunds\",\"has_more\":false,\"data\":[]},\"amount_refunded\":0,\"failure_code\":null,\"failure_msg\":null,\"metadata\":{},\"credential\":{},\"description\":null}},\"object\":\"event\",\"request\":\"iar_DOu9CCuXHqfTS8GGiPqvT088\",\"pending_webhooks\":0}";
        Gson gson=new Gson();
//        Map<String,Object> requstMap=gson.fromJson(str,HashMap.class);
//        String type=requstMap.get("type").toString();
//        LinkedTreeMap data=(LinkedTreeMap)requstMap.get("data");
////        LinkedTreeMap dataMap=gson.fromJson(data,LinkedTreeMap.class);
//        LinkedTreeMap dataMap=(LinkedTreeMap) data.get("object");
////        Charge charge=gson.fromJson(null, Charge.class);
////        System.out.println(charge.getOrderNo());
//        boolean paid=(boolean)dataMap.get("paid");
//        Integer amount=new Double (dataMap.get("amount").toString()).intValue();
//        Long time_paid=(Long)dataMap.get("time_paid");
//        System.out.println(time_paid);
         List<String> voiceDescList= Lists.newArrayList();
        voiceDescList.add("声音青涩犹如初恋");
        voiceDescList.add("绵软哼唧小鼻音");
        voiceDescList.add("不太认真");
        String str=gson.toJson(voiceDescList);
        Users users=new Users();
        users.setNick(str);
        System.out.println(str);

        String userStr=gson.toJson(users);
        String s=UUIDUitl.get();
        System.out.println(s);

    }
}
