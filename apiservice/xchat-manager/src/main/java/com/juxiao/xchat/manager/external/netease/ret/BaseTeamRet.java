package com.juxiao.xchat.manager.external.netease.ret;

import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 10:27
 */
@Data
public class BaseTeamRet {

    private int code;

    private Object data;

    private String msg;

    public BaseTeamRet(){}


    public BaseTeamRet(int code,Object data,String msg){
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public BaseTeamRet(int code,String msg){
        this.code = code;
        this.msg = msg;
    }

}
