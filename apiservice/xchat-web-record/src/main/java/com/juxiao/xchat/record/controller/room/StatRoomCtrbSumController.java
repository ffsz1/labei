package com.juxiao.xchat.record.controller.room;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.room.dto.RoomCtrbTopDTO;
import com.juxiao.xchat.dao.room.dto.StatRoomUserCtrbSumDTO;
import com.juxiao.xchat.service.record.room.StatRoomCtrbSumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/roomctrb")
public class StatRoomCtrbSumController {

    @Autowired
    private StatRoomCtrbSumService roomCtrbSumService;

    /**
     * 查询房间贡献榜，分为财富、魅力分栏，下面有日、周、总榜
     *
     * @param uid      房主ID
     * @param dataType 1：日；2：周；3：总榜
     * @param type     1：财富；2：魅力分栏
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/queryByType", method = RequestMethod.GET)
    public WebServiceMessage listRoomContribution(@RequestParam(value = "uid", required = false) Long uid,
                                                  @RequestParam(value = "dataType", required = false) Integer dataType,
                                                  @RequestParam(value = "type", required = false) Integer type) {
        try {
            List<StatRoomUserCtrbSumDTO> list = roomCtrbSumService.listRoomContribution(uid, dataType, type);
            return WebServiceMessage.success(list);
        } catch (WebServiceException e) {
            return WebServiceMessage.failure(e);
        }
    }

    /**
     * 查询房间贡献榜，分为财富、魅力分栏，下面有日、周、总榜
     *
     * @param uid      房主ID
     * @param dataType 1：财富；2：魅力分栏
     * @param type     1：日；2：周；3：总榜
     * @return
     */
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/queryByType", method = RequestMethod.GET)
    public WebServiceMessage listRoomContributionV2(@RequestParam(value = "uid", required = false) Long uid,
                                                    @RequestParam(value = "dataType", required = false) Integer dataType,
                                                    @RequestParam(value = "type", required = false) Integer type) {
        return this.listRoomContribution(uid, dataType, type);
    }

    @Authorization
    @SignVerification
    @RequestMapping(value = "v1/listRoomCtrbTop", method = RequestMethod.GET)
    public WebServiceMessage listRoomCtrbTop(@RequestParam(value = "roomUid", required = false) Long roomUid) throws WebServiceException {
        List<RoomCtrbTopDTO> list = roomCtrbSumService.listRoomCtrbTop(roomUid);
        return WebServiceMessage.success(list);
    }
}
