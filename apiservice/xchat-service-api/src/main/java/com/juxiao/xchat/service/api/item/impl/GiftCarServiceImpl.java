package com.juxiao.xchat.service.api.item.impl;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.juxiao.xchat.base.constant.redis.RedisKey;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.utils.ListUtil;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.item.GiftCarPurseRecordDao;
import com.juxiao.xchat.dao.item.domain.GiftCarPurseRecordDO;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.item.dto.GiftCarPurseRecordDTO;
import com.juxiao.xchat.dao.item.enumeration.CarGetType;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.cache.redis.RedisManager;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.level.LevelManager;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.user.UserPurseManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.item.GiftCarService;
import com.juxiao.xchat.service.api.item.vo.GiftCarVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

@Service
public class GiftCarServiceImpl implements GiftCarService {
    /**
     * 赠送座驾消息模板
     */
    public static String GIV_CAR_MESSAGE_FORMAT = "{0} 赠送给您座驾【{1}】,快点坐上去游玩吧！";

    @Autowired
    private Gson gson;

    @Autowired
    private GiftCarPurseRecordDao carPurseRecordDao;

    @Autowired
    private GiftCarManager giftCarManager;

    @Autowired
    private RedisManager redisManager;

    @Autowired
    private UsersManager usersManager;

    @Autowired
    private UserPurseManager userPurseManager;

    @Autowired
    private AppVersionManager versionService;

    @Autowired
    private LevelManager levelManager;

    @Override
    public List<GiftCarVO> listGiftCars(Long uid, Integer pageNum, Integer pageSize, String os, String app, String appVersion, String ip) throws WebServiceException {
        if (uid == null) {
            throw new WebServiceException(WebServiceCode.PARAM_ERROR);
        }

        List<String> carids;
        if (versionService.checkAuditingVersion(os, app, appVersion, ip,uid)) {
            carids = giftCarManager.listAllGiftCarids();
        } else {
            carids = giftCarManager.listAllGiftCarids();
            if (carids == null) {
                carids = Lists.newArrayList();
            }
        }

        // 如果座驾无效了，但用户购买过而且还没有到期，就把已购买的座驾放到前面显示
        List<String> userCarids = giftCarManager.listUserCarids(uid);
        if (userCarids != null && userCarids.size() > 0) {
            GiftCarPurseRecordDTO recordDto;
            for (String carId : userCarids) {
                if (!carids.contains(carId)) {
                    recordDto = giftCarManager.getUserCarPurseRecord(uid, Long.valueOf(carId));
                    if (recordDto != null && recordDto.getCarDate() > 0) {
                        carids.add(0, carId);
                    }
                }
            }
        }

        if (carids.size() == 0) {
            return Lists.newArrayList();
        }

        if (pageNum == null || pageNum < 1) pageNum = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 50) pageSize = 10;
        Integer size = carids.size();
        Integer skip = (pageNum - 1) * pageSize;
        if (skip >= size) {
            return Lists.newArrayList();
        }
        if (skip + pageSize > size) {
            carids = carids.subList(skip, size);
        } else {
            carids = carids.subList(skip, skip + pageSize);
        }
        GiftCarVO giftCarVo;
        GiftCarPurseRecordDTO recordDto;
        List<GiftCarVO> list = Lists.newArrayList();
        for (String carId : carids) {
            GiftCarDTO carDto = giftCarManager.getGiftCar(Integer.valueOf(carId));
            if (carDto == null) {
                continue;
            }
            giftCarVo = new GiftCarVO();
            BeanUtils.copyProperties(carDto, giftCarVo);

            recordDto = giftCarManager.getUserCarPurseRecord(uid, Long.valueOf(carDto.getCarId()));
            if (recordDto == null) {
                giftCarVo.setIsPurse(0);
                giftCarVo.setDays(0);
                giftCarVo.setDaysRemaining(0);
            } else {
                giftCarVo.setDays(recordDto.getCarDate());
                giftCarVo.setDaysRemaining(recordDto.getCarDate());
                if (recordDto.getCarDate() == 0) {
                    giftCarVo.setIsPurse(0);
                } else {
                    if (Byte.valueOf("1").equals(recordDto.getIsUse())) {
                        giftCarVo.setIsPurse(2);
                    } else {
                        giftCarVo.setIsPurse(1);
                    }
                }
            }
            if(carDto.getCreateTime().after(DateUtils.duDate(new Date(), -3))) {
            	giftCarVo.setIsNew(1);
            }else {
            	giftCarVo.setIsNew(0);
            }
            list.add(giftCarVo);
        }
        return list;
    }

    @Override
    public List<GiftCarVO> listMall(Long uid, Integer pageNum, Integer pageSize) throws WebServiceException {
        List<String> list = giftCarManager.listByMall();
        if (list == null) {
            return Lists.newArrayList();
        }
        list = ListUtil.page(list, pageNum, pageSize);
        //
        List<GiftCarVO> result = Lists.newArrayList();
        list.forEach(id -> {
            GiftCarDTO dto = giftCarManager.getGiftCar(Integer.valueOf(id));
            if (dto != null) {
                result.add(convertToVO(dto, null));
            }
        });
        return result;
    }

    @Override
    public List<GiftCarVO> listUserGiftCar(Long uid, Long queryUid, Integer pageNum, Integer pageSize) throws WebServiceException {
        if (uid == null) {
            return Lists.newArrayList();
        }

        List<String> list;

        long userId = queryUid == null ? uid : queryUid;
        list = giftCarManager.listUserCarids(userId);
        list = ListUtil.page(list, pageNum, pageSize);
        List<GiftCarVO> result = Lists.newArrayList();
        list.forEach(id -> {
            if (StringUtils.isBlank(id)) {
                return;
            }

            GiftCarPurseRecordDTO record = giftCarManager.getUserCarPurseRecord(userId, Long.valueOf(id));
            if (record == null) {
                return;
            }

            if (record.getCarDate() <= 0) {
                // 删除用户座驾缓存,重新加载用户座驾
                redisManager.hdel(RedisKey.gift_car_purse_list.getKey(), userId + "");
            } else {
                GiftCarDTO dto = giftCarManager.getGiftCar(Integer.valueOf(id));
                if (dto != null) {
                    result.add(convertToVO(dto, record));
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
    private GiftCarVO convertToVO(GiftCarDTO dto, GiftCarPurseRecordDTO record) {
        GiftCarVO vo = new GiftCarVO();
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
            vo.setDays(record.getCarDate());
            vo.setDaysRemaining(record.getCarDate());
        } else {
            // 没有购买该礼物
            vo.setIsPurse(0);
            vo.setDays(0);
            vo.setDaysRemaining(0);
        }
        return vo;
    }

    @Override
    public void purse(Long uid, Integer carId, Integer type) throws WebServiceException {
        if (uid == null || carId == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO usersDto = usersManager.getUser(uid);
        if (usersDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        GiftCarDTO carDto = giftCarManager.getGiftCar(carId);
        if (carDto == null) {
            throw new WebServiceException(WebServiceCode.GIFT_CAR_NOT_EXISTS);
        }
        if (!Boolean.TRUE.equals(carDto.getAllowPurse())) {
            throw new WebServiceException(WebServiceCode.MALL_CANNOT_PURSE);
        }
        if (carDto.getLeftLevel() != null && carDto.getLeftLevel() > 0) {
            // 有等级限制
            int level = levelManager.getUserExperienceLevelSeq(uid);
            if (level < carDto.getLeftLevel()) {
                throw new WebServiceException(WebServiceCode.MALL_PURSE_LEVEL_LEFT);
            }
        }

        userPurseManager.updateReduceGold(uid, carDto.getGoldPrice().intValue(), true,"购买座驾", null);

        this.resetCar(uid);
        giftCarManager.saveUserCar(uid, carId, carDto.getEffectiveTime().intValue(), 1, "恭喜您成功购买座驾【" + carDto.getCarName() + "】,快点坐上去游玩吧！");
    }

    @Override
    public void use(Long uid, Integer carId) throws WebServiceException {
        if (uid == null || carId == null) {
            throw new WebServiceException(WebServiceCode.PARAMETER_ILLEGAL);
        }

        UsersDTO users = usersManager.getUser(uid);
        if (users == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        // 取消座驾选择
        if (carId == -1) {
            this.resetCar(uid);
            return;
        }

        GiftCarDTO carDto = giftCarManager.getGiftCar(carId);
        if (carDto == null) {
            throw new WebServiceException(WebServiceCode.GIFT_CAR_NOT_EXISTS);
        }

        GiftCarPurseRecordDTO carPurseRecordDto = giftCarManager.getUserCarPurseRecord(uid, carId.longValue());
        if (carPurseRecordDto == null || carPurseRecordDto.getCarDate() == 0) {
            throw new WebServiceException(WebServiceCode.GIFT_CAR_NOT_EXISTS);
        }
        this.resetCar(uid);

        GiftCarPurseRecordDO purseRecordDo = new GiftCarPurseRecordDO();
        purseRecordDo.setRecordId(carPurseRecordDto.getRecordId());
        purseRecordDo.setIsUse(new Byte("1"));
        carPurseRecordDao.update(purseRecordDo);

        carPurseRecordDto.setIsUse(purseRecordDo.getIsUse());
        redisManager.hset(RedisKey.gift_car_purse.getKey(), uid + "_" + carId, gson.toJson(carPurseRecordDto));
    }

    @Override
    public void give(Long uid, Integer carId, Long targetUid) throws WebServiceException {
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
        GiftCarDTO giftCarDTO = giftCarManager.getGiftCar(carId);
        if (giftCarDTO == null) {
            throw new WebServiceException(WebServiceCode.GIFT_CAR_NOT_EXISTS);
        }
        // 判断座驾是否支持赠送
        if (giftCarDTO.getCarStatus() == 2 || giftCarDTO.getGoldPrice() == 0) {
            throw new WebServiceException("此类座驾不支持赠送");
        }
        if (!Boolean.TRUE.equals(giftCarDTO.getAllowPurse())) {
            throw new WebServiceException(WebServiceCode.MALL_CANNOT_GIVE);
        }
        if (giftCarDTO.getLeftLevel() != null && giftCarDTO.getLeftLevel() > 0) {
            // 有等级限制
            int level = levelManager.getUserExperienceLevelSeq(targetUid);
            if (level < giftCarDTO.getLeftLevel()) {
                throw new WebServiceException(WebServiceCode.MALL_GIVE_LEVEL_LEFT);
            }
        }

        // 减金币
        userPurseManager.updateReduceGold(uid, giftCarDTO.getGoldPrice().intValue(), true,"赠送座驾", null);
        // 保存用户座驾信息
/*        giftCarManager.saveUserCar(targetUser.getUid(), giftCarDTO.getCarId(), giftCarDTO.getEffectiveTime().intValue(),
                CarGetType.user_give.getValue(), MessageFormat.format(GIV_CAR_MESSAGE_FORMAT, user.getNick(), giftCarDTO.getCarName()));
        */
        giftCarManager.saveUserCarWithSelfMsg(user.getUid(),targetUser.getUid(), giftCarDTO.getCarId(), giftCarDTO.getEffectiveTime().intValue(),
                CarGetType.user_give.getValue(), MessageFormat.format(GIV_CAR_MESSAGE_FORMAT, user.getNick(), giftCarDTO.getCarName()));
    }

    private void resetCar(Long uid) {
        List<GiftCarPurseRecordDTO> list = carPurseRecordDao.listUserInuseCars(uid);
        for (GiftCarPurseRecordDTO carPurseRecordDto : list) {
            GiftCarPurseRecordDO recordDo = new GiftCarPurseRecordDO();
            recordDo.setRecordId(carPurseRecordDto.getRecordId());
            recordDo.setIsUse((byte) 0);
            carPurseRecordDao.update(recordDo);
            carPurseRecordDto.setIsUse(recordDo.getIsUse());
            redisManager.hset(RedisKey.gift_car_purse.getKey(), carPurseRecordDto.getUid() + "_" + carPurseRecordDto.getCarId(), gson.toJson(carPurseRecordDto));
        }
    }
}
