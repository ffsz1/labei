package com.erban.admin.main.mapper;

import com.erban.admin.main.dto.UsersMiningMustDTO;

import java.util.List;
import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2018/10/15
 * @time 15:32
 */
public interface UsersMiningMustMgr {

    List<UsersMiningMustDTO> selectByParam(Map<String, Object> map);

    String selectGiftName(Integer giftId);
}
