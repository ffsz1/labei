package com.juxiao.xchat.service.api.guild;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.guild.dto.GuildHallApplyRecordDTO;
import com.juxiao.xchat.service.api.guild.bo.ApplyHallParamBo;
import com.juxiao.xchat.service.api.guild.bo.VerifyParamBo;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;

import java.util.List;

public interface GuildHallApplyRecordService {

    boolean applyJoinHall(ApplyHallParamBo paramBo) throws WebServiceException;

    boolean applyExitHall(Long uid) throws WebServiceException;

    int getApplyJoinCount(Long uid) throws WebServiceException;

    List<GuildHallApplyRecordDTO> getApplyJoinRecords(IndexParam param, Long uid) throws WebServiceException;

    boolean verify(VerifyParamBo paramBo) throws WebServiceException;
}
