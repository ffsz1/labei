package com.erban.main.service.user;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.PrivatePhoto;
import com.erban.main.model.PrivatePhotoExample;
import com.erban.main.mybatismapper.PrivatePhotoMapper;
import com.erban.main.service.api.QiniuService;
import com.erban.main.service.base.BaseService;
import com.erban.main.vo.PrivatePhotoVo;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.*;

/**
 * @author yanghaoyu 个人照片管理
 */

@Service
public class PrivatePhotoService extends BaseService{

    @Autowired
    private PrivatePhotoMapper privatePhotoMapper;
    @Autowired
    private QiniuService qiniuService;


    /**
     * 上传个人图片的旧接口，因为之前是用网易云保存图片，需要把网易云的图片再上传到七牛
     *
     * @param uid
     * @param photoStr
     * @return
     */
    public BusiResult upload(Long uid, String photoStr) {
        String[] photos = photoStr.split(",");
        int i = 1;
        Date date = new Date();
        for (String photo : photos) {
            try {
                // 迁移到七牛服务器
                String fileName = qiniuService.uploadByUrl(photo);
                PrivatePhoto privatePhoto = new PrivatePhoto();
                privatePhoto.setUid(uid);
                privatePhoto.setPhotoUrl(qiniuService.mergeUrlAndSlim(fileName));
                privatePhoto.setSeqNo(i++);
                privatePhoto.setCreateTime(date);
                privatePhotoMapper.insertSelective(privatePhoto);
                String json = gson.toJson(privatePhoto);
                jedisService.hwrite(RedisKey.private_photo.getKey() + uid, privatePhoto.getPid().toString(), json);
            } catch (Exception e) {
                logger.error("upload error", e);
            }
        }
        return new BusiResult(BusiStatus.SUCCESS);
    }

    /* 得到用户的个人图片 */
    public List<PrivatePhoto> getPrivatePhoto(Long uid) {
        Map<String, String> map = jedisService.hgetAllBykey(RedisKey.private_photo.getKey() + uid);
        List<PrivatePhoto> privatePhotos = new ArrayList<>();
        if (map != null && map.size() != 0) {
            Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, String> entry = it.next();
                String value = entry.getValue();
                if (StringUtils.isNotEmpty(value)) {
                    PrivatePhoto privatePhoto = gson.fromJson(value, PrivatePhoto.class);
                    privatePhotos.add(privatePhoto);
                }
            }
        } else {
            PrivatePhotoExample example = new PrivatePhotoExample();
            example.createCriteria().andUidEqualTo(uid);
            privatePhotos = privatePhotoMapper.selectByExample(example);
            for (PrivatePhoto privatePhoto : privatePhotos) {
                String json = gson.toJson(privatePhoto);
                jedisService.hwrite(RedisKey.private_photo.getKey() + uid, privatePhoto.getPid().toString(), json);
            }
        }
        return privatePhotos;
    }

    public List<PrivatePhotoVo> converToPrivatePhotoListVo(List<PrivatePhoto> privatephotoList) {
        List<PrivatePhotoVo> privatePhotoVos = Lists.newArrayList();
        if (CollectionUtils.isEmpty(privatephotoList)) {
            return privatePhotoVos;
        }
        for (PrivatePhoto privatePhoto : privatephotoList) {
            PrivatePhotoVo privatePhotoVo = convertPrivatePhotoToVo(privatePhoto);
            privatePhotoVos.add(privatePhotoVo);
        }
        return privatePhotoVos;
    }

    private PrivatePhotoVo convertPrivatePhotoToVo(PrivatePhoto privatePhoto) {
        PrivatePhotoVo privatePhotoVo = new PrivatePhotoVo();
        privatePhotoVo.setPid(privatePhoto.getPid());
        privatePhotoVo.setPhotoUrl(privatePhoto.getPhotoUrl());
        privatePhotoVo.setSeqNo(privatePhoto.getSeqNo());
        privatePhotoVo.setCreateTime(privatePhoto.getCreateTime());
        return privatePhotoVo;
    }

    public BusiResult delPhoto(Long uid, Long pid) {
        deletePhoto(pid, uid);
        return new BusiResult(BusiStatus.SUCCESS);
    }

    private void deletePhoto(Long pid, Long uid) {
        privatePhotoMapper.deleteByPrimaryKey(pid);
        deletePhotoCache(pid, uid);
    }

    private void deletePhotoCache(Long pid, Long uid) {
        jedisService.hdelete(RedisKey.private_photo.getKey() + uid, String.valueOf(pid), null);
    }

}
