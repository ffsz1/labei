package com.erban.web.controller.netease;

import com.erban.main.service.ErBanNetEaseService;
import com.xchat.common.netease.neteaseacc.result.AddrNetEaseRet;
import com.xchat.common.netease.neteaseacc.result.BaseNetEaseRet;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/netease")
public class NeteaseController {
    @Autowired
    private ErBanNetEaseService erBanNetEaseService;

    @RequestMapping(value = "inRoom",method = RequestMethod.POST)
    @ResponseBody
    public BusiResult inRoom(Long roomId, String accId){
        if(roomId==null || accId==null){
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        try {
            AddrNetEaseRet addrNetEaseRet = erBanNetEaseService.inRoom(roomId, accId, 1);
            if(addrNetEaseRet.getCode()==200){
                return new BusiResult(BusiStatus.SUCCESS, addrNetEaseRet.getAddr());
            }else {
                return new BusiResult(BusiStatus.NOAUTHORITY, addrNetEaseRet.getDesc(), addrNetEaseRet.getCode());
            }
        } catch (Exception e) {
            return new BusiResult(BusiStatus.SERVERERROR);
        }
    }

}
