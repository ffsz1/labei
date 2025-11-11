package com.juxiao.xchat.dao.event.dto;

import lombok.Data;

/**
 * 魅力用户DTO对象
 *
 * @author chris
 * @date 2019-07-10
 */
@Data
public class UsersCharmDTO {
    private Long uid;
    private Long erbanNo;
    private String nick;
    private String signature;
    private String userVoice;
    private Integer voiceDuration;
    private Integer gender;
    private String avatar;
    private Integer rank;
    private Integer total;
}
