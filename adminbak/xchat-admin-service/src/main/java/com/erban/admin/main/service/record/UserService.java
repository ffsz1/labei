package com.erban.admin.main.service.record;

import com.erban.admin.main.mapper.UserMapperExpand;
import com.erban.admin.main.vo.UsersVo;
import com.google.common.collect.Lists;
import com.xchat.common.utils.BlankUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("userService2")
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserMapperExpand userMapperExpand;

    /**
     * 获取用户信息(金币数量)
     *
     * @param erNos
     * @return
     */
    public List<UsersVo> getUserList(String erNos) {
        if (BlankUtil.isBlank(erNos)) {
            return Lists.newArrayList();
        }
        String[] arr = erNos.split("\n");
        List<Long> erNoList = Lists.newArrayList();
        for (String erNo : arr) {
            erNoList.add(Long.valueOf(erNo.trim()));
        }
        return userMapperExpand.selectUserWithGold(erNoList);
    }

    /**
     * 获取用户信息(钻石数量)
     *
     * @param erNos
     * @return
     */
    public List<UsersVo> getUserDiamondList(String erNos) {
        if (BlankUtil.isBlank(erNos)) {
            return Lists.newArrayList();
        }

        String[] arr = erNos.split("\n");
        List<Long> erNoList = Lists.newArrayList();
        for (String erNo : arr) {
            erNoList.add(Long.valueOf(erNo.trim()));
        }
        return userMapperExpand.selectUserWithDiamond(erNoList);
    }
}
