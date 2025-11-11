package com.erban.admin.main.service;

import com.erban.main.dto.ReviewConfigDTO;
import com.erban.main.model.AppChannel;
import com.erban.main.model.AppChannelExample;
import com.erban.main.model.ReviewConfig;
import com.erban.main.model.RoomTag;
import com.erban.main.mybatismapper.AppChannelMapper;
import com.erban.main.mybatismapper.AppSystemMapper;
import com.erban.main.mybatismapper.ReviewConfigMapper;
import com.erban.main.mybatismapper.RoomTagMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2018/10/22
 * @time 15:36
 */
@Service
public class AndroidReviewConfigService {

    @Autowired
    private ReviewConfigMapper reviewConfigMapper;

    @Autowired
    private RoomTagMapper roomTagMapper;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private AppChannelMapper appChannelMapper;

    @Autowired
    private AppSystemMapper appSystemMapper;


    public int save(ReviewConfig reviewConfig, boolean isEdit) {
        int result = 0;
        if (isEdit) {
            ReviewConfigDTO androidReviewConfigDTO  = getById(reviewConfig.getId());
            result = reviewConfigMapper.updateByPrimaryKeySelective(reviewConfig);
            if (result > 0) {
                Gson gson = new Gson();
                if (reviewConfig.getStatus() == 1) {
                    String[] oldStr = androidReviewConfigDTO.getTagName().split(",");
                    Arrays.asList(oldStr).stream().forEach(item -> {
                        jedisService.hdel(RedisKey.review_config.getKey(), reviewConfig.getId().toString() + "_" + item);
                    });
                    String[] newsStr = reviewConfig.getTagName().split(",");
                    Arrays.asList(newsStr).stream().forEach(item -> {
                        ReviewConfigDTO reviewConfigDTO = new ReviewConfigDTO();
                        reviewConfigDTO.setSystem(androidReviewConfigDTO.getSystem());
                        reviewConfigDTO.setChannel(androidReviewConfigDTO.getChannel());
                        reviewConfigDTO.setCreateTime(androidReviewConfigDTO.getCreateTime());
                        reviewConfigDTO.setId(androidReviewConfigDTO.getId());
                        reviewConfigDTO.setRechargeAmount(androidReviewConfigDTO.getRechargeAmount());
                        reviewConfigDTO.setStatus(androidReviewConfigDTO.getStatus());
                        reviewConfigDTO.setVersions(androidReviewConfigDTO.getVersions());
                        reviewConfigDTO.setTagName(item);
                        jedisService.hset(RedisKey.review_config.getKey(), androidReviewConfigDTO.getId().toString() + "_" + item, gson.toJson(reviewConfigDTO));
                    });

                } else {
                    String[] temp = reviewConfig.getTagName().split(",");
                    Arrays.asList(temp).stream().forEach(item -> {
                        jedisService.hdel(RedisKey.review_config.getKey(), reviewConfig.getId().toString() + "_" + item);
                    });
                }
            }
        } else {
            result = reviewConfigMapper.insert(reviewConfig);
            if (result > 0) {
                Gson gson = new Gson();
                ReviewConfigDTO androidReviewConfigDTO  = getById(reviewConfig.getId());
                if (reviewConfig.getStatus() == 1) {
                    String[] str = reviewConfig.getTagName().split(",");
                    Arrays.asList(str).stream().forEach(item -> {
                        ReviewConfigDTO reviewConfigDTO = new ReviewConfigDTO();
                        reviewConfigDTO.setSystem(androidReviewConfigDTO.getSystem());
                        reviewConfigDTO.setChannel(androidReviewConfigDTO.getChannel());
                        reviewConfigDTO.setCreateTime(androidReviewConfigDTO.getCreateTime());
                        reviewConfigDTO.setId(androidReviewConfigDTO.getId());
                        reviewConfigDTO.setRechargeAmount(androidReviewConfigDTO.getRechargeAmount());
                        reviewConfigDTO.setStatus(androidReviewConfigDTO.getStatus());
                        reviewConfigDTO.setVersions(androidReviewConfigDTO.getVersions());
                        reviewConfigDTO.setTagName(item);
                        jedisService.hset(RedisKey.review_config.getKey(), androidReviewConfigDTO.getId().toString() + "_" + item, gson.toJson(reviewConfigDTO));
                    });
                } else {
                    jedisService.hdel(RedisKey.review_config.getKey(), androidReviewConfigDTO.getId().toString());
                }
            }
        }

        return result;
    }

    public int deleteById(Integer id) {
        int result = 0;
        try {
            ReviewConfigDTO reviewConfig = getById(id);
            result = reviewConfigMapper.deleteByPrimaryKey(id);
            if (result > 0) {
                String[] oldStr = reviewConfig.getTagName().split(",");
                Arrays.asList(oldStr).stream().forEach(item -> {
                    jedisService.hdel(RedisKey.review_config.getKey(), reviewConfig.getId().toString() + "_" + item);
                });

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public PageInfo<ReviewConfigDTO>  getList(String searchText, int pageNumber, int pageSize) {
        if (pageNumber < 1 || pageSize < 0) {
            throw new IllegalArgumentException("page cann't less than 1 or size cann't less than 0");
        }
        PageHelper.startPage(pageNumber, pageSize);

        List<ReviewConfigDTO> list = reviewConfigMapper.selectByList(searchText);

        return new PageInfo<>(list);
    }

    public ReviewConfigDTO getById(Integer id) {
        ReviewConfigDTO androidReviewConfigDTO = new ReviewConfigDTO();
        ReviewConfig androidReviewConfig = reviewConfigMapper.selectByPrimaryKey(id);
        androidReviewConfigDTO.setTagName(androidReviewConfig.getTagName());
        androidReviewConfigDTO.setCreateTime(androidReviewConfig.getCreateTime());
        androidReviewConfigDTO.setId(androidReviewConfig.getId());
        androidReviewConfigDTO.setRechargeAmount(androidReviewConfig.getRechargeAmount());
        androidReviewConfigDTO.setStatus(androidReviewConfig.getStatus());
        androidReviewConfigDTO.setVersions(androidReviewConfig.getVersions());
        androidReviewConfigDTO.setChannel(androidReviewConfig.getChannel());
        androidReviewConfigDTO.setSystem(appSystemMapper.selectByPrimaryKey(androidReviewConfig.getSystemId()).getName());
        return androidReviewConfigDTO;
    }

    public List<RoomTag> getTagList() {
        List<RoomTag> roomTags = roomTagMapper.selectByExample(null);
        return roomTags;
    }

    public List<AppChannel> selectTypeByChannels(Integer systemId) {
        AppChannelExample appChannelExample = new AppChannelExample();
        appChannelExample.createCriteria().andSystemIdEqualTo(systemId);
        return appChannelMapper.selectByExample(appChannelExample);
    }
}
