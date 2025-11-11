package com.juxiao.xchat.service.api.item.params;

import lombok.Data;

/**
 * @author chris
 * @Title: 爆出礼物
 * @date 2018/11/16
 * @time 15:58
 */
@Data
public class BurstGift {

    private Long uid;
    private String avatar;
    private String nick;
    private Long sendUid;
    private String sendNick;
    private String sendAvatar;
    private Long roomId;
    private Long roomUid;
    private Integer giftId;
    private String giftName;
    private Integer giftNum;
    private String picUrl;
    private String vggUrl;
}
