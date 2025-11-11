package com.erban.main.service.user;

import com.beust.jcommander.internal.Lists;
import com.erban.main.model.Face;
import com.erban.main.model.FaceExample;
import com.erban.main.model.redis.MicroSeqModel;
import com.erban.main.mybatismapper.FaceMapper;
import com.erban.main.service.SysConfService;
import com.erban.main.vo.FaceMapVo;
import com.erban.main.vo.FaceVo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by liuguofu on 2017/9/11.
 */
@Service
public class FaceService {
    @Autowired
    private FaceMapper faceMapper;
    @Autowired
    private JedisService jedisService;

    @Autowired
    private SysConfService sysConfService;
    private Gson gson=new Gson();

    public BusiResult<FaceMapVo> queryFaceList() {
        BusiResult<FaceMapVo> busiResult = new BusiResult<FaceMapVo>(BusiStatus.SUCCESS);
        FaceMapVo faceMapVo=new FaceMapVo();
        List<FaceVo> faceVoList=getFaceListCache();
        if(CollectionUtils.isEmpty(faceVoList)){
            FaceExample faceDefExample = new FaceExample();
            faceDefExample.createCriteria().andFaceStatusEqualTo(Constant.FaceStatus.using).andFaceIdNotEqualTo(1);
            faceDefExample.setOrderByClause(" seq_no asc");
            List<Face> faceList = faceMapper.selectByExample(faceDefExample);
            List<FaceVo> faceVoListRela=Lists.newArrayList();
            if (!CollectionUtils.isEmpty(faceList)) {
                faceVoList=convertFaceToVo(faceList);
                for(FaceVo faceVo:faceVoList){
                    List<FaceVo> childFaceList=getChildFaceList(faceVo.getFaceId(),faceVoList);
                    if(!CollectionUtils.isEmpty(childFaceList)){//
                        faceVo.setChildren(childFaceList);
                    }
                    if(!checkHasParent(faceVo,faceVoList)){
                        faceVoListRela.add(faceVo);
                    }
                }
                saveFaceListCache(faceVoListRela);
            }
            faceVoList=faceVoListRela;
        }
        String version=sysConfService.getSysConfValueById(Constant.SysConfId.face_version);
        faceMapVo.setVersion(version);
        faceMapVo.setFaces(faceVoList);
        busiResult.setData(faceMapVo);
        return busiResult;
    }

    /**
     * 查询指定的表情是否有父类表情
     * @param faceVoCheck
     * @param faceVoList
     * @return
     */
    private boolean checkHasParent(FaceVo faceVoCheck,List<FaceVo> faceVoList){
        Integer parentId=faceVoCheck.getFaceParentId();
        boolean result=false;
        for(FaceVo faceVo2:faceVoList){
            if(faceVo2.getFaceId().equals(parentId)){
                result=true;
                break;
            }
        }
        return result;

    }

    private List<FaceVo> getChildFaceList(Integer faceId,List<FaceVo> faceVoList){
        List<FaceVo> childFaceList=Lists.newArrayList();
        for(FaceVo faceVo:faceVoList){
            Integer faceParentId=faceVo.getFaceParentId();
            if(faceParentId.equals(faceId)){
                childFaceList.add(faceVo);
            }
        }
        return childFaceList;
    }
    private List<FaceVo> getFaceListCache(){
        String faceListStr=jedisService.read(RedisKey.face.getKey());
        Type type = new TypeToken<List<FaceVo>>(){}.getType();
        List<FaceVo> faceList = gson.fromJson(faceListStr, type);
        return faceList;

    }
    private void saveFaceListCache(List<FaceVo> faceList){
        jedisService.set(RedisKey.face.getKey(),gson.toJson(faceList));
    }
    private List<FaceVo> convertFaceToVo(List<Face> faceList){
        List<FaceVo> faceVoList=Lists.newArrayList();
        for(Face face:faceList){
            FaceVo faceVo=convertFaceToVo(face);
            faceVoList.add(faceVo);
        }
        return faceVoList;
    }
    private FaceVo convertFaceToVo(Face face){
        FaceVo faceVo=new FaceVo();
        faceVo.setFaceId(face.getFaceId());
        faceVo.setFaceName(face.getFaceName());
        faceVo.setFaceParentId(face.getFaceParentId());
        faceVo.setFacePicUrl(face.getFacePicUrl());
        faceVo.setHasGifUrl(face.getHasGifUrl());
        faceVo.setFaceGifUrl(face.getFaceGifUrl());
        faceVo.setShow(face.getIsShow());
        faceVo.setFaceValue(face.getFaceValue());
        return faceVo;
    }

}
