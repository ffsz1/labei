package com.juxiao.xchat.service.api.room;


/**
 * @class: NetEaseReciveService.java
 * @author: chenjunsheng
 * @date 2018/6/27
 */
public interface NetEaseReciveService {

    /**
     * @param curTime
     * @param md5
     * @param checkSum
     * @param requestBody
     * @return
     */
    int reciveNetEaseMsg(String curTime, String md5, String checkSum, String requestBody);
}
