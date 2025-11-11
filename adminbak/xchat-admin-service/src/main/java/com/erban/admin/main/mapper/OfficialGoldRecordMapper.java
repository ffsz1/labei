package com.erban.admin.main.mapper;

import com.erban.admin.main.model.OfficialGoldRecord;
import com.erban.admin.main.model.OfficialGoldRecordExample;
import java.util.List;

import com.erban.admin.main.vo.GiveGoldRecordDTO;
import com.erban.admin.main.vo.GiveGoldRecordParam;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

public interface OfficialGoldRecordMapper {
    /**
     * 根据查询条件统计赠送总额
     * @param param 查询条件
     * @return 赠送总额
     */
    Long sumByParam(GiveGoldRecordParam param);

    /**
     * 查询赠送金币的记录
     * @param param 查询条件
     * @return
     */
    Page<GiveGoldRecordDTO> selectByParam(GiveGoldRecordParam param);

    int countByExample(OfficialGoldRecordExample example);

    int deleteByExample(OfficialGoldRecordExample example);

    int deleteByPrimaryKey(Integer recordId);

    int insert(OfficialGoldRecord record);

    int insertSelective(OfficialGoldRecord record);

    List<OfficialGoldRecord> selectByExample(OfficialGoldRecordExample example);

    OfficialGoldRecord selectByPrimaryKey(Integer recordId);

    int updateByExampleSelective(@Param("record") OfficialGoldRecord record, @Param("example") OfficialGoldRecordExample example);

    int updateByExample(@Param("record") OfficialGoldRecord record, @Param("example") OfficialGoldRecordExample example);

    int updateByPrimaryKeySelective(OfficialGoldRecord record);

    int updateByPrimaryKey(OfficialGoldRecord record);
}
