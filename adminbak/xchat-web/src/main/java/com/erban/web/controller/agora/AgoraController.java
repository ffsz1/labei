package com.erban.web.controller.agora;

import com.erban.main.service.agora.AgoraService;
import com.xchat.common.annotation.SignVerification;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/agora")
public class AgoraController {
    @Autowired
    private AgoraService agoraService;

    @ResponseBody
    @RequestMapping(value = "getKey", method = RequestMethod.POST)
    public BusiResult queryActivityHomeList(String roomId, Long uid) {
        if (roomId == null || uid == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        return agoraService.getChannelKey(roomId, uid);
    }

}
