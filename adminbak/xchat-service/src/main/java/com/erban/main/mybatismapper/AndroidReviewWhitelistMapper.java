package com.erban.main.mybatismapper;

import com.erban.main.dto.AndroidReviewWhitelistDTO;
import com.erban.main.model.AndroidReviewWhitelist;
import com.erban.main.model.AndroidReviewWhitelistExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface AndroidReviewWhitelistMapper {
    int countByExample(AndroidReviewWhitelistExample example);

    int deleteByExample(AndroidReviewWhitelistExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(AndroidReviewWhitelist record);

    int insertSelective(AndroidReviewWhitelist record);

    List<AndroidReviewWhitelist> selectByExample(AndroidReviewWhitelistExample example);

    AndroidReviewWhitelist selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") AndroidReviewWhitelist record, @Param("example") AndroidReviewWhitelistExample example);

    int updateByExample(@Param("record") AndroidReviewWhitelist record, @Param("example") AndroidReviewWhitelistExample example);

    int updateByPrimaryKeySelective(AndroidReviewWhitelist record);

    int updateByPrimaryKey(AndroidReviewWhitelist record);

    List<AndroidReviewWhitelistDTO> selectByList(@Param("searchText") String searchText);
}
