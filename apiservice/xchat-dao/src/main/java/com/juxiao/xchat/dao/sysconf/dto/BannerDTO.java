package com.juxiao.xchat.dao.sysconf.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BannerDTO implements Comparable<BannerDTO> {
    private Integer bannerId;
    private String bannerName;
    private String bannerPic;
    // 跳转类型 [1.App; 2.聊天室; 3.H5]
    private Byte skipType;
    private String skipUri;
    private Integer seqNo;
    private Byte isNewUser;
    // 显示位置 [1.首页顶部; 2.发现页顶部]
    private Integer viewType;

    @Override
    public int compareTo(BannerDTO o) {
        return o.seqNo > this.seqNo ? -1 : 1;
    }
}
