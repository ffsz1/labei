package com.juxiao.xchat.manager.common.sysconf.resp;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2019-04-30
 * @time 18:48
 */
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class IpMessageResult {
    private int ret;
    private Map<String, Object> data;
    private String msg;
}