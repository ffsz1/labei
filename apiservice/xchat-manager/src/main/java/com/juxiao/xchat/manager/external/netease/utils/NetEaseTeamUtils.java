package com.juxiao.xchat.manager.external.netease.utils;

import com.google.gson.Gson;
import com.juxiao.xchat.manager.external.netease.ret.BaseTeamRet;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseTeamRet;
import lombok.extern.slf4j.Slf4j;

/**
 * @author chris
 * @Title: 封装云信群返回工具
 * @date 2018/11/26
 * @time 10:29
 */
@Slf4j
public class NetEaseTeamUtils {

    private static final int SUCCESS = 200;
    private static final int PERMISSION_DENIED_ERROR = 403;
    private static final int PARAMETER_ERROR = 414;
    private static final int REQUEST_FREQUENCY_CONTROL = 416;
    private static final int REPEAT_REQUEST_ERROR = 431;
    private static final int SERVER_INTERNAL_ERROR = 500;
    private static final int MAXIMUM_NUMBER_OF_PEOPLE_ERROR = 801;
    private static final int QUANTITY_REACHED_LIMIT_ERROR = 806;
    private static final int OBJECT_NOT_EXIST_ERROR = 404;


    /**
     * 构建封装云信返回参数
     * @param netEaseRet
     * @return
     */
    public static BaseTeamRet buildReturn(NetEaseTeamRet netEaseRet){
        if(netEaseRet.getCode() == SUCCESS){
            Gson gson = new Gson();
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),gson.toJson(netEaseRet),"返回成功");
            return new BaseTeamRet(netEaseRet.getCode(),netEaseRet,"成功!");
        }else if(netEaseRet.getCode() == PERMISSION_DENIED_ERROR){
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"非法操作或没有权限!");
            return new BaseTeamRet(netEaseRet.getCode(),"没有权限或请先撤销管理员权限!");
        }else if(netEaseRet.getCode() == PARAMETER_ERROR){
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"参数错误!");
            return new BaseTeamRet(netEaseRet.getCode(),"参数错误!");
        }else if(netEaseRet.getCode() == REQUEST_FREQUENCY_CONTROL){
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"频率控制!");
            return new BaseTeamRet(netEaseRet.getCode(),"频率过于控制!");
        }else if(netEaseRet.getCode() == REPEAT_REQUEST_ERROR){
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"HTTP重复请求!");
            return new BaseTeamRet(netEaseRet.getCode(),"HTTP重复请求!");
        }else if(netEaseRet.getCode() == SERVER_INTERNAL_ERROR){
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"服务器内部错误!");
            return new BaseTeamRet(netEaseRet.getCode(),"服务器内部错误!");
        }else if(netEaseRet.getCode() == MAXIMUM_NUMBER_OF_PEOPLE_ERROR){
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"群人数达到上限!");
            return new BaseTeamRet(netEaseRet.getCode(),"群人数达到上限!");
        }else if(netEaseRet.getCode() == QUANTITY_REACHED_LIMIT_ERROR){
            log.info("[云信返回信息] 返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"创建群数量达到限制!");
            return new BaseTeamRet(netEaseRet.getCode(),"创建群数量达到限制!");
        }
        log.info("[云信返回信息]  返回code:{},返回值:{},返回消息:{}",netEaseRet.getCode(),"对象不存在!");
        return new BaseTeamRet(OBJECT_NOT_EXIST_ERROR,"对象不存在!");
    }
}
