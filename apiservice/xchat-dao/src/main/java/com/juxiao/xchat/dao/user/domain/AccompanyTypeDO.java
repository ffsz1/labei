package com.juxiao.xchat.dao.user.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: 陪玩分类
 * @date 2018/11/15
 * @time 12:35
 */
@Data
public class AccompanyTypeDO {

    private Integer id;

    private String name;

    private String pic;

    private Integer status;

    private Date createTime;
}
