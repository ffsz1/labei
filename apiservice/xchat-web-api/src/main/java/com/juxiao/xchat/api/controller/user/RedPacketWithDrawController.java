package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.charge.dto.RedPacketCashProdDTO;
import com.juxiao.xchat.dao.charge.dto.UserPacketRecordDTO;
import com.juxiao.xchat.service.api.charge.vo.WithdrawRedPacketVO;
import com.juxiao.xchat.service.api.user.RedPacketWithDrawService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 红包controller
 */
@RestController
@RequestMapping("/redpacket")
@Api(tags = "用户信息接口",description = "用户信息接口")
public class RedPacketWithDrawController {
    @Autowired
    private RedPacketWithDrawService redpacketService;

    /**
     * 获取有效的的红包(withdraw_packet_cash_prod)产品
     *
     * @return
     */
    @SignVerification
    @Authorization
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public WebServiceMessage listPacketCashProd() {
        List<RedPacketCashProdDTO> list = redpacketService.listPacketCashProd();
        return WebServiceMessage.success(list);
    }


    /**
     * 所有用户红包提现数据展示
     *
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/drawlist", method = RequestMethod.GET)
    public WebServiceMessage getUserWithDrawList() {
        List<UserPacketRecordDTO> list = redpacketService.listUserPacketRecord();
        return WebServiceMessage.success(list);
    }

    /**
     * 红包提现
     *
     * @param uid
     * @param packetId
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/withdraw", method = RequestMethod.POST)
    public WebServiceMessage withDraw(@RequestParam("uid") Long uid,
                                      @RequestParam("packetId") Integer packetId,
                                      @RequestParam(value = "type",defaultValue = "1") int type) throws WebServiceException {
        WithdrawRedPacketVO redpacketVo = redpacketService.withDraw(uid, packetId,type);
        return WebServiceMessage.success(redpacketVo);
    }

    /**
     * 红包提现
     *
     * @param uid
     * @param packetId
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/v2/withdraw", method = RequestMethod.POST)
    public WebServiceMessage withDraw(@RequestParam("uid") Long uid,
                                      @RequestParam("packetId") Integer packetId,
                                      @RequestParam(value = "type",defaultValue = "1") int type,
                                      @RequestParam("openId") String openId) throws WebServiceException {
        WithdrawRedPacketVO redpacketVo = redpacketService.withReadPackageDraw(uid, packetId,type,openId);
        return WebServiceMessage.success(redpacketVo);
    }

    /**
     * 获取用户红包数
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/getUserRedPacket", method = RequestMethod.GET)
    public WebServiceMessage getUserRedPacket(@RequestParam("uid") Long uid) {
        return WebServiceMessage.success(redpacketService.getUserRedPacket(uid));
    }


    /**
     * 红包提现记录
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/getUserWithdrawRedPacketRecord", method = RequestMethod.GET)
    public WebServiceMessage getUserWithdrawRedPacketRecord(@RequestParam("uid") Long uid) {
        return WebServiceMessage.success(redpacketService.listWithdrawRedPacketRecord(uid));
    }
}
