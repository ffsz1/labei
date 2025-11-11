package com.juxiao.xchat.service.api.charge.vo;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 提现用户信息
 *
 * @class: WithDrawVO.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class WithDrawCashVO {
    private Long uid;

    private Double diamondNum;

    private Long goldNum;

    public WithDrawCashVO(Long uid, Double diamondNum) {
        this.uid = uid;
        this.diamondNum = diamondNum;
    }
}
