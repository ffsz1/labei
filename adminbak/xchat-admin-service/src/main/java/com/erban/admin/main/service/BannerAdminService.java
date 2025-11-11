package com.erban.admin.main.service;


import com.erban.admin.main.service.base.BaseService;
import com.erban.main.model.Banner;
import com.erban.main.model.BannerExample;
import com.erban.main.model.Users;
import com.erban.main.model.UsersExample;
import com.erban.main.mybatismapper.BannerMapper;

import com.erban.main.mybatismapper.UsersMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.utils.BlankUtil;
import com.xchat.common.utils.DateTimeUtil;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class BannerAdminService extends BaseService {
    @Autowired
    private BannerMapper bannerMapper;
    @Autowired
    private JedisService jedisService;
    @Autowired
    private UsersMapper usersMapper;

    public PageInfo<Banner> getBannerList(Integer pageNumber, Integer pageSize, int skipType, int bannerStatus,
                                          Integer viewType) {
        BannerExample bannerExample = new BannerExample();
        bannerExample.setOrderByClause("seq_no,banner_status,start_time");
        BannerExample.Criteria criteria = bannerExample.createCriteria();
        if (skipType != 0) {
            byte i = (byte) skipType;
            criteria.andSkipTypeEqualTo(i);
        }
        if (!BlankUtil.isBlank(bannerStatus) && bannerStatus != 0) {
            byte status = (byte) bannerStatus;
            criteria.andBannerStatusEqualTo(status);
        }
        if (viewType != null && viewType == 0) {
            List<Byte> list = new ArrayList<>();
            list.add((byte) 1);
            list.add((byte) 3);
            list.add((byte) 4);
            criteria.andViewTypeIn(list);
        } else if (viewType != null && viewType != 0) {
            criteria.andViewTypeEqualTo(viewType.byteValue());
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Banner> bannerList = bannerMapper.selectByExample(bannerExample);
        return new PageInfo<>(bannerList);
    }

    public void delBanner(Integer bannerId) {
    	jedisService.delKeys(RedisKey.eff_banner.getKey());
        bannerMapper.deleteByPrimaryKey(bannerId);
    }

    public Banner getOneBannerById(Integer bannerId) {
        Banner banner = bannerMapper.selectByPrimaryKey(bannerId);
        Byte skipType = banner.getSkipType();
        if (skipType.equals((byte) 2)) {
            String val = banner.getSkipUri();
            String userUid = UidToErbanNo(val);
            banner.setSkipUri(userUid);
        }
        return banner;
    }

    public int saveBanner(Banner banner, boolean isEdit, String startTimeString, String endTimeString) {
        Byte skipType = banner.getSkipType();
        if (skipType.equals((byte) 2)) {
            String usererBanNo = banner.getSkipUri();
            String userUid = erbanNoToUid(usererBanNo);
            banner.setSkipUri(userUid);
        }

        banner.setSkipUri("http://" + banner.getSkipUri());
        if (isEdit) {
            if (startTimeString != null) {
                Date date1 = DateTimeUtil.convertStrToDate(startTimeString);
                banner.setStartTime(date1);
            }
            if (endTimeString != null) {
                Date date2 = DateTimeUtil.convertStrToDate(endTimeString);
                banner.setEndTime(date2);
            }
            bannerMapper.updateByPrimaryKeySelective(banner);
            jedisService.hdel(RedisKey.banner.getKey(), banner.getBannerId().toString());
        } else {
            if (startTimeString != null) {
                Date date1 = DateTimeUtil.convertStrToDate(startTimeString);
                banner.setStartTime(date1);
            }
            if (endTimeString != null) {
                Date date2 = DateTimeUtil.convertStrToDate(endTimeString);
                banner.setEndTime(date2);
            }
            bannerMapper.insert(banner);
        }
        jedisService.hdeleteKey(RedisKey.banner_list.getKey());
        jedisService.delKeys(RedisKey.eff_banner.getKey());
        return 1;
    }

    private String erbanNoToUid(String erBanNo) {
        if (erBanNo == null) {
            return null;
        }
        UsersExample example = new UsersExample();
        long longUid = Long.parseLong(erBanNo);
        example.createCriteria().andErbanNoEqualTo(longUid);
        List<Users> usersList = usersMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        }
        return String.valueOf(usersList.get(0).getUid());
    }

    private String UidToErbanNo(String Uid) {
        if (Uid == null) {
            return null;
        }
        UsersExample example = new UsersExample();
        long longUid = Long.parseLong(Uid);
        example.createCriteria().andUidEqualTo(longUid);
        List<Users> usersList = usersMapper.selectByExample(example);
        if (CollectionUtils.isEmpty(usersList)) {
            return null;
        }
        return String.valueOf(usersList.get(0).getErbanNo());
    }
}
