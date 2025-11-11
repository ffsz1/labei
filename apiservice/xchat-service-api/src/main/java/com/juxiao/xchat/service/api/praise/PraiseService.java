package com.juxiao.xchat.service.api.praise;

import com.juxiao.xchat.dao.praise.domain.Praise;
import com.juxiao.xchat.service.api.praise.vo.PraiseVo;

/**
 * 声友点赞接口
 * author ： liangxiao
 * date :2020-09-25
 */
public interface PraiseService {
    /**
     * 动态话题点赞
     */
    PraiseVo doPraiseTopic(Praise praise);

    /**
     * 评论点赞
     */
    PraiseVo doPraiseComment(Praise praise);
}
