package com.juxiao.xchat.dao.user.query;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserPacketSumQuery {

    private Long uid;

    private Date startTime;

    private Date endTime;


}
