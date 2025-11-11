package com.juxiao.xchat.manager.external.netease.ret;

import com.juxiao.xchat.manager.external.netease.bo.FriendBO;
import lombok.Data;

import java.util.List;

/**
 * 好友列表
 */
@Data
public class FriendRet {

    private Long code;
    private Long size;
    private List<FriendBO> friends;
}
