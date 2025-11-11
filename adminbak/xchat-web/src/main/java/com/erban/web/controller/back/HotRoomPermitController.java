package com.erban.web.controller.back;

import com.erban.main.service.HotRoomPermitService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/10/8.
 */
@Controller
@RequestMapping("/homepermit")
public class HotRoomPermitController {
    private static final Logger logger = LoggerFactory.getLogger(HotRoomPermitController.class);

    @Autowired
    private HotRoomPermitService hotRoomPermitService;

    /**
     * 给房间添加牌照
     *
     * @param erbanNo
     * @return
     */
    @RequestMapping(value = "add", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult addPermit(Long erbanNo) {
        try {
            return hotRoomPermitService.addHotRoomPermitService(erbanNo);
        } catch (Exception e) {
            logger.error("addPermit error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }

    /**
     * 删除房间的牌照
     *
     * @param erbanNo
     * @return
     */
    @RequestMapping(value = "delete", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult deletePermit(Long erbanNo) {
        try {
            return hotRoomPermitService.deleteHotRoomPermitService(erbanNo);
        } catch (Exception e) {
            logger.error("deletePermit error,erbanNo=" + erbanNo, e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
    }
}


