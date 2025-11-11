package com.erban.admin.main.service.system;

import com.erban.admin.main.dto.FaceJsonDTO;
import com.erban.admin.main.dto.FacesDTO;
import com.erban.main.model.FaceJson;
import com.erban.main.mybatismapper.FaceJsonMapper;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.xchat.oauth2.service.common.util.DESUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-05-14
 * @time 17:22
 */
@Service
public class FaceJsonAdminService {

    @Autowired
    private FaceJsonMapper faceJsonMapper;

    public List<FacesDTO> getFaceJson() throws Exception{
        FaceJson faceJson =  faceJsonMapper.getFaceJson();
        if(faceJson != null){
            String result = DESUtils.DESAndBase64Decrypt(faceJson.getJson(),"1ea53d260ecf11e7b56e00163e046a26");
            Gson gson = new Gson();
            FaceJsonDTO faceJsonDTO = gson.fromJson(result, FaceJsonDTO.class);
            return faceJsonDTO.getFaces();
        }
        return Lists.newArrayList();
    }

}
