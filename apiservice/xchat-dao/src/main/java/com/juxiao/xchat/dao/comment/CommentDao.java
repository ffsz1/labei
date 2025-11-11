package com.juxiao.xchat.dao.comment;

import com.juxiao.xchat.dao.comment.domain.Comment;
import com.juxiao.xchat.dao.comment.dto.CommentDTO;
import com.juxiao.xchat.dao.comment.query.CommentQuery;
import com.juxiao.xchat.dao.wish.query.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentDao {

    int deleteByPrimaryKey(Long id);

    int insert(Comment record);

    int insertSelective(Comment record);

    Comment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Comment record);

    int updateByPrimaryKey(Comment record);


    /**
     *  分页查询评论
     * @param commentQuery 查询条件
     * @param page 分页条件
     * @return 页面数据
     */
    List<CommentDTO> listComment(@Param("query") CommentQuery commentQuery, @Param("page") Page page);

    /**
     *  分页查询评论
     * @param commentQuery 查询条件
     * @param page 分页条件
     * @param isorder =1 时根据创建时间排序
     * @return 页面数据
     */
    List<CommentDTO> listCommentOrderByCTime(@Param("query") CommentQuery commentQuery, @Param("page") Page page, @Param("isorder") Integer isorder);

    Long getSize(@Param("query") CommentQuery commentQuery);

    /**
     * 更新回复数
     * @param id
     * @return
     */
    int upReplynum(@Param("id")Long id,@Param("num")int num);

    /**
     * 更新点赞数
     * @param id
     * @return
     */
    int upPraisenum(@Param("id")Long id,@Param("num")int num);


    /**
     * 查询一条记录
     * @param id
     * @return
     */
    CommentDTO selectById(@Param("id")Long id);
}