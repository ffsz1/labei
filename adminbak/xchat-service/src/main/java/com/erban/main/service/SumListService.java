package com.erban.main.service;

import com.erban.main.model.AuctionDeal;
import com.erban.main.model.StatSumList;
import com.erban.main.model.StatSumListExample;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.StatSumListMapper;
import com.erban.main.service.user.UsersService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.SumListVo;
import com.google.common.collect.Lists;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Service
public class SumListService {
	private static final org.slf4j.Logger logger = LoggerFactory.getLogger(SumListService.class);
	@Autowired
	private StatSumListMapper statSumListMapper;
	@Autowired
	private JedisService jedisService;
	@Autowired
	private AuctionDealService auctionDealService;
	@Autowired
	private UsersService usersService;

	private Gson gson = new Gson();

	/* 查询总榜 */
	public List<SumListVo> queryList(Long roomUid) {
		String str = jedisService.hget(RedisKey.sum_List.getKey(), roomUid.toString());
		List<StatSumList> statSumList = Lists.newArrayList();
		if (StringUtils.isNotBlank(str)) {
			Type type = new TypeToken<List<StatSumList>>() {
			}.getType();
			statSumList = gson.fromJson(str, type);
		} else {
			StatSumListExample example = new StatSumListExample();
			example.createCriteria().andRoomUidEqualTo(roomUid);
			statSumList = statSumListMapper.selectByExample(example);
		}
		if (statSumList == null || statSumList.size() == 0) {
			return null;
		}
		List<SumListVo> sumListVos = convertRoomListToVoList(statSumList);
		return sumListVos;

	}

	private SumListVo convertRoomToVo(StatSumList statSumList) {
		SumListVo sumListVo = new SumListVo();
		sumListVo.setPrice(statSumList.getPrice());
		sumListVo.setProdId(statSumList.getProdId());
		sumListVo.setUid(statSumList.getUid());
		sumListVo.setRoomUid(statSumList.getRoomUid());
		Users users = usersService.getUsersByUid(statSumList.getUid());
		sumListVo.setAvatar(users.getAvatar());
		sumListVo.setNick(users.getNick());
		sumListVo.setGender(users.getGender());
		return sumListVo;
	}

	private List<SumListVo> convertRoomListToVoList(List<StatSumList> statSumLists) {
		List<SumListVo> sumListVos = Lists.newArrayList();
		Collections.sort(statSumLists);
		if (CollectionUtils.isEmpty(statSumLists)) {
			return sumListVos;
		}
		for (StatSumList statSumList : statSumLists) {
			SumListVo sumListVo = convertRoomToVo(statSumList);
			sumListVos.add(sumListVo);
		}
		return sumListVos;
	}

	/* 更新总榜单 */
	public void updateSumList(String auctId, Long roomUid) {
		AuctionDeal auctionDeal = auctionDealService.getAuctionDealById(auctId);
		List<StatSumList> statSumLists = Lists.newArrayList();
		if (auctionDeal == null) {
			logger.info("参数异常,订单不存在：auctId=" + auctId);
			return;
		}
		StatSumList sumList = new StatSumList();
		String sumListStr = jedisService.hget(RedisKey.sum_List.getKey(), roomUid.toString());
		if (sumListStr == null) {
			StatSumListExample example = new StatSumListExample();
			example.createCriteria().andRoomUidEqualTo(roomUid);
			statSumLists = statSumListMapper.selectByExample(example);
			if (statSumLists == null) {
				addSumList(roomUid, auctionDeal, sumList);
				int id = statSumListMapper.insertSelective(sumList);
				sumList.setId((long) id);
				statSumLists.add(sumList);
				String json = gson.toJson(statSumLists);
				jedisService.hwrite(RedisKey.sum_List.getKey(), roomUid.toString(), json);
				return;
			}
		} else {
			Type type = new TypeToken<List<StatSumList>>() {
			}.getType();
			statSumLists = gson.fromJson(sumListStr, type);
		}
		if (statSumLists.size() < 10) {
			addSumList(roomUid, auctionDeal, sumList);
			statSumListMapper.insertSelective(sumList);
		} else {
			addSumList(roomUid, auctionDeal, sumList);
			Collections.sort(statSumLists);
			StatSumList sumList2 = statSumLists.remove(statSumLists.size() - 1);
			statSumListMapper.insertSelective(sumList);
			statSumListMapper.deleteByPrimaryKey(sumList2.getId());
		}
		statSumLists.add(sumList);
		String json = gson.toJson(statSumLists);
		jedisService.hwrite(RedisKey.sum_List.getKey(), roomUid.toString(), json);
	}

	private void addSumList(Long roomUid, AuctionDeal auctionDeal, StatSumList statSumList) {
		statSumList.setRoomUid(roomUid);
		statSumList.setUid(auctionDeal.getDealUid());
		statSumList.setProdId(auctionDeal.getAuctUid());
		statSumList.setPrice(auctionDeal.getDealMoney());
		statSumList.setCreateTime(new Date());
	}
}
