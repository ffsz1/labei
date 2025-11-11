package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.UserTodayShareDTO;
import com.juxiao.xchat.dao.user.dto.UserWxappDrawDTO;
import com.juxiao.xchat.manager.common.user.UserWxappDrawManager;
import com.juxiao.xchat.manager.common.user.UserWxappDrawRecordManager;
import com.juxiao.xchat.service.api.user.UserShareService;
import com.juxiao.xchat.service.api.user.vo.WxappShareInfoVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 */
@Service
public class UserShareServiceImpl implements UserShareService {

    @Autowired
    private UserWxappDrawManager wxappDrawManager;
    @Autowired
    private UserWxappDrawRecordManager wxappDrawRecordManager;

    @Override
    public WxappShareInfoVO getUserWxappShareInfo(Long uid) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        UserWxappDrawDTO wxappDrawDto = wxappDrawManager.getUserWxappDraw(uid);
        List<UserTodayShareDTO> list = wxappDrawRecordManager.listTodayShareUser(uid);
        return new WxappShareInfoVO(wxappDrawDto == null ? 0 : wxappDrawDto.getLeftDrawCount(), list);
    }
}
