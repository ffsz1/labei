package com.juxiao.xchat.service.api.praise;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.praise.domain.UsersPraise;
import com.juxiao.xchat.dao.praise.dto.UsersPraiseDTO;
import com.juxiao.xchat.service.api.praise.vo.UsersPraisedVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;

import java.util.Date;
import java.util.List;

public interface UsersPraiseService {

    /**
     * 点赞
     * @param uid 点赞发动人
     * @param praisedUid 被点赞人
     * @return
     */
    WebServiceMessage doPraise(Long uid,Long praisedUid);

    /**
     * 查询点赞状态
     * @param uid 点赞查询人
     * @param praisedUid 被查看点赞人
     * @return
     */
    UsersPraisedVo findStatus(Long uid, Long praisedUid);

    /**
     * 查询历史点赞数据
     * @param praisedUid
     * @param pageBo
     * @return
     */
    List<UsersPraiseDTO> findHistory(Long praisedUid, PageBo pageBo);
}
