package com.juxiao.xchat.dao.wish;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.dao.wish.domain.Wish;

import com.juxiao.xchat.dao.wish.dto.WishDTO;
import com.juxiao.xchat.dao.wish.query.Page;
import com.juxiao.xchat.dao.wish.query.WishQuery;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WishDao {
    /**
     * 插入用户心愿，寻爱
     * @param wish
     */
    @TargetDataSource
    void insertWish(@Param("wish") Wish wish);

    /**
     * 心愿列表
     * @param wishQuery 查询条件
     * @param page 分页条件
     * @return
     */
    @TargetDataSource
    List<WishDTO> listWish(@Param("query") WishQuery wishQuery, @Param("page") Page page);

    /**
     * 返回心愿总数
     * @param wishQuery 查询条件
     * @return
     */
    @TargetDataSource
    Long getSize(@Param("query") WishQuery wishQuery);

    /**
     * 查询单个心愿
     * @param uid
     * @return
     */
    @TargetDataSource
    Wish getById(Long uid);

    List<Long> listUidsByRecords(@Param("gender")Integer gender);
}
