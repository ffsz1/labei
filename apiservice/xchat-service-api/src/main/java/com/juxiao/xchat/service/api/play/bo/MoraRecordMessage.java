package com.juxiao.xchat.service.api.play.bo;

import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2019-06-04
 * @time 14:57
 */
@Data
public class MoraRecordMessage {

    private String recordId;

    private Long uid;

    private Long erbanNo;

    private String nick;

    private String avatar;

    private String subject;

    private Integer experienceLevel;

    private Integer charmLevel;

    private Integer giftId;

    private Integer giftNum;

    private String giftName;

    private String giftUrl;

    private String opponentNick;



}
