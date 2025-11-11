package com.juxiao.xchat.api.controller.room;

import com.juxiao.xchat.base.web.WebServiceMessage;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Deprecated
@RestController
@RequestMapping("/auction")
@Api(description = "其他接口", tags = "其他")
public class RoomAuctionController {

    @Deprecated
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getCurentAuction(Long uid) {
        return WebServiceMessage.success(null);
    }
}
