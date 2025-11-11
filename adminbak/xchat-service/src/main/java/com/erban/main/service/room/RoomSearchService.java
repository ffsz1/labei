package com.erban.main.service.room;


import com.erban.main.model.NobleUsers;
import com.erban.main.mybatismapper.RoomMapperExpand;
import com.erban.main.service.base.BaseService;
import com.erban.main.service.noble.NobleUsersService;
import com.erban.main.vo.SearchVo;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 房间搜索
 */
@Service
public class RoomSearchService extends BaseService {


    @Autowired
    RoomMapperExpand roomMapperExpand;
    @Autowired
    private NobleUsersService nobleUsersService;

    /**
     * @param key 关键字（包括拉贝号，用户昵称，房间标题）
     * @return
     */
    public List<SearchVo> searchRoomByKey(String key,List<Long> uids) {
        Map<String, Object> map = new HashMap<>();
        if(StringUtils.isNotBlank(key)){
            map.put("key", key);
        }
        if(uids!=null){
            map.put("uids", uids);
        }
        List<SearchVo> usersList = roomMapperExpand.searchRoomByKey(map);
        if (BlankUtil.isBlank(usersList)) {
            return null;
        }
        return usersList;

    }


}
