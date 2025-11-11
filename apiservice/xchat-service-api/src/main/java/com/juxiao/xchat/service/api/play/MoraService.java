package com.juxiao.xchat.service.api.play;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.service.api.play.vo.*;

import java.util.List;

/**
 * @author chris
 * @Title:
 * @date 2019-06-01
 * @time 22:10
 */
public interface MoraService {

    /**
     * 获取配置状态
     * @param uid  uid
     * @param roomId 房间ID
     * @return int
     * @throws  WebServiceException WebServiceException
     */
    int getState(Long uid,Long roomId) throws WebServiceException;


    /**
     * 获取猜拳发起记录
     * @param uid uid
     * @param roomId roomId
     * @return MoraVO
     * @throws WebServiceException WebServiceException
     */
    List<MoraMessageVO> getMoraRecord(Long uid, Long roomId) throws WebServiceException;


    /**
     * 获取猜拳信息
     * @param uid uid
     * @param probability 概率(1.高 2.中 3.低) 默认低
     * @param roomId  房间ID
     * @return MoraInfoVO
     * @throws  WebServiceException WebServiceException
     */
    MoraInfoVO getMoraInfo(Long uid, Long roomId,Integer probability) throws WebServiceException;

    /**
     * 确认发起Pk
     * @param uid uid
     * @param roomId 房间ID
     * @param probability 概率(1.高 2.中 3.低)
     * @param choose  选择(1.剪刀 2.石头 3.布)
     * @param giftId 礼物ID
     * @param giftNum 数量
     * @throws  WebServiceException WebServiceException
     * @return String
     */
    String confirmPk(Long uid,Long roomId,Integer probability,Integer choose,Integer giftId,Integer giftNum) throws WebServiceException;


    /**
     * 加入参与PK
     * @param uid uid
     * @param recordId 发起记录ID
     * @return JoinInfoVO
     * @throws  WebServiceException WebServiceException
     */
    JoinInfoVO joinPk(Long uid,String recordId) throws WebServiceException;


    /**
     * 确认加入PK
     * @param uid uid
     * @param recordId 发起记录ID
     * @param choose 选择(1.剪刀 2.石头 3.布)
     * @throws WebServiceException WebServiceException
     */
     void confirmJoinPk(Long uid,String recordId,Integer choose)throws WebServiceException;

    /**
     * 猜拳记录
     * @param uid uid
     * @param current  current
     * @param pageSize  pageSize
     * @return MoraRecordVO
     * @throws  WebServiceException WebServiceException
     */
    List<MoraRecordVO> getMoraRecord(Long uid, Integer current, Integer pageSize) throws WebServiceException;


    /**
     * 获取概率
     * @param uid uid
     * @param roomId roomId
     * @return List<ProbabilityVO>
     */
    List<ProbabilityVO> getProbability(Long uid,Long roomId) throws WebServiceException;
}
