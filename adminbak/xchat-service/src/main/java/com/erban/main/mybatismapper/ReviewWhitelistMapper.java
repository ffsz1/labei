package com.erban.main.mybatismapper;

import com.erban.main.dto.ReviewWhitelistDTO;
import com.erban.main.model.ReviewWhitelist;
import com.erban.main.model.ReviewWhitelistExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ReviewWhitelistMapper {
    int countByExample(ReviewWhitelistExample example);

    int deleteByExample(ReviewWhitelistExample example);

    int deleteByPrimaryKey(Long id);

    int insert(ReviewWhitelist record);

    int insertSelective(ReviewWhitelist record);

    List<ReviewWhitelist> selectByExample(ReviewWhitelistExample example);

    ReviewWhitelist selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") ReviewWhitelist record, @Param("example") ReviewWhitelistExample example);

    int updateByExample(@Param("record") ReviewWhitelist record, @Param("example") ReviewWhitelistExample example);

    int updateByPrimaryKeySelective(ReviewWhitelist record);

    int updateByPrimaryKey(ReviewWhitelist record);

    List<ReviewWhitelistDTO> selectByPage(@Param("search") String search);}
