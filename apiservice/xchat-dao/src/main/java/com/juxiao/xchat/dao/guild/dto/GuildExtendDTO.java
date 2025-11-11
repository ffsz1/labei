package com.juxiao.xchat.dao.guild.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 描述：
 *
 * @创建时间： 2020/10/21 12:01
 * @作者： carl
 */
@Data
public class GuildExtendDTO extends GuildDTO {
    @JsonProperty("isMyGuild")
    private boolean isMyGuild;
}
