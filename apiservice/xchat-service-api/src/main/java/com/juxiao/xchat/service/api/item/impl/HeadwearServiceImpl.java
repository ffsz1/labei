package com.juxiao.xchat.service.api.item.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.utils.ListUtil;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.HeadwearPurseRecordDao;
import com.juxiao.xchat.dao.item.domain.HeadwearPurseRecordDO;
import com.juxiao.xchat.dao.item.dto.HeadwearDTO;
import com.juxiao.xchat.dao.item.dto.HeadwearPurseRecordDTO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.HeadwearManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.item.HeadwearService;
import com.juxiao.xchat.service.api.item.vo.HeadwearVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class HeadwearServiceImpl implements HeadwearService {
    @Autowired
    private HeadwearPurseRecordDao headwearPurseRecordDao;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private HeadwearManager headwearManager;
    @Autowired
    private LevelManager levelManager;
    @Autowired
    private RedisManager redisManager;
    @Autowired
    private UserPurseManager userPurseManager;
    @Autowired
    private Gson gson;


    @Override
    public List<HeadwearVO> listHeadwears(Long uid, Integer pageNum, Integer pageSize) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }
        List<String> headwearIds = headwearManager.listAllHeadwearids();
        if (headwearIds == null) {
            headwearIds = Lists.newArrayList();
        }
        List<String> userheadwearids = headwearManager.listUserHeadwearid(uid);
        HeadwearPurseRecordDTO headwearPurseRecordDto;
        if (userheadwearids != null && userheadwearids.size() > 0) {
            for (String headwearId : userheadwearids) {
                if (!headwearIds.contains(headwearId)) {
                    headwearPurseRecordDto = headwearManager.getHeadwearRecord(uid, Integer.valueOf(headwearId));
                    if (headwearPurseRecordDto != null && headwearPurseRecordDto.getHeadwearDate() > 0) {
                        headwearIds.add(0, headwearId);
                    }
                }
            }
        }
        if (headwearIds.size() == 0) {
            return Lists.newArrayList();
        }
        if (pageNum == null || pageNum < 1) {
            pageNum = 1;
        }
        if (pageSize == null || pageSize < 1 || pageSize > 50) {
            pageSize = 10;
        }
        Integer size = headwearIds.size();
        Integer skip = (pageNum - 1) * pageSize;
        if (skip >= size) {
            return Lists.newArrayList();
        }
        if (skip + pageSize > size) {
            headwearIds = headwearIds.subList(skip, size);
        } else {
            headwearIds = headwearIds.subList(skip, skip + pageSize);
        }
        Integer level = levelManager.getUserExperienceLevelSeq(uid);
        List<HeadwearVO> headwears = Lists.newArrayList();
        HeadwearDTO headwearDto;
        HeadwearVO headwearVo;
        HeadwearPurseRecordDTO headwearRecord;
        for (String headwearId : headwearIds) {
            headwearDto = headwearManager.getHeadwear(Integer.valueOf(headwearId));
            if (headwearDto == null) {
                continue;
            }
            headwearVo = new HeadwearVO();
            headwearVo.setHeadwearId(headwearDto.getHeadwearId());
            headwearVo.setGoldPrice(headwearDto.getGoldPrice());
            if (headwearDto.getHeadwearId() == 1) {
                headwearVo.setPicUrl("https://pic.chaoxuntech.com/headwear_level_" + level.toString() + ".png");
                headwearVo.setHeadwearName(level + headwearDto.getHeadwearName());
            } else {
                headwearVo.setPicUrl(headwearDto.getPicUrl());
                headwearVo.setHeadwearName(headwearDto.getHeadwearName());
            }
            headwearVo.setHasGifPic(headwearDto.getHasGifPic());
            headwearVo.setGifUrl(headwearDto.getGifUrl());
            headwearVo.setHasVggPic(headwearDto.getHasVggPic());
            headwearVo.setVggUrl(headwearDto.getVggUrl());
            headwearVo.setEffectiveTime(headwearDto.getEffectiveTime());
            headwearVo.setTimeLimit(headwearDto.getIsTimeLimit());
            headwearRecord = headwearManager.getHeadwearRecord(uid, Integer.valueOf(headwearId));
            if (headwearRecord == null) {
                headwearVo.setIsPurse(0);
                headwearVo.setDays(0);
                headwearVo.setDaysRemaining(0);
            } else {
                if (headwearRecord.getHeadwearDate() == 0) {
                    headwearVo.setIsPurse(0);
                } else {
                    if (headwearRecord.getIsUse() != null && headwearRecord.getIsUse().intValue() == 1) {
                        headwearVo.setIsPurse(2);
                    } else {
                        headwearVo.setIsPurse(1);
                    }
                }
                headwearVo.setDays(headwearRecord.getHeadwearDate());
                headwearVo.setDaysRemaining(headwearRecord.getHeadwearDate());
            }
            if(headwearDto.getCreateTime().after(DateUtils.duDate(new Date(), -3))) {
            	headwearVo.setIsNew(1);
            }else {
            	headwearVo.setIsNew(0);
            }
            headwears.add(headwearVo);
        }
        return headwears;
    }

    @Override
    public List<HeadwearVO> listMall(Long uid, Integer pageNum, Integer pageSize) throws WebServiceException {
        List<String> list = headwearManager.listByMall();
        if (list == null) {
            return Lists.newArrayList();
        }
        list = ListUtil.page(list, pageNum, pageSize);
        //
        List<HeadwearVO> result = Lists.newArrayList();
        list.forEach(id -> {
            HeadwearDTO dto = headwearManager.getHeadwear(Integer.valueOf(id));
            if (dto != null) {
                result.add(convertToVO(dto, null));
            }
        });
        return result;
    }

    @Override
    public List<HeadwearVO> listUserHeadwear(Long uid, Long queryUid, Integer pageNum, Integer pageSize) throws WebServiceException {
        if (uid == null) {
            return Lists.newArrayList();
        }

        long userId = queryUid == null ? uid : queryUid;
        List<String> list = headwearManager.listUserHeadwearid(userId);
        //
        list = ListUtil.page(list, pageNum, pageSize);
        List<HeadwearVO> result = Lists.newArrayList();
        list.forEach(id -> {
            if (StringUtils.isNotBlank(id)) {
                HeadwearPurseRecordDTO record = headwearManager.getHeadwearRecord(userId, Integer.valueOf(id));
                if (record == null) {
                    return;
                }

                if (record.getHeadwearDate() <= 0) {
                    // 删除用户头饰缓存,重新加载用户头饰
                    redisManager.hdel(RedisKey.headwear_purse_list.getKey(), userId + "");
                } else {
                    HeadwearDTO dto = headwearManager.getHeadwear(Integer.valueOf(id));
                    if (dto != null) {
                        result.add(convertToVO(dto, record));
                    }
                }
            }
        });
        return result;
    }

    /**
     * 对象转换
     *
     * @param dto
     * @param record
     * @return
     */
    private HeadwearVO convertToVO(HeadwearDTO dto, HeadwearPurseRecordDTO record) {
        HeadwearVO vo = new HeadwearVO();
        BeanUtils.copyProperties(dto, vo);
        if(dto.getCreateTime().after(DateUtils.duDate(new Date(), -3))) {
        	vo.setIsNew(1);
        }else {
        	vo.setIsNew(0);
        }
        if (record != null) {
            int isPurse = record.getIsUse() != null && record.getIsUse().intValue() == 1 ? 2 : 1;
            // 2 表示正在使用
            vo.setIsPurse(isPurse);
            vo.setDays(record.getHeadwearDate());
            vo.setDaysRemaining(record.getHeadwearDate());
        } else {
            // 没有购买该礼物
            vo.setIsPurse(0);
            vo.setDays(0);
            vo.setDaysRemaining(0);
        }
        return vo;
    }

    @Override
    public void purse(Long uid, Integer headwearId, Integer type) throws WebServiceException {
        if (uid == null || headwearId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        HeadwearDTO headwear = headwearManager.getHeadwear(headwearId);
        if (headwear == null) {
            throw new WebServiceException(WebServiceCode.NOT_HAVING_LIST);
        }

        if (!Boolean.TRUE.equals(headwear.getAllowPurse())) {
            throw new WebServiceException(WebServiceCode.MALL_CANNOT_PURSE);
        }
        if (headwear.getLeftLevel() != null && headwear.getLeftLevel() > 0) {
            // 有等级限制
            int level = levelManager.getUserExperienceLevelSeq(uid);
            if (level < headwear.getLeftLevel()) {
                throw new WebServiceException(WebServiceCode.MALL_PURSE_LEVEL_LEFT);
            }
        }

        userPurseManager.updateReduceGold(uid, headwear.getGoldPrice().intValue(), true,"购买头饰", null);

        this.resetHeadwear(uid);
        headwearManager.saveUserHeadwear(uid, headwearId, headwear.getEffectiveTime().intValue(), 1, "恭喜您成功购买头饰【" + headwear.getHeadwearName() + "】,快点去搭配吧！");
    }

    @Override
    public void use(Long uid, Integer headwearId) throws WebServiceException {
        if (uid == null || headwearId == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        if (headwearId == -1) {// 取消座驾选择
            this.resetHeadwear(uid);
            return;
        }

        HeadwearDTO headwear = headwearManager.getHeadwear(headwearId);
        if (headwear == null) {
            throw new WebServiceException(WebServiceCode.NOT_HAVING_LIST);
        }

        HeadwearPurseRecordDTO purseRecordDto = headwearManager.getHeadwearRecord(uid, headwearId);
        if (purseRecordDto == null || purseRecordDto.getHeadwearDate() == 0) {
            throw new WebServiceException(WebServiceCode.NO_AUTHORITY);
        }

        this.resetHeadwear(uid);

        HeadwearPurseRecordDO purseRecordDo = new HeadwearPurseRecordDO();
        purseRecordDo.setRecordId(purseRecordDto.getRecordId());
        purseRecordDo.setIsUse(new Byte("1"));
        headwearPurseRecordDao.update(purseRecordDo);

        purseRecordDto.setIsUse(purseRecordDo.getIsUse());
        redisManager.hset(RedisKey.headwear_purse.getKey(), uid + "_" + headwearId, gson.toJson(purseRecordDto));
    }

    @Override
    public void give(Long uid, Integer headwearId, Long targetUid) throws WebServiceException {
        if (uid == null || targetUid == null || uid.equals(targetUid)) {
            // 考虑要不要将赠送给自己的直接转换成购买.
            throw new WebServiceException("不能赠送给自己");
        }
        UsersDTO user = usersManager.getUser(uid);
        if (user == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        UsersDTO targetUser = usersManager.getUser(targetUid);
        if (targetUser == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }
        HeadwearDTO headwear = headwearManager.getHeadwear(headwearId);
        if (headwear == null) {
            throw new WebServiceException(WebServiceCode.NOT_HAVING_LIST);
        }
        // 判断座驾是否支持赠送
        if (headwear.getHeadwearStatus() == 2 || headwear.getGoldPrice() == 0) {
            throw new WebServiceException("此类座驾不支持赠送");
        }
        if (!Boolean.TRUE.equals(headwear.getAllowPurse())) {
            throw new WebServiceException(WebServiceCode.MALL_CANNOT_GIVE);
        }
        if (headwear.getLeftLevel() != null && headwear.getLeftLevel() > 0) {
            // 有等级限制
            int level = levelManager.getUserExperienceLevelSeq(targetUid);
            if (level < headwear.getLeftLevel()) {
                throw new WebServiceException(WebServiceCode.MALL_GIVE_LEVEL_LEFT);
            }
        }

        // 减金币
        userPurseManager.updateReduceGold(uid, headwear.getGoldPrice().intValue(), true,"赠送头饰", null);
        // 保存用户座驾信息
/*        headwearManager.saveUserHeadwear(targetUid, headwearId, headwear.getEffectiveTime().intValue(),
                5, user.getNick() + "赠送给您头饰【" + headwear.getHeadwearName() + "】,快点去搭配吧！");*/
        //limself 2020/10/21 修改信息发送人
        headwearManager.saveUserHeadwearWithSelfMsg(uid,targetUid, headwearId, headwear.getEffectiveTime().intValue(),
                5, user.getNick() + "赠送给您头饰【" + headwear.getHeadwearName() + "】,快点去搭配吧！");
    }

    private void resetHeadwear(Long uid) {
        List<HeadwearPurseRecordDTO> list = headwearPurseRecordDao.listInuseHeadwearRecord(uid);
        if (list == null) {
            return;
        }
        for (HeadwearPurseRecordDTO recordDto : list) {
            HeadwearPurseRecordDO purseRecordDo = new HeadwearPurseRecordDO();
            purseRecordDo.setRecordId(recordDto.getRecordId());
            purseRecordDo.setIsUse(new Byte("0"));
            headwearPurseRecordDao.update(purseRecordDo);
            recordDto.setIsUse(purseRecordDo.getIsUse());
            redisManager.hset(RedisKey.headwear_purse.getKey(), uid + "_" + recordDto.getHeadwearId(), gson.toJson(recordDto));
        }

    }

}
