package com.erban.main.mybatismapper;

import java.util.Date;

public interface NobleUsersMapperMgr {

    int updateRenewNobleUsers(Date expire, Byte count, Long uid);

    int reduceRecomCount(Long uid);
}
