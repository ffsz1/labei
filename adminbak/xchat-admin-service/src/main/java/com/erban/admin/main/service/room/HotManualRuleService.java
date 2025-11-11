package com.erban.admin.main.service.room;

import com.erban.admin.main.service.room.bo.HotManualRuleBO;
import com.erban.main.model.HomeHotManualRecomm;
import com.erban.main.model.Users;
import com.erban.main.model.domain.HotManualRuleDO;
import com.erban.main.model.dto.HotManualRuleDTO;
import com.erban.main.mybatismapper.HomeHotManualRecommMapper;
import com.erban.main.mybatismapper.dao.HotManualRuleDAO;
import com.erban.main.mybatismapper.query.HotManualRuleQuery;
import com.erban.main.service.user.UsersService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.DateUtil;
import com.xchat.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Service
public class HotManualRuleService {

    @Autowired
    private HotManualRuleDAO hotManualRuleDAO;
    @Autowired
    private UsersService usersService;
    @Autowired
    private HomeHotManualRecommMapper homeHotManualRecommMapper;

    /**
     * 列表查询
     * @param pageNum
     * @param pageSize
     * @param query
     * @return
     */
    public PageInfo<HotManualRuleDTO> list(int pageNum, int pageSize, HotManualRuleQuery query) {
        //
        PageHelper.startPage(pageNum, pageSize);
        List<HotManualRuleDTO> list = hotManualRuleDAO.listByParam(query);
        return new PageInfo<>(list);
    }

    /**
     * 保存信息
     * @param ruleBO
     * @return
     */
    public BusiResult save(HotManualRuleBO ruleBO) {
        Users users = usersService.getUsresByErbanNo(ruleBO.getErbanNo());
        if (users == null) {
            return new BusiResult(BusiStatus.USERNOTEXISTS);
        }
        HotManualRuleDO ruleDO = new HotManualRuleDO();
        ruleDO.setUid(users.getUid());
        ruleDO.setWeekDays(ruleBO.getWeekDays());
        ruleDO.setStartDate(ruleBO.getStartDate());
        ruleDO.setEndDate(ruleBO.getEndDate());
        ruleDO.setCreateDate(new Date());
        ruleDO.setStatus(ruleBO.getStatus());
        ruleDO.setSeqNo(ruleBO.getSeqNo());
        ruleDO.setViewType(ruleBO.getViewType());
        int count;
        if (ruleBO.getId() == null) {
            count = hotManualRuleDAO.insert(ruleDO);
        } else {
            ruleDO.setId(ruleBO.getId());
            count = hotManualRuleDAO.update(ruleDO);
        }
        if (count > 0) {
            // 判断今天有没有记录
            String weekDays = ruleBO.getWeekDays();
            if (StringUtils.isNotBlank(weekDays)) {
                String[] arr = weekDays.split(",");
                int num = (Calendar.getInstance().get(Calendar.DAY_OF_WEEK)) - 1;
                for(String str : arr) {
                    if (num == Integer.valueOf(str)) {
                        // 添加到记录当中
                        saveToHotManual(ruleDO);
                        break;
                    }
                }
            }
            return new BusiResult(BusiStatus.SUCCESS);
        }
        return new BusiResult(BusiStatus.SERVERERROR);
    }

    /**
     * 添加记录到首页推荐表
     * @param ruleDO
     */
    public void saveToHotManual(HotManualRuleDO ruleDO) {
        Date now = new Date();
        Date startDate, endDate;
        String[] startArr, endArr;
        startArr = ruleDO.getStartDate().split(":");
        endArr = ruleDO.getEndDate().split(":");
        startDate = DateUtil.setTimeHourOfDay(now, Integer.valueOf(startArr[0]), Integer.valueOf(startArr[1]), 0);
        endDate = DateUtil.setTimeHourOfDay(now, Integer.valueOf(endArr[0]), Integer.valueOf(endArr[1]), 0);
        if (startDate.getTime() < now.getTime()) {
            // 忽略开始时间小于当前时间的记录
            return ;
        }
        HomeHotManualRecomm homeHotManualRecomm = new HomeHotManualRecomm();
        homeHotManualRecomm.setUid(ruleDO.getUid());
        int seqNo = ruleDO.getSeqNo() == null ? 1 : ruleDO.getSeqNo();
        homeHotManualRecomm.setSeqNo(seqNo);
        homeHotManualRecomm.setStatus(new Byte("1"));
        homeHotManualRecomm.setStartValidTime(startDate);
        homeHotManualRecomm.setEndValidTime(endDate);
        homeHotManualRecomm.setCreateTime(now);
        homeHotManualRecommMapper.insertSelective(homeHotManualRecomm);
    }

    /**
     * 根据ID查询信息
     * @param id
     * @return
     */
    public HotManualRuleDTO getById (Long id) {
        if (id == null) {
            return null;
        }
        return hotManualRuleDAO.getById(id);
    }

    /**
     * 根据ID 删除, 物理删除
     * @param id
     * @return
     */
    public int delete(Long id) {
        if (id == null) {
            return 0;
        }
        return hotManualRuleDAO.delete(id);
    }

    /**
     * 查询今天需要上热门的记录
     * @return
     */
//    public List<HotManualRuleDO> toRecommByToday (){
//        HotManualRuleQuery query = new HotManualRuleQuery();
//        // 获取今天星期几
//        int weekDay = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
//        query.setWeekDay(String.valueOf(weekDay - 1));
//        List<HotManualRuleDO> list = hotManualRuleDAO.listByToday(query);
//        if (list.isEmpty()) {
//            return Lists.newArrayList();
//        }
//        HomeHotManualRecomm homeHotManualRecomm;
//        Date now = new Date();
//        Date startDate, endDate;
//        String[] startArr, endArr;
//        for (HotManualRuleDO ruleDO : list) {
//            startArr = ruleDO.getStartDate().split(":");
//            endArr = ruleDO.getEndDate().split(":");
//            startDate = setTime(now, Integer.valueOf(startArr[0]), Integer.valueOf(startArr[1]), Integer.valueOf(startArr[2]));
//            endDate = setTime(now, Integer.valueOf(endArr[0]), Integer.valueOf(endArr[1]), Integer.valueOf(endArr[2]));
//            homeHotManualRecomm = new HomeHotManualRecomm();
//            homeHotManualRecomm.setUid(ruleDO.getUid());
//            homeHotManualRecomm.setSeqNo(1);
//            homeHotManualRecomm.setStatus(new Byte("1"));
//            homeHotManualRecomm.setStartValidTime(startDate);
//            homeHotManualRecomm.setEndValidTime(endDate);
//            homeHotManualRecomm.setCreateTime(now);
//            homeHotManualRecommMapper.insertSelective(homeHotManualRecomm);
//        }
//        return list;
//    }


}
