package com.juxiao.xchat.service.api.soundcard;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.soundcard.vo.SoundCardResultVO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @class: SoundCardService.java
 * @author: chenjunsheng
 * @date 2018/5/24
 */

public interface SoundCardService {
    /**
     * 获取随机文本
     *
     * @param uid
     * @return
     * @author: chenjunsheng
     * @date 2018/5/28
     */
    String getText(Long uid) throws WebServiceException;

    /**
     * 分析鉴别用户的声音
     *
     * @param uid
     * @param sound
     * @return
     * @author: chenjunsheng
     * @date 2018/5/24
     */
    SoundCardResultVO analysis(Long uid, MultipartFile sound) throws WebServiceException;
}