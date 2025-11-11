package com.juxiao.xchat.service.task.user;

import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.user.vo.UserSimpleVO;
import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;

import java.util.Date;
import java.util.List;

public interface UsersTaskService {
    void saveOppositeSex(String gender, Date startDate, Date endDate);

    /**
     * 刷新声音匹配的缓存
     */
    void refreshSoundPool();
}
