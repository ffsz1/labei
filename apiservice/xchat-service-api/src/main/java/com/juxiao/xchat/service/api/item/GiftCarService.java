package com.juxiao.xchat.service.api.item;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.item.vo.GiftCarVO;

import java.util.List;

/**
 * @class: GiftCarService.java
 * @author: chenjunsheng
 * @date 2018/6/20
 */
public interface GiftCarService {

    /**
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    List<GiftCarVO> listGiftCars(Long uid, Integer pageNum, Integer pageSize, String os, String app, String appVersion, String ip) throws WebServiceException;

    /**
     * 座驾商城
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    List<GiftCarVO> listMall(Long uid, Integer pageNum, Integer pageSize) throws WebServiceException;

    /**
     * 用户拥有的座驾
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    List<GiftCarVO> listUserGiftCar(Long uid, Long queryUid, Integer pageNum, Integer pageSize) throws WebServiceException;


    /**
     * @param uid
     * @param carId
     * @param type
     */
    void purse(Long uid, Integer carId, Integer type) throws WebServiceException;

    /**
     * @param uid
     * @param carId
     * @throws WebServiceException
     */
    void use(Long uid, Integer carId) throws WebServiceException;

    /**
     * 赠送座驾
     *
     * @param uid       赠送的用户
     * @param carId     座驾ID
     * @param targetUid 接收的用户
     */
    void give(Long uid, Integer carId, Long targetUid) throws WebServiceException;
}
