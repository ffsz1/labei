package com.juxiao.xchat.service.api.play.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2019-06-04
 * @time 17:37
 */
@Data
public class MoraConfigDTO {

    private Integer id;

    private Long uid;

    private String start;

    private String end;

    private Integer status;

    private Date createTime;
}
