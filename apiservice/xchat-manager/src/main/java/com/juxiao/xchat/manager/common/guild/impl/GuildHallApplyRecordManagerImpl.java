package com.juxiao.xchat.manager.common.guild.impl;

import com.juxiao.xchat.dao.guild.GuildHallApplyRecordDao;
import com.juxiao.xchat.manager.common.guild.GuildHallApplyRecordManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 描述：
 *
 * @创建时间： 2020/10/14 14:46
 * @作者： carl
 */
@Slf4j
@Service
public class GuildHallApplyRecordManagerImpl implements GuildHallApplyRecordManager {

    @Autowired
    private GuildHallApplyRecordDao guildHallApplyRecordDao;

}
