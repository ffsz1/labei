package com.juxiao.xchat.service.api.item.params;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @author chris
 * @Title: 全服礼物
 * @date 2018/11/16
 * @time 15:25
 */
@Data
public class FullParams {

    private String giftName;

    private Integer count;

    private String nick;

    @JSONField(name = "isFull")
    private boolean isFull;


}
