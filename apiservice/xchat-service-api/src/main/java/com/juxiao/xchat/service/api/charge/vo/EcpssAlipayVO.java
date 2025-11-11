package com.juxiao.xchat.service.api.charge.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @ClassName: EcpssAlipayVO
 * @Description: 汇潮支付-支付宝支付返回数据
 * @Author: alwyn
 * @Date: 2019/6/13 19:12
 * @Version: 1.0
 */
@Data
public class EcpssAlipayVO {

    @ApiModelProperty("支付URL")
    private String payUrl;

    public EcpssAlipayVO() {
        this.payUrl = "";
    }

    public EcpssAlipayVO(String payUrl) {
        this.payUrl = payUrl;
    }
}

