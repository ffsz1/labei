package com.juxiao.xchat.dao.output.bo;

import lombok.Data;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/17
 * @time 16:31
 */
@Data
public class OutputValueParam {

    private Integer pageNum  = 0;
    private Integer pageSize = 30;
    //平台
    private String os;
    private String channel;
    private String linkedmeChannel;
    private Byte gender;
    private String signBegin;
    private String signEnd;
    private Byte showType;
    private String uidList;
    private List<String> mediumChannel;
    private String medium;
    private String groupId;
    //
    private Long shareUid;
    private List<Long> shareUidList;
    //
    private String date;
    private Integer days;

}
