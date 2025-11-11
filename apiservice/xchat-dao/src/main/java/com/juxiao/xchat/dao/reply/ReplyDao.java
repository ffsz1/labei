package com.juxiao.xchat.dao.reply;

import com.juxiao.xchat.dao.reply.domain.Reply;
import com.juxiao.xchat.dao.reply.dto.ReplyDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 评论回复dao接口
 * author :liangxiao
 * date:2020-09-25
 */
@Mapper
public interface ReplyDao {

    int deleteByPrimaryKey(Long id);

    int insert(Reply record);

    int insertSelective(Reply record);

    Reply selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Reply record);

    int updateByPrimaryKey(Reply record);

    /**
     * 查询回复列表 根据评论id
     * @param commentid 评论id
     * @return
     */
    List<ReplyDTO> selectByCommentId(@Param("commentid")Long commentid,@Param("page")Page page);

    int deleteByCommentId(@Param("commentid")Long commentid);
}