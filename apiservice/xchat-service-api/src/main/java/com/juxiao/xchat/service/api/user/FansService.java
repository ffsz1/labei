package com.juxiao.xchat.service.api.user;

import com.juxiao.xchat.base.web.WebServiceException;
import com.juxiao.xchat.dao.user.dto.FansFollowDTO;

import java.util.List;
import java.util.Map;

/**
 * @class: FansService.java
 * @author: chenjunsheng
 * @date 2018/6/12
 */
public interface FansService {

    /**
     * 关注某个用户
     *
     * @param likeUid
     * @param likeUid
     * @throws WebServiceException
     */
    void likeSomeBody(Long likeUid, Long likedUid) throws Exception;

    /**
     * 取消关注某个用户
     *
     * @param likeUid
     * @param cancelLikeUid
     * @throws WebServiceException
     */
    void cancelLikeSomeBody(Long likeUid, Long cancelLikeUid) throws Exception;

    /**
     * 查询uid是否喜欢checkUid
     *
     * @param uid
     * @param isLikeUid
     * @return
     */
    boolean checkUserLike(Long uid, Long isLikeUid) throws WebServiceException;

    /**
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     */
    List<FansFollowDTO> listFollowing(Long uid, Integer pageNo, Integer pageSize) throws WebServiceException;

    /**
     * @param uid
     * @param pageNo
     * @param pageSize
     * @return
     * @throws WebServiceException
     */
    Map<String, Object> listFans(Long uid, Integer pageNo, Integer pageSize) throws WebServiceException;

    /**
     * 查询所有的粉丝
     * @param uid 用户ID
     * @return
     * @throws WebServiceException
     */
    List<FansFollowDTO> listFans(Long uid) throws WebServiceException;
}
