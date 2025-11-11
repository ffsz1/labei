package com.juxiao.xchat.service.record.output.dto;

import lombok.Data;

/**
 * @author chris
 * @Title:
 * @date 2018/10/19
 * @time 15:35
 */
@Data
public class HomeChannelDTO {

    private String id;
    private String channel;

    public HomeChannelDTO(){

    }

    public HomeChannelDTO(String id,String channel){
        this.id = id;
        this.channel = channel;
    }

}
