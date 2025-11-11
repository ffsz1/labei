package com.juxiao.xchat.manager.external.im.bo;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class ImRoomMemberBO {
    private Long uid;

    private String nick;

    private String avatar;

    private Byte gender;

    private Long erbanNo;

    private Byte defUser;

    private String headwearUrl;

    private String headwearName;

    private String carUrl;

    private String carName;

    private Integer experLevel;

    private Integer charmLevel;

    private Date createTime;

    public Long getCreateTime() {
        return createTime == null ? 0 : createTime.getTime();
    }
}
