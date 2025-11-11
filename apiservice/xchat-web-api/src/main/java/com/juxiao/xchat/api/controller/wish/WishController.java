package com.juxiao.xchat.api.controller.wish;

import com.alibaba.fastjson.JSON;
import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.DateUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.wish.domain.Wish;
import com.juxiao.xchat.dao.wish.query.WishQuery;
import com.juxiao.xchat.manager.external.async.AsyncNetEaseTrigger;
import com.juxiao.xchat.service.api.user.vo.UserVO;
import com.juxiao.xchat.service.api.wish.WishService;
import com.juxiao.xchat.service.api.wish.bo.PageBo;
import com.juxiao.xchat.service.api.wish.bo.WishBo;
import com.juxiao.xchat.service.api.wish.vo.PageInfo;
import com.juxiao.xchat.service.api.wish.vo.WishVo;
import io.swagger.annotations.*;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@RestController
@Api(tags = "心愿接口", description = "心愿接口")
@RequestMapping("/wish")
public class WishController {
    @Autowired
    private WishService wishService;


/*    @ApiOperation(value = "添加心愿接口", notes = "需要ticket、加密验证")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "remarks", value = "用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "viceUrl", value = "用户所在房间UID", dataType = "long", required = true),
            @ApiImplicitParam(name = "personalLabels", value = "赠送的礼物ID", dataType = "int", required = true),
            @ApiImplicitParam(name = "meetLabel", value = "赠送的礼物数量", dataType = "int", required = true),
            @ApiImplicitParam(name = "ticket", value = "登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "操作系统", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "客户端版本", dataType = "string", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success")
    })*/
    @PostMapping("/save")
    public WebServiceMessage saveWish(@RequestParam(value = "uid") long uid,
                                      @RequestParam(value = "remarks",required=false) String remarks,
                                      @RequestParam(value = "viceUrl",required=false) String viceUrl,
                                      @RequestParam(value = "personalLabels",required=false) Integer[] personalLabels,
                                      @RequestParam(value = "meetLabels",required=false) Integer[] meetLabels,
                                      @RequestParam(value = "audioDura",required=false) Integer audioDura
                                     ) {
        /*        Integer[] personalLabel = JSON.parseObject("", Integer[].class);*/
        List<Integer> personal = null;
        if(personalLabels!=null&&personalLabels.length>0){
            personal=Arrays.asList(personalLabels);
        }
        List<Integer> meet = null;
        if(meetLabels!=null&&meetLabels.length>0){
            meet=Arrays.asList(meetLabels);
        }
        WishBo wishBo=new WishBo(uid,remarks,viceUrl,audioDura,personal,meet);
        int result = wishService.save(wishBo);
        if(result==1){
            return new WebServiceMessage(200,"心愿已保存！");
        }else {
            return new WebServiceMessage(505,"心愿保存失败！");
        }
    }

    @RequestMapping("/wishPage")
    public WebServiceMessage wishPage(WishQuery wishQuery, PageBo pageBo,Integer day){
        String message = pageBo.checkParam();
        if(message !=null) return WebServiceMessage.failure(message);
        if(day!=null){
            Date date = DateUtils.removeTime(new Date());
            Date date1 = DateUtils.duDate(date, day*(-1));
            wishQuery.setUpdateTime(date1);
        }else{
            wishQuery.setUpdateTime(null);
        }
        if(wishQuery.getGender()==null){
            //null
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        else if(wishQuery.getGender()==0){
            //=0时，不加筛选，男女都查
            wishQuery.setGender(null);
        }
        List<WishVo> wishVos = wishService.listWish2(wishQuery, pageBo);
        return WebServiceMessage.success(wishVos);
    }

    /**
     * 查询单个心愿
     * @param uid
     * @return
     */
    @RequestMapping("/get")
    public WebServiceMessage get(Long uid){
        return wishService.getWish(uid);
    }
}
