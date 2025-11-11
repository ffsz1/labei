package com.xchat.common.netease.neteaseacc.result;

import java.util.List;
import java.util.Map;

public class RoomUserListRet {

	/**
	 * "roomList": [ { "roomid": 9931372, "level": 0, "nick": "111", "accid":
	 * "900243", "type": "CREATOR", "onlineStat": false, "ext": null, "avator":
	 * "https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV82MjE2NDUyMDVfMTQ5OTc2ODIwOTgzMl8wYjE3MWIxOC05YzU2LTQ2YWItOTVhNy03ZDNkNmE4ODI4ODY="
	 * } ] },
	 */
	private Map<String, List<Map<String, Object>>> desc;

	public Map<String, List<Map<String, Object>>> getDesc() {
		return desc;
	}

	public void setDesc(Map<String, List<Map<String, Object>>> desc) {
		this.desc = desc;
	}

}
