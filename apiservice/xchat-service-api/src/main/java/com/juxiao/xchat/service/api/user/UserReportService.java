package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.user.bo.UserItemReportBO;
import com.juxiao.xchat.service.api.user.bo.UserReportBO;

/**
 * @class: UserReportService.java
 * @author: chenjunsheng
 * @date 2018/7/23
 */
public interface UserReportService {

    /**
     * @param reportBo
     * @return
     */
    void save(UserReportBO reportBo) throws WebServiceException;

    /**
     * 头像举报
     * @param reportBo reportBo
     */
    void saveAvatar(UserItemReportBO reportBo)throws WebServiceException;

    /**
     * 相册举报
     * @param reportBo reportBo
     */
    void saveAlbum(UserItemReportBO reportBo)throws WebServiceException;
}
