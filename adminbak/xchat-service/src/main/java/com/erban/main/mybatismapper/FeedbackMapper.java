package com.erban.main.mybatismapper;

import com.erban.main.model.Feedback;
import com.erban.main.model.FeedbackExample;
import java.util.List;

public interface FeedbackMapper {
    int deleteByPrimaryKey(String feedbackId);

    int insert(Feedback record);

    int insertSelective(Feedback record);

    List<Feedback> selectByExample(FeedbackExample example);

    Feedback selectByPrimaryKey(String feedbackId);

    int updateByPrimaryKeySelective(Feedback record);

    int updateByPrimaryKey(Feedback record);
}
