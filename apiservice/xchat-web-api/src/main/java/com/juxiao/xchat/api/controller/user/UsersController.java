package com.juxiao.xchat.api.controller.user;

import com.juxiao.xchat.base.annotation.Authorization;
import com.juxiao.xchat.base.annotation.Client;
import com.juxiao.xchat.base.annotation.SignVerification;
import com.juxiao.xchat.base.utils.DateTimeUtils;
import com.juxiao.xchat.base.utils.HttpServletUtils;
import com.juxiao.xchat.base.web.WebServiceCode;
import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.item.dto.GiftCarDTO;
import com.juxiao.xchat.dao.sysconf.domain.HomeChannelDO;
import com.juxiao.xchat.dao.user.dto.UsersDTO;
import com.juxiao.xchat.manager.common.item.GiftCarManager;
import com.juxiao.xchat.manager.common.mcoin.McoinMissionManager;
import com.juxiao.xchat.manager.common.sysconf.AppVersionManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.manager.common.user.UsersManager;
import com.juxiao.xchat.service.api.sysconf.HomeChannelService;
import com.juxiao.xchat.service.api.user.*;
import com.juxiao.xchat.service.api.user.bo.DeviceInfoBO;
import com.juxiao.xchat.service.api.user.bo.UserUpdateBO;
import com.juxiao.xchat.service.api.user.bo.UserUpdateV3BO;
import com.juxiao.xchat.service.api.user.vo.UserVO;
import io.swagger.annotations.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Api(tags = "用户信息接口", description = "用户信息接口")
public class UsersController {
    private final Logger logger = LoggerFactory.getLogger(UsersController.class);
    @Autowired
    private AppVersionManager versionService;
    @Autowired
    private UsersManager usersManager;
    @Autowired
    private UsersService usersService;
    @Autowired
    private UserPacketService packetService;
    @Autowired
    private UserShareRecordService shareRecordService;
    @Autowired
    private AccountLoginRecordService accountLoginRecordService;
    @Autowired
    private HomeChannelService homeChannelService;
    @Autowired
    private GiftCarManager giftCarManager;
//    @Autowired
//    private AccountsService accountsService;
    @Autowired
    private McoinMissionManager missionManager;

    @ApiOperation(value = "用户信息获取接口", notes = "根据uid获取该用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "os", value = "用户所在房间UID", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "赠送的礼物数量", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserVO.class)
    })
//    @SignVerification
    @RequestMapping(value = "get", method = RequestMethod.GET)
    public WebServiceMessage getUser(HttpServletRequest request,
                                     @RequestParam("uid") Long uid,
                                     @RequestParam(value = "os", required = false) String os,
                                     @RequestParam(value = "appid", required = false) String appid,
                                     @RequestParam(value = "appVersion", required = false, defaultValue = "1.0.3") String appVersion) throws WebServiceException {
        UserVO userVO = usersService.getUser(uid,null);
        try {
            accountLoginRecordService.saveRecord(request, userVO);
        } catch (Exception e) {
            logger.error("[ 记录登录错误 ]", e);
        }

        try {
            missionManager.achieveWeeklyMission(uid);
        } catch (Exception e) {
            logger.error("[ 萌币商城 ]记录异常：", e);
        }
        //注入绑定昵称
/*        Map<String,String> resultMap= accountsService.getBindNick(uid);
        if(resultMap!=null) {
            userVO.setWeixinNick(resultMap.get("weixinNick"));
            userVO.setQqNick(resultMap.get("qqNick"));
        }*/
        return WebServiceMessage.success(userVO);
    }

    @ApiOperation(value = "小程序获取用户信息接口", notes = "根据queryUid获取该用户信息", tags = {"用户信息接口", "小程序接口"})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "queryUid", value = "要查看的用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "用户所在房间UID", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "赠送的礼物数量", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserVO.class)
    })
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "v2/get", method = RequestMethod.GET)
    public WebServiceMessage getUserV2(HttpServletRequest request,
                                       @RequestParam("uid") Long uid,
                                       @RequestParam(value = "queryUid", required = false) Long queryUid) throws WebServiceException {
        UserVO userVo;
        if (queryUid != null) {
            userVo = usersService.getUser(queryUid,uid);
        } else {
            userVo = usersService.getUser(uid,null);
        }
        userVo.setPhone(userVo.getPhone() == null ? "" : StringUtils.removeStart(StringUtils.leftPad(StringUtils.right(userVo.getPhone(), 4), StringUtils.length(userVo.getPhone()), "*"), "***"));
        try {
            accountLoginRecordService.saveRecord(request, userVo);
        } catch (Exception e) {
            logger.error("[ 记录登录错误 ]", e);
        }

        try {
            missionManager.achieveWeeklyMission(uid);
        } catch (Exception e) {
            logger.error("[ 萌币商城 ]记录异常：", e);
        }
        return WebServiceMessage.success(userVo);
    }

    @ApiOperation(value = "APP获取用户信息，新版接口", notes = "根据uid获取该用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "queryUid", value = "要查看的用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true),
            @ApiImplicitParam(name = "os", value = "用户所在房间UID", dataType = "string", required = true),
            @ApiImplicitParam(name = "appVersion", value = "赠送的礼物数量", dataType = "string", required = true),
            @ApiImplicitParam(name = "t", value = "当前时间戳", dataType = "long", required = true),
            @ApiImplicitParam(name = "sn", value = "签名加密串", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserVO.class)
    })
    @Authorization
    @SignVerification
    @RequestMapping(value = "v3/get", method = RequestMethod.GET)
    public WebServiceMessage getUserV3(HttpServletRequest request,
                                       @RequestParam("uid") Long uid,
                                       @RequestParam("queryUid") Long queryUid) throws WebServiceException {
        UserVO userVO = usersService.getUser(queryUid,uid);
        if (userVO != null && !uid.equals(queryUid)) {
            userVO.setPhone(null);
        }
        try {
            accountLoginRecordService.saveRecord(request, userVO);
        } catch (Exception e) {
            logger.error("[ 记录登录错误 ]", e);
        }

        try {
            missionManager.achieveWeeklyMission(uid);
        } catch (Exception e) {
            logger.error("[ 萌币商城 ]记录异常：", e);
        }
        return WebServiceMessage.success(userVO);
    }

    @ApiOperation(value = "H5页面获取用户信息", notes = "根据uid获取该用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true),
            @ApiImplicitParam(name = "ticket", value = "用户登录凭证", dataType = "string", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserVO.class)
    })
    @Authorization
    @RequestMapping(value = "v4/get", method = RequestMethod.GET)
    public WebServiceMessage getUserV4(@RequestParam("uid") Long uid) throws WebServiceException {
        UsersDTO userDto = usersManager.getUser(uid);
        if (userDto == null) {
            throw new WebServiceException(WebServiceCode.USER_NOT_EXISTS);
        }

        UserVO userVo = new UserVO();
        userVo.setNick(userDto.getNick());
        userVo.setAvatar(userDto.getAvatar());
        return WebServiceMessage.success(userVo);
    }

    @ApiOperation(value = "im服务获取用户信息", notes = "根据uid获取该用户信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "uid", value = "当前用户ID", dataType = "long", required = true)
    })
    @ApiResponses({
            @ApiResponse(code = 200, message = "success", response = UserVO.class)
    })
    @RequestMapping(value = "v5/get", method = RequestMethod.GET)
    public WebServiceMessage getUserV5(@RequestParam("uid") Long uid) throws WebServiceException {
        UserVO userVo = usersService.getUserV5(uid);
        return WebServiceMessage.success(userVo);
    }

    @SignVerification
    @RequestMapping(value = "/isBindPhone", method = RequestMethod.GET)
    public WebServiceMessage bindPhoneByUid(@RequestParam("uid") Long uid) throws WebServiceException {
        usersService.isBindPhone(uid);
        return WebServiceMessage.success(null);
    }

    @Authorization
    @SignVerification
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public WebServiceMessage saveOrUpdateUser(UserUpdateBO updateBo, DeviceInfoBO deviceinfo) throws Exception {
    	if(updateBo.getBirth()!=null) {
    		int age=DateTimeUtils.getAge(updateBo.getBirth());
    		if(age<18){//注册完善资料 未满十八岁不支持注册
    			throw new WebServiceException(WebServiceCode.SIGNUP_TOAST_ERROR);
    		}
    	}
    	shareRecordService.saveUserShareRegisterRecord(updateBo.getUid(), updateBo.getShareCode());
        UserVO userVo = usersService.saveOrUpdateUser(updateBo, deviceinfo);
        this.saveShareRegisterRecord(updateBo, userVo);
        return WebServiceMessage.success(userVo);
    }

    @Authorization
    @SignVerification
    @RequestMapping(value = "updatev2", method = RequestMethod.POST)
    public WebServiceMessage saveOrUpdateUserByUidV2(UserUpdateBO updateBo, DeviceInfoBO deviceinfo) throws Exception {
        return this.saveOrUpdateUser(updateBo, deviceinfo);
    }

    @Authorization
    @SignVerification
    @RequestMapping(value = "/v2/update", method = RequestMethod.POST)
    public WebServiceMessage saveOrUpdateUserByUid(UserUpdateBO updateBo, DeviceInfoBO deviceinfo) throws Exception {
        return this.saveOrUpdateUser(updateBo, deviceinfo);
    }

    @Authorization
    @SignVerification(client = Client.WXAPP)
    @RequestMapping(value = "/v3/update", method = RequestMethod.POST)
    public WebServiceMessage saveOrUpdateUserByUidV3(@RequestBody UserUpdateV3BO updateBo) throws Exception {
        DeviceInfoBO deviceinfo = new DeviceInfoBO();
        BeanUtils.copyProperties(updateBo, deviceinfo);
        return this.saveOrUpdateUser(updateBo, deviceinfo);
    }

    /**
     * 批量查询用户
     *
     * @param uids
     * @return
     */
    @SignVerification
    @RequestMapping(value = "list", method = RequestMethod.GET)
    public WebServiceMessage getUserByUids(@RequestParam("uids") String uids) throws WebServiceException {
        List<UserVO> list = usersService.listUsersByUids(uids);
        return WebServiceMessage.success(list);
    }

    /**
     * 确认手机
     *
     * @return
     */
    @SignVerification
    @RequestMapping(value = "confirm", method = RequestMethod.POST)
    public WebServiceMessage confirm(@RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "smsCode", required = false) String smsCode) throws WebServiceException {
        if (StringUtils.isBlank(phone) || StringUtils.isBlank(smsCode)) {
            return WebServiceMessage.failure(WebServiceCode.PARAM_ERROR);
        }
        usersService.confirm(phone, smsCode);
        return WebServiceMessage.success(null);
    }

    /**
     * 更换手机
     *
     * @return
     */
    @SignVerification
    @RequestMapping(value = "replace", method = RequestMethod.POST)
    public WebServiceMessage replace(@RequestParam(value = "uid", required = false) Long uid,
                                     @RequestParam(value = "phone", required = false) String phone,
                                     @RequestParam(value = "smsCode", required = false) String smsCode) throws WebServiceException {
        usersService.replace(uid, phone, smsCode);
        return WebServiceMessage.success(null);
    }

    /**
     * 根据云信token获取用户ID
     *
     * @param token
     * @return
     * @throws WebServiceException
     */
    @SignVerification
    @RequestMapping(value = "/getUidByToken", method = RequestMethod.GET)
    public WebServiceMessage getUidByToken(@RequestParam("token") String token) throws WebServiceException {
        Long uid = usersService.getNetEaseTokenUid(token);
        return WebServiceMessage.success(uid);
    }

    /**
     * 检查设备的唯一标识
     *
     * @param idfa
     * @return
     * @throws WebServiceException
     */
    @RequestMapping(value = "/checkIdfa", method = RequestMethod.GET)
    public WebServiceMessage checkIdfa(@RequestParam("idfa") String idfa) throws WebServiceException {
        return WebServiceMessage.success(usersService.checkIdfa(idfa));
    }

    @RequestMapping(value = "/checkIdfaXQ", method = RequestMethod.GET)
    public WebServiceMessage checkIdfaXQ(@RequestParam("idfa") String idfa) throws WebServiceException {
        return WebServiceMessage.success(usersService.checkIdfaXQ(idfa));
    }

    @RequestMapping(value = "/checkIdfaJY", method = RequestMethod.GET)
    public WebServiceMessage checkIdfaJY(@RequestParam("idfa") String idfa) throws WebServiceException {
        return WebServiceMessage.success(usersService.checkIdfaJY(idfa));
    }

    /**
     * 渠道方idfa点击回调接口，记录保存入库
     * @param appid
     * @param idfa
     * @param idfamd5
     * @param clicktime
     * @return
     * @throws WebServiceException
     */
    @RequestMapping(value = "/idfa/click", method = RequestMethod.GET)
    public WebServiceMessage idfaClick(@RequestParam String appid, @RequestParam String idfa, @RequestParam(name = "idfamd5",defaultValue = "0")String idfamd5, String clicktime) throws WebServiceException {
        return WebServiceMessage.success(usersService.insertIdfaClick(appid,idfa,idfamd5,clicktime));
    }

    /**
     * 显示最近一周内新注册的用户，排序按照注册时间排列，最新的在最前面；
     *
     * @param uid
     * @param gender
     * @param pageNum
     * @param pageSize
     * @param os
     * @param appVersion
     * @return
     */
    @SignVerification
    @RequestMapping("/newUserList")
    public WebServiceMessage listNewUsers(@RequestParam(value = "uid", required = false) Long uid,
                                          @RequestParam(value = "gender", required = false) Byte gender,
                                          @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
                                          @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
                                          @RequestParam(value = "os", required = false) String os,
                                          @RequestParam(value = "appid", required = false) String appid,
                                          @RequestParam(value = "appVersion", required = false) String appVersion,
                                          HttpServletRequest request) throws WebServiceException {
        if (versionService.checkAuditingVersion(os, appid, appVersion, HttpServletUtils.getRemoteIpV4(request),uid)) {
            return WebServiceMessage.success(usersService.listNewUsers4Auditing(appid, gender, pageNum));
        } else {
            return WebServiceMessage.success(usersService.listNewUsers(appid, uid, gender, pageNum, pageSize));
        }
    }

    @Async
    public void saveShareRegisterRecord(UserUpdateBO updateBo, UserVO userVo) {

        if (userVo.getOperType() != 2) {
            return;
        }

//        try {
//            packetService.getFirstPacket(updateBo.getUid());
//        } catch (Exception e) {
//            logger.error("[ 获得新人红包 ]处理异常,uid:>{}", updateBo.getUid(), e);
//        }

//        try {
//            giveGiftCar(shareUidStr, uid);
//        } catch (Exception e) {
//            logger.error("[渠道下载注册]赠送座驾,shareUid:{},uid:{}", shareUidStr, uid);
//        }

//        if (!userVo.getHasRegPacket() || StringUtils.isBlank(updateBo.getShareCode())) {
//                return;
//            }
//        try {
//
//            logger.info("[ 保存/更新用户信息 ] 异步执行红包邀请活动,用户首次注册，并且属于被人邀请人，则邀请人获得红包，shareCode:>{}，uid:>{}", updateBo.getShareCode(), updateBo.getUid());
//            shareRecordService.saveUserShareRegisterRecord(Long.valueOf(updateBo.getShareCode()), updateBo.getUid());
//        } catch (Exception e) {
//            logger.error("邀请注册---邀请人获得红包" + e.getMessage());
//        }
    }

    /**
     * 渠道注册赠送座驾
     * @param shareUid 分享的uid, 就是渠道ID
     * @param uid 注册的用户
     */
    public void giveGiftCar(String shareUid, Long uid) {
        HomeChannelDO channelDO = homeChannelService.getByChannel(shareUid);
        if (channelDO != null && channelDO.getId() != null && channelDO.getGroupId() == 1) {
            GiftCarDTO carDTO = giftCarManager.getGiftCar(16);
            if (carDTO != null) {
                // 渠道下载的赠送座驾
                giftCarManager.saveUserCar(uid, carDTO.getCarId(), 7, 1, "恭喜您获得座驾【" + carDTO.getCarName() + "】体验7天;");
            } else {
                logger.error("[渠道下载注册]赠送座驾,座驾失效,shareUid:{},uid:{}", shareUid, uid);
            }
        }
    }


    /**
     *
     * @title: 检测是否设置过密码
     * @param: uid:用户UID
     * @return:  true || false
     * @date: 2018/11/1 14:49
     */
    @SignVerification
    @Authorization
    @PostMapping("/checkPwd")
    @ApiOperation("检测是否设置过密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
    })
    public WebServiceMessage checkPwd(@RequestParam(value = "uid", required = false) Long uid){
        return WebServiceMessage.success(usersService.checkPwd(uid));
    }

    /**
     *
     * @title: 检测是否设置过二级密码
     * @param: uid:用户UID
     * @return:  true || false
     * @date: 2020/11/25 14:49
     */
    @SignVerification
    @Authorization
    @PostMapping("/checkSecondPwd")
    @ApiOperation("检测是否设置过二级密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
    })
    public WebServiceMessage checkSecondPwd(@RequestParam(value = "uid", required = false) Long uid){
        return WebServiceMessage.success(usersService.checkSecondPwd(uid));
    }


    /**
     * 点击获取验证码
     *
     * @param phone
     * @param request
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @Authorization
    @SignVerification
    @RequestMapping(value = "/getSendSms", method = RequestMethod.POST)
    public WebServiceMessage getSendSms(HttpServletRequest request,
                                        @RequestParam(value = "phone", required = false) String phone,
                                        @RequestParam(value = "deviceId", required = false) String deviceId,
                                        @RequestParam(value = "imei", required = false) String imei,
                                        @RequestParam(value = "os", required = false) String os,
                                        @RequestParam(value = "osversion", required = false) String osversion,
                                        @RequestParam(value = "channel", required = false) String channel,
                                        @RequestParam(value = "appVersion", required = false) String appVersion,
                                        @RequestParam(value = "model", required = false) String model) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return usersService.getSendSms(ip, phone, deviceId, imei, os, osversion, channel, appVersion, model);
    }

    /**
     * 点击根据uid获取验证码
     *
     * @param uid
     * @param request
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/getSms", method = RequestMethod.GET)
    public WebServiceMessage getSms(HttpServletRequest request,
                                    @RequestParam(value = "uid", required = false) Long uid,
                                    @RequestParam(value = "deviceId", required = false) String deviceId,
                                    @RequestParam(value = "imei", required = false) String imei,
                                    @RequestParam(value = "os", required = false) String os,
                                    @RequestParam(value = "osversion", required = false) String osversion,
                                    @RequestParam(value = "channel", required = false) String channel,
                                    @RequestParam(value = "appVersion", required = false) String appVersion,
                                    @RequestParam(value = "model", required = false) String model) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return usersService.getCode(ip, uid, deviceId, imei, os, osversion, channel, appVersion, model);
    }


    /**
     * 校验验证码
     * @param request
     * @param code
     * @param phone
     * @return
     */
    @SignVerification
    @RequestMapping(value = "/validateCode", method = RequestMethod.GET)
    @ApiOperation("校验验证码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="phone",value="手机号码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="code",value="验证码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="uid",value="uid",dataType="string", paramType = "query"),
    })
    public WebServiceMessage validateCode(HttpServletRequest request, @RequestParam(value = "uid") Long uid,
                                          @RequestParam(value = "phone") String phone,
                                          @RequestParam(value = "code") String code) throws Exception {
        String ip = HttpServletUtils.getRemoteIpV4(request);
        return usersService.validateCode(ip, phone,code,uid);
    }





    /**
     * 设置密码
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    @SignVerification
    @Authorization
    @PostMapping("setPwd")
    @ApiOperation("设置登陆密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="password",value="密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="confirmPwd",value="确认密码",dataType="string", paramType = "query")
    })
    public WebServiceMessage setPwd(@RequestParam(value = "uid") Long uid,
                                    @RequestParam(value = "password")String password,
                                    @RequestParam(value = "confirmPwd")String confirmPwd){
        return usersService.setPwd(uid,password,confirmPwd);
    }


    /**
     * 修改密码
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    @SignVerification
    @Authorization
    @PostMapping("modifyPwd")
    @ApiOperation("修改登陆密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="oldPwd",value="请输入当前登录密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="password",value="请输入新的登录密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="confirmPwd",value="再次输入新的登陆密码",dataType="string", paramType = "query")
    })
    public WebServiceMessage modifyPwd(@RequestParam(value = "uid") Long uid,@RequestParam(value = "oldPwd")String oldPwd,@RequestParam(value = "password")String password,@RequestParam(value = "confirmPwd")String confirmPwd){
        return usersService.modifyPwd(uid,oldPwd,password,confirmPwd);
    }

    /**
     * 设置二级密码
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    @SignVerification
    @Authorization
    @PostMapping("setSecondPwd")
    @ApiOperation("设置二级密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="password",value="密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="confirmPwd",value="确认密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="code",value="验证码",dataType="string", paramType = "query")
    })
    public WebServiceMessage setSecondPwd(@RequestParam(value = "uid") Long uid,
                                    @RequestParam(value = "password")String password,
                                    @RequestParam(value = "confirmPwd")String confirmPwd,
                                    @RequestParam(value = "code")String code) throws Exception{
        return usersService.setSecondPwd(uid,password,confirmPwd, code);
    }


    /**
     * 修改二级密码
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    @SignVerification
    @Authorization
    @PostMapping("modifySecondPwd")
    @ApiOperation("修改二级密码")
    @ApiImplicitParams({
            @ApiImplicitParam(name="uid",value="uid",dataType="long", paramType = "query"),
            @ApiImplicitParam(name="oldPwd",value="请输入当前二级密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="password",value="请输入新的二级密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="confirmPwd",value="再次输入新的二级密码",dataType="string", paramType = "query"),
            @ApiImplicitParam(name="code",value="验证码",dataType="string", paramType = "query")
    })
    public WebServiceMessage modifySecondPwd(@RequestParam(value = "uid") Long uid,
                                             @RequestParam(value = "oldPwd")String oldPwd,
                                             @RequestParam(value = "password")String password,
                                             @RequestParam(value = "confirmPwd")String confirmPwd,
                                             @RequestParam(value = "code")String code) throws Exception{
        return usersService.modifySecondPwd(uid,oldPwd,password,confirmPwd, code);
    }




    /**
     * 云信同步接口
     * @return
     * @throws WebServiceException
     */
    @RequestMapping(value = "v2/batchSyncToNetEase", method = RequestMethod.GET)
    public WebServiceMessage batchSyncToNetEase(@RequestParam(value = "start") Long start) throws WebServiceException {
        usersService.batchSyncToNetEase(start);
        return WebServiceMessage.success(null);
    }

}
