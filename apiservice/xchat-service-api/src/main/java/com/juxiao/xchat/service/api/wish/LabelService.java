package com.juxiao.xchat.service.api.wish;



import com.juxiao.xchat.dao.wish.domain.Label;
import com.juxiao.xchat.service.api.wish.vo.LabelVo;

import java.util.List;

public interface LabelService {
    /**
     * 获取遇见标签库
     * @return
     */
    List<Label> getMeetLabels();

    /**
     * 获取个性标签库
     * @return
     */
    List<Label> getPensonlLabels();

    /**
     * 获取全部标签
     * @return
     */
    LabelVo getLabels();
}
