package com.erban.admin.main.mapper;

import com.erban.admin.main.vo.OutputValueParam;
import com.erban.admin.main.vo.OutputValueVo;

import java.util.List;

public interface OutputValueMapperMgr {

    List<OutputValueVo> selectByParam(OutputValueParam outputValueParam);

}
