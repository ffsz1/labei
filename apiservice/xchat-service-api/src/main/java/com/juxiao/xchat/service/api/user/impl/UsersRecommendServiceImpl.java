package com.juxiao.xchat.service.api.user.impl;

import com.juxiao.xchat.dao.user.FansDao;
import com.juxiao.xchat.dao.user.dto.FansDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.user.UsersRecommendService;
import com.juxiao.xchat.service.api.user.vo.UserVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

@Service
public class UsersRecommendServiceImpl implements UsersRecommendService {
    @Autowired
    private UsersManager usersManager;

    @Autowired
    private FansDao fansDao;

    @Override
    public List<UserVO> getRecommendUsers(Long uid, Integer size) {
        List<Long> uIdList = usersManager.getCharmLastChangeUids(size, uid);
        final List<UserVO> result = new ArrayList<>();
        if (!CollectionUtils.isEmpty(uIdList)) {
            uIdList.forEach(node -> {
                UsersDTO u = usersManager.getUser(node);
                if (u != null) {
                    UserVO vo = new UserVO();
                    BeanUtils.copyProperties(u, vo);
                    vo.setUserVoice(u.getUserVoice());
                    vo.setVoiceDura(u.getVoiceDura());
                    FansDTO fansDTO = fansDao.getUserLike(uid, node);
                    if (fansDTO != null) {
                        vo.setIsFan(true);
                    } else {
                        vo.setIsFan(false);
                    }
                    result.add(vo);
                }
            });
        }
        return result;
    }
}
