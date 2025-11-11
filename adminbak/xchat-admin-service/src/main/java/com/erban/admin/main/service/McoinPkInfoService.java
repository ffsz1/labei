package com.erban.admin.main.service;

import com.erban.admin.main.utils.HttpUtils;
import com.erban.main.model.McoinPkInfo;
import com.erban.main.mybatismapper.McoinPkInfoMapper;
import com.erban.main.util.StringUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class McoinPkInfoService {

    private static final Logger log = LoggerFactory.getLogger(McoinPkInfoService.class);
    @Autowired
    private McoinPkInfoMapper mcoinPkInfoMapper;
    @Autowired
    private JedisService jedisService;

    @Value("${sendPKMQMessage}")
    private String url;

    public PageInfo<McoinPkInfo> getList(int pageNumber, int pageSize, int term, int pkStatus) {
        PageHelper.startPage(pageNumber, pageSize,"create_time DESC");
        List<McoinPkInfo> mcoinPkInfoList = mcoinPkInfoMapper.findMcoinPkInfo(term, pkStatus);
        if(mcoinPkInfoList.size() > 0){
            mcoinPkInfoList.forEach(item->{
                if(item.getBluePolls() == null && item.getRedPolls() == null){
                    String redPolls = Optional.ofNullable(jedisService.hget(RedisKey.mcoin_pk_support_red_polls + "_" + item.getTerm(),item.getTerm()+"")).orElse("0");
                    String bluePolls = Optional.ofNullable(jedisService.hget(RedisKey.mcoin_pk_support_blue_polls + "_" + item.getTerm(),item.getTerm()+"")).orElse("0");
                    item.setBluePolls(Integer.valueOf(bluePolls));
                    item.setRedPolls(Integer.valueOf(redPolls));
                }
            });
        }
        return new PageInfo<>(mcoinPkInfoList);
    }

    public int saveMcoinPkInfo(McoinPkInfo mcoinPkInfo){

        mcoinPkInfo.setCreateTime(new Date());
        mcoinPkInfo.setUpdateTime(new Date());
        int status = mcoinPkInfoMapper.insert(mcoinPkInfo);
        if (status > 0) {
            if(mcoinPkInfo.getPkStatus() == 1){
                try {
                    HttpUtils.get(url,null);
                }catch (Exception e){
                    log.error("发送PK延迟MQ消息出现异常,异常信息:{}",e);
                }
            }
            jedisService.del(RedisKey.mcoin_pk_info_list_string.getKey());
        }
        return status;
    }

    public McoinPkInfo findByPkStatus(byte pkStatus) {

       return mcoinPkInfoMapper.findByPkStatus(pkStatus);
    }

    public McoinPkInfo getOne(Long id) {

        return mcoinPkInfoMapper.selectByPrimaryKey(id);
    }

    public int updateMcoinPkInfo(McoinPkInfo mcoinPkInfo) {

        int status = 0;
        status = mcoinPkInfoMapper.updateByPrimaryKeySelective(mcoinPkInfo);

        return status;
    }
}

