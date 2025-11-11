package com.erban.main.mybatismapper;

import com.erban.main.model.PayErrorInfo;
import com.erban.main.model.PayErrorInfoExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface PayErrorInfoMapper {
    int countByExample(PayErrorInfoExample example);

    int deleteByExample(PayErrorInfoExample example);

    int deleteByPrimaryKey(Long id);

    int insert(PayErrorInfo record);

    int insertSelective(PayErrorInfo record);

    List<PayErrorInfo> selectByExample(PayErrorInfoExample example);

    PayErrorInfo selectByPrimaryKey(Long id);

    int updateByExampleSelective(@Param("record") PayErrorInfo record, @Param("example") PayErrorInfoExample example);

    int updateByExample(@Param("record") PayErrorInfo record, @Param("example") PayErrorInfoExample example);

    int updateByPrimaryKeySelective(PayErrorInfo record);

    int updateByPrimaryKey(PayErrorInfo record);
}
