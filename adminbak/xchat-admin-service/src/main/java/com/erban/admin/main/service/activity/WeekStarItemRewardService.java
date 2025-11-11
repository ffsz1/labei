package com.erban.admin.main.service.activity;

import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.dto.WeekStarGiftDTO;
import com.erban.main.dto.WeekStarItemRewardDTO;
import com.erban.main.model.Gift;
import com.erban.main.model.GiftCar;
import com.erban.main.model.Headwear;
import com.erban.main.model.WeekStarItemReward;
import com.erban.main.mybatismapper.WeekStarGiftMapper;
import com.erban.main.mybatismapper.WeekStarItemRewardMapper;
import com.erban.main.service.gift.GiftCarService;
import com.erban.main.service.gift.GiftService;
import com.erban.main.service.headwear.HeadwearService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.BeanUtils;
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
public class WeekStarItemRewardService {

    @Autowired
    private WeekStarItemRewardMapper weekStarItemRewardMapper;

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
    private WeekStarGiftMapper weekStarGiftMapper;

    public PageInfo<WeekStarItemRewardDTO> getList(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<WeekStarItemRewardDTO> list = weekStarItemRewardMapper.selectByList(searchText);
        if (list.size() > 0) {
            list.forEach(item -> {
                if (item.getAdminId() != null) {
                    AdminUser adminUser = adminUserService.getAdminUserById(item.getAdminId());
                    item.setAdminName(adminUser != null ? adminUser.getUsername() : "");
                }
                Gift gift = giftService.getGiftById(item.getGiftId());
                item.setGiftName(gift != null ? gift.getGiftName() : "");
                if (item.getType() == 1) {
                    GiftCar giftCar = giftCarService.getOneByJedisId(item.getItemId().toString());
                    item.setItemName(giftCar != null ? giftCar.getCarName() : "");
                } else {
                    Headwear headwear = headwearService.getOneByJedisId(item.getItemId().toString());
                    item.setItemName(headwear != null ? headwear.getHeadwearName() : "");
                }

            });
        }
        return new PageInfo<>(list);
    }

    public List<WeekStarGiftDTO> getGifts() {
        List<WeekStarGiftDTO> weekStarGiftDTOS = weekStarGiftMapper.selectByEffectiveGifts();
        if (weekStarGiftDTOS.size() > 0) {
            weekStarGiftDTOS.forEach(item -> {
                Gift gift = giftService.getGiftById(item.getGiftId());
                item.setGiftName(gift != null ? gift.getGiftName() : "");
            });
        }
        return weekStarGiftDTOS;
    }

    public int getEffectiveCount() {
        return weekStarItemRewardMapper.countEffective();
    }

    public int save(WeekStarItemReward weekStarItemReward, boolean isEdit, int adminId) {
        if (!isEdit) {
            weekStarItemReward.setAdminId(adminId);
            weekStarItemReward.setStatus(1);
            weekStarItemReward.setCreateTime(new Date());
            return weekStarItemRewardMapper.insert(weekStarItemReward);
        } else {
            weekStarItemReward.setAdminId(adminId);
            weekStarItemReward.setStatus(1);
            return weekStarItemRewardMapper.updateByPrimaryKeySelective(weekStarItemReward);
        }
    }


    public int deleteById(Long id) {
        WeekStarItemReward weekStarItemReward = getById(id);
        int result = weekStarItemRewardMapper.deleteByPrimaryKey(weekStarItemReward.getId());
        if (result > 0) {
            jedisService.hdel(RedisKey.week_star_item_reward.getKey(), weekStarItemReward.getItemId().toString());

        }
        return result;
    }

    public WeekStarItemReward getById(Long id) {
        WeekStarItemReward weekStarItemReward = weekStarItemRewardMapper.selectByPrimaryKey(id);
        if (weekStarItemReward != null) {
            Gift gift = giftService.getGiftById(weekStarItemReward.getGiftId());
            weekStarItemReward.setGiftName(gift != null ? gift.getGiftName() : "");
        }
        return weekStarItemReward;
    }
}
