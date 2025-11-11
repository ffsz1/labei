package com.juxiao.xchat.dao.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class PrivatePhotoDTO implements Comparable<PrivatePhotoDTO> {
    private Long pid;
    private String photoUrl;
    private Integer seqNo;
    private Date createTime;
    private Integer photoStatus;

    @Override
    public int compareTo(PrivatePhotoDTO photoVo) {
        long photoTimeStamps = photoVo.createTime.getTime();
        long thisTimeStamps = this.createTime.getTime();
        if (photoTimeStamps > thisTimeStamps) {
            return 1;
        } else if (photoTimeStamps < thisTimeStamps) {
            return -1;
        } else {
            return 0;
        }
    }
}
