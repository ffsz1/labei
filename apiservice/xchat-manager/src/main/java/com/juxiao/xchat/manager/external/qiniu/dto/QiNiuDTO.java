package com.juxiao.xchat.manager.external.qiniu.dto;

import lombok.Data;

import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2019-05-21
 * @time 20:02
 */
@Data
public class QiNiuDTO {

    private int code;
    private String message;
    private Map<String, Object> result;
}

