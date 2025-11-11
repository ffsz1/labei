package com.juxiao.xchat.manager.external.netease.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.juxiao.xchat.manager.external.netease.NetEaseTeamManager;
import com.juxiao.xchat.manager.external.netease.bo.NetEaseCreateTeamBO;
import com.juxiao.xchat.manager.external.netease.bo.NetEaseTeamBO;
import com.juxiao.xchat.manager.external.netease.conf.NetEaseConf;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseTeamRet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author chris
 * @Title:
 * @date 2018/11/26
 * @time 10:30
 */
@Service
@Slf4j
public class NetEaseTeamManagerImpl implements NetEaseTeamManager {

    private static final String CREATE_TEAM = "https://api.netease.im/nimserver/team/create.action";
    private static final String UPDATE_TEAM = "https://api.netease.im/nimserver/team/update.action";
    private static final String ADD_TEAM = "https://api.netease.im/nimserver/team/add.action";
    private static final String KICK_TEAM = "https://api.netease.im/nimserver/team/kick.action";
    private static final String MUTE_TEAM = "https://api.netease.im/nimserver/team/muteTeam.action";
    private static final String MUTE_T_LIST = "https://api.netease.im/nimserver/team/muteTlist.action";
    private static final String MUTE_T_LIST_ALL = "https://api.netease.im/nimserver/team/muteTlistAll.action";
    private static final String LIST_TEAM_MUTE = "https://api.netease.im/nimserver/team/listTeamMute.action";
    private static final String REMOVE_TEAM = "https://api.netease.im/nimserver/team/remove.action";
    private static final String QUERY = "https://api.netease.im/nimserver/team/query.action";
    private static final String QUERY_DETAIL = "https://api.netease.im/nimserver/team/queryDetail.action";
    private static final String GET_MARK_READ_INFO = "https://api.netease.im/nimserver/team/getMarkReadInfo.action";
    private static final String CHANGE_OWNER = "https://api.netease.im/nimserver/team/changeOwner.action";
    private static final String ADD_MANAGER = "https://api.netease.im/nimserver/team/addManager.action";
    private static final String REMOVE_MANAGER = "https://api.netease.im/nimserver/team/removeManager.action";
    private static final String JOIN_TEAMS = "https://api.netease.im/nimserver/team/joinTeams.action";
    private static final String UPDATE_TEAMS_NICK = "https://api.netease.im/nimserver/team/updateTeamNick.action";
    private static final String LEAVE = "https://api.netease.im/nimserver/team/leave.action";


    @Autowired
    private Gson gson;

    @Autowired
    private NetEaseConf neteaseConf;

    /**
     * 创建群
     * @param netEaseTeamBO
     * @return
     */
    @Override
    public NetEaseTeamRet createTeam(NetEaseCreateTeamBO netEaseTeamBO) throws Exception{
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(CREATE_TEAM, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tname", netEaseTeamBO.getTame());
        param.put("owner", netEaseTeamBO.getOwner());
        param.put("members", netEaseTeamBO.getMembers());
        if(netEaseTeamBO.getAnnouncement() != null){
            param.put("announcement", netEaseTeamBO.getAnnouncement());
        }
        if(netEaseTeamBO.getIntro() != null) {
            param.put("intro", netEaseTeamBO.getIntro());
        }
        param.put("msg", netEaseTeamBO.getMsg());
        param.put("magree", netEaseTeamBO.getMagree());
        param.put("joinmode", netEaseTeamBO.getJoinmode());
        if(netEaseTeamBO.getCustom() != null) {
            param.put("custom", netEaseTeamBO.getCustom());
        }
        if(netEaseTeamBO.getIcon() != null) {
            param.put("icon", netEaseTeamBO.getIcon());
        }
        if(netEaseTeamBO.getBeinvitemode() != null){
            param.put("beinvitemode", netEaseTeamBO.getBeinvitemode());
        }
        if(netEaseTeamBO.getInvitemode() != null) {
            param.put("invitemode", netEaseTeamBO.getInvitemode());
        }
        if(netEaseTeamBO.getUptinfomode() != null) {
            param.put("uptinfomode", netEaseTeamBO.getUptinfomode());
        }
        if(netEaseTeamBO.getUpcustommode() != null) {
            param.put("upcustommode", netEaseTeamBO.getUpcustommode());
        }
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信创建群 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", CREATE_TEAM, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 加入群
     * @param netEaseTeamBO
     * @return
     */
    @Override
    public NetEaseTeamRet joinTeam(NetEaseTeamBO netEaseTeamBO) throws Exception{
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(ADD_TEAM, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",netEaseTeamBO.getTid());
        param.put("owner",netEaseTeamBO.getOwner());
        param.put("members",netEaseTeamBO.getMembers());
        param.put("magree",netEaseTeamBO.getMagree());
        param.put("msg",netEaseTeamBO.getMsg());
        if(netEaseTeamBO.getAttach() != null){
            param.put("attach",netEaseTeamBO.getAttach());
        }
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信加入群 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms ", ADD_TEAM, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 踢出群
     * @param netEaseTeamBO
     * @return
     */
    @Override
    public NetEaseTeamRet kickTeam(NetEaseTeamBO netEaseTeamBO) throws Exception{
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(KICK_TEAM, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",netEaseTeamBO.getTid());
        param.put("owner",netEaseTeamBO.getOwner());
        param.put("member",netEaseTeamBO.getMember());
        if(netEaseTeamBO.getMembers() != null){
            param.put("members",netEaseTeamBO.getMembers());
        }
        if(netEaseTeamBO.getAttach() != null){
            param.put("attach",netEaseTeamBO.getAttach());
        }
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信踢出群 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", KICK_TEAM, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 修改消息提醒开关
     *
     * @param tid   网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param accid 要操作的群成员accid
     * @param ope   1：关闭消息提醒，2：打开消息提醒，其他值无效
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet muteTeam(String tid, String accid, int ope) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(MUTE_TEAM, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("accid",accid);
        param.put("ope",ope);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信[群]修改消息提醒开关 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", MUTE_TEAM, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 高级群禁言群成员
     *
     * @param tid   网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param owner 群主accid
     * @param accid 禁言对象的accid
     * @param mute  1-禁言，0-解禁
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet muteList(String tid, String owner, String accid, int mute) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(MUTE_T_LIST, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("owner",owner);
        param.put("accid",accid);
        param.put("mute",mute);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信[群]高级群禁言群成员 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", MUTE_T_LIST, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 将群组整体禁言 - > 禁言群组，普通成员不能发送消息，创建者和管理员可以发送消息
     *
     * @param tid      网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param owner    群主的accid
     * @param mute     true:禁言，false:解禁(mute和muteType至少提供一个，都提供时按mute处理)
     * @param muteType 禁言类型 0:解除禁言，1:禁言普通成员 3:禁言整个群(包括群主)
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet muteListAll(String tid, String owner, String mute, int muteType) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(MUTE_T_LIST_ALL, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("owner",owner);
        param.put("mute",mute);
        param.put("muteType",muteType);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信[群]将群组整体禁言  ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", MUTE_T_LIST_ALL, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 获取群组禁言的成员列表
     *
     * @param tid   网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param owner 群主的accid
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet listTeamMute(String tid, String owner) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(LIST_TEAM_MUTE, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("owner",owner);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信[群]获取群组禁言的成员列表  ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", LIST_TEAM_MUTE, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 解散群 - 删除整个群，会解散该群，需要提供群主accid，谨慎操作！
     *
     * @param tid   网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner 群主用户帐号，最大长度32字符
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet remove(String tid, String owner) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(REMOVE_TEAM, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("owner",owner);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信解散群 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", REMOVE_TEAM, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 编辑群资料
     *
     * @param netEaseTeamBO
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet update(NetEaseCreateTeamBO netEaseTeamBO) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(UPDATE_TEAM, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid", netEaseTeamBO.getTid());
        param.put("owner", netEaseTeamBO.getOwner());
        if(netEaseTeamBO.getTame() != null){
            param.put("tname", netEaseTeamBO.getTame());
        }
        if(netEaseTeamBO.getMembers() != null) {
            param.put("members", netEaseTeamBO.getMembers());
        }
        if(netEaseTeamBO.getAnnouncement() != null){
            param.put("announcement", netEaseTeamBO.getAnnouncement());
        }
        if(netEaseTeamBO.getIntro() != null) {
            param.put("intro", netEaseTeamBO.getIntro());
        }
        if(netEaseTeamBO.getJoinmode() != null) {
            param.put("joinmode", netEaseTeamBO.getJoinmode());
        }
        if(netEaseTeamBO.getCustom() != null) {
            param.put("custom", netEaseTeamBO.getCustom());
        }
        if(netEaseTeamBO.getIcon() != null) {
            param.put("icon", netEaseTeamBO.getIcon());
        }
        if(netEaseTeamBO.getBeinvitemode() != null){
            param.put("beinvitemode", netEaseTeamBO.getBeinvitemode());
        }
        if(netEaseTeamBO.getInvitemode() != null) {
            param.put("invitemode", netEaseTeamBO.getInvitemode());
        }
        if(netEaseTeamBO.getUptinfomode() != null) {
            param.put("uptinfomode", netEaseTeamBO.getUptinfomode());
        }
        if(netEaseTeamBO.getUpcustommode() != null) {
            param.put("upcustommode", netEaseTeamBO.getUpcustommode());
        }
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信编辑群资料 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", UPDATE_TEAM, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 群信息与成员列表查询
     * 1.高级群信息与成员列表查询，一次最多查询30个群相关的信息，跟据ope参数来控制是否带上群成员列表；
     * 2.查询群成员会稍微慢一些，所以如果不需要群成员列表可以只查群信息；
     * 3.此接口受频率控制，某个应用一分钟最多查询30次，超过会返回416，并且被屏蔽一段时间；
     * 4.群成员的群列表信息中增加管理员成员admins的返回。
     *
     * @param tids 群id列表，如["3083","3084"]
     * @param ope  1表示带上群成员列表，0表示不带群成员列表，只返回群信息
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet query(String tids, int ope) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(QUERY, neteaseConf.getAppKey(), neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tids",tids);
        param.put("ope",ope);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信群信息与成员列表查询 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", QUERY, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 获取群组详细信息
     *
     * @param tid 群id，群唯一标识，创建群时会返回
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet queryDetail(String tid) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(QUERY_DETAIL,neteaseConf.getAppKey(),neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信群获取群组详细信息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", QUERY, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 获取群组已读消息的已读详情信息
     *
     * @param tid       群id，群唯一标识，创建群时会返回
     * @param msgid     发送群已读业务消息时服务器返回的消息ID
     * @param fromAccid 消息发送者账号
     * @param snapshot  是否返回已读、未读成员的accid列表，默认为false
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet getMarkReadInfo(Long tid, Long msgid, String fromAccid, boolean snapshot) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(GET_MARK_READ_INFO,neteaseConf.getAppKey(),neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("msgid",msgid);
        param.put("fromAccid",fromAccid);
        param.put("snapshot",snapshot);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信群获取群组已读消息的已读详情信息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", GET_MARK_READ_INFO, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 移交群主
     * 1.转换群主身份；
     * 2.群主可以选择离开此群，还是留下来成为普通成员。
     *
     * @param tid      网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner    群主用户帐号，最大长度32字符
     * @param newowner 新群主帐号，最大长度32字符
     * @param leave    1:群主解除群主后离开群，2：群主解除群主后成为普通成员。其它414
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet changeOwner(String tid, String owner, String newowner, int leave) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(CHANGE_OWNER,neteaseConf.getAppKey(),neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("owner",owner);
        param.put("newowner",newowner);
        param.put("leave",leave);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信群移交群主 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", CHANGE_OWNER, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 任命管理员
     * 提升普通成员为群管理员，可以批量，但是一次添加最多不超过10个人。
     *
     * @param tid     网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner   群主用户帐号，最大长度32字符
     * @param members ["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，长度最大1024字符（一次添加最多10个管理员）
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet addManager(String tid, String owner, String members) throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String,Object> resultMap = optionManager(ADD_MANAGER,tid,owner,members);
        String result = resultMap.get("result").toString();
        log.info("[ 云信群任命管理员]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", ADD_MANAGER, gson.toJson(resultMap.get("params")), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 解除管理员身份，可以批量，但是一次解除最多不超过10个人
     *
     * @param tid     网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner   群主用户帐号，最大长度32字符
     * @param members ["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，长度最大1024字符（一次解除最多10个管理员）
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet removeManager(String tid, String owner, String members) throws Exception {
        long startTime = System.currentTimeMillis();
        Map<String,Object> resultMap = optionManager(REMOVE_MANAGER,tid,owner,members);
        log.info("[ 云信群解除管理员身份]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", REMOVE_MANAGER, gson.toJson(resultMap.get("params")), resultMap.get("result"), (System.currentTimeMillis() - startTime));
        return gson.fromJson(resultMap.get("result").toString(), NetEaseTeamRet.class);
    }

    /**
     * 抽取公用方法
     * @param url
     * @param tid
     * @param owner
     * @param members
     * @return
     * @throws Exception
     */
    public Map<String,Object> optionManager(String url ,String tid, String owner, String members) throws Exception{
        NetEaseClient client = new NetEaseClient(url,neteaseConf.getAppKey(),neteaseConf.getAppSecret());
        Map<String, Object> retultMap = Maps.newHashMap();
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("owner",owner);
        param.put("members",members);
        String result = client.buildHttpPostParam(param).executePost();
        retultMap.put("params",param);
        retultMap.put("result",result);
        return retultMap;
    }

    /**
     * 获取某个用户所加入高级群的群信息
     *
     * @param accid 要查询用户的accid
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet joinTeams(String accid) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(JOIN_TEAMS,neteaseConf.getAppKey(),neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("accid",accid);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信群获取某个用户所加入高级群的群信息 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", JOIN_TEAMS, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 修改指定账号在群内的昵称
     *
     * @param tid    群唯一标识，创建群时网易云通信服务器产生并返回
     * @param owner  群主 accid
     * @param accid  要修改群昵称的群成员 accid
     * @param nick   accid 对应的群昵称，最大长度32字符 false
     * @param custom 自定义扩展字段，最大长度1024字节 false
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet updateTeamNick(String tid, String owner, String accid, String nick, String custom) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(UPDATE_TEAMS_NICK,neteaseConf.getAppKey(),neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("owner",owner);
        param.put("accid",accid);
        param.put("nick",nick);
        if(custom != null) {
            param.put("custom", custom);
        }
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信群修改指定账号在群内的昵称 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", UPDATE_TEAMS_NICK, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }

    /**
     * 高级群主动退群
     *
     * @param tid   网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param accid 退群的accid
     * @return
     * @throws Exception
     */
    @Override
    public NetEaseTeamRet leave(String tid, String accid) throws Exception {
        long startTime = System.currentTimeMillis();
        NetEaseClient client = new NetEaseClient(LEAVE,neteaseConf.getAppKey(),neteaseConf.getAppSecret());
        Map<String, Object> param = Maps.newHashMap();
        param.put("tid",tid);
        param.put("accid",accid);
        String result = client.buildHttpPostParam(param).executePost();
        log.info("[ 云信群高级群主动退群 ]接口:>{}，请求:>{}，返回:>{}，耗时:>{} /ms", LEAVE, gson.toJson(param), result, (System.currentTimeMillis() - startTime));
        return gson.fromJson(result, NetEaseTeamRet.class);
    }
}

