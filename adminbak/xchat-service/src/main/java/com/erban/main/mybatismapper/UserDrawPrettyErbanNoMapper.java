package com.erban.main.mybatismapper;

import com.erban.main.model.UserDrawPrettyErbanNo;
import com.erban.main.model.UserDrawPrettyErbanNoExample;
import java.util.List;

import com.erban.main.param.admin.PrettyErbanNoParam;
import com.erban.main.vo.admin.UserPrettyNoVo;
import org.apache.ibatis.annotations.Param;

public interface UserDrawPrettyErbanNoMapper {
    int countByExample(UserDrawPrettyErbanNoExample example);

    int deleteByExample(UserDrawPrettyErbanNoExample example);

    int deleteByPrimaryKey(Long prettyErbanNo);

    int insert(UserDrawPrettyErbanNo record);

    int insertSelective(UserDrawPrettyErbanNo record);

    List<UserDrawPrettyErbanNo> selectByExample(UserDrawPrettyErbanNoExample example);

    UserDrawPrettyErbanNo selectByPrimaryKey(Long prettyErbanNo);

    int updateByExampleSelective(@Param("record") UserDrawPrettyErbanNo record, @Param("example") UserDrawPrettyErbanNoExample example);

    int updateByExample(@Param("record") UserDrawPrettyErbanNo record, @Param("example") UserDrawPrettyErbanNoExample example);

    int updateByPrimaryKeySelective(UserDrawPrettyErbanNo record);

    int updateByPrimaryKey(UserDrawPrettyErbanNo record);

    List<UserPrettyNoVo> selectByQuery(PrettyErbanNoParam prettyErbanNoParam);
}
