package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.user.vo.UserPacketRecordVO;

/**
 * @class: UserShareRecordService.java
 * @author: chenjunsheng
 * @date 2018/6/14
 */
public interface UserShareRecordService {


    /**
     * 保存用户的分享记录
     *
     * @param uid
     * @param shareType
     * @param sharePageId
     * @param targetUid
     * @return
     * @throws WebServiceException
     */
    UserPacketRecordVO saveShareRecord(Long uid, String shareType, Integer sharePageId, Long targetUid) throws WebServiceException;

    /**
     * @param uid
     * @param targetUid
     * @return
     */
    UserPacketRecordVO saveUserShareRegisterRecord(Long uid, Long targetUid);


    /**
     * 新人分享红包
     *
     * @param uid
     * @param shareCode
     * @return
     */
    UserPacketRecordVO saveUserShareRegisterRecord(Long uid, String shareCode) throws WebServiceException;
}
