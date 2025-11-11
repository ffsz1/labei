package com.erban.main.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * Created by PaperCut on 2018/2/3.
 */
@Component
@Lazy(value = false)
@Order(-1)
public class SystemConfig {
    public static String secretaryUid;

    public static String smsTemplateid;

    // 声优，收礼物方、订单服务者佣金
    public static Double appAkira;
    // 竞拍订单房主佣金
    public static Double roomOwnerAuctOrder;
    // 礼物官方佣金
    public static Double officialGift;
    // 竞拍订单官方佣金
    public static Double officialAuctOrder;

    @Value("${SECRETARY_UID}")
    public void setSecretaryUid(String secretaryUid) {
        SystemConfig.secretaryUid = secretaryUid;
    }

    @Value("${smsTemplateid}")
    public void setSmsTemplateid(String smsTemplateid) {
        SystemConfig.smsTemplateid = smsTemplateid;
    }

    @Value("${commission_akira}")
    public void setAppAkira(Double appAkira) {
        SystemConfig.appAkira = appAkira;
    }

    @Value("${commission_roomOwnerAuctOrder}")
    public void setRoomOwnerAuctOrder(Double roomOwnerAuctOrder) {
        SystemConfig.roomOwnerAuctOrder = roomOwnerAuctOrder;
    }

    @Value("${commission_officialGift}")
    public void setOfficialGift(Double officialGift) {
        SystemConfig.officialGift = officialGift;
    }

    @Value("${commission_officialAuctOrder}")
    public void setOfficialAuctOrder(Double officialAuctOrder) {
        SystemConfig.officialAuctOrder = officialAuctOrder;
    }
}
