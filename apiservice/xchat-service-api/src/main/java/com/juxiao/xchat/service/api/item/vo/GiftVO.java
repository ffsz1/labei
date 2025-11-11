package com.juxiao.xchat.service.api.item.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class GiftVO implements Comparable<GiftVO> {
    private Integer giftId;
    private String giftName;
    private Long goldPrice;
    private String giftUrl;
    private Integer nobleId;
    private String nobleName;
    private Boolean isNobleGift;
    private Integer seqNo;
    private Boolean hasGifPic;
    private Byte giftType;
    private String gifUrl;
    private Boolean hasVggPic;//是否有vgg动画
    private String vggUrl;//vgg动画链接
    private Boolean hasLatest;//是否是最新礼物
    private Boolean hasTimeLimit;//是否是限时礼物
    private Boolean hasEffect;//是否有特效
    private Integer userGiftPurseNum;//用户礼物数
    private Boolean isExpressGift;// 是否是表白礼物

    @Override
    public int compareTo(GiftVO gift) {
        if (gift == null || gift.seqNo == null) {
            return -1;
        }
        if (this.seqNo == null) {
            return 1;
        }
        if (this.seqNo.equals(gift.seqNo)) {
            return 0;
        }
        return gift.seqNo > this.seqNo ? -1 : 1;
    }
}
