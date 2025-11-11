package com.juxiao.xchat.dao.wish;

import com.juxiao.xchat.dao.config.TargetDataSource;
import com.juxiao.xchat.dao.wish.domain.Label;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface LabelDao {

    @TargetDataSource
    int deleteByPrimaryKey(Integer id);
    @TargetDataSource
    int insert(Label record);
    @TargetDataSource
    int insertSelective(Label record);
    @TargetDataSource
    Label selectByPrimaryKey(Integer id);
    @TargetDataSource
    int updateByPrimaryKeySelective(Label record);
    @TargetDataSource
    int updateByPrimaryKey(Label record);

    /**
     * 查询标签列表
     * @param type 标签类型
     * @return
     */
    @TargetDataSource
    List<Label> listLabel(String type);

    /**
     * 查询所有标签
     * @return
     */
    @TargetDataSource
    List<Label> getAllLabel();
}