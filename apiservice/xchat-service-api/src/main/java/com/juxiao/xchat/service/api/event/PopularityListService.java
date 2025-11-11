package com.juxiao.xchat.service.api.event;

import com.juxiao.xchat.service.api.event.vo.PopularityListVO;

import java.util.List;
import java.util.Map;

public interface PopularityListService {
    Map<String, List<PopularityListVO>> getTop20List();

    PopularityListVO getMyRank(Long uid);

    List<PopularityListVO> getUserRecommend(Long uid);

    Map<String, List<PopularityListVO>> getLastWeekRank();
}
