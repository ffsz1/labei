package com.erban.web.controller.drawprize;

import com.beust.jcommander.internal.Lists;
import com.beust.jcommander.internal.Maps;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liuguofu on 2017/12/15.
 */
public class PrizeCalc {
    private static DecimalFormat doubleFormat = new DecimalFormat("0.0000");
//    A:再抽一次，B:5金币，C:30金币，D:100金币，E:300金币，F:1000金币，
//    G:3000金币，H:8888金币，I:六位靓号（价值10 000金币）J：四位靓号（价值15000金币）

    /**
     * 2. 充值48只能抽BCD，期望为22.5金币 1,6,20 赔率
     3. 充值98只能抽BCDE，期望为46金币 1,6,20,60
     4. 充值198只能抽CDEF，期望为93金币 3,10,30,100
     5. 充值498只能抽CDEFGHIJ,期望为245金币 3,10,30,100,300,888,100,150
     6. 充值998只能抽DEFGHIJ，期望为492金币 1,3,10,30,888,100,150
     7. 充值4998只能抽EFGHIJ,期望为2480金币 3,10,30,888,100,150
     8. 充值9999只能抽FGHIJ，期望为5000金币 10,30,888,100,150
     *
     */
    private static Map data= Maps.newHashMap();
    private static List<Integer> prizeTimes= Lists.newArrayList();
//    private static int [] prize8={1,6,20};
//    private static int [] prize48={1,6,20};
//    private static int [] prize98={1,6,20,60};
//    private static int [] prize198={3,10,30,100};
//    private static int [] prize498={3,10,30,100,300,88,100,150};
//    private static int [] prize998={1,3,10,30,88,100,150};
//    private static int [] prize4998={3,10,30,88,100,150};
//    private static int [] prize9999={10,30,88,100,150};

    private static int [] prize8={5,30,100};
    private static int [] prize48={5,30,100};
    private static int [] prize98={5,30,100,300};
    private static int [] prize198={30,100,300,1000};
    private static int [] prize498={30,100,300,1000,3000,8888,10000,15000};
    private static int [] prize998={100,300,1000,3000,8888,10000,15000};
    private static int [] prize4998={300,1000,3000,8888,10000,15000};
    private static int [] prize9999={1000,3000,8888,10000,15000};

    private static int  bb=1;

    private static String prizeName[]={"充值8元","充值48元","充值98元","充值198元","充值498元","充值998元",
            "充值4998元","充值9999元"};


    public static void main(String args[]){
        calc();

    }
    public static void calc(){
        List<int []> list=Lists.newArrayList();
        list.add(prize8);
        list.add(prize48);
        list.add(prize98);
        list.add(prize198);
        list.add(prize498);
        list.add(prize998);
        list.add(prize4998);
        list.add(prize9999);
        String str="";
        for(int i=0;i<list.size();i++){
            int prizes[]=list.get(i);
            double maxTimes=prizes[0];
            double sumData=0;
            for(int j=0;j<prizes.length;j++){
                sumData=sumData+maxTimes/prizes[j];
            }

            double total=0.00;
            double qiwang=0.00;
            for(int j=0;j<prizes.length;j++){
                double prizeTime=prizes[j];
                double prizeChance=(bb*maxTimes)/(prizeTime*sumData);
                prizeChance = new Double(doubleFormat.format(prizeChance));
//                str=str+prizeName[i]+"赔率"+prizeTime+"倍的中奖概率为="+prizeChance+";";
                str=str+prizeChance+",";
                qiwang=qiwang+prizeChance*prizeTime;
                total=total+prizeChance;
            }
            str+=prizeName[i]+";期望值="+qiwang+";total="+total+"\n";

        }
        System.out.println(str);

    }
}
