package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;

/**
 * @class: PrivatePhotoService.java
 * @author: chenjunsheng
 * @date 2018/7/18
 */
public interface PrivatePhotoService {

    /**
     * 上传用户照片
     *
     * @param uid
     * @param photoStr
     */
    void uploadV2(Long uid, String photoStr) throws WebServiceException;

    /**
     * 删除用户图片
     *
     * @param uid
     * @param pid
     */
    void delete(Long uid, Long pid) throws WebServiceException;
}
