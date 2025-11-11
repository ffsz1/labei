package com.erban.admin.main.mapper;

import com.erban.admin.main.vo.HomeHotManualRecommVo;

import java.util.List;
import java.util.Map;

public interface HomeRecommMapperMgr {

    List<HomeHotManualRecommVo> selectByParam(Map<String, Object> map);

}
