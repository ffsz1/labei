package com.erban.admin.main.mapper;

import com.erban.admin.main.vo.GiftVo;
import com.xchat.oauth2.service.param.AccountParam;
import com.xchat.oauth2.service.vo.admin.AccountVo;

import java.util.List;
import java.util.Map;

public interface StatReportMapper {
    List<AccountVo> selectByChargeAmount(AccountParam accountParam);

    List<AccountVo> selectByExechangeDiamond(AccountParam accountParam);

    List<AccountVo> selectByDiamond(AccountParam accountParam);

    List<AccountVo> selectByExper(AccountParam accountParam);

    List<AccountVo> selectByCharm(AccountParam accountParam);

    List<GiftVo> getGiftSend(Map<String, Object> map);
}
