package com.erban.main.service.room;

import com.erban.main.service.ErBanNetEaseService;
import com.google.gson.Gson;
import com.xchat.common.netease.neteaseacc.result.RubbishRet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by liuguofu on 2017/5/20.
 */
@Service
public class RoomNetEaseService {
    private static final Logger logger = LoggerFactory.getLogger(RoomNetEaseService.class);
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    private Gson gson=new Gson();

    public int setChatRoomMemberRole(Long roomId, String operator, String target, int opt, String optvalue) throws Exception{
        RubbishRet rubbishRet=erBanNetEaseService.setChatRoomMemberRole(roomId,operator,target,opt,optvalue);
        if(rubbishRet.getCode()!=200){
            logger.error("设置管理员失败，失败code="+rubbishRet.getCode());
            throw new Exception("设置管理员失败，失败code="+rubbishRet.getCode());
        }
        return 0;
    }


}
