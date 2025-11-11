package com.erban.admin.main.service.activity;

import com.erban.admin.main.model.AdminUser;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.dto.WeekStarGiftDTO;
import com.erban.main.model.Gift;
import com.erban.main.model.WeekStarGift;
import com.erban.main.model.WeekStarGiftNotice;
import com.erban.main.model.WeekStarGiftNoticeExample;
import com.erban.main.mybatismapper.WeekStarGiftNoticeMapper;
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
 * @time 17:29
 */
@Service
public class WeekStarGiftNoticeService {

    @Autowired
    private WeekStarGiftNoticeMapper weekStarGiftNoticeMapper;

    @Autowired
    private AdminUserService adminUserService;

    @Autowired
    private GiftService giftService;

    @Autowired
    private JedisService jedisService;



    public PageInfo<WeekStarGiftDTO> getList(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<WeekStarGiftDTO> list = weekStarGiftNoticeMapper.selectByList(searchText);
        if(list.size() > 0){
            list.forEach(item ->{
            	if(item.getAdminId() != null){
                	AdminUser adminUser=adminUserService.getAdminUserById(item.getAdminId());
                    item.setAdminName(adminUser!=null?adminUser.getUsername():"");
                }
                Gift gift=giftService.getGiftById(item.getGiftId());
                item.setGiftName(gift!=null?gift.getGiftName():"");

            });
        }
        return new PageInfo<>(list);
    }

    public int getEffectiveCount() {
        return weekStarGiftNoticeMapper.countEffective();
    }

    public int save(WeekStarGiftNotice weekStarGiftNotice, boolean isEdit, int adminId) {
        if(!isEdit){
            weekStarGiftNotice.setAdminId(adminId);
            weekStarGiftNotice.setStatus(1);
            weekStarGiftNotice.setCreateTime(new Date());
            int result = weekStarGiftNoticeMapper.insert(weekStarGiftNotice);
            if(result > 0){
                saveCache(weekStarGiftNotice);
            }
            return result;
        }else{
            weekStarGiftNotice.setAdminId(adminId);
            weekStarGiftNotice.setStatus(1);
            int result = weekStarGiftNoticeMapper.updateByPrimaryKeySelective(weekStarGiftNotice);
            if(result > 0){
                saveCache(weekStarGiftNotice);
            }
            return result;
        }
    }

    private void saveCache(WeekStarGiftNotice weekStarGiftNotice){
        jedisService.hset(RedisKey.week_star_gift_notice.getKey(),weekStarGiftNotice.getGiftId().toString(),new Gson().toJson(weekStarGiftNotice));
    }

    public int deleteById(Long id) {
        WeekStarGiftNotice weekStarGiftNotice = getById(id);
        int result = weekStarGiftNoticeMapper.deleteByPrimaryKey(weekStarGiftNotice.getId());
        if(result > 0){
            jedisService.hdel(RedisKey.week_star_gift_notice.getKey(),weekStarGiftNotice.getGiftId().toString());
        }
        return result;
    }

    public WeekStarGiftNotice getById(Long id) {
        return weekStarGiftNoticeMapper.selectByPrimaryKey(id);
    }

    public WeekStarGiftNotice getByGiftId(Integer giftId) {
        return weekStarGiftNoticeMapper.selectByGiftId(giftId);
    }
}
