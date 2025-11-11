package com.juxiao.xchat.manager.common.user;


import com.juxiao.xchat.dao.user.dto.PrivatePhotoDTO;

import java.util.List;

/**
 * @class: PrivatePhotoManager.java
 * @author: chenjunsheng
 * @date 2018/6/15
 */
public interface PrivatePhotoManager {

    /**
     * @param uid
     * @return
     */
    List<PrivatePhotoDTO> listUserPrivatePhoto(Long uid);
}
