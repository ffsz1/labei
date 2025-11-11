package com.erban.admin.main.service.guild;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.erban.admin.main.service.base.BaseService;
import com.erban.admin.main.vo.UsersVo;
import com.erban.main.config.SystemConfig;
import com.erban.main.model.Room;
import com.erban.main.mybatismapper.RoomMapper;
import com.erban.main.param.neteasepush.NeteasePushParam;
import com.erban.main.param.neteasepush.NeteaseSendMsgBatchParam;
import com.erban.main.param.neteasepush.NeteaseSendMsgParam;
import com.erban.main.service.SendSysMsgService;
import com.erban.admin.main.mapper.GuildHallApplyRecordMapper;
import com.erban.admin.main.mapper.GuildHallMapper;
import com.erban.admin.main.mapper.GuildHallMemberMapper;
import com.erban.admin.main.mapper.GuildMapper;
import com.erban.admin.main.mapper.UserMapperExpand;
import com.erban.admin.main.model.Guild;
import com.erban.admin.main.model.GuildExample;
import com.erban.admin.main.model.GuildHall;
import com.erban.admin.main.model.GuildHallApplyRecord;
import com.erban.admin.main.model.GuildHallApplyRecordExample;
import com.erban.admin.main.model.GuildHallExample;
import com.erban.admin.main.model.GuildHallMember;
import com.erban.admin.main.model.GuildHallMemberExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xchat.common.constant.Attach;
import com.xchat.common.constant.Constant;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.CommonUtil;
import com.xchat.oauth2.service.service.JedisService;

/**
 * 后台管理--公会管理
 */
@Service
public class GuildService extends BaseService {

	@Autowired
	private GuildMapper guildMapper;
	@Autowired
	private JedisService jedisService;
	@Autowired
	private UserMapperExpand userMapperExpand;
	@Autowired
	private GuildHallMapper guildHallMapper;
	@Autowired
	private GuildHallMemberMapper guildHallMemberMapper;
	@Autowired
	private RoomMapper roomMapper;
	@Autowired
	private SendSysMsgService sendSysMsgService;
	@Autowired
	private GuildHallApplyRecordMapper guildHallApplyRecordMapper;

	/**
	 * 公会列表
	 * 
	 * @param guildNo      公会id
	 * @param presidentUid 公会长id
	 */
	public PageInfo<Guild> getGuildList(Integer pageNumber, Integer pageSize, String guildNo, Long erbanNo) {
		PageHelper.startPage(pageNumber, pageSize);
		List<Guild> guildList = guildMapper.getGuildList(guildNo, erbanNo);
		return new PageInfo<Guild>(guildList);
	}

	/**
	 * 公会详情
	 * 
	 * @param id
	 * @return
	 */
	public Guild getOneGuildById(Long id) {
		Guild guild = guildMapper.findNotIsDelById(id);
		if (guild != null) {
			UsersVo users = userMapperExpand.selectUidByUsers(guild.getPresidentUid());
			guild.setNick(users.getNick());
			guild.setErbanNo(users.getErbanNo());
			guild.setIsDel(false);
		}
		return guild;
	}

	public Guild getOneGuildByUid(Long uid) {
		GuildExample guildExample = new GuildExample();
		GuildExample.Criteria criteria = guildExample.createCriteria();
		criteria.andPresidentUidEqualTo(uid);
		criteria.andIsDelEqualTo(false);
		List<Guild> guildList = guildMapper.selectByExample(guildExample);
		if(!guildList.isEmpty()) {
			return guildList.get(0);
		}
		return null;
	}
	public GuildHallMember getOneMemberByUid(Long uid) {
		GuildHallMemberExample guildHallMemberExample=new GuildHallMemberExample();
		GuildHallMemberExample.Criteria criteria = guildHallMemberExample.createCriteria();
		criteria.andUidEqualTo(uid);
		criteria.andIsDelEqualTo(false);
		List<GuildHallMember> memberList=guildHallMemberMapper.selectByExample(guildHallMemberExample);
		if(!memberList.isEmpty()) {
			return memberList.get(0);
		}
		return null;
	}
	public GuildHall getOneHallByUid(Long uid) {
		Room room = roomMapper.selectByPrimaryKey(uid);
		GuildHall hall=null;
		if (room != null) {
			hall = guildHallMapper.selectByRoomId(room.getRoomId());
		}
		return hall;
	}
	/**
	  * 添加编辑公会
	 * 
	 * @param guild
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public BusiResult saveGuild(Guild guild) {
		BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
		UsersVo users = userMapperExpand.selectUidByErbanNo(guild.getErbanNo());
		if(users!=null) {
			guild.setPresidentUid(users.getUid());
		}else {
			return new BusiResult(BusiStatus.NOTEXISTS);
		}
		Long uid=guild.getPresidentUid();
		Guild old=getOneGuildByUid(uid);
		GuildHall hall=getOneHallByUid(uid);
		GuildHallMember member=getOneMemberByUid(uid);
		
		if (guild.getId() == null) {
			if(old!=null) {
				return new BusiResult(BusiStatus.GUILD_USER_ERBANNO_IS_EXITIST);
			}
			if(hall!=null||member!=null) {
				return new BusiResult(BusiStatus.GUILD_USER_IS_EXITIST);
			}
			String guildNo = CommonUtil.getRandomNumStr(3);
			guild.setGuildNo(guildNo);
			guild.setIsDel(false);
			guild.setCreateTime(new Date());
			guildMapper.insert(guild);
			GuildHallMember gMember = new GuildHallMember();
			gMember.setGuildId(guild.getId());
			gMember.setHallId(null);
			gMember.setUid(uid);
			gMember.setIsDel(false);
			gMember.setMemberType(0);
			gMember.setCreateTime(new Date());
			guildHallMemberMapper.insert(gMember);
			sendMsg(uid, "你已成为"+guild.getName()+"（公会ID："+guildNo+"）公会长");
			
		} else {
			if(old!=null) {
				if(old.getId().compareTo(guild.getId())!=0) {
					return new BusiResult(BusiStatus.GUILD_USER_ERBANNO_IS_EXITIST);
				}
			}
			if(hall!=null) {
				if(hall.getGuildId().compareTo(guild.getId())!=0) {
					return new BusiResult(BusiStatus.GUILD_USER_IS_EXITIST);
				}
			}
			if(member!=null) {
				if(member.getGuildId().compareTo(guild.getId())!=0) {
					return new BusiResult(BusiStatus.GUILD_USER_IS_EXITIST);
				}
			}
			Guild g=getOneGuildById(guild.getId());
			guild.setUpdateTime(new Date());
			guildMapper.updateByPrimaryKeySelective(guild);
			if(member==null) {
				GuildHallMember gMember = new GuildHallMember();
				gMember.setGuildId(guild.getId());
				gMember.setHallId(null);
				gMember.setUid(uid);
				gMember.setIsDel(false);
				gMember.setMemberType(0);
				gMember.setCreateTime(new Date());
				guildHallMemberMapper.insert(gMember);
				
			}else {
				member.setMemberType(0);
				member.setUpdateTime(new Date());
				guildHallMemberMapper.updateByPrimaryKeySelective(member);
			}
			if(g.getPresidentUid().compareTo(guild.getPresidentUid())!=0) {
				GuildHallMember oldMember=getOneMemberByUid(g.getPresidentUid());
				if(oldMember!=null) {
					GuildHall h=getOneHallByUid(g.getPresidentUid());
					if(h!=null) {
						oldMember.setMemberType(1);
						oldMember.setUpdateTime(new Date());
						guildHallMemberMapper.updateByPrimaryKeySelective(oldMember);//修改原公会长成员类型
					}else {
						guildHallMemberMapper.isDelMemberById(oldMember.getId());
					}
				}
				sendMsg(g.getPresidentUid(), guild.getName()+"公会已转移给"+guild.getErbanNo());
			}
			jedisService.hdel(RedisKey.guild.getKey(), guild.getId().toString());
		}
		jedisService.del(RedisKey.guild_list.getKey());
		jedisService.del(RedisKey.guild_recommend.getKey("1_10"));
		jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), guild.getPresidentUid().toString());
		jedisService.hdel(RedisKey.guild_member_exist.getKey(), guild.getPresidentUid().toString());
		return busiResult;
	}

	/**
	 * 解散公会
	 * 
	 * @param guildId
	 */
	@Transactional(rollbackFor = Exception.class)
	public void delGuild(Long guildId) {
		Set<String> uids=new HashSet<String>();
		Guild guild = getOneGuildById(guildId);
		guild.setIsDel(true);
		guild.setUpdateTime(new Date());
		guildMapper.updateByPrimaryKeySelective(guild);
		uids.add(guild.getPresidentUid().toString());

		GuildHallExample hallExample = new GuildHallExample();
		GuildHallExample.Criteria hallCriteria = hallExample.createCriteria();
		hallCriteria.andGuildIdEqualTo(guildId);
		hallCriteria.andIsDelEqualTo(false);
		List<GuildHall> guildHallList = guildHallMapper.selectByExample(hallExample);
		guildHallList.stream().forEach(hall -> {
			Room room = roomMapper.selectByRoomId(hall.getRoomId());
			if(room!=null) {
				uids.add(room.getUid().toString());
				jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), room.getUid().toString());
			}
			jedisService.hdel(RedisKey.guild_hall.getKey(), hall.getId().toString());
			jedisService.del(RedisKey.guild_hall_member_list.getKey(hall.getId().toString()));
			jedisService.hdel(RedisKey.guild_member_exist.getKey(), room.getUid().toString());
			jedisService.del(RedisKey.guild_hall_apply_record_list.getKey(hall.getId()+"_1_10"));
			
		});
		guildHallMapper.isDelHallByGuildId(guildId);

		GuildHallMemberExample memberExample = new GuildHallMemberExample();
		GuildHallMemberExample.Criteria memberCriteria = memberExample.createCriteria();
		memberCriteria.andGuildIdEqualTo(guildId);
		memberCriteria.andIsDelEqualTo(false);
		List<GuildHallMember> guildHallMemberList = guildHallMemberMapper.selectByExample(memberExample);
		guildHallMemberList.stream().forEach(m -> {
			uids.add(m.getUid().toString());
			jedisService.del(RedisKey.guild_member.getKey(m.getUid().toString()));
			jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), m.getUid().toString());
			jedisService.hdel(RedisKey.guild_member_exist.getKey(), m.getUid().toString());
		});
		
		guildHallMemberMapper.isDelMemberByGuildId(guildId);
		if(!uids.isEmpty()) {
			List<String> uidList=new ArrayList<String>(uids);
			sendBatchMsgMsg(uidList, guild.getName()+"(公会id："+guild.getGuildNo()+")已解散，你已被移出公会");
		}
		jedisService.hdel(RedisKey.guild.getKey(), guildId.toString());
		jedisService.del(RedisKey.guild_list.getKey());
		jedisService.del(RedisKey.guild_recommend.getKey("1_10"));
		jedisService.del(RedisKey.guild_hall_list.getKey(guildId.toString()));
		jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), guild.getPresidentUid().toString());
		jedisService.del(RedisKey.guild_member_list.getKey(guildId.toString()));
		jedisService.hdel(RedisKey.guild_member_exist.getKey(), guild.getPresidentUid().toString());
		jedisService.del(RedisKey.guild_apply_record_list.getKey(guildId+"_1_10"));
		
		
	}

	/**
	 * 厅列表
	 * 
	 * @param pageNumber
	 * @param pageSize
	 * @param guildId
	 * @return
	 */
	public PageInfo<GuildHall> getGuildHallList(Integer pageNumber, Integer pageSize, Long guildId) {
		PageHelper.startPage(pageNumber, pageSize);
		List<GuildHall> guildHallList = guildHallMapper.findGuildHallList(guildId);
		return new PageInfo<>(guildHallList);
	}
	
	public GuildHall getOneGuildHallByRoomId(Long roomId) {
		GuildHall hall=guildHallMapper.selectByRoomId(roomId);
		if(hall!=null) {
			Room room = roomMapper.selectByRoomId(roomId);
			hall.setHallUid(room!=null?room.getUid():null);
			hall.setRoomName(room!=null?room.getTitle():"");
		}
		return hall;
	}
	
	public GuildHall getOneGuildHallByHallId(Long hallId) {
		GuildHall hall=guildHallMapper.selectById(hallId);
		if(hall!=null) {
			Room room = roomMapper.selectByRoomId(hall.getRoomId());
			hall.setHallUid(room!=null?room.getUid():null);
			hall.setRoomName(room!=null?room.getTitle():"");
		}
		return hall;
	}

	/**
	 * 新增厅
	 * 
	 * @param guildId
	 * @param erbanNo
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@Transactional(rollbackFor = Exception.class)
	public BusiResult addHall(Long guildId, Long erbanNo) {
		BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
		try {
			Guild guildF=getOneGuildById(guildId);
			if(guildF==null) {
				logger.warn("公会Id:{}数据不存在",guildId);
				return new BusiResult(BusiStatus.GUILD_GUILDID_NOTEXIT);
			}
			UsersVo users = userMapperExpand.selectUidByErbanNo(erbanNo);
			if(users == null) {
				logger.warn("房间Id数据不存在");
				return new BusiResult(BusiStatus.GUILD_ROOMID_NOTEXIT);
			}
			Guild old=getOneGuildByUid(users.getUid());
			if(old!=null) {
				if(old.getId().compareTo(guildId)!=0) {
					return new BusiResult(BusiStatus.GUILD_HALL_USER_IS_EXITIST);
				}
			}
			GuildHallMember member=getOneMemberByUid(users.getUid());
			if(member!=null) {
				if(member.getGuildId().compareTo(guildId)!=0||member.getHallId()!=null) {
					return new BusiResult(BusiStatus.GUILD_HALL_USER_IS_EXITIST);
				}
			}
			Room room = roomMapper.selectByPrimaryKey(users.getUid());
			if (room != null) {
				GuildHall hall = guildHallMapper.selectByRoomId(room.getRoomId());
				if (hall != null) {// 该厅已加入其他公会
					return new BusiResult(BusiStatus.GUILD_HALL_USER_IS_EXITIST);
				} else {
					hall = new GuildHall();
					hall.setGuildId(guildId);
					hall.setRoomId(room.getRoomId());
					hall.setMemberCount(1);
					hall.setCreateTime(new Date());
					hall.setIsDel(false);
					guildHallMapper.insert(hall);
					if(member!=null) {
						if(guildF.getPresidentUid().compareTo(member.getUid())==0) {//用户是会长
							member.setHallId(hall.getId());
							member.setMemberType(0);
							member.setIsDel(false);
							member.setUpdateTime(new Date());
							guildHallMemberMapper.updateByPrimaryKeySelective(member);
						}
					}else {
						GuildHallMember gMember = new GuildHallMember();
						gMember.setGuildId(guildId);
						gMember.setHallId(hall.getId());
						gMember.setUid(users.getUid());
						gMember.setIsDel(false);
						gMember.setMemberType(1);
						gMember.setCreateTime(new Date());
						guildHallMemberMapper.insert(gMember);
					}
					
					if(users.getUid().compareTo(guildF.getPresidentUid())!=0) {//厅主与会长不是同一人
						sendMsg(guildF.getPresidentUid(), room.getTitle()+"（房间ID："+erbanNo +"）已加入"+guildF.getName());//发给公会长
					}
					sendMsg(users.getUid(), room.getTitle()+"（房间ID："+erbanNo +"）已加入"+guildF.getName());//发给厅主
					busiResult.setMessage("添加成功");
					jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), room.getUid().toString());
					jedisService.del(RedisKey.guild_member_list.getKey(guildId.toString()));
					jedisService.hdel(RedisKey.guild_member_exist.getKey(), room.getUid().toString());
					jedisService.del(RedisKey.guild_hall_apply_record_list.getKey(hall.getId()+"_1_10"));
					jedisService.del(RedisKey.guild_apply_record_list.getKey(guildId+"_1_10"));
					jedisService.hdel(RedisKey.guild.getKey(), guildId.toString());
				}
			} else {
				logger.warn("房间Id数据不存在");
				busiResult.setCode(2);
				busiResult.setMessage("房间Id数据不存在");
			}

		} catch (Exception e) {
			logger.error("addHall error", e);
			busiResult.setCode(500);
			busiResult.setMessage("添加失败");
		}
		return busiResult;
		
	}

	
	/**
	  * 删除厅
	 * 
	 * @param hallId
	 */
	@SuppressWarnings({ "rawtypes"})
	@Transactional(rollbackFor = Exception.class)
	public BusiResult delHall(Long hallId) {
		BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
		try {
			Set<String> uids=new HashSet<String>();
			GuildHall hall=getOneGuildHallByHallId(hallId);
			if(hall!=null) {
				guildHallMapper.isDelHallById(hallId);
				Guild guild = getOneGuildById(hall.getGuildId());
				if(guild!=null) {
					sendMsg(hall.getHallUid(), "你的厅"+hall.getRoomName()+"已被移出"+guild.getName()+"公会");
				}
				
				GuildHallMemberExample memberExample = new GuildHallMemberExample();
				GuildHallMemberExample.Criteria memberCriteria = memberExample.createCriteria();
				memberCriteria.andHallIdEqualTo(hallId);
				memberCriteria.andIsDelEqualTo(false);
				List<GuildHallMember> guildHallMemberList = guildHallMemberMapper.selectByExample(memberExample);
				guildHallMemberList.stream().forEach(m -> {
					if(hall.getHallUid().compareTo(m.getUid())!=0) {
						uids.add(m.getUid().toString());
					}
					jedisService.del(RedisKey.guild_member.getKey(m.getUid().toString()));
					jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), m.getUid().toString());
					jedisService.hdel(RedisKey.guild_member_exist.getKey(), m.getUid().toString());
					if(guild!=null&&guild.getPresidentUid().compareTo(m.getUid())==0) {//厅成员是会长
						m.setHallId(null);
						m.setMemberType(0);
						m.setUpdateTime(new Date());
						guildHallMemberMapper.updateByPrimaryKey(m);
					}else {
						guildHallMemberMapper.isDelMemberById(m.getId());
					}
				});
				if(!uids.isEmpty()) {
					List<String> uidList=new ArrayList<String>(uids);
					sendBatchMsgMsg(uidList, "你所在的厅"+hall.getRoomName()+"已被移出"+guild.getName()+"公会，你已被移出公会");
				}
				jedisService.hdel(RedisKey.guild_hall.getKey(), hallId.toString());
				jedisService.del(RedisKey.guild_hall_list.getKey(hall.getGuildId().toString()));
				jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), hall.getHallUid().toString());
				jedisService.del(RedisKey.guild_member_list.getKey(hall.getGuildId().toString()));
				jedisService.del(RedisKey.guild_hall_member_list.getKey(hallId.toString()));
				jedisService.hdel(RedisKey.guild_member_exist.getKey(), hall.getHallUid().toString());
				jedisService.del(RedisKey.guild_hall_apply_record_list.getKey(hallId+"_1_10"));
				jedisService.del(RedisKey.guild_apply_record_list.getKey(hall.getGuildId()+"_1_10"));
				jedisService.hdel(RedisKey.guild.getKey(), hall.getGuildId().toString());
			}
		} catch (Exception e) {
			logger.error("delHall error", e);
			busiResult.setCode(500);
			busiResult.setMessage("删除失败");
		}
		return busiResult;
	}
	
	
	
	/**
	 * 成员列表
	 * @param pageNumber
	 * @param pageSize
	 * @param guildId
	 * @param hallId
	 * @return
	 */
	public PageInfo<GuildHallMember> getMemberList(Integer pageNumber, Integer pageSize, Long guildId,Long hallId,String type) {
		PageHelper.startPage(pageNumber, pageSize);
		if(type.equals("guild")) {
			List<GuildHallMember> guildHallMemberList = guildHallMemberMapper.findGuildMember(guildId);
			return new PageInfo<>(guildHallMemberList);
		}else {
			List<GuildHallMember> guildHallMemberList = guildHallMemberMapper.findHallMember(guildId, hallId);
			return new PageInfo<>(guildHallMemberList);
		}
	}
	
	
	/**
	  * 新增成员
	 * @param guildId
	 * @param hallId
	 * @param uid
	 * @return
	 */
	@SuppressWarnings({ "rawtypes"})
	@Transactional(rollbackFor = Exception.class)
	public BusiResult addMember(Long guildId,Long hallId, Long erbanNo) {
		BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
		try {
			Set<String> uids=new HashSet<String>();
			UsersVo users = userMapperExpand.selectUidByErbanNo(erbanNo);
			if(users==null) {
				logger.warn("用户erbanNo：{}数据不存在",erbanNo);
				return new BusiResult(BusiStatus.GUILD_USERID_NOTEXIT);
			}
			Long uid=users.getUid();
			Guild guild=getOneGuildById(guildId);
			if(guild==null) {
				logger.warn("公会Id：{}数据不存在",guildId);
				return new BusiResult(BusiStatus.GUILD_GUILDID_NOTEXIT);
			}
			uids.add(guild.getPresidentUid().toString());
			GuildHall hall=getOneGuildHallByHallId(hallId);
			if(hall==null) {
				logger.warn("厅Id：{}数据不存在",hallId);
				return new BusiResult(BusiStatus.GUILD_HALLID_NOTEXIT);
			}
			uids.add(hall.getHallUid().toString());
			Guild old=getOneGuildByUid(users.getUid());
			if(old!=null) {
				if(old.getId().compareTo(guildId)!=0) {
					return new BusiResult(BusiStatus.GUILD_HALL_USER_IS_EXITIST);
				}else {
					return new BusiResult(BusiStatus.GUILD_USER_ERBANNO_EXITIST_WARN);
				} 
			}
			Room room = roomMapper.selectByPrimaryKey(users.getUid());
			if (room != null) {
				GuildHall oldHall = getOneGuildHallByRoomId(room.getRoomId());
				if (oldHall != null) {// 该厅已加入其他公会
					busiResult.setCode(494);
					if(oldHall.getId().compareTo(hallId)==0) {
						busiResult.setMessage("该用户是该厅的厅主");
					}else {
						busiResult.setMessage("该用户已加入其他厅/公会");
					}
					return busiResult;
				}
			}
			
			GuildHallMember member=getOneMemberByUid(users.getUid());
			if(member!=null) {
				if(member.getHallId()!=null&&member.getHallId().compareTo(hallId)==0) {
					busiResult.setCode(494);
					busiResult.setMessage("该用户已加入该厅");
					return busiResult;
				}else {
					busiResult.setCode(494);
					busiResult.setMessage("该用户已加入其他厅/公会");
					return busiResult;
				}
			}else {
				GuildHallMemberExample memberExample = new GuildHallMemberExample();
				GuildHallMemberExample.Criteria memberCriteria = memberExample.createCriteria();
				memberCriteria.andGuildIdEqualTo(guildId);
				memberCriteria.andHallIdEqualTo(hallId);
				memberCriteria.andIsDelEqualTo(false);
				int count=guildHallMemberMapper.countByExample(memberExample);
				if(count>=200) {
					busiResult.setCode(3);
					busiResult.setMessage("该厅已满员");
					return busiResult;
				}
				member = new GuildHallMember();
				member.setGuildId(guildId);
				member.setHallId(hallId);
				member.setUid(uid);
				member.setCreateTime(new Date());
				member.setIsDel(false);
				member.setMemberType(2);
				guildHallMemberMapper.insert(member);
				
				List<GuildHallApplyRecord> applyList= getApplyList(null, uid, 0, 0);//申请加入记录
				applyList.stream().forEach(r -> {
					if(hallId.compareTo(r.getHallId())==0) {//申请记录存在uid 申请加入待审核数据，  如果是 同一厅 的变为 3（已完成）, 否则 4（已失效）
						r.setStatus(3);
					}else {
						r.setStatus(4);
					}
					r.setUpdateTime(new Date()); 
					r.setApproverUid(null);
					r.setApproveTime(new Date());
					guildHallApplyRecordMapper.updateByPrimaryKey(r);
				});
				
				sendMsg(uid,"你已成功加入"+guild.getName()+"公会--"+hall.getRoomName());//发给成员
				if(!uids.isEmpty()) {
					List<String> uidList=new ArrayList<String>(uids);
					sendBatchMsgMsg(uidList, users.getNick()+"（ID："+users.getErbanNo()+"）已加入"+guild.getName()+"公会--"+hall.getRoomName());//发给厅主和公会长
				}
				jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), member.getUid().toString());
				jedisService.del(RedisKey.guild_member_list.getKey(guildId.toString()));
				jedisService.del(RedisKey.guild_hall_member_list.getKey(hallId.toString()));
				jedisService.hdel(RedisKey.guild_member_exist.getKey(), member.getUid().toString());
				jedisService.del(RedisKey.guild_hall_apply_record_list.getKey(hallId+"_1_10"));
				jedisService.del(RedisKey.guild_apply_record_list.getKey(guildId+"_1_10"));
				jedisService.hdel(RedisKey.guild_hall.getKey(), hallId.toString());
				jedisService.hdel(RedisKey.guild.getKey(), guildId.toString());
			}
		} catch (Exception e) {
			logger.error("addMember error", e);
			busiResult.setCode(500);
			busiResult.setMessage("添加失败");
		}
		return busiResult;
	}
	
	/**
	  * 删除成员
	 * 
	 * @param hallId
	 */
	@SuppressWarnings({ "rawtypes"})
	@Transactional(rollbackFor = Exception.class)
	public BusiResult delMember(Long memberId) {
		BusiResult busiResult = new BusiResult(BusiStatus.SUCCESS);
		try {
			GuildHallMember member=guildHallMemberMapper.selectByPrimaryKey(memberId);
			if(member!=null) {
				Guild guild=getOneGuildById(member.getGuildId());
				if(guild.getPresidentUid().compareTo(member.getUid())==0) {//会长与成员是同一人
					GuildHallMember old=member;
					old.setHallId(null);
					old.setUpdateTime(new Date());
					guildHallMemberMapper.updateByPrimaryKey(old);
				}else {
					guildHallMemberMapper.isDelMemberById(memberId);
				}
				List<GuildHallApplyRecord> applyList= getApplyList(null, member.getUid(), 1, 0);//申请退出记录
				applyList.stream().forEach(r -> {
					if(member.getHallId()!=null&&member.getHallId().compareTo(r.getHallId())==0) {//申请退出  如果是 同一厅 的变为 3（已完成）
						r.setStatus(3);
						r.setUpdateTime(new Date()); 
						r.setApproverUid(null);
						r.setApproveTime(new Date());
						guildHallApplyRecordMapper.updateByPrimaryKey(r);
					}
				});
				sendMsg(member.getUid(), "你已被移出"+guild.getName()+"公会");
				jedisService.del(RedisKey.guild_member.getKey(member.getUid().toString()));
				jedisService.hdel(RedisKey.guild_member_in_hall.getKey(), member.getUid().toString());
				jedisService.del(RedisKey.guild_member_list.getKey(member.getGuildId().toString()));
				if(member.getHallId()!=null) {
					jedisService.del(RedisKey.guild_hall_member_list.getKey(member.getHallId().toString()));
					jedisService.del(RedisKey.guild_hall_apply_record_list.getKey(member.getHallId()+"_1_10"));
				}
				jedisService.hdel(RedisKey.guild_member_exist.getKey(), member.getUid().toString());
				jedisService.del(RedisKey.guild_apply_record_list.getKey(member.getGuildId()+"_1_10"));
				jedisService.hdel(RedisKey.guild_hall.getKey(), member.getHallId().toString());
				jedisService.hdel(RedisKey.guild.getKey(), member.getGuildId().toString());
			} 
		} catch (Exception e) {
			logger.error("delMember error", e);
			busiResult.setCode(500);
			busiResult.setMessage("删除失败");
		}
		return busiResult;
	}

	private void sendMsg(Long uid, String msg) {
		// 发送消息给用户
		NeteaseSendMsgParam neteaseSendMsgParam = new NeteaseSendMsgParam();
		neteaseSendMsgParam.setFrom(SystemConfig.secretaryUid);
		neteaseSendMsgParam.setOpe(0);
		neteaseSendMsgParam.setType(0);
		neteaseSendMsgParam.setTo(uid.toString());
		neteaseSendMsgParam.setBody(msg);
		sendSysMsgService.sendMsg(neteaseSendMsgParam);

		NeteasePushParam neteasePushParam = new NeteasePushParam();
		neteasePushParam.setTo(String.valueOf(uid));
		Attach attach = new Attach();
		attach.setFirst(Constant.DefMsgType.userRealAudit);
		attach.setSecond(Constant.DefMsgType.userRealAudit);
		attach.setData("");
		neteasePushParam.setAttach(attach);
		sendSysMsgService.sendSysAttachMsg(neteasePushParam);
	}

	private void sendBatchMsgMsg(List<String> uids, String msg) {
		NeteaseSendMsgBatchParam neteaseSendMsgBatchParam = new NeteaseSendMsgBatchParam();
		neteaseSendMsgBatchParam.setFromAccid(SystemConfig.secretaryUid);
		neteaseSendMsgBatchParam.setContent(msg);
		neteaseSendMsgBatchParam.setType(0);
		neteaseSendMsgBatchParam.setToAccids(uids);
		sendSysMsgService.sendBatchMsgMsg(neteaseSendMsgBatchParam);
	}
	
	/**
	  * 厅下拉列表
	 * @param guildId
	 * @return
	 */
	public List<Map<String,Object>> getHallListByGuildId(Long guildId){
		return guildHallMapper.findByGuildId(guildId);
	}
	
	/**
	  * 申请记录列表
	 * @param hallId 厅Id
	 * @param uid 用户Id
	 * @param type 类型：0（申请加入），1（申请退出），2（逐出）
	 * @param status 审核状态：0（待审核），1（不通过），2（通过），3（已完成），4（已失效）
	 * @return
	 */
	public List<GuildHallApplyRecord> getApplyList(Long hallId,Long uid,Integer type,Integer status){
		GuildHallApplyRecordExample applyExample = new GuildHallApplyRecordExample();
		applyExample.setOrderByClause(" create_time desc");
		GuildHallApplyRecordExample.Criteria applyCriteria = applyExample.createCriteria();
		if(hallId!=null) {
			applyCriteria.andHallIdEqualTo(hallId);
		}
		if(uid!=null) {
			applyCriteria.andUidEqualTo(uid);
		}
		if(type!=null) {
			applyCriteria.andTypeEqualTo(type);
		}
		if(status!=null) {
			applyCriteria.andStatusEqualTo(status);
		}
		List<GuildHallApplyRecord> list=guildHallApplyRecordMapper.selectByExample(applyExample);
		return list;
	}
	
	/**
	  * 公会下拉列表
	 * @return
	 */
	public List<Map<String,Object>> getGuildAll(){
		return guildMapper.getGuildAll();
	}
}
