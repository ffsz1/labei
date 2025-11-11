package com.juxiao.xchat.service.api.item;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.item.vo.HeadwearVO;

import java.util.List;

public interface HeadwearService {

    /**
     * 加载用户的头饰
     *
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    List<HeadwearVO> listHeadwears(Long uid, Integer pageNum, Integer pageSize) throws WebServiceException;

    /**
     * 获取商城里的头饰
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    List<HeadwearVO> listMall(Long uid, Integer pageNum, Integer pageSize) throws WebServiceException;

    /**
     * 获取商城里的头饰
     * @param uid
     * @param pageNum
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    List<HeadwearVO> listUserHeadwear(Long uid, Long queryUid, Integer pageNum, Integer pageSize) throws WebServiceException;


    void purse(Long uid, Integer headwearId, Integer type) throws WebServiceException;

    void use(Long uid, Integer headwearId) throws WebServiceException;

    /**
     * 赠送头饰
     * @param uid 赠送的用户
     * @param headwearId 头饰ID
     * @param targetUid 接收的用户
     */
    void give(Long uid, Integer headwearId, Long targetUid) throws WebServiceException;

}
