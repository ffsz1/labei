package com.juxiao.xchat.service.api.guild;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.dto.GuildDTO;
import com.juxiao.xchat.dao.guild.dto.GuildExtendDTO;
import com.juxiao.xchat.service.api.guild.vo.GuildDetailVo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;

import java.util.List;

/**
 * 描述：
 *
 * @创建时间： 2020/10/13 11:39
 * @作者： carl
 */
public interface GuildService {
    List<GuildDTO> getRecommendList(IndexParam param);

    GuildDetailVo getDetail(Long guildId, Long uid) throws WebServiceException;

    List<GuildExtendDTO> search(String key, Long uid) throws WebServiceException;
}
