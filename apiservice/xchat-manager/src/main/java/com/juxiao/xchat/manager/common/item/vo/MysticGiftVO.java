package com.juxiao.xchat.manager.common.item.vo;

import lombok.Data;

/**
 * @Auther: alwyn
 * @Description:
 * @Date: 2018/9/13 14:24
 */
@Data
public class MysticGiftVO {

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
