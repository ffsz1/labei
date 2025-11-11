package com.juxiao.xchat.manager.external.weixin.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 *
 */
@Getter
@Setter
@ApiModel("WxRequestPayment")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WxappRequestPaymentVO {
    @ApiModelProperty(value = "时间戳从1970年1月1日00:00:00至今的秒数,即当前的时间", name = "timeStamp", required = true)
    private Long timeStamp;
    @ApiModelProperty(value = "随机字符串，长度为32个字符以下", name = "nonceStr", required = true)
    private String nonceStr;
    @ApiModelProperty(hidden = true)
    private String prepayId;
    @ApiModelProperty(value = "签名类型，默认为MD5，支持HMAC-SHA256和MD5。注意此处需与统一下单的签名类型一致", name = "signType", required = true)
    private String signType;
    @ApiModelProperty(value = "签名", name = "paySign", required = true)
    private String paySign;

    public WxappRequestPaymentVO() {
        this.timeStamp = System.currentTimeMillis() / 1000;
        this.signType = "MD5";
    }

    @ApiModelProperty(value = "统一下单接口返回的 prepay_id 参数值，提交格式如：prepay_id=*", name = "package", required = true)
    public String getPackage() {
        return "prepay_id=" + prepayId;
    }


    public String getPrepayId() {
        return null;
    }
}
