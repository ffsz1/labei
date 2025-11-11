package com.juxiao.xchat.dao.wish;

import com.juxiao.xchat.dao.config.TargetDataSource;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WishLabelDao {
    @TargetDataSource
    void insertWishLabels(@Param("uid") Long uid, @Param("ids") List<Integer> LabelIds);
    @TargetDataSource
    List<String> listLabelName(@Param("uid") Long uid, @Param("type") String type);
    @TargetDataSource
    void deleteByUid(Long uid);
}
