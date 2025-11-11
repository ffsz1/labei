package com.juxiao.xchat.manager.mq.bo;

import lombok.Data;

@Data
public class BindPhoneMessageBO {

    private Long uid;
    private String phone;
    /** 1 绑定, 0 解绑 */
    private Integer type;
}
