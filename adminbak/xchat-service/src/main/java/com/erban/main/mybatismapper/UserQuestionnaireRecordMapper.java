package com.erban.main.mybatismapper;

import com.erban.main.model.UserQuestionnaireRecord;
import com.erban.main.model.UserQuestionnaireRecordExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface UserQuestionnaireRecordMapper {
    int countByExample(UserQuestionnaireRecordExample example);

    int deleteByExample(UserQuestionnaireRecordExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(UserQuestionnaireRecord record);

    int insertSelective(UserQuestionnaireRecord record);

    List<UserQuestionnaireRecord> selectByExample(UserQuestionnaireRecordExample example);

    UserQuestionnaireRecord selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") UserQuestionnaireRecord record, @Param("example") UserQuestionnaireRecordExample example);

    int updateByExample(@Param("record") UserQuestionnaireRecord record, @Param("example") UserQuestionnaireRecordExample example);

    int updateByPrimaryKeySelective(UserQuestionnaireRecord record);

    int updateByPrimaryKey(UserQuestionnaireRecord record);
}
