package com.erban.admin.main.service.activity;

import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.dto.WeekStarGiftDTO;
import com.erban.main.model.Gift;
import com.erban.main.model.WeekStarGift;
import com.erban.main.model.WeekStarGiftExample;
import com.erban.main.mybatismapper.WeekStarGiftMapper;
import com.erban.main.service.gift.GiftService;
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
 * @time 16:37
 */
@Service
public class WeekStarGiftService {
    @Autowired
    private WeekStarGiftMapper weekStarGiftMapper;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private GiftService giftService;

    public PageInfo<WeekStarGiftDTO> getList(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<WeekStarGiftDTO> list = weekStarGiftMapper.selectByList(searchText);
        if (list.size() > 0) {
            list.forEach(item -> {
                if (item.getAdminId() != null) {
                    AdminUser adminUser = adminUserService.getAdminUserById(item.getAdminId());
                    item.setAdminName(adminUser != null ? adminUser.getUsername() : "");
                }
                Gift gift = giftService.getGiftById(item.getGiftId());
                item.setGiftName(gift != null ? gift.getGiftName() : "");

            });
        }
        return new PageInfo<>(list);
    }

    public int getEffectiveCount() {
        return weekStarGiftMapper.countEffective();
    }

    public int save(WeekStarGift weekStarGift, boolean isEdit, Integer adminId) {
        if (!isEdit) {
            weekStarGift.setAdminId(adminId);
            weekStarGift.setStatus(1);
            weekStarGift.setCreateTime(new Date());
            int result = weekStarGiftMapper.insert(weekStarGift);
            if (result > 0) {
                saveCache(weekStarGift);
            }
            return result;
        } else {
            weekStarGift.setAdminId(adminId);
            weekStarGift.setStatus(1);
            int result = weekStarGiftMapper.updateByPrimaryKeySelective(weekStarGift);
            if (result > 0) {
                saveCache(weekStarGift);
            }
            return result;
        }
    }

    private void saveCache(WeekStarGift weekStarGift) {
        jedisService.hset(RedisKey.week_star_gift.getKey(), weekStarGift.getGiftId().toString(),
                new Gson().toJson(weekStarGift));
    }

    public int deleteById(Long id) {
        WeekStarGift weekStarGift = weekStarGiftMapper.selectByPrimaryKey(id);
        int result = weekStarGiftMapper.deleteByPrimaryKey(weekStarGift.getId());
        if (result > 0) {
            jedisService.hdel(RedisKey.week_star_gift.getKey(), weekStarGift.getGiftId().toString());
        }
        return result;
    }

    public WeekStarGift getById(Long id) {
        return weekStarGiftMapper.selectByPrimaryKey(id);
    }

    public WeekStarGift getByGiftId(Integer giftId) {
        return weekStarGiftMapper.selectByGiftId(giftId);
    }

    public List<WeekStarGiftDTO> getNormalGifts() {
        return weekStarGiftMapper.selectByNormalGifts();
    }
}
