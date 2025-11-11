package com.erban.admin.main.mapper;

import com.erban.admin.main.vo.FeedbackVo;

import java.util.List;
import java.util.Map;

public interface FeedBackaAdminMapperMgr {

    List<FeedbackVo> selectByParam(Map<String, Object> map);

}
