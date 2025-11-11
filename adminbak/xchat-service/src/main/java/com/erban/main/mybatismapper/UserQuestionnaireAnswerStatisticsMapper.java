package com.erban.main.mybatismapper;

import com.erban.main.model.UserQuestionnaireAnswerStatistics;
import com.erban.main.model.UserQuestionnaireAnswerStatisticsExample;
import com.erban.main.model.UserQuestionnaireAnswerStatisticsKey;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserQuestionnaireAnswerStatisticsMapper {
    int countByExample(UserQuestionnaireAnswerStatisticsExample example);

    int deleteByExample(UserQuestionnaireAnswerStatisticsExample example);

    int deleteByPrimaryKey(UserQuestionnaireAnswerStatisticsKey key);

    int insert(UserQuestionnaireAnswerStatistics record);

    int insertSelective(UserQuestionnaireAnswerStatistics record);

    List<UserQuestionnaireAnswerStatistics> selectByExample(UserQuestionnaireAnswerStatisticsExample example);

    UserQuestionnaireAnswerStatistics selectByPrimaryKey(UserQuestionnaireAnswerStatisticsKey key);

    int updateByExampleSelective(@Param("record") UserQuestionnaireAnswerStatistics record, @Param("example") UserQuestionnaireAnswerStatisticsExample example);

    int updateByExample(@Param("record") UserQuestionnaireAnswerStatistics record, @Param("example") UserQuestionnaireAnswerStatisticsExample example);

    int updateByPrimaryKeySelective(UserQuestionnaireAnswerStatistics record);

    int updateByPrimaryKey(UserQuestionnaireAnswerStatistics record);
}
