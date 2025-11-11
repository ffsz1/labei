package com.erban.main.mybatismapper;

import com.erban.main.model.Fans;

import java.util.List;

public interface FansMapperExpand {
    public List<Fans> getFollowingListByUidPage(Long uid, int pageNo, int pageSize);

    Integer getFollowingCount(Long uid);

    Integer getFansCount(Long uid);

    List<Fans> getFansListByUidPage(Long uid, Integer pageNo, Integer pageSize);
}
