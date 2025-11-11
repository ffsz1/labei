package com.juxiao.xchat.service.api.user.vo;

import lombok.Data;

/**
 * @Auther: alwyn
 * @Description:
 * @Date: 2018/9/12 15:27
 */
@Data
public class DrawGiftVO {

    private Integer giftId;
    private String giftName;
    private Byte giftType;
    private String picUrl;
    private String vggUrl;
    private Integer giftNum;
    private Long goldPrice;

    public long getTotal() {
        int num = getGiftNum() == null ? 0 : getGiftNum();
        long price = getGoldPrice() == null ? 0 : getGoldPrice();
        return num * price;
    }
}
