package com.erban.main.service;

import java.util.*;

import com.erban.main.model.Users;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.xchat.common.netease.neteaseacc.result.FileUploadRet;
import com.xchat.common.utils.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.erban.main.model.Banner;
import com.erban.main.mybatismapper.BannerMapper;
import com.erban.main.vo.BannerVo;
import com.xchat.common.redis.RedisKey;

@Service
public class BannerService extends CacheBaseService<Banner, BannerVo> {
	@Autowired
	private BannerMapper bannerMapper;
	@Autowired
	private UsersService usersService;

	@Override
	public BannerVo getOneByJedisId(String jedisId) {
		return getOne(RedisKey.banner.getKey(), jedisId, "select * from banner where banner_id = ? and banner_status = 1 ", jedisId);
	}

	@Override
	public BannerVo entityToCache(Banner entity) {
		BannerVo bannerVo = new BannerVo();
		bannerVo.setBannerId(entity.getBannerId());
		bannerVo.setBannerName(entity.getBannerName());
		bannerVo.setBannerPic(entity.getBannerPic());
		bannerVo.setSeqNo(entity.getSeqNo());
		bannerVo.setSkipType(entity.getSkipType());
		bannerVo.setSkipUri(entity.getSkipUri());
		bannerVo.setIsNewUser(entity.getIsNewUser());
		return bannerVo;
	}

	public String refreshCache(String jedisCode, String jedisKey){
		return refreshListCacheByKey(null, jedisCode, jedisKey, "getBannerId", "select * from banner where os_type in ("+jedisKey+") and banner_status = 1 order by seq_no asc ");
	}

	public String getListByOs(String os){
		String jedisKey;
		if("ios".equalsIgnoreCase(os)){
			jedisKey = "0,2";
		}else if("android".equalsIgnoreCase(os)){
			jedisKey = "0,1";
		}else{
			jedisKey = "0,1,2";
		}
		String str = jedisService.hget(RedisKey.banner_list.getKey(), jedisKey);
		if(StringUtils.isBlank(str)){
			str = refreshCache(RedisKey.banner_list.getKey(), jedisKey);
		}
		return str;
	}

	public List<BannerVo> removeByUid(List<BannerVo> bannerVoList, Long uid){
		Users users = usersService.getUsersByUid(uid);
		if(users==null){
			return bannerVoList;
		}
		boolean isNew = false;
		Date date = DateTimeUtil.getLastDay(new Date(), 3);
		if(users.getCreateTime().getTime()-date.getTime()>0){
			isNew = true;
		}
		List<BannerVo> bannerVos = new ArrayList<>();
		for(BannerVo bannerVo:bannerVoList){
			if(isNew && (bannerVo.getIsNewUser().intValue()==0 || bannerVo.getIsNewUser().intValue()==1)){
				bannerVos.add(bannerVo);
			}else if(!isNew && (bannerVo.getIsNewUser().intValue()==0 || bannerVo.getIsNewUser().intValue()==2)) {
				bannerVos.add(bannerVo);
			}
		}
		return bannerVos;
	}

	public List<BannerVo> getBannerList(Long uid, String os){
		String str = getListByOs(os);
		if(StringUtils.isBlank(str)){
			return new ArrayList<>();
		}
		List<BannerVo> bannerVoList = getList(str);
		if(uid!=null){
			bannerVoList = removeByUid(bannerVoList, uid);
		}
		return bannerVoList;
	}

	public void addBanner(FileUploadRet fileUploadRet, String title, Byte type, Integer seqNo) {
		Banner banner = new Banner();
		banner.setBannerName(title);
		banner.setBannerPic(fileUploadRet.getUrl());
		banner.setSeqNo(seqNo);
		banner.setSkipType(type);
		banner.setCreateTiem(new Date());
		String bannerStr = gson.toJson(banner);
		jedisService.hwrite(RedisKey.banner.getKey(), banner.getBannerId().toString(), bannerStr);
		bannerMapper.insertSelective(banner);
	}

}
