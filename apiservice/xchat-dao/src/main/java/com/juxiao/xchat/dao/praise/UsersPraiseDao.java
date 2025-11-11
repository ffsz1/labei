package com.juxiao.xchat.dao.praise;

import com.juxiao.xchat.dao.praise.domain.UsersPraise;
import com.juxiao.xchat.dao.praise.dto.UsersPraiseDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UsersPraiseDao {

    int deleteByPrimaryKey(Long id);

    int insert(UsersPraise record);

    int insertSelective(UsersPraise record);

    UsersPraise selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UsersPraise record);

    int updateByPrimaryKey(UsersPraise record);

    /**
     * 查询今天是否被赞记录
     * @return
     */
    UsersPraise findPraised(@Param("uid")Long uid,@Param("praisedUid")Long praisedUid,@Param("date")String date);

    /**
     * 查询被赞列表
     * @return
     */
    List<UsersPraiseDTO>  selectByPraisedUid(@Param("praisedUid")Long praisedUid,@Param("page")Page page);

    Long getSize(@Param("praisedUid")Long praisedUid);
}