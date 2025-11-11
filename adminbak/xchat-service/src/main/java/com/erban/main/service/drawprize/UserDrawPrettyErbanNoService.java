package com.erban.main.service.drawprize;

import com.erban.main.model.UserDrawPrettyErbanNo;
import com.erban.main.model.UserDrawPrettyErbanNoExample;
import com.erban.main.mybatismapper.UserDrawPrettyErbanNoMapper;
import com.erban.main.service.base.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class UserDrawPrettyErbanNoService extends BaseService {
    @Autowired
    private UserDrawPrettyErbanNoMapper userDrawPrettyErbanNoMapper;

    /**
     * @param type 1七位靓号
     */
    public UserDrawPrettyErbanNo getNotUsePrettyErbanNoByType(Byte type) {
        UserDrawPrettyErbanNoExample userDrawPrettyErbanNoExample = new UserDrawPrettyErbanNoExample();
        userDrawPrettyErbanNoExample.createCriteria().andUseStatusEqualTo(new Byte("1")).andTypeEqualTo(type);
        userDrawPrettyErbanNoExample.setOrderByClause("seq asc, create_time asc");
        List<UserDrawPrettyErbanNo> userDrawPrettyErbanNoList = userDrawPrettyErbanNoMapper.selectByExample(userDrawPrettyErbanNoExample);
        if (CollectionUtils.isEmpty(userDrawPrettyErbanNoList)) {
            return new UserDrawPrettyErbanNo();
        } else {
            return userDrawPrettyErbanNoList.get(0);
        }
    }

    public void updatePrettyErbanNoByType(Byte type) {
        jdbcTemplate.update("UPDATE `user_draw_pretty_erban_no` SET `use_status`='2' WHERE type = ? and use_status = 1 order by seq asc, create_time asc limit 1", type);
    }

}
