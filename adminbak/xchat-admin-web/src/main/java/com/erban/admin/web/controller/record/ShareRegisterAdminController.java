package com.erban.admin.web.controller.record;

import com.alibaba.fastjson.JSON;
import com.erban.admin.main.service.record.ShareRegisterAdminService;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.HomeChannel;
import com.erban.main.model.HomeChannelExample;
import com.erban.main.model.StatPacketRegister;
import com.erban.main.model.Users;
import com.erban.main.mybatismapper.HomeChannelMapper;
import com.erban.main.param.admin.UserAssociateParam;
import com.erban.main.service.statis.StatPacketRegisterService;
import com.erban.main.service.user.UserShareRecordService;
import com.erban.main.service.user.UsersService;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 分享注册成功后，因未传入分享人uid，导致未发生Async异步执行红包邀请活动，
 * 此controller专门用户后台管理员手动执行邀请
 */
@Controller
@RequestMapping("/admin/shareRegister")
public class ShareRegisterAdminController extends BaseController {
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserShareRecordService userShareRecordService;
    @Autowired
    private StatPacketRegisterService statPacketRegisterService;
    @Autowired
    private ShareRegisterAdminService shareRegisterAdminService;
    @Autowired
    private HomeChannelMapper homeChannelMapper;

    /**
     * 后台关联分享者与注册者
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/addAssociate", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getRedPacketWithDrawList(Long shareErbanNo,Long registerErbanNo, String shareUid) {
        logger.info("接口调用（/admin/shareRegister/addAssociate），分享注册后，关联分享人和注册人,接口入参:shareErbanNo{},registerErbanNo:{}", shareErbanNo,registerErbanNo);
        BusiResult busiResult = null;
        if (shareUid == null) {
            if (shareErbanNo == null || registerErbanNo==null) {
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
//        用户是否存在
            Users shareUser = usersService.getUsersByErBanNo(shareErbanNo);
            Users registerUser = usersService.getUsersByErBanNo(registerErbanNo);
            if (shareUser==null || registerUser==null || shareUser.getUid()==null || registerUser.getUid()==null){
                return new BusiResult(BusiStatus.ADMIN_SHAREREGISTER_NOUSERS);
            }
//        注册用户是否已经关联邀请人
            StatPacketRegister shareRegisterByRegister = statPacketRegisterService.getShareRegisterByRegisterUid(registerErbanNo);
            if(shareRegisterByRegister!=null){
                return new BusiResult(BusiStatus.ADMIN_SHAREREGISTER_CONFILT);//已经关联
            }
            try {
//            执行红包邀请活动,用户首次注册，并且属于被人邀请人，则邀请人获得红包
                busiResult = userShareRecordService.saveUserShareRegisterRecord(shareUser.getUid(), registerUser.getUid());
            } catch (Exception e) {
                logger.error("分享注册后，关联分享人和注册人出错,错误原因:{}", e);
                busiResult = new BusiResult(BusiStatus.SERVERERROR);
            }
            logger.info("分享注册后，关联分享人和注册人(/admin/shareRegister/addAssociate),接口出参：{}",JSON.toJSONString(busiResult));
            return busiResult;
        } else {
            // 使用shareUid, 不走邀请红包的逻辑, 直接修改被邀请人的shareUid
            if(registerErbanNo == null) {
                return new BusiResult(BusiStatus.PARAMETERILLEGAL);
            }
            Users registerUser = usersService.getUsresByErbanNo(registerErbanNo);
            if (registerUser == null) {
                return new BusiResult(BusiStatus.USERNOTEXISTS);
            }
            if (registerUser.getShareUid() != null) {
                return new BusiResult(BusiStatus.ADMIN_SHAREREGISTER_CONFILT);
            }
            HomeChannelExample example = new HomeChannelExample();
            example.createCriteria().andChannelEqualTo(shareUid);
            List<HomeChannel> list = homeChannelMapper.selectByExample(example);
            if (list == null || list.isEmpty()) {
                return new BusiResult(BusiStatus.SERVERERROR, "渠道信息不存在", null);
            }
            registerUser.setShareUid(Long.valueOf(shareUid));
            usersService.update(registerUser);
            logger.info("[修改用户shareUid],registerUid:{},shareUid:{}", registerUser.getUid(), shareUid);
            return new BusiResult(BusiStatus.SUCCESS);
        }
    }

    /**
     * 统计邀请人邀请注册人数
     * @param
     * @return
     */
    @RequestMapping(value = "/statShareRegister", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getAssociateList(@RequestBody UserAssociateParam userAssociateParam) {
        logger.info("接口调用（/admin/shareRegister/statShareRegister），邀请人数统计，接口入参:userAssociateParam:{}", JSON.toJSONString(userAssociateParam));
        BusiResult busiResult = null;
        if (userAssociateParam == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = shareRegisterAdminService.queryStat(userAssociateParam);
        } catch (Exception e) {
            logger.error("接口调用（/admin/shareRegister/statShareRegister），邀请人数统计出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("接口调用（/admin/shareRegister/statShareRegister），邀请人数统计接口出参：{}",JSON.toJSONString(busiResult));
        return busiResult;
    }

    /**
     * 获取注册明细
     *
     */
    @RequestMapping(value = "/registerList", method = RequestMethod.POST)
    @ResponseBody
    public BusiResult getRegisterList(@RequestBody UserAssociateParam userAssociateParam) {
        logger.info("接口调用（/admin/shareRegister/registerList），邀请明细，接口入参:userAssociateParam:{}",JSON.toJSONString(userAssociateParam));
        BusiResult busiResult = null;
        if (userAssociateParam==null || userAssociateParam.getUid() == null) {
            return new BusiResult(BusiStatus.PARAMETERILLEGAL);
        }

        try {
            busiResult = shareRegisterAdminService.getRegisterList(userAssociateParam);
        } catch (Exception e) {
            logger.error("接口调用（/admin/shareRegister/registerList），邀请明细出错,错误原因:{}", e);
            busiResult = new BusiResult(BusiStatus.SERVERERROR);
        }
        logger.info("接口调用（/admin/shareRegister/registerList），邀请明细接口出参：{}",JSON.toJSONString(busiResult));
        return busiResult;
    }
}
