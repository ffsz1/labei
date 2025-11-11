package com.erban.admin.web.controller.guild;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.common.ErrorCode;
import com.erban.admin.main.model.Guild;
import com.erban.admin.main.model.GuildHall;
import com.erban.admin.main.model.GuildHallMember;
import com.erban.admin.main.service.guild.GuildService;
import com.erban.admin.web.base.BaseController;
import com.github.pagehelper.PageInfo;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.common.utils.BlankUtil;

/**
 * 后台管理--公会管理
 */
@Controller
@RequestMapping("/admin/guild")
public class GuildController extends BaseController {

	@Autowired
	private GuildService guildService;

	/**
	  *公会列表
	 * 
	 * @param guildNo      公会id
	 * @param presidentUid 公会长id
	 */
	@RequestMapping(value = "/getGuildList", method = RequestMethod.GET)
	@ResponseBody
	public void getGuildList(String guildNo, Long erbanNo) {
		PageInfo<Guild> pageInfo = guildService.getGuildList(getPageNumber(), getPageSize(), guildNo, erbanNo);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("total", pageInfo.getTotal());
		jsonObject.put("rows", pageInfo.getList());
		writeJson(jsonObject.toJSONString());
	}

	/**
	 * 公会详情
	 * 
	 * @param id
	 */
	@RequestMapping(value = "/getOneGuild")
	@ResponseBody
	public void getOneGuild(Long id) {
		JSONObject jsonObject = new JSONObject();
		if (!BlankUtil.isBlank(id)) {
			try {
				Guild guild = guildService.getOneGuildById(id);
				if (guild != null) {
					jsonObject.put("entity", guild);
				}
			} catch (Exception e) {
				logger.warn("getOneGuild fail,cause by " + e.getMessage(), e);
			}
		}
		writeJson(jsonObject.toJSONString());
	}

	/**
	 * 公会新增编辑
	 * 
	 * @param guild
	 */
	@RequestMapping(value = "/saveGuild", method = RequestMethod.POST)
	@ResponseBody
	public BusiResult saveGuild(Guild guild) {
		if (guild != null) {
			return guildService.saveGuild(guild);
		} else {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL);
		}
	}

	/**
	 * 解散公会
	 * 
	 * @param guildId
	 */
	@RequestMapping(value = "/delGuild", method = RequestMethod.POST)
	@ResponseBody
	public void delGuild(Long guildId) {
		int result = 1;
		if (!BlankUtil.isBlank(guildId)) {
			try {
				guildService.delGuild(guildId);
			} catch (Exception e) {
				result = ErrorCode.SERVER_ERROR;
				logger.warn("delGuild fail,cause by " + e.getMessage(), e);
			}
		} else {
			result = ErrorCode.ERROR_NULL_ARGU;
		}
		writeJsonResult(result);
	}

	/**
	  *厅列表
	 * @param guildId
	 */
	@RequestMapping(value = "/getGuildHallList", method = RequestMethod.GET)
	@ResponseBody
	public void getGuildHallList(Long guildId) {
		PageInfo<GuildHall> pageInfo = guildService.getGuildHallList(getPageNumber(), getPageSize(), guildId);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("total", pageInfo.getTotal());
		jsonObject.put("rows", pageInfo.getList());
		writeJson(jsonObject.toJSONString());
	}

	/**
	 * 新增厅
	 * 
	 * @param guildId
	 * @param erbanNo
	 * @return
	 */
	@RequestMapping(value = "/addHall", method = RequestMethod.POST)
	@ResponseBody
	public BusiResult addHall(Long guildId, Long erbanNo) {
		if (guildId == null || erbanNo == null) {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL);
		}
		return guildService.addHall(guildId, erbanNo);
	}

	/**
	 * 删除厅
	 * @param hallId
	 * @return
	 */
	@RequestMapping(value = "/delHall", method = RequestMethod.POST)
	@ResponseBody
	public BusiResult delHall(Long hallId) {
		if (hallId == null) {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL);
		}
		return guildService.delHall(hallId);
	}
	
	/**
	  *成员列表
	 * @param guildId 公会Id
	 * @param hallId 厅Id
	 */
	@RequestMapping(value = "/getMemberList", method = RequestMethod.GET)
	@ResponseBody
	public void getMemberList(Long guildId,Long hallId,String type) {
		PageInfo<GuildHallMember> pageInfo = guildService.getMemberList(getPageNumber(), getPageSize(), guildId,hallId,type);
		JSONObject jsonObject = new JSONObject();
		jsonObject.put("total", pageInfo.getTotal());
		jsonObject.put("rows", pageInfo.getList());
		writeJson(jsonObject.toJSONString());
	}
	
	/**
	  * 新增成员
	 * 
	 * @param guildId
	 * @param erbanNo
	 * @return
	 */
	@RequestMapping(value = "/addMember", method = RequestMethod.POST)
	@ResponseBody
	public BusiResult addMember(Long guildId,Long hallId, Long erbanNo) {
		if (guildId == null || hallId == null || erbanNo==null) {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL);
		}
		return guildService.addMember(guildId, hallId, erbanNo);
	}
	
	/**
	 * 删除成员
	 * @param hallId
	 * @return
	 */
	@RequestMapping(value = "/delMember", method = RequestMethod.POST)
	@ResponseBody
	public BusiResult delMember(Long memberId) {
		if (memberId == null) {
			return new BusiResult(BusiStatus.PARAMETERILLEGAL);
		}
		return guildService.delMember(memberId);
	}
	
	/**
	  *厅下拉列表
	 * @param guildId
	 */
	@RequestMapping(value = "/getHallList", method = RequestMethod.GET)
	@ResponseBody
	public BusiResult getHallList(Long guildId) {
		List<Map<String,Object>> data = guildService.getHallListByGuildId(guildId);
		return new BusiResult(BusiStatus.SUCCESS, data);
	}
	
	/**
	 *公会下拉列表
	 */
	@RequestMapping(value = "/getGuildAll", method = RequestMethod.GET)
	@ResponseBody
	public BusiResult getGuildAll() {
		List<Map<String,Object>> data = guildService.getGuildAll();
		return new BusiResult(BusiStatus.SUCCESS, data);
	}
}
