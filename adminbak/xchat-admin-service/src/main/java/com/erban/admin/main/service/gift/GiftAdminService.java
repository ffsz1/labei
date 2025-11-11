package com.erban.admin.main.service.gift;

import com.erban.admin.main.base.RedisSupportService;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.service.base.BaseService;
import com.erban.main.base.BaseMapper;
import com.erban.main.model.*;
import com.erban.main.mybatismapper.GiftMapper;
import com.erban.main.mybatismapper.UserPurseMapperExpand;
import com.erban.main.service.gift.GiftSendRecordService;
import com.erban.main.service.record.BillRecordService;
import com.erban.main.service.user.UserPurseService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class GiftAdminService extends RedisSupportService<Gift, GiftExample> {
    @Autowired
    private GiftMapper giftMapper;

    @Autowired
    private UserPurseService userPurseService;

    @Autowired
    private GiftSendRecordService giftSendRecordService;

    @Autowired
    private BillRecordService billRecordService;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private UserPurseMapperExpand userPurseMapperExpand;

    private Gson gson = new Gson();

    /**
     * 获取所有有效的礼物列表
     *
     * @return
     */
    public List<Gift> getAllGift() {
        GiftExample example = new GiftExample();
        example.createCriteria().andGiftStatusEqualTo((byte) 1);
        return giftMapper.selectByExample(example);
    }

    /**
     * 获取520及以上金币的礼物
     *
     * @param giftType 礼物类型
     * @return
     */
    public List<Gift> getHighPriceGift(byte giftType) {
        return giftMapper.queryHighPriceGifts(giftType);
    }

    /**
     * 补送钻石
     *
     * @param uid
     * @param targetUid
     * @param roomUid
     * @param giftId
     * @param giftNum
     */
    public UserPurse repairDiamond(Long uid, Long targetUid, Long roomUid, Integer giftId, Integer giftNum) {
        Gift gift = giftMapper.selectByPrimaryKey(giftId);
        if (gift == null) {
            return null;
        }

        // 插入收礼物的记录
        Long totalGoldNum = giftNum * gift.getGoldPrice();
        Double totalDiamond = giftNum * gift.getGoldPrice() * 0.5;
        GiftSendRecord giftSendRecord = giftSendRecordService.insertGiftInfo(uid, targetUid, roomUid, (byte) 2
                , 1, giftId, giftNum, totalGoldNum, giftNum * gift.getGoldPrice());
        // 插入账单的记录
        billRecordService.insertBillRecord(targetUid, uid, giftSendRecord.getSendRecordId().toString(), (byte) 6
                , totalDiamond, null, null);
        // 更新DB的钱包
        userPurseMapperExpand.updatePurseDiamond(totalDiamond, targetUid);
        // 删除缓存钱包
        jedisService.hdel(RedisKey.user_purse.getKey(), targetUid.toString());
        return userPurseService.getUserPurseFromDb(targetUid);
    }

    /**
     * 获取礼物列表
     *
     * @param status 礼物状态(1、有效 2、无效)
     * @param page
     * @param size
     * @return
     */
    public PageInfo<Gift> getGiftByPage(byte status, byte giftType, String giftName, int page, int size) {
        PageHelper.startPage(page, size);
        GiftExample example = new GiftExample();
        GiftExample.Criteria criteria = example.createCriteria();
        // 判断是否需要增加状态条件
        if (status != 0) {
            criteria.andGiftStatusEqualTo(status);
        }
        if (giftType != 0) {
            criteria.andGiftTypeEqualTo(giftType);
        }
        if (StringUtils.isNotBlank(giftName)) {
            example.setLikeName(giftName);
        }
        example.setOrderByClause(" seq_no asc");
        return new PageInfo<Gift>(giftMapper.selectByExample(example));
    }

    public int saveGift(Gift entity) {
        if (entity.getGiftId() == null) {
            // 如果是第一次保存.则判断是否礼物名称是否重复
            GiftExample example = new GiftExample();
            example.createCriteria().andGiftNameEqualTo(entity.getGiftName().trim());
            int result = countByExample(example);
            if (result > 0) {
                throw new BusinessException("该礼物名称已重复");
            }
            return giftMapper.insertSelective(entity);
        } else {
            return giftMapper.updateByPrimaryKeySelective(entity);
        }
    }

    /*
    public int save(Gift gift, boolean isEdit)
    {
        int result = 0;
        if(isEdit) {
            // 更新数据库
            result = giftMapper.updateByPrimaryKeySelective(gift);
        } else {
            gift.setCreateTime(new Date());
            result = giftMapper.insertSelective(gift);
        }
        if(result > 0) {
            // 更新礼物缓存
            updateGiftCache(gift.getGiftId(), gift);
        }
        return result;
    }

    public Gift getById(Integer id)
    {
        return giftMapper.selectByPrimaryKey(id);
    }

    public int deleteByIds(Integer[] ids)
    {
        int count = 0;
        for(Integer id : ids)
            count += delete(id);
        return count;
    }

    public int delete(Integer id)
    {
        deleteGiftCache(id);

        return giftMapper.deleteByPrimaryKey(id);
    }

    private void deleteGiftCache(Integer id)
    {
        jedisService.hdel(RedisKey.gift.getKey(), id.toString());
    }

    private void updateGiftCache(Integer id, Gift gift)
    {
        jedisService.hwrite(RedisKey.gift.getKey(), id.toString(), gson.toJson(gift));
    }
    */

    @Override
    public boolean beforeInsert(Gift entity) {
        entity.setCreateTime(new Date());
        return super.beforeInsert(entity);
    }

    @Override
    protected String getRedisKey() {
        return RedisKey.gift.getKey();
    }

    @Override
    public Object getId(Gift entity) {
        return entity.getGiftId();
    }

    @Override
    protected BaseMapper<Gift, GiftExample> getMapper() {
        return giftMapper;
    }
}
