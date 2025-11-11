package com.erban.web.controller.gift;

import com.google.common.collect.Maps;
import com.xchat.common.netease.neteaseacc.NetEaseBaseClient;

import java.util.Map;

import static com.erban.web.controller.gift.GiftMuiltThreadTest.failcount;
import static com.erban.web.controller.gift.GiftMuiltThreadTest.succount;

/**
 * Created by liuguofu on 2017/7/24.
 */
public class GiftMuiltThreadTest {

    public static Integer succount=0;
    public static Integer failcount=0;

    public static void main(String args[]){
        gradData();
        System.out.println("succount="+succount);
        System.out.println("failcount="+failcount);

    }
    public static void gradData(){
        for (int i = 0; i < 1000; i++) {
            HandleThread handleThread = new HandleThread();
            Thread thread = new Thread(handleThread);
            thread.start();
        }

    }
}
class HandleThread implements Runnable {
    private static String url="http://www.daxiaomao.com/gift/send?channel=appstore&giftId=1000&ispType=65535&model=iPhone7,1&netType=2&os=iOS&osVersion=10.1.1&targetUid=90000028&ticket=eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjo5MDAwMDAyOCwidGlja2V0X2lkIjoiYWE0MTFkM2MtOTA1NS00NDY5LThkZTctNjU3ZjM3NTE3M2MwIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.GVlCc0tacikjZ3PNuXE7lkr4-S1FQ48yv20TQ9N-MlY&type=1&uid=90000028";
//    private static String url="http://115.28.95.16/gift/send?channel=appstore&giftId=1000&ispType=65535&model=iPhone7,1&netType=2&os=iOS&osVersion=10.1.1&targetUid=90000028&ticket=eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjo5MDAwMDAyOCwidGlja2V0X2lkIjoiYWE0MTFkM2MtOTA1NS00NDY5LThkZTctNjU3ZjM3NTE3M2MwIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.GVlCc0tacikjZ3PNuXE7lkr4-S1FQ48yv20TQ9N-MlY&type=1&uid=90000028";
//    private static String url="http://121.42.180.146/gift/send?channel=appstore&giftId=1000&ispType=65535&model=iPhone7,1&netType=2&os=iOS&osVersion=10.1.1&targetUid=90000028&ticket=eyJhbGciOiJIUzI1NiJ9.eyJ0aWNrZXRfdHlwZSI6bnVsbCwidWlkIjo5MDAwMDAyOCwidGlja2V0X2lkIjoiYWE0MTFkM2MtOTA1NS00NDY5LThkZTctNjU3ZjM3NTE3M2MwIiwiZXhwIjozNjAwLCJjbGllbnRfaWQiOiJlcmJhbi1jbGllbnQifQ.GVlCc0tacikjZ3PNuXE7lkr4-S1FQ48yv20TQ9N-MlY&type=1&uid=90000028";
    public HandleThread() {

    }

    public void run() {
        NetEaseBaseClient NetEaseBaseClient=new NetEaseBaseClient(url);
        Map map=Maps.newHashMap();
        try {
            String str=NetEaseBaseClient.buildHttpPostParam(map).executePost();

            synchronized (succount){
                succount++;
                System.out.println("succount="+succount);
            }
        } catch (Exception e) {
            synchronized (failcount){
                failcount++;
                System.out.println("failcount="+failcount);
            }
        }

    }
}


