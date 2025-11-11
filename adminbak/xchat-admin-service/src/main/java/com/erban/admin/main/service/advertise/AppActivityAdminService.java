package com.erban.admin.main.service.advertise;

import com.erban.admin.main.dto.ActivityDTO;
import com.erban.admin.main.mapper.AdminRoleMapper;
import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.admin.main.vo.ActivityVO;
import com.erban.main.model.ActivityHtml;
import com.erban.main.model.AppActivity;
import com.erban.main.model.AppActivityExample;
import com.erban.main.mybatismapper.AppActivityMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class AppActivityAdminService extends BaseService {
    @Autowired
    private AppActivityMapper appActivityMapper;

    @Autowired
    private AdminUserService adminUserService;

    public PageInfo getList(int page, int size) {
        PageHelper.startPage(page, size);
        AppActivityExample example = new AppActivityExample();
        return new PageInfo(appActivityMapper.selectByExample(example));
    }

    public int save(AppActivity appActivity) {
        try {
            appActivity.setActAlertVersion("1.0.0");
            if (appActivity.getActId() == null) {
                appActivityMapper.insert(appActivity);
            } else {
                appActivityMapper.updateByPrimaryKey(appActivity);
            }
            jedisService.del(RedisKey.act.getKey());
            return 1;
        } catch (Exception e) {
            return 0;
        }
    }

    public AppActivity get(Integer actId) {
        return appActivityMapper.selectByPrimaryKey(actId);
    }

    /**
     * 获取所有活动页
     *
     * @param page         每页大小
     * @param size         页码
     * @param activityId   活动ID
     * @param activityName 活动名称
     * @return
     */
    public PageInfo getAll(int page, int size, String activityId, String activityName) {
        PageHelper.startPage(page, size);
        List<ActivityVO> activityVOList = new ArrayList<>();
        List<ActivityHtml> activityHtmlList = appActivityMapper.queryActivityHtml(activityId, activityName);
        activityHtmlList.forEach(item -> {
            ActivityVO activityVO = new ActivityVO();
            activityVO.setId(item.getId());
            activityVO.setActivityId(item.getActivityId());
            activityVO.setActivityName(item.getActivityName());
            activityVO.setActivityImage(item.getActivityImage());
            activityVO.setActivityShareImage(item.getActivityShareImage());
            activityVO.setActivityShareTitle(item.getActivityShareTitle());
            activityVO.setActivityShareContent(item.getActivityShareContent());
            activityVO.setActivityLink(item.getActivityLink());
            activityVO.setCreateTime(item.getCreateTime());
            activityVO.setAdminName(adminUserService.getAdminUserById(item.getAdminId()).getUsername());
            activityVOList.add(activityVO);
        });
        return new PageInfo(activityVOList);
    }

    /**
     * 增加或修改活动页
     *
     * @param activityDTO 活动页DTO对象
     * @param modify      是否为修改
     * @return
     */
    public boolean addOrModifyActivityPage(ActivityDTO activityDTO, int adminId, boolean modify) {
        boolean flag = false;
        int result = 0;

        ActivityHtml activityHtml = new ActivityHtml();
        BeanUtils.copyProperties(activityDTO, activityHtml);
        activityHtml.setAdminId(adminId);

        if (!modify) {
            String activityId = getNow();
            activityHtml.setActivityId(activityId);
            activityHtml.setCreateTime(new Date());
            activityHtml.setUpdateTime(new Date());
            activityHtml.setActivityLink("http://www.haijiaoxingqiu.cn/front/activity/template.html?id=" + activityId);
            result = appActivityMapper.insertActivityShare(activityHtml);
        } else {
            activityHtml.setUpdateTime(new Date());
            result = appActivityMapper.updateActivityShare(activityHtml);
        }

        if (result > 0) {
            flag = true;
        }

        return flag;
    }

    /**
     * 获取当前年月日时分秒字符串
     *
     * @return
     */
    private String getNow() {
        Calendar now = Calendar.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(now.get(Calendar.YEAR));
        stringBuilder.append((now.get(Calendar.MONTH) + 1));
        stringBuilder.append(now.get(Calendar.DAY_OF_MONTH));
        stringBuilder.append(now.get(Calendar.HOUR_OF_DAY));
        stringBuilder.append(now.get(Calendar.MINUTE));
        stringBuilder.append(now.get(Calendar.SECOND));
        return stringBuilder.toString();
    }
}
