package com.erban.admin.main.service.activity;

import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.dto.WeekStarGiftDTO;
import com.erban.main.dto.WeekStarItemRewardDTO;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.WeekStarGiftMapper;
import com.erban.main.mybatismapper.WeekStarGiftNoticeMapper;
import com.erban.main.mybatismapper.WeekStarItemNoticeRewardMapper;
import com.erban.main.mybatismapper.WeekStarItemRewardMapper;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.gift.GiftService;
import com.erban.main.service.headwear.HeadwearService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-17
 * @time 17:57
 */
@Service
public class WeekStarItemNoticeRewardService {

    @Autowired
    private WeekStarItemNoticeRewardMapper weekStarItemNoticeRewardMapper;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private GiftService giftService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private HeadwearService headwearService;

    @Autowired
    private GiftCarService giftCarService;

    @Autowired
    private WeekStarGiftNoticeMapper weekStarGiftNoticeMapper;

    public PageInfo<WeekStarItemRewardDTO> getList(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<WeekStarItemRewardDTO> list = weekStarItemNoticeRewardMapper.selectByList(searchText);
        if(list.size() > 0){
            list.forEach(item ->{
                if(item.getAdminId() != null){
                	AdminUser adminUser=adminUserService.getAdminUserById(item.getAdminId());
                    item.setAdminName(adminUser!=null?adminUser.getUsername():"");
                }
                Gift gift=giftService.getGiftById(item.getGiftId());
                item.setGiftName(gift!=null?gift.getGiftName():"");
                if(item.getType() == 1){
                    GiftCar giftCar = giftCarService.getOneByJedisId(item.getItemId().toString());
                    item.setItemName(giftCar!=null?giftCar.getCarName():"");
                }else{
                    Headwear headwear = headwearService.getOneByJedisId(item.getItemId().toString());
                    item.setItemName(headwear!=null?headwear.getHeadwearName():"");
                }

            });
        }
        return new PageInfo<>(list);
    }

    public List<WeekStarGiftDTO> getGifts() {
        List<WeekStarGiftDTO> weekStarGiftDTOS = weekStarGiftNoticeMapper.selectByEffectiveGifts();
        if(weekStarGiftDTOS.size() > 0){
            weekStarGiftDTOS.forEach(item ->{
                Gift gift=giftService.getGiftById(item.getGiftId());
                item.setGiftName(gift!=null?gift.getGiftName():"");
            });
        }
        return weekStarGiftDTOS;
    }


    public int save(WeekStarItemNoticeReward weekStarItemReward, boolean isEdit, int adminId) {
        if(!isEdit){
            weekStarItemReward.setAdminId(adminId);
            weekStarItemReward.setStatus(1);
            weekStarItemReward.setCreateTime(new Date());
            return  weekStarItemNoticeRewardMapper.insert(weekStarItemReward);
        }else{
            weekStarItemReward.setAdminId(adminId);
            weekStarItemReward.setStatus(1);
            return weekStarItemNoticeRewardMapper.updateByPrimaryKeySelective(weekStarItemReward);
        }
    }


    public int deleteById(Long id) {
        WeekStarItemNoticeReward weekStarItemReward = getById(id);
        return weekStarItemNoticeRewardMapper.deleteByPrimaryKey(weekStarItemReward.getId());
    }

    public WeekStarItemNoticeReward getById(Long id) {
        WeekStarItemNoticeReward weekStarItemReward  = weekStarItemNoticeRewardMapper.selectByPrimaryKey(id);
        if(weekStarItemReward!=null) {
        	Gift gift=giftService.getGiftById(weekStarItemReward.getGiftId());
            weekStarItemReward.setGiftName(gift!=null?gift.getGiftName():"");
        }
        return weekStarItemReward;
    }
}
