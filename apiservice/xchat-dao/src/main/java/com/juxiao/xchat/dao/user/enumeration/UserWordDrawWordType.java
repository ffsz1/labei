package com.juxiao.xchat.dao.user.enumeration;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户字体抽奖 字体类型
 */
public enum UserWordDrawWordType {
    //扭蛋
    ND_NIU(UserWordDrawActivityType.NIU_DAN, "niu"),  //扭
    ND_DAN(UserWordDrawActivityType.NIU_DAN, "dan"),  //蛋
    ND_ZHONG(UserWordDrawActivityType.NIU_DAN, "zhong"),//中
    ND_DA(UserWordDrawActivityType.NIU_DAN, "da"),  //大
    ND_JIANG(UserWordDrawActivityType.NIU_DAN, "jiang"), //奖

    ;
    private UserWordDrawWordType(UserWordDrawActivityType activityType, String word){
        this.activityType = activityType;
        this.word = word;
    }

    private UserWordDrawActivityType activityType;

    private String word;

    public UserWordDrawActivityType getActivityType() {
        return activityType;
    }

    public String getWord() {
        return word;
    }


    public static List<String> getActivityAllCode(int activityType){
        List<String> list = new ArrayList<>();
        for(UserWordDrawWordType type : UserWordDrawWordType.values()){
            if(type.getActivityType().getType() == activityType){
                list.add(type.getWord());
            }
        }
        return list;
    }


}
