package com.erban.main.mybatismapper;

import com.erban.main.model.YoungGTask;
import com.erban.main.model.YoungGTaskExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface YoungGTaskMapper {
    int countByExample(YoungGTaskExample example);

    int deleteByExample(YoungGTaskExample example);

    int deleteByPrimaryKey(Long id);

    int insert(YoungGTask record);

    int insertSelective(YoungGTask record);

    List<YoungGTask> selectByExample(YoungGTaskExample example);

    YoungGTask selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") YoungGTask record, @Param("example") YoungGTaskExample example);

    int updateByExample(@Param("record") YoungGTask record, @Param("example") YoungGTaskExample example);

    int updateByPrimaryKeySelective(YoungGTask record);

    int updateByPrimaryKey(YoungGTask record);
}
