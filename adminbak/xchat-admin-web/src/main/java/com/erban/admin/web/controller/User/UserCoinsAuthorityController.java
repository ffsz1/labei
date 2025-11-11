package com.erban.admin.web.controller.User;

import com.alibaba.fastjson.JSONObject;
import com.erban.admin.main.dto.UsersPhotoDTO;
import com.erban.admin.web.base.BaseController;
import com.erban.main.model.Users;
import com.erban.main.service.user.UsersService;
import com.erban.main.vo.UserVo;
import com.github.pagehelper.PageInfo;
import com.google.gson.Gson;
import com.xchat.common.redis.RedisKey;
import com.xchat.common.result.BusiResult;
import com.xchat.common.status.BusiStatus;
import com.xchat.oauth2.service.model.vo.UserAuthorityVO;
import com.xchat.oauth2.service.param.AccountParam;
import com.xchat.oauth2.service.service.JedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/user/coinsAuthority")
public class UserCoinsAuthorityController extends BaseController {
    @Autowired
    private UsersService usersService;

    @Autowired
    private JedisService jedisService;

    private Gson gson = new Gson();

    /**
     * 查询所有已开通用户金币转账权限信息
     *
     * @return
     */
    @RequestMapping("/getAllUser")
    @ResponseBody
    public void geAllUserInfo() {
        JSONObject jsonObject = new JSONObject();
        List<UserAuthorityVO> authorityVOList = new ArrayList<>();
        Map<String, String> userMap = jedisService.hgetAll(RedisKey.author_give_gold.getKey());

        userMap.forEach((key, value) -> {
            UserAuthorityVO authorityVO = gson.fromJson(value, UserAuthorityVO.class);
            if (authorityVO.getAuthority()) {
                authorityVOList.add(authorityVO);
            }
        });

        jsonObject.put("total", jedisService.hlen(RedisKey.author_give_gold.getKey()));
        jsonObject.put("rows", authorityVOList);
        writeJson(jsonObject.toJSONString());
    }

    /**
     * 设置金币转账权限
     *
     * @param uid       用户ID
     * @param authority 权限是否开通
     * @return
     */
    @RequestMapping("/setAuthority")
    @ResponseBody
    public BusiResult setTransferAuthority(@RequestParam(value = "uid") Long uid, @RequestParam(value = "authority") Boolean authority) {
        UserVo uv = usersService.getUserVoByUid(uid);
        UserAuthorityVO ua = new UserAuthorityVO();
        ua.setUid(uv.getUid());
        ua.setErbanNo(uv.getErbanNo());
        ua.setNick(uv.getNick());
        ua.setAuthority(authority);

        String status = jedisService.hget(RedisKey.author_give_gold.getKey(), uid.toString());
        if (StringUtils.isBlank(status) || authority.toString() != status) {
            jedisService.hset(RedisKey.author_give_gold.getKey(), uid.toString(), gson.toJson(ua));
        }

        BusiResult br = new BusiResult(BusiStatus.SUCCESS);
        br.setData(authority == true ? "开通" : "关闭");
        return br;
    }

    /**
     * 查询某个用户
     *
     * @param erbanNo   拉贝号
     * @return
     */
    @RequestMapping("/searchUser")
    @ResponseBody
    public void searchUser(@RequestParam(value = "erbanNo") Long erbanNo) {
        if (erbanNo == null) {
            geAllUserInfo();
        }

        JSONObject jsonObject = new JSONObject();
        List<UserAuthorityVO> authorityVOList = new ArrayList<>();

        Users user = usersService.getUsersByErBanNo(erbanNo);

        UserAuthorityVO authorityVO = new UserAuthorityVO();
        UserAuthorityVO ua =  gson.fromJson(jedisService.hget(RedisKey.author_give_gold.getKey(), user.getUid().toString()), UserAuthorityVO.class);
        authorityVO.setUid(user.getUid());
        authorityVO.setErbanNo(erbanNo);
        authorityVO.setNick(user.getNick());
        authorityVO.setAuthority(ua == null ? false : ua.getAuthority());
        authorityVOList.add(authorityVO);

        jsonObject.put("total", 1);
        jsonObject.put("rows", authorityVOList);
        writeJson(jsonObject.toJSONString());
    }
}
