package com.erban.main.service;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.ChargeProd;
import com.erban.main.mybatismapper.ChargeProdMapper;
import com.erban.main.service.base.CacheBaseService;
import com.erban.main.util.StringUtils;
import com.erban.main.vo.ChargeProdVo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChargeProdService extends CacheBaseService<ChargeProd, ChargeProd> {
	@Autowired
	private ChargeProdMapper chargeProdMapper;

	@Override
	public ChargeProd getOneByJedisId(String jedisId) {
		return getOne(RedisKey.charge_prod.getKey(), jedisId, "select * from charge_prod where charge_prod_id = ? and prod_status = 1 ", jedisId);
	}

	@Override
	public ChargeProd entityToCache(ChargeProd entity) {
		return entity;
	}

	public String refreshProdListCache(String jedisCode) {
		return refreshListCacheByCode(null, jedisCode, "getChargeProdId", "select * from charge_prod where prod_status = 1 order by seq_no asc ");
	}

	public List<ChargeProd> query(int type) {
		String str = jedisService.get(RedisKey.charge_prod_list.getKey());
		if (StringUtils.isEmpty(str)) {
			str = refreshProdListCache(RedisKey.charge_prod_list.getKey());
		}
		List<ChargeProd> list = new ArrayList<>();
		List<ChargeProd> chargeProdList = getList(str);
		for(ChargeProd chargeProd:chargeProdList){
			if (chargeProd.getChannel() == type) {
				list.add(chargeProd);
			}
		}
		return list;
	}

	private void saveChargeProdCache(ChargeProd chargeProd) {
		jedisService.hwrite(RedisKey.charge_prod.getKey(), chargeProd.getChargeProdId(), gson.toJson(chargeProd));
	}

	public ChargeProd getChargeProdById(String chargeProdId) {
		String chargeProdStr = jedisService.hget(RedisKey.charge_prod.getKey(), chargeProdId);
		ChargeProd chargeProd = null;
		if (StringUtils.isEmpty(chargeProdStr)) {
			chargeProd = chargeProdMapper.selectByPrimaryKey(chargeProdId);
			if (chargeProd == null) {
				return null;
			} else {
				saveChargeProdCache(chargeProd);
			}
		}
		chargeProd = gson.fromJson(chargeProdStr, ChargeProd.class);
		return chargeProd;
	}

	public BusiResult<List<ChargeProdVo>> getAllChargeProdVoList(int type,String appVersion,String os) {
		BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
		//判断是否是iOS   审核:全部列表;非审核:显示一个支付方式。  )
		if ("iOS".equalsIgnoreCase(os) && !StringUtils.isBlank(appVersion) && type==1) {
			ChargeProdVo chargeProdVo = getChargeProdVoList(3).get(0);
			chargeProdVo.setProdDesc("请点击底部优惠充值");
			busiResult.setData(Arrays.asList(chargeProdVo));
		} else {
			busiResult.setData(getChargeProdVoList(type));
		}
		return busiResult;
	}

	private List<ChargeProdVo> getChargeProdVoList(int type){
		List<ChargeProd> chargeProdList = query(type);
		List<ChargeProdVo> chargeProdVoList = Lists.newArrayList();
		for (ChargeProd chargeProd : chargeProdList) {
			ChargeProdVo chargeProdVo = new ChargeProdVo();
			chargeProdVo.setChannel(chargeProd.getChannel());
			chargeProdVo.setChargeProdId(chargeProd.getChargeProdId());
			chargeProdVo.setProdName(chargeProd.getProdName());
			chargeProdVo.setGiftGoldNum(chargeProd.getGiftGoldNum());
			chargeProdVo.setMoney(chargeProd.getMoney());
			chargeProdVo.setSeqNo(chargeProd.getSeqNo());
			chargeProdVo.setProdDesc(chargeProd.getProdDesc());
			chargeProdVoList.add(chargeProdVo);
		}
		Collections.sort(chargeProdVoList);
		return chargeProdVoList;
	}

}
