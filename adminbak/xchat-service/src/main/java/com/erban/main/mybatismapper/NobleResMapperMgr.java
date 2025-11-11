package com.erban.main.mybatismapper;

import com.erban.main.model.NobleRes;

import java.util.List;

public interface NobleResMapperMgr {

    List<NobleRes> selectResList(int type, int defId, int skip, int size);
}
