package com.juxiao.xchat.service.api.comment;

import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.comment.dto.CommentDTO;
import com.juxiao.xchat.dao.comment.query.CommentQuery;
import com.juxiao.xchat.service.api.comment.bo.CommentBo;
import com.juxiao.xchat.service.api.comment.vo.CommentVo;
import com.juxiao.xchat.service.api.wish.bo.PageBo;

import java.util.List;


public interface CommentService {

    /**
     * 新增一条评论
     * @param commentBo 评论数据
     * @return 提示信息
     */
    WebServiceMessage add(CommentBo commentBo);

    /**
     * 修改一条评论
     * @param commentBo
     * @return
     */
    WebServiceMessage update(CommentBo commentBo);

    /**
     * 删除一条评论
     * @param id
     * @return
     */
    WebServiceMessage delete(Long id);

    /**
     * 查询一条评论
     * @param id,
     * @param uid,
     * @return
     */
    WebServiceMessage getComment(Long id,Long uid);

    /**
     *  分页查询评论
     * @param commentQuery 查询条件
     * @param pageBo 分页条件
     * @return 页面数据
     */
    List<CommentVo> listComment(CommentQuery commentQuery, PageBo pageBo,Long uid);

    List<CommentVo> listComment(CommentQuery commentQuery, PageBo pageBo, int isorder,Long uid);

}
