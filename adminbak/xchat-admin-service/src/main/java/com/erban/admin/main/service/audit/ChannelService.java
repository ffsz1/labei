package com.erban.admin.main.service.audit;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.BusinessException;
import com.erban.admin.main.dto.ChannelIpDTO;
import com.erban.admin.main.ret.DataMessage;
import com.erban.admin.main.ret.IpMessageRet;
import com.erban.admin.main.utils.AddressUtils;
import com.erban.admin.main.utils.HttpUtils;
import com.erban.main.model.*;
import com.erban.main.model.dto.RoomDTO;
import com.erban.main.mybatismapper.*;
import com.erban.main.service.room.RoomService;
import com.erban.main.service.user.UsersService;
import com.erban.main.wechat.util.StringUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.StringUtils;
import com.xchat.oauth2.service.service.JedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Author: alwyn
 * @Description: 审核渠道
 * @Date: 2018/10/19 17:46
 */
@Service
public class ChannelService {

    @Autowired
    private ChannelMapper channelMapper;
    @Autowired
    private ChannelRoomMapper channelRoomMapper;
    @Autowired
    private RoomService roomService;

    @Autowired
    private ChannelIconMapper channelIconMapper;

    @Autowired
    private ChannelBannerMapper channelBannerMapper;

    @Autowired
    private BannerMapper bannerMapper;

    @Autowired
    private IconMapper iconMapper;

    @Autowired
    private JedisService jedisService;

    @Autowired
    private ChannelUsersMapper channelUsersMapper;

    @Autowired
    private ChannelIpMapper channelIpMapper;

    @Autowired
    private UsersService usersService;

    /**
     * 分页查询渠道信息
     *
     * @param pageNun
     * @param pageSize
     * @return
     */
    public PageInfo page(int pageNun, int pageSize) {
        PageHelper.startPage(pageNun, pageSize);
        ChannelExample example = new ChannelExample();
        example.setOrderByClause("create_time desc");
        List<Channel> list = channelMapper.selectByExample(example);
        return new PageInfo(list);
    }

    /**
     * 更加id查询渠道信息
     *
     * @param id
     * @return
     */
    public Channel get(Integer id) {
        if (id == null) {
            return null;
        }
        return channelMapper.selectByPrimaryKey(id);
    }

    /**
     * 保存渠道信息
     *
     * @param channel
     * @return
     */
    public int save(Channel channel) {
        if (channel == null) {
            return 0;
        }
        if (channel.getId() == null) {
            ChannelExample example = new ChannelExample();
            example.createCriteria().andChannelEqualTo(channel.getChannel());
            int count = channelMapper.countByExample(example);
            if (count > 0) {
                throw new BusinessException("该渠道名称已存在");
            }
            channel.setCreateTime(new Date());
            return channelMapper.insertSelective(channel);
        } else {
            return channelMapper.updateByPrimaryKeySelective(channel);
        }
    }

    /**
     * 添加渠道房间
     *
     * @param id
     * @param uids
     */
    public void saveChannelRoom(Integer id, String uids) {
        if (id == null || StringUtils.isBlank(uids)) {
            throw new BusinessException("参数异常");
        }
        Channel channel = channelMapper.selectByPrimaryKey(id);
        if (channel == null) {
            throw new BusinessException("渠道信息不存在");
        }
        String[] arr = uids.split(",");
        Set<Long> set = Sets.newHashSet();
        List<Long> list = channelRoomMapper.listUidByChannel(id);
        set.addAll(list);
        for (int i = 0; i < arr.length; i++) {
            if (StringUtils.isNotBlank(arr[i])) {
                // 检查房间信息是否存在
                Room room = roomService.getRoomByDB(Long.valueOf(arr[i]));
                if (room != null) {
                    set.add(room.getUid());
                }
            }
        }
        // 先删除该渠道的房间信息, 防止重复
        channelRoomMapper.deleteByChannel(id);
        // 保持渠道房间信息
        channelRoomMapper.saveChannelRoom(id, set);
    }


    public void delChannelRoom(Integer id, Long uid) {
        if (id == null || uid == null) {
            return;
        }
        channelRoomMapper.deleteChannelRoom(id, uid);
    }
    /**
     * 删除渠道信息
     *
     * @param id
     * @return
     */
    public int delete(Integer id) {
        if (id == null) {
            return 0;
        }
        //
        return channelMapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据渠道ID 查询房间列表
     *
     * @param id
     * @param pageNun
     * @param pageSize
     * @return
     */
    public PageInfo<RoomDTO> pageRoom(Integer id, int pageNun, int pageSize) {
        if (id == null) {
            return new PageInfo(Lists.newArrayList());
        }
        PageHelper.startPage(pageNun, pageSize);
        List<RoomDTO> list = channelRoomMapper.listByChannelId(id);
        return new PageInfo(list);
    }

    public BusiResult saveChannelIcon(Integer id, Integer iconId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (id == null || iconId == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }

        Icon icon = iconMapper.selectByPrimaryKey(iconId);
        if(icon == null){
            return new BusiResult(BusiStatus.NOTEXISTS);
        }

        Channel channel = channelMapper.selectByPrimaryKey(id);
        if (channel == null) {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Set<Long> set = Sets.newHashSet();
        List<Long> list = channelIconMapper.listIconByChannel(id);
        if(list.size() > 0) {
            set.addAll(list);
        }
        set.add(iconId.longValue());
        // 先删除该渠道的房间信息, 防止重复
        ChannelIconExample channelIconExample = new ChannelIconExample();
        channelIconExample.createCriteria().andChannelIdEqualTo(id);
        channelIconMapper.deleteByExample(channelIconExample);
        channelIconMapper.saveChannelIcon(id, set);
        jedisService.del(RedisKey.channel_audit_icon.getKey());
        return busiResult;
    }

    public void delChannelIcon(Integer channelId, Long iconId) {
        if (channelId == null || iconId == null) {
            return;
        }
        ChannelIconExample channelIconExample = new ChannelIconExample();
        channelIconExample.createCriteria().andChannelIdEqualTo(channelId).andIconIdEqualTo(iconId);
        channelIconMapper.deleteByExample(channelIconExample);
    }

    public BusiResult saveChannelBanner(Integer id, Integer bannerId) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (id == null || bannerId == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Banner banner = bannerMapper.selectByPrimaryKey(bannerId);
        if(banner == null){
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Channel channel = channelMapper.selectByPrimaryKey(id);
        if (channel == null) {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Set<Long> set = Sets.newHashSet();
        List<Long> list = channelBannerMapper.listBannerIdByChannel(id);
        if(list.size() > 0) {
            set.addAll(list);
        }
        set.add(bannerId.longValue());
        // 先删除该渠道的房间信息, 防止重复
        ChannelBannerExample channelBannerExample = new ChannelBannerExample();
        channelBannerExample.createCriteria().andChannelIdEqualTo(id);
        channelBannerMapper.deleteByExample(channelBannerExample);
        channelBannerMapper.saveChannelBanner(id, set);
        jedisService.del(RedisKey.channel_audit_banner.getKey());
        return busiResult;
    }

    public void delChannelBanner(Integer channelId, Long bannerId) {
        if (channelId == null || bannerId == null) {
            return;
        }
        ChannelBannerExample channelBannerExample = new ChannelBannerExample();
        channelBannerExample.createCriteria().andChannelIdEqualTo(channelId).andBannerIdEqualTo(bannerId);
        channelBannerMapper.deleteByExample(channelBannerExample);
    }

    public PageInfo<Icon> pageIcon(Integer id, int pageNumber, int pageSize) {
        if (id == null) {
            return new PageInfo(Lists.newArrayList());
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Icon> list = channelIconMapper.listByChannelId(id);
        return new PageInfo(list);
    }

    public PageInfo<Banner> pageBanner(Integer id, int pageNumber, int pageSize) {
        if (id == null) {
            return new PageInfo(Lists.newArrayList());
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Banner> list = channelBannerMapper.listByChannelId(id);
        return new PageInfo(list);
    }

    public PageInfo<Users> pageUsers(Integer id, int pageNumber, int pageSize) {
        if (id == null) {
            return new PageInfo(Lists.newArrayList());
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<Users> list = channelUsersMapper.listByChannelId(id);
        return new PageInfo(list);
    }

    public BusiResult saveChannelUsers(Integer id, Long erbanNo) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (id == null || erbanNo == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Users users = usersService.getUsersByErBanNo(erbanNo);
        if(users == null){
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Channel channel = channelMapper.selectByPrimaryKey(id);
        if (channel == null) {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Set<Long> set = Sets.newHashSet();
        List<Long> list = channelUsersMapper.listUsersIdByChannel(id);
        if(list.size() > 0) {
            set.addAll(list);
        }
        set.add(users.getUid());
        // 先删除该渠道的房间信息, 防止重复
        ChannelUsersExample example = new ChannelUsersExample();
        example.createCriteria().andChannelIdEqualTo(id);
        channelUsersMapper.deleteByExample(example);
        channelUsersMapper.saveChannelUsers(id, set);
        jedisService.del(RedisKey.channel_audit_users.getKey());
        return busiResult;
    }

    public void delChannelUsers(Integer channelId, Long uid) {
        if (channelId == null || uid == null) {
            return;
        }
        ChannelUsersExample example = new ChannelUsersExample();
        example.createCriteria().andChannelIdEqualTo(channelId).andUidEqualTo(uid);
        channelUsersMapper.deleteByExample(example);
    }

    public PageInfo<ChannelIpDTO> pageIp(Integer id, int pageNumber, int pageSize) {
        if (id == null) {
            return new PageInfo(Lists.newArrayList());
        }
        PageHelper.startPage(pageNumber, pageSize);
        List<ChannelIp> list = channelIpMapper.listByChannelId(id);
        if(list == null){
            return new PageInfo<>(Lists.newArrayList());
        }
        List<ChannelIpDTO> channelIpDTOS = Lists.newArrayList();
        list.forEach(item ->{
            try {
                Gson gson = new Gson();
                ChannelIpDTO channelIpDTO = new ChannelIpDTO();
                String url = "https://api01.aliyun.venuscn.com/ip";
                String result = HttpUtils.get(url, "ip=" + item.getIp(), "37774f940dee4b45b29f744fd4e58869");
                IpMessageRet ret = gson.fromJson(result, IpMessageRet.class);
                if(ret.getRet() == 200){
                    DataMessage dataMessage =  ret.getData();
                    channelIpDTO.setIp(item.getIp());
                    channelIpDTO.setCountry(dataMessage.getCountry());
                    channelIpDTO.setRegion(dataMessage.getRegion());
                    channelIpDTO.setCity(dataMessage.getCity());
                    channelIpDTO.setIsp(dataMessage.getIsp());
                    channelIpDTO.setArea(dataMessage.getArea());
                    channelIpDTO.setChannelId(item.getChannelId());
                } else{
                    channelIpDTO.setIp(item.getIp());
                    channelIpDTO.setChannelId(item.getChannelId());
                }
                channelIpDTOS.add(channelIpDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }


        });
        return new PageInfo(channelIpDTOS);
    }

    public BusiResult saveChannelIp(Integer id, String ip) {
        BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
        if (id == null || ip == null) {
            return new BusiResult(BusiStatus.PARAMERROR);
        }
        Channel channel = channelMapper.selectByPrimaryKey(id);
        if (channel == null) {
            return new BusiResult(BusiStatus.NOTEXISTS);
        }
        Set<String> set = Sets.newHashSet();
        List<String> list = channelIpMapper.listIpByChannel(id);
        if(list.size() > 0) {
            set.addAll(list);
        }
        set.add(ip);
        // 先删除该渠道的房间信息, 防止重复
        ChannelIpExample example = new ChannelIpExample();
        example.createCriteria().andChannelIdEqualTo(id);
        channelIpMapper.deleteByExample(example);
        channelIpMapper.saveChannelIp(id, set);
        jedisService.del(RedisKey.channel_audit_ip.getKey());
        return busiResult;
    }

    public void delChannelIp(Integer channelId, String ip) {
        if (channelId == null || ip == null) {
            return;
        }
        ChannelIpExample example = new ChannelIpExample();
        example.createCriteria().andChannelIdEqualTo(channelId).andIpEqualTo(ip);
        channelIpMapper.deleteByExample(example);
    }
}
