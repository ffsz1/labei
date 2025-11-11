package com.erban.main.mybatismapper;

import com.erban.main.model.McoinDrawIssue;
import com.erban.main.model.McoinDrawIssueExample;
import com.erban.main.model.dto.McoinDrawIssueDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface McoinDrawIssueMapper {
    int countByExample(McoinDrawIssueExample example);

    int deleteByExample(McoinDrawIssueExample example);

    int deleteByPrimaryKey(Long id);

    int insert(McoinDrawIssue record);

    int insertSelective(McoinDrawIssue record);

    List<McoinDrawIssue> selectByExample(McoinDrawIssueExample example);

    McoinDrawIssue selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") McoinDrawIssue record, @Param("example") McoinDrawIssueExample example);

    int updateByExample(@Param("record") McoinDrawIssue record, @Param("example") McoinDrawIssueExample example);

    int updateByPrimaryKeySelective(McoinDrawIssue record);

    int updateByPrimaryKey(McoinDrawIssue record);

    /**
     *
     * @param itemType
     * @param issueStatus
     * @return
     */
    List<McoinDrawIssueDTO> findMcoinDrawIssue(@Param("itemType") int itemType, @Param("issueStatus") int issueStatus);
}
