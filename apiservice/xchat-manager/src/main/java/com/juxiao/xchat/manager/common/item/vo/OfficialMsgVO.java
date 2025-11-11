package com.juxiao.xchat.manager.common.item.vo;

import lombok.Data;

/**
 * @Auther: alwyn
 * @Description: 大厅消息
 * @Date: 2018/9/14 12:44
 */
@Data
public class OfficialMsgVO {

    private String msg;
    private OfficialUserVO params;
}
