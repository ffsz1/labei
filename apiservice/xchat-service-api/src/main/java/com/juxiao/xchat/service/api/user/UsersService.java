package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.base.web.WebServiceMessage;
import com.juxiao.xchat.dao.user.dto.UserNewDTO;
import com.juxiao.xchat.service.api.user.bo.DeviceInfoBO;
import com.juxiao.xchat.service.api.user.bo.UserUpdateBO;
import com.juxiao.xchat.service.api.user.vo.UserVO;

import java.util.List;

/**
 * 用户业务处理接口
 *
 * @class: UsersService.java
 * @author: chenjunsheng
 * @date 2018/6/11
 */
public interface UsersService {

    /**
     * 绑定手机
     *
     * @param uid
     * @param phone
     * @param code
     * @throws WebServiceMessage
     */
    boolean bindPhone(Long uid, String phone, String code) throws WebServiceException;

    /**
     * 获取绑定手机验证码
     *
     * @param ip
     * @param phone
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    WebServiceMessage getBoundPhoneCode(String ip, String phone, String deviceId, String imei, String os, String osversion, String channel, String appVersion, String model) throws Exception;

    /**
     * 点击获取验证码
     *
     * @param ip
     * @param uid
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    WebServiceMessage getCode(String ip, Long uid, String deviceId, String imei, String os, String osversion, String channel, String appVersion, String model) throws Exception;

    /**
     * 获取用户信息
     *
     * @param uid
     * @return
     */
    UserVO getUser(Long uid,Long queryUid) throws WebServiceException;

    /**
     *
     * @param uid
     * @return
     * @throws WebServiceException
     */
    UserVO getUserV5(Long uid) throws WebServiceException;

    /**
     * 判断是否绑定手机
     *
     * @param uid
     * @return
     */
    void isBindPhone(Long uid) throws WebServiceException;

    /**
     * 保存或者更新用户信息
     *
     * @param updateBo
     * @param deviceInfo
     * @return
     */
    UserVO saveOrUpdateUser(UserUpdateBO updateBo, DeviceInfoBO deviceInfo) throws Exception;

    /**
     * 根据用户ID数据查询所有的用户
     *
     * @param uids
     * @return
     */
    List<UserVO> listUsersByUids(String uids) throws WebServiceException;

    /**
     * 更换手机号码
     *
     * @param uid
     * @param phone
     * @param smsCode
     */
    void replace(Long uid, String phone, String smsCode) throws WebServiceException;

    /**
     * @param token
     * @return
     */
    Long getNetEaseTokenUid(String token) throws WebServiceException;

    /**
     * @param idfa
     * @return
     * @throws WebServiceException
     */
    int checkIdfa(String idfa) throws WebServiceException;

    int checkIdfaXQ(String idfa) throws WebServiceException;

    int checkIdfaJY(String idfa) throws WebServiceException;

    /**
     * 1）显示最近一周内新注册的用户，排序按照注册时间排列，最新的在最前面；
     * 2) 过滤掉被封禁的用户
     *
     * @param uid
     * @param gender
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<UserNewDTO> listNewUsers(String app, Long uid, Byte gender, Integer pageNum, Integer pageSize);

    /**
     * 查询IOS审核版本中的新注册用户
     *
     * @param gender
     * @param pageNum
     * @return
     */
    List<UserNewDTO> listNewUsers4Auditing(String app, Byte gender, Integer pageNum);


    /**
     * 设置登陆密码
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    WebServiceMessage setPwd(Long uid, String password, String confirmPwd);




    /**
     * 修改登陆密码
     * @param uid
     * @param oldPwd
     * @param password
     * @param confirmPwd
     * @return
     */
    WebServiceMessage modifyPwd(Long uid,String oldPwd, String password, String confirmPwd);

    /**
     * 设置二级密码
     * @param uid
     * @param password
     * @param confirmPwd
     * @return
     */
    WebServiceMessage setSecondPwd(Long uid, String password, String confirmPwd, String code) throws Exception;

    /**
     * 修改二级密码
     * @param uid
     * @param oldPwd
     * @param password
     * @param confirmPwd
     * @return
     */
    WebServiceMessage modifySecondPwd(Long uid,String oldPwd, String password, String confirmPwd, String code) throws Exception;

    /**
     * 校验手机验证码
     * @param ip
     * @param phone
     * @param code
     * @return
     */
    WebServiceMessage validateCode(String ip, String phone, String code,Long uid)  throws WebServiceException;

    /**
     * 获取验证码
     * @param ip
     * @param phone
     * @param deviceId
     * @param imei
     * @param os
     * @param osversion
     * @param channel
     * @param appVersion
     * @param model
     * @return
     */
    WebServiceMessage getSendSms(String ip, String phone, String deviceId, String imei, String os, String osversion, String channel, String appVersion, String model) throws Exception;

    /**
     * 检测是否设置过密码
     * @param uid
     * @return
     */
    boolean checkPwd(Long uid);

    /**
     * 检测是否设置过二级密码
     * @param uid
     * @return
     */
    boolean checkSecondPwd(Long uid);

    void confirm(String phone,String smsCode) throws WebServiceException;

    int insertIdfaClick(String appid, String idfa, String idfamd5, String clicktime);

    void batchSyncToNetEase(Long start) throws WebServiceException;
}
