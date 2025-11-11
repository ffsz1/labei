package com.erban.web.controller.user;

import com.erban.main.service.user.FaceService;
import com.erban.main.vo.FaceMapVo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by liuguofu on 2017/9/11.
 */
@Controller
@RequestMapping("/face")
public class FaceController {
    private static final Logger logger = LoggerFactory.getLogger(FaceController.class);
    @Autowired
    private FaceService faceService;
    @RequestMapping(value = "list", method = RequestMethod.GET)
    @ResponseBody
    public BusiResult queryFaceList() {
        BusiResult<FaceMapVo> busiResult  = null;
        try {
            busiResult = faceService.queryFaceList();
        } catch (Exception e) {
            logger.error(" queryFaceList error..uid=", e);
            return new BusiResult(BusiStatus.BUSIERROR);
        }
        return busiResult;
    }

}
