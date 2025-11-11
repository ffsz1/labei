//package com.juxiao.xchat.service.api.sysconf.impl;
//
//import com.google.common.collect.Lists;
//import com.juxiao.xchat.base.utils.HttpServletUtils;
//import com.juxiao.xchat.base.utils.ListUtil;
//import com.juxiao.xchat.base.web.WebServiceException;
//import com.juxiao.xchat.dao.sysconf.dto.ChannelDTO;
//import com.juxiao.xchat.manager.common.level.LevelManager;
//import com.juxiao.xchat.manager.common.room.vo.RoomVo;
//import com.juxiao.xchat.service.api.sysconf.ChannelService;
//import com.juxiao.xchat.service.api.sysconf.conf.VivoConfig;
//import com.juxiao.xchat.service.api.sysconf.enumeration.ChannelEnum;
//import com.juxiao.xchat.service.api.sysconf.vo.ChannelAuditVO;
//import com.juxiao.xchat.service.api.sysconf.vo.HomeV2Vo;
//import com.juxiao.xchat.service.api.sysconf.vo.IndexParam;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.Collections;
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * @Auther: alwyn
// * @Description: vivo 首页的业务
// * @Date: 2018/10/15 19:48
// */
//@Service("homeVivoService")
//public class HomeVivoServiceImpl extends BaseHomeServiceImpl {
//
//    @Autowired
//    private LevelManager levelManager;
//    @Autowired
//    private VivoConfig vivoConfig;
//    @Autowired
//    private ChannelService channelService;
//
//    @Override
//    public HomeV2Vo index(IndexParam indexParam, String clientIp, ChannelEnum channel) throws WebServiceException {
//        return null;
//    }
//
//    @Override
//    public List<RoomVo> findRoomByTag(ChannelEnum channel, Long uid, Long tagId, Integer pageNum, Integer pageSize, String os, String app, String appVersion, String clientIp) throws WebServiceException {
//        return null;
//    }
//
//    @Override
//    public List<RoomVo> getHomeHotRoom(ChannelEnum channel, Long uid, String appVersion, Integer pageNum, Integer pageSize) {
//        return null;
//    }
//
//    @Override
//    public HomeV2Vo index(IndexParam indexParam, String clientIp) throws WebServiceException {
//        if(indexParam == null || indexParam.getUid() == null) {
//            return super.index(indexParam, clientIp);
//        }
//        ChannelDTO channelDTO = channelService.getByName(ChannelEnum.vivo);
//        if (channelDTO == null) {
//            return super.index(indexParam, clientIp);
//        }
//        if (!Boolean.TRUE.equals(channelDTO.getAuditOption()) ) {
//            return super.index(indexParam, clientIp);
//        }
//        long time = System.currentTimeMillis();
//        if (channelDTO.getBeginTime() != null && channelDTO.getBeginTime().getTime() < time && channelDTO.getEndTime() != null && channelDTO.getEndTime().getTime() > time) {
//            // 在审核期间内
//            int level = levelManager.getUserExperienceLevelSeq(indexParam.getUid());
//            if (channelDTO.getLeftLevel() != null && level > channelDTO.getLeftLevel()) {
//                // 该用户等级大于vivo的审核等级
//                return super.index(indexParam, clientIp);
//            }
//            HomeV2Vo v2Vo = new HomeV2Vo();
//            if (indexParam.getPageNum() == 1) {
//                // 返回审核期间的数据
//                v2Vo.setBanners(vivoConfig.getBanners());
//                v2Vo.setRoomTagList(Lists.newArrayList());
//                v2Vo.setListGreenRoom(Lists.newArrayList());
//                v2Vo.setHotRooms(Lists.newArrayList());
//                v2Vo.setHomeIcons(Lists.newArrayList());
//            }
//            List<Long> uidList = channelService.listByChannelId(channelDTO.getId());
//            if (uidList == null) {
//                v2Vo.setListRoom(Lists.newArrayList());
//            } else {
//                uidList = ListUtil.page(uidList, indexParam.getPageNum(), indexParam.getPageSize());
//                List<RoomVo> list = super.listRoomByUid(uidList);
//                Collections.sort(list);
//                v2Vo.setListRoom(list);
//            }
//            v2Vo.setViewType(ChannelEnum.vivo.getViewType());
//            return v2Vo;
//        } else {
//            return super.index(indexParam, clientIp);
//        }
//    }
//
//    @Override
//    public List<RoomVo> findRoomByTag(Long uid, Long tagId, Integer pageNum, Integer pageSize, String os,
//                                      String app, String appVersion, String clientIp) throws WebServiceException {
//        if (tagId == null) {
//            return Lists.newArrayList();
//        }
//        ChannelDTO channelDTO = channelService.getByName(ChannelEnum.vivo);
//        if (channelDTO == null) {
//            return super.findRoomByTag(uid, tagId, pageNum, pageSize, os, app, appVersion, clientIp);
//        }
//        if (!Boolean.TRUE.equals(channelDTO.getAuditOption())) {
//            return super.findRoomByTag(uid, tagId, pageNum, pageSize, os, app, appVersion, clientIp);
//        }
//        long time = System.currentTimeMillis();
//        if (channelDTO.getBeginTime() != null && channelDTO.getBeginTime().getTime() < time &&
//                channelDTO.getEndTime() != null && channelDTO.getEndTime().getTime() > time) {
//            // 在审核期间内
//            int level = levelManager.getUserExperienceLevelSeq(uid);
//            if (channelDTO.getLeftLevel() != null && level > channelDTO.getLeftLevel()) {
//                // 该用户等级大于vivo的审核等级
//                return super.findRoomByTag(uid, tagId, pageNum, pageSize, os, app, appVersion, clientIp);
//            }
//            List<Long> uidList = channelService.listByChannelId(channelDTO.getId());
//            List<RoomVo> result = Lists.newArrayList();
//            if (uidList != null) {
//                result = super.listRoomByUid(uidList);
//            }
//            // 过滤房间标签
//            return result.stream().filter((RoomVo vo) -> tagId.longValue() == vo.getTagId()).collect(Collectors.toList());
//        }
//        return super.findRoomByTag(uid, tagId, pageNum, pageSize, os, app, appVersion, clientIp);
//    }
//
//
//    @Override
//    public List<RoomVo> getHomeHotRoom(Long uid, String appVersion, Integer pageNum, Integer pageSize) {
//        ChannelAuditVO auditVO = channelService.checkAudit(ChannelEnum.vivo, appVersion, uid);
//        if (auditVO != null && auditVO.isAudit()) {
//            return Lists.newArrayList();
//        }
//        return super.getHomeHotRoom(uid,appVersion, pageNum, pageSize);
//    }
//}
