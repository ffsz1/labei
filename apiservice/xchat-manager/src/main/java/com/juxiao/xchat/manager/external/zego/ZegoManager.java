package com.juxiao.xchat.manager.external.zego;

public interface ZegoManager {

    /**
     * 生成第三方token
     *
     * @param roomUid
     * @param uid
     * @return
     */
    String generateThirdToken(Long roomUid, Long uid) throws Exception;

    /**
     * 获取即构 AccessToken
     *
     * @return
     */
    String getAccessToken() throws Exception;
}
