package com.juxiao.xchat.manager.common.item.vo;

import lombok.Data;

/**
 * @Auther: alwyn
 * @Description: 大厅聊天用户信息
 * @Date: 2018/9/14 12:47
 */
@Data
public class OfficialUserVO {
    /** 头像 */
    private String avatar;
    /** uid */
    private Long uid;
    /** 等级 */
    private Long charmLevel;
    /** 昵称 */
    private String nick;
    /** 消息的 文字颜色 */
    private String txtColor;

}
