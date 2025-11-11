package com.erban.admin.main.mapper;

import com.erban.admin.main.vo.TreasureBoxVo;

import java.util.List;
import java.util.Map;

public interface TreasureBoxMapperMgr {
    List<TreasureBoxVo> selectByExample(Map<String, Object> map);

    TreasureBoxVo selectByCount(Map<String, Object> map);

    List<TreasureBoxVo> selectByExportExample(Map<String, Object> map);

    List<TreasureBoxVo> selectByUsersWhitelisExample(Map<String, Object> map);

    List<TreasureBoxVo> selectByExportWhitelistExample(Map<String, Object> map);

    TreasureBoxVo selectByWhitelistCount(Map<String, Object> map);
}
