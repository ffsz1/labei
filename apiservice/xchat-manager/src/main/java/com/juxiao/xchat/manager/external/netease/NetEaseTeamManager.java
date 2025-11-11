package com.juxiao.xchat.manager.external.netease;

import com.juxiao.xchat.manager.external.netease.bo.NetEaseCreateTeamBO;
import com.juxiao.xchat.manager.external.netease.bo.NetEaseTeamBO;
import com.juxiao.xchat.manager.external.netease.ret.NetEaseTeamRet;

/**
 * @author chris
 * @Title: 云信群
 * @date 2018/11/26
 * @time 10:24
 */
public interface NetEaseTeamManager {

    /**
     * 创建群
     * @param netEaseTeamBO
     * @return
     * @throws Exception
     */
    NetEaseTeamRet createTeam(NetEaseCreateTeamBO netEaseTeamBO) throws Exception;

    /**
     * 加入群
     * @param netEaseTeamBO
     * @return
     * @throws Exception
     */
    NetEaseTeamRet joinTeam(NetEaseTeamBO netEaseTeamBO) throws Exception;

    /**
     * 踢出群
     * @param netEaseTeamBO
     * @return
     * @throws Exception
     */
    NetEaseTeamRet kickTeam(NetEaseTeamBO netEaseTeamBO) throws Exception;

    /**
     * 修改消息提醒开关
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param accid 要操作的群成员accid
     * @param ope 1：关闭消息提醒，2：打开消息提醒，其他值无效
     * @return
     * @throws Exception
     */
    NetEaseTeamRet muteTeam(String tid,String accid,int ope) throws Exception;

    /**
     * 高级群禁言群成员
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param owner 群主accid
     * @param accid 禁言对象的accid
     * @param mute 1-禁言，0-解禁
     * @return
     * @throws Exception
     */
    NetEaseTeamRet muteList(String tid,String owner,String accid,int mute)throws Exception;

    /**
     * 将群组整体禁言 - > 禁言群组，普通成员不能发送消息，创建者和管理员可以发送消息
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param owner 群主的accid
     * @param mute true:禁言，false:解禁(mute和muteType至少提供一个，都提供时按mute处理)
     * @param muteType 禁言类型 0:解除禁言，1:禁言普通成员 3:禁言整个群(包括群主)
     * @return
     * @throws Exception
     */
    NetEaseTeamRet muteListAll(String tid,String owner,String mute,int muteType)throws Exception;

    /**
     * 获取群组禁言的成员列表
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param owner 群主的accid
     * @return
     * @throws Exception
     */
    NetEaseTeamRet listTeamMute(String tid,String owner)throws Exception;

    /**
     * 解散群 - 删除整个群，会解散该群，需要提供群主accid，谨慎操作！
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner 群主用户帐号，最大长度32字符
     * @return
     * @throws Exception
     */
    NetEaseTeamRet remove(String tid,String owner)throws Exception;

    /**
     * 编辑群资料
     * @param netEaseCreateTeamBO
     * @return
     * @throws Exception
     */
    NetEaseTeamRet update(NetEaseCreateTeamBO netEaseCreateTeamBO)throws Exception;

    /**
     * 群信息与成员列表查询
     * 1.高级群信息与成员列表查询，一次最多查询30个群相关的信息，跟据ope参数来控制是否带上群成员列表；
     * 2.查询群成员会稍微慢一些，所以如果不需要群成员列表可以只查群信息；
     * 3.此接口受频率控制，某个应用一分钟最多查询30次，超过会返回416，并且被屏蔽一段时间；
     * 4.群成员的群列表信息中增加管理员成员admins的返回。
     * @param tids 群id列表，如["3083","3084"]
     * @param ope 1表示带上群成员列表，0表示不带群成员列表，只返回群信息
     * @return
     * @throws Exception
     */
    NetEaseTeamRet query(String tids,int ope)throws Exception;

    /**
     * 获取群组详细信息
     * @param tid 群id，群唯一标识，创建群时会返回
     * @return
     * @throws Exception
     */
    NetEaseTeamRet queryDetail(String tid)throws Exception;

    /**
     * 获取群组已读消息的已读详情信息
     * @param tid 群id，群唯一标识，创建群时会返回
     * @param msgid 发送群已读业务消息时服务器返回的消息ID
     * @param fromAccid 消息发送者账号
     * @param snapshot 是否返回已读、未读成员的accid列表，默认为false
     * @return
     * @throws Exception
     */
    NetEaseTeamRet getMarkReadInfo(Long tid,Long msgid,String fromAccid,boolean snapshot)throws Exception;

    /**
     * 移交群主
     * 1.转换群主身份；
     * 2.群主可以选择离开此群，还是留下来成为普通成员。
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner 群主用户帐号，最大长度32字符
     * @param newowner 新群主帐号，最大长度32字符
     * @param leave 1:群主解除群主后离开群，2：群主解除群主后成为普通成员。其它414
     * @return
     * @throws Exception
     */
    NetEaseTeamRet changeOwner(String tid,String owner,String newowner,int leave)throws Exception;

    /**
     * 任命管理员
     * 提升普通成员为群管理员，可以批量，但是一次添加最多不超过10个人。
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner 群主用户帐号，最大长度32字符
     * @param members ["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，长度最大1024字符（一次添加最多10个管理员）
     * @return
     * @throws Exception
     */
    NetEaseTeamRet addManager(String tid,String owner,String members)throws Exception;

    /**
     * 解除管理员身份，可以批量，但是一次解除最多不超过10个人
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回，最大长度128字符
     * @param owner 群主用户帐号，最大长度32字符
     * @param members ["aaa","bbb"](JSONArray对应的accid，如果解析出错会报414)，长度最大1024字符（一次解除最多10个管理员）
     * @return
     * @throws Exception
     */
    NetEaseTeamRet removeManager(String tid,String owner,String members)throws Exception;

    /**
     * 获取某个用户所加入高级群的群信息
     * @param accid 要查询用户的accid
     * @return
     * @throws Exception
     */
    NetEaseTeamRet joinTeams(String accid)throws Exception;

    /**
     * 修改指定账号在群内的昵称
     * @param tid 群唯一标识，创建群时网易云通信服务器产生并返回
     * @param owner 群主 accid
     * @param accid 要修改群昵称的群成员 accid
     * @param nick accid 对应的群昵称，最大长度32字符 false
     * @param custom 自定义扩展字段，最大长度1024字节 false
     * @return
     * @throws Exception
     */
    NetEaseTeamRet updateTeamNick(String tid,String owner,String accid,String nick,String custom)throws Exception;

    /**
     * 高级群主动退群
     * @param tid 网易云通信服务器产生，群唯一标识，创建群时会返回
     * @param accid 退群的accid
     * @return
     * @throws Exception
     */
    NetEaseTeamRet leave(String tid,String accid)throws Exception;

}
