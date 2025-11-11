package com.juxiao.xchat.dao.sysconf.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author chris
 * @Title: app标签分类审核配置
 * @date 2018/10/30
 * @time 11:19
 */
@Data
public class ReviewConfigDTO {

    private Integer id;

    private String tagName;

    private Integer rechargeAmount;

    private Integer status;

    private Date createTime;

    private String versions;

    private String channel;

    private String system;

}
