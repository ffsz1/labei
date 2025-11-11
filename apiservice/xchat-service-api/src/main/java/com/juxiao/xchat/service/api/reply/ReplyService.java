package com.juxiao.xchat.service.api.reply;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.reply.domain.Reply;
import com.juxiao.xchat.dao.reply.dto.ReplyDTO;
import com.juxiao.xchat.service.api.wish.bo.PageBo;

import java.util.List;

/**
 * 评论回复接口
 * author：liangxiao
 * date:2020-09-25
 */
public interface ReplyService {

    /**
     * 添加回复
     * @param reply 回复
     * @return
     */
    WebServiceMessage add(Reply reply);

    /**
     * 获取回复列表
     * @param commentid 评论id
     * @return
     */
    WebServiceMessage getReplyByCommentid(Long commentid, PageBo pageBo);

    /**
     * 删除回复
     * @param id
     * @return
     */
    WebServiceMessage deleteReply(Long id);
}
