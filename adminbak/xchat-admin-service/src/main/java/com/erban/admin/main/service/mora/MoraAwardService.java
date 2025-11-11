package com.erban.admin.main.service.mora;

import com.erban.admin.main.bo.MoraAwardBO;
import com.erban.admin.main.bo.MoraAwardGiftBO;
import com.erban.admin.main.service.system.AdminUserService;
import com.erban.main.dto.MoraAwardDTO;
import com.erban.main.model.MoraAward;
import com.erban.main.mybatismapper.MoraAwardMapper;
import com.erban.main.service.gift.GiftService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chris
 * @Title:
 * @date 2019-05-30
 * @time 11:20
 */
@Service
public class MoraAwardService {

    @Autowired
    private MoraAwardMapper moraAwardMapper;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private AdminUserService adminUserService;


    public int save(MoraAwardBO moraAwardBO,boolean isEdit,Integer adminId){
        int result = 0;
        MoraAward moraAward = new MoraAward();
        List<MoraAwardGiftBO> moraAwardGiftBOS = Lists.newArrayList();
        for(int i = 0; i < moraAwardBO.getGiftId().length; i++){
            MoraAwardGiftBO moraAwardGiftBO = new MoraAwardGiftBO();
            moraAwardGiftBO.setGiftId(moraAwardBO.getGiftId()[i]);
            moraAwardGiftBO.setNum(moraAwardBO.getNum()[i]);
            moraAwardGiftBOS.add(moraAwardGiftBO);
        }
        moraAward.setJson(new Gson().toJson(moraAwardGiftBOS));
        int valid = moraAwardMapper.selectByIsUse(moraAwardBO.getProbability());
        if(valid == 0){
            moraAward.setIsUse(1);
        }else{
            moraAward.setIsUse(2);
        }
        if(!isEdit){
            moraAward.setAdminId(adminId);
            moraAward.setProbability(moraAwardBO.getProbability());
            moraAward.setGrade(moraAwardBO.getGrade());
            moraAward.setCreateTime(new Date());
            result = moraAwardMapper.insert(moraAward);
            if(result > 0 && moraAward.getIsUse() == 1){
                jedisService.hset(RedisKey.mora_award.getKey(),moraAward.getProbability().toString(),new Gson().toJson(moraAward));
            }
        }else{
            moraAward = moraAwardMapper.selectByPrimaryKey(moraAwardBO.getId());
            moraAward.setAdminId(adminId);
            moraAward.setProbability(moraAwardBO.getProbability());
            moraAward.setGrade(moraAwardBO.getGrade());
            result = moraAwardMapper.updateByPrimaryKeySelective(moraAward);
        }
        return result;
    }

    public MoraAward getById(Integer id) {
        return moraAwardMapper.selectByPrimaryKey(id);
    }

    public int delete(Integer id) {
        return moraAwardMapper.deleteByPrimaryKey(id);
    }

    public BusiResult use(Integer id) {
        MoraAward moraAward = getById(id);
        if (moraAward != null) {
            jdbcTemplate.update("update mora_award set is_use = 0 where probability = ?", moraAward.getProbability());
            moraAward.setIsUse(1);
           int result =  moraAwardMapper.updateByPrimaryKeySelective(moraAward);
           if(result > 0 && moraAward.getIsUse() == 1) {
               jedisService.hset(RedisKey.mora_award.getKey(),moraAward.getProbability().toString(),new Gson().toJson(moraAward));
               return new BusiResult(BusiStatus.SUCCESS);
           }
        }
        return new BusiResult(BusiStatus.NOTEXISTS);
    }

    public PageInfo<MoraAwardDTO> getList(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);

        List<MoraAwardDTO> list = moraAwardMapper.selectByPage(searchText);
        if(list.size() > 0){
            list.forEach(item ->{
                MoraAwardGiftBO[] array = new Gson().fromJson(item.getJson(),MoraAwardGiftBO[].class);
                List<MoraAwardGiftBO> moraAwardGiftBOList = Arrays.asList(array);
                StringBuilder sb = new StringBuilder();
                AtomicInteger i  = new AtomicInteger(1);
                moraAwardGiftBOList.forEach(temp ->{
                    sb.append("【");
                    sb.append("礼物").append(i).append("Id:").append(temp.getGiftId());
                    sb.append("\r\n");
                    sb.append("\r\n");
                    sb.append("|");
                    sb.append("\r\n");
                    sb.append("\r\n");
                    sb.append("数量").append(i).append(":").append(temp.getNum());
                    sb.append("】");
                    sb.append("\r\n");
                    sb.append("\r\n");
                    i.getAndIncrement();
                });
                item.setJson(sb.toString());
                if(item.getAdminId() != null){
                    item.setAdminName(adminUserService.getAdminUserById(item.getAdminId()).getUsername());
                }
            });
        }
        return new PageInfo<>(list);
    }
}
