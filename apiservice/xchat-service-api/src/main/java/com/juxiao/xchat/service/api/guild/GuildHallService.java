package com.juxiao.xchat.service.api.guild;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.guild.vo.GuildHallDetailVo;

public interface GuildHallService {

    GuildHallDetailVo getHallDetail(Long hallId, Long uid) throws WebServiceException;
}
