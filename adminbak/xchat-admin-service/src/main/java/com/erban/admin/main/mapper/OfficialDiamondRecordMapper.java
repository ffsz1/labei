package com.erban.admin.main.mapper;

import com.erban.admin.main.model.OfficialDiamondRecord;
import com.erban.admin.main.vo.GiveDiamondRecordDTO;
import com.erban.admin.main.vo.GiveDiamondRecordParam;
import com.github.pagehelper.Page;

public interface OfficialDiamondRecordMapper {
    /**
     * 根据查询条件统计赠送总额
     *
     * @param param 查询条件
     * @return 赠送总额
     */
    Long sumByParam(GiveDiamondRecordParam param);

    /**
     * 查询赠送金币的记录
     *
     * @param param 查询条件
     * @return
     */
    Page<GiveDiamondRecordDTO> selectByParam(GiveDiamondRecordParam param);

    int insert(OfficialDiamondRecord record);
}
