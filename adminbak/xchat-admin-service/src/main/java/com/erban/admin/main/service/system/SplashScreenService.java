package com.erban.admin.main.service.system;

import com.erban.admin.main.dto.SplashScreenDTO;
import com.erban.admin.main.mapper.AdminUserMapper;
import com.erban.admin.main.mapper.SplashScreenMapper;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.ActivityVO;
import com.erban.admin.main.vo.SplashScreenVO;
import com.erban.main.model.ActivityHtml;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SplashScreenService extends BaseService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SplashScreenMapper splashScreenMapper;

    /**
     * 获取所有闪屏信息
     *
     * @param page     每页大小
     * @param size     页码
     * @param type     跳转类型 [0.无; 1.App页面; 2.聊天室; 3.H5页面]
     * @param status   状态 [1.未启用; 2.启用中]
     * @param userType 用户 [0.全部; 1.新用户; 2.老用户]
     * @return
     */
    public PageInfo getAll(int page, int size, Integer type, Integer status, Integer userType) {
        PageHelper.startPage(page, size);
        List<SplashScreenVO> splashScreenVOList = new ArrayList<>();
        List<SplashScreenDTO> splashScreenDTOList = splashScreenMapper.queryByCondition(type, status, userType);
        splashScreenDTOList.forEach(item -> {
            SplashScreenVO splashScreenVO = new SplashScreenVO();
            splashScreenVO.setPicId(item.getPicId());
            splashScreenVO.setPicName(item.getPicName());
            splashScreenVO.setUserType(item.getUserType());
            splashScreenVO.setPicStatus(item.getPicStatus());
            splashScreenVO.setPicImage(item.getPicImage());
            splashScreenVO.setPicUrl(item.getPicUrl());
            splashScreenVO.setPicType(item.getPicType());
            splashScreenVO.setCreateTime(item.getCreateTime());
            splashScreenVO.setStartTime(item.getStartTime());
            splashScreenVO.setEndTime(item.getEndTime());
            splashScreenVOList.add(splashScreenVO);
        });
        return new PageInfo(splashScreenVOList);
    }

    /**
     * 根据ID获取闪屏信息
     *
     * @param id ID
     * @return
     */
    public SplashScreenVO getOne(Integer id) {
        SplashScreenVO splashScreenVO = new SplashScreenVO();
        SplashScreenDTO splashScreenDTO = splashScreenMapper.queryByID(id);
        BeanUtils.copyProperties(splashScreenDTO, splashScreenVO);
        return splashScreenVO;
    }

    /**
     * 增加或修改闪屏信息
     *
     * @param splashScreenDTO 闪屏DTO对象
     * @param modify          是否为修改
     * @return
     */
    public boolean addOrModifyActivityPage(SplashScreenDTO splashScreenDTO, boolean modify) {
        boolean flag = false;
        int result = 0;

        if (!modify) {
            if (splashScreenDTO.getCreateTime() == null) {
                splashScreenDTO.setCreateTime(new Date());
            }
            result = splashScreenMapper.insertSplashScreen(splashScreenDTO);
        } else {
            result = splashScreenMapper.updateSplashScreen(splashScreenDTO);
        }

        if (result > 0) {
            flag = true;
            jedisService.del(RedisKey.sys_conf_splash.getKey("tianya"));
            jedisService.del(RedisKey.sys_conf_splash.getKey("1001"));
        }

        return flag;
    }
}
