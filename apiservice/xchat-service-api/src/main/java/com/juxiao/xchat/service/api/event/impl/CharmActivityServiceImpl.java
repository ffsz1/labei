package com.juxiao.xchat.service.api.event.impl;

import com.google.common.collect.Lists;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.dao.event.UsersCharmDAO;
import com.juxiao.xchat.dao.event.dto.UsersCharmDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.event.CharmActivityService;
import com.juxiao.xchat.service.api.event.vo.UsersCharmVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author chris
 * @date 2019-07-10
 */
@Service
public class CharmActivityServiceImpl implements CharmActivityService {

    @Autowired
    private UsersCharmDAO usersCharmDAO;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private UsersManager usersManager;


    /**
     * 获取魅力周榜数据
     *
     * @param uid      uid
     * @return List
     */
    @Override
    public List<UsersCharmVO> getUsersCharmByPage(Long uid) {
        List<Long> noList = getUidList();
        List<UsersCharmVO> charmVOS = Lists.newArrayList();
        if(noList.size() > 0) {
            List<UsersCharmDTO> usersCharmDTOS = usersCharmDAO.selectUsersCharmByPage(noList);
            if(usersCharmDTOS.size() > 0){
                usersCharmDTOS.forEach(item ->{
                    UsersDTO usersDTO = usersManager.getUser(item.getUid());
                    UsersCharmVO usersCharmVO = new UsersCharmVO();
                    usersCharmVO.setUid(item.getUid());
                    usersCharmVO.setRank(item.getRank());
                    usersCharmVO.setTotalNum(item.getTotal());
                    usersCharmVO.setAvatar(usersDTO.getAvatar());
                    usersCharmVO.setErbanNo(usersDTO.getErbanNo());
                    usersCharmVO.setNick(usersDTO.getNick());
                    charmVOS.add(usersCharmVO);
                });
            }
        }
        return charmVOS;
    }



    private List<Long> getUidList(){
        Map<String, String> result = Optional.ofNullable(redisManager.hgetAll(RedisKey.users_charm_uid.getKey())).orElse(new HashMap<>());
        List<Long> list = new ArrayList<>();
        if(!CollectionUtils.isEmpty(result)) {
            for(Map.Entry<String, String> entry : result.entrySet()) {
                list.add(Long.valueOf(entry.getValue()));
            }
            return list;
        }else{
            list = usersCharmDAO.selectByUid();
            return list;
        }
    }
}
