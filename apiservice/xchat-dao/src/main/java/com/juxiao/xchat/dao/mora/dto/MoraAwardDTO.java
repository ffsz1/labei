package com.juxiao.xchat.dao.mora.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title:
 * @date 2019-06-03
 * @time 14:22
 */
@Data
public class MoraAwardDTO {

    private Integer id;

    private String json;

    private Integer probability;

    private Integer isUse;

    private Integer grade;

    private Date createTime;
}
