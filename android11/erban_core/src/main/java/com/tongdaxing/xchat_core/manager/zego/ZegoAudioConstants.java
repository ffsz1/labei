package com.tongdaxing.xchat_core.manager.zego;

/**
 * 创建者      Created by Edwin
 * 创建时间    2018/11/1
 * 描述        自定义即构相关常量数据
 *
 * 更新者      Edwin
 * 更新时间
 * 更新描述
 *
 * @author Edwin
 */
public interface ZegoAudioConstants {

    /**
     * ZEGO APP_ID
     */
    long ZEGO_APP_ID = 197844898;

    long ZEGO_DEGUB_APP_ID = 753120966;

    /**
     * ZEGO_SIGN_KEY
     */
    byte[] ZEGO_SIGN_KEY = {0x04, (byte) 0xf7,0x21, (byte) 0xbf, (byte) 0x98,0x12, (byte) 0xb0,0x75, (byte) 0x85,0x46,
            0x51,0x61,0x0c,0x65,0x4b,0x14, (byte) 0xfa, (byte) 0x8c, (byte) 0xdc, (byte) 0xd4,0x2b, (byte) 0xbc,
            (byte) 0x81, (byte) 0xa4,0x32, (byte) 0x98,0x5d, (byte) 0xbe, (byte) 0xe8, (byte) 0xe3,0x14,0x35};

    byte[] ZEGO_DEBUG_SIGN_KEY = {(byte) 0x89, (byte) 0xb4, (byte) 0xde,0x67, (byte) 0xfb, (byte) 0x9f, (byte) 0xf6,
            (byte) 0xb9, (byte) 0x8f, (byte) 0xe3, (byte) 0x8b, (byte) 0xda,0x22, (byte) 0x84,0x49, (byte) 0xd8,
            0x43,0x25, (byte) 0xf9,0x0e, (byte) 0xc4, (byte) 0xb2, (byte) 0x84, (byte) 0xe5 ,0x4b, (byte) 0xc0 , 0x2c,
            0x1a, (byte) 0xcd, 0x69, 0x34, 0x6e};

    /**
     * 双声道模式
     */
    int ZEGO_AUDIO_CHANNEL_COUNT = 2;

    /**
     * 码率
     */
    int ZEGO_NORMAL_AUDIO_BITRATE = 48 * 1000;
    int ZEGO_HIGH_AUDIO_BITRATE = 96 * 1000;

    /**
     * 音频前处理功能, 默认声道数
     */
    int ZEGO_ENABLE_AUDIO_PREP2_CHANNEL = 0;

    /**
     * 音频前处理功能, 是否对前处理后的数据进行编码。
     */
    boolean ZEGO_ENABLE_AUDIO_PREP2_ENCODE = false;

    /**
     * 音频前处理功能, 默认采样率sampleRate
     */
    int ZEGO_ENABLE_AUDIO_PREP2_SAMPLE_RATE = 0;

    /**
     * 音频前处理功能, 采样数
     */
    int ZEGO_ENABLE_AUDIO_PREP2_SAMPLE = 1;

    /**
     * 手动发布直播还是自动发布直播
     * true 手动推流  false 自动推流
     */
    boolean ZEGO_AUTO_MANUAL_PUBLISH = true;

    /**
     * 用户声浪周期设置
     */
    int ZEGO_SOUND_LEVEL_CYCLE = 1000;

    /**
     * 业务逻辑类型
     *
     * 0：直播  2：语音通话
     */
    int ZEGO_BUSINESS_TYPE_LIVE_BROADCAST = 0;
    int ZEGO_BUSINESS_TYPE_VOICE_CALL = 2;

    /**
     * 即构推拉流质量最差等级
     */
    int ZEGO_STREAM_QUALITY_LOW = 3;

    /**
     * 声网默认正确返回code
     */
    int ZEGO_ON_OPERATION_SUCCESS_CODE = 0;

    /**
     * 房间事件处理状态码
     */
    int ZEGO_ON_LOGIN_COMPLETION_CODE = 0;
    int ZEGO_ON_SOUND_LEVEL_UPDATE_CODE = 1;
    int ZEGO_ON_CAPTURE_SOUND_LEVEL_UPDATE_CODE = 2;
    int ZEGO_ON_PUBLISH_RETRY_CODE = 3;
    int ZEGO_ON_PLAY_RETRY_CODE = 4;
    int ZEGO_ON_RESTART_CONNECTION_CODE = 5;

    /**
     * 推流失败，重新推流最大数
     */
    int ZEGO_PUBLISH_RETRY_COUNT = 10;

    /**
     * loginRoom的错误码说明
     *
     * 错误码	            说明
     * stateCode = 0	    登录成功
     * stateCode = -1	    没有配置为测试环境，请调用 [setUseTestEnv:YES]
     * stateCode = -2	    调用次数太频繁，超出后台限制。请调整调用次数，每分钟最多 30 次
     * stateCode = 102	    域名解析超时，请更换网络验证是否正常
     * stateCode = 1042	    网络连不上，请检查当前网络是否正常
     * stateCode = 4102	    无法解析，请检查当前的网络是否正常
     * stateCode = 4103	    登录超时，请检查当前的网络是否正常
     * stateCode = 4124	    登录超时，请检查当前的网络是否正常
     * stateCode = 4131	    网络连接失败，一般由于网络波动或其他异常导致，请检查当前网络是否正常
     * stateCode = 1048677	环境不存在。请检查:1)setUseTestEnv 和 setBusinessType这两个接口是否设置正确
     * stateCode = 1048680	房间不存在，请检查房间是否存在
     * stateCode = 1049578	观众不允许创建房间。 LoginRoom的时候，role设置了audience，然后又调用了setRoomConfig接口将audienceCreateRoom的参数设置为FALSE了，导致观众无法创建房间。解决方案：将audienceCreateRoom的参数设置为TRUE就可以了
     * stateCode = 1050578	third token auth error(第三方token认证错误)。
     */

    /**
     * onPublishStateUpdate的错误码说明
     *
     * 错误码	            说明
     * stateCode = 0	    Success	直播成功
     * stateCode = 3	    FatalError	直播遇到严重错误。stateCode = 1/2/3 基本不会出现
     * stateCode = 4	    CreateStreamError	创建直播流失败。请确认 1)userId，userName 是否为空，2)流名是否被重复利用了。建议每次推流的流名都保持唯一。
     * stateCode = 7	    MediaServerNetWorkError	媒体服务器连接失败。请检查：1)推流端是否正常推流。2)正式环境和测试环境是否设置都是同一个。3).网络是否正常
     * stateCode = 8	    DNSResolveError	DNS 解析失败。原因: 推流节点为空，请确认startPublishing的flag是否为0/2.
     * stateCode = 9	    NotLoginError	未 loginRoom 就直接startpublishing。请确认推流前已 loginRoom
     * stateCode = 10	    LogicServerNetWrokError	逻辑服务器网络错误（网络断开约 3 分钟时会返回该错误）。请检查网络是否正常
     * stateCode = 105	    PublishBadNameError	推流BadName
     * stateCode = 66547	PublishForbidError	禁止推流, 请检查：是否已调用后台禁止推流接口禁止此streamid推流。
     * stateCode = 131073	PublishDeniedError	推流被拒绝
     * stateCode = 16777217	AddStreamError	添加流信息失败, 同一房间的两个用户推了同一条流名，导致第二个推流的用户推流被拒绝了
     */

    /**
     * onPlayStateUpdate的错误码说明
     *
     * 错误码	            说明
     * stateCode = 0	    Success	播放成功
     * stateCode = 3	    FatalError	直播遇到严重错误。stateCode = 1/2/3 基本不会出现
     * stateCode = 6	    NoStreamError	流不存在。请检查：1)AppID 是否相同，要保证一致。2)是否同时开启测试环境或同时在正式环境下
     * stateCode = 7	    MediaServerNetWorkError	媒体服务器连接失败。请检查：1)推流端是否正常推流。2)正式环境和测试环境是否设置都是同一个。3).网络是否正常
     * stateCode = 9	    NotLoginError	未 loginRoom 就直接 startPlayingStream。请确认拉流前已 loginRoom
     * stateCode = 197612	PlayStreamNotExistError	拉的流不存在, 请检查：拉流的streamid是否已推流成功。
     * stateCode = 197619	PlayForbidError	禁止拉流, 请检查：是否已调用后台禁止推流接口禁止此streamid拉流。
     * stateCode = 262145	PlayDeniedError	拉流被拒绝
     * stateCode = 16777219	ParameterError	拉流参数错误，请确认loginroom是否已经成功调用
     */

    /**
     * onDisconnect的错误码说明
     *
     * 错误码	                      说明
     * ErrorCode = 16777219	网络断开。 断网90秒仍没有恢复后会回调这个错误，onDisconnect后会停止推流和拉流
     */

    /**
     * OnKickOut的错误码说明
     *
     * 错误码	                       说明
     * ErrorCode = 16777219	被踢出房间。 有另外的设备用同样的userID登录了同样的房间，造成前面登录的用户被踢出房间
     * ErrorCode = 16777220	被踢出房间。 后台调用踢人接口将这个用户踢出房间
     */

    /**
     * onInitSDK的错误码说明
     *
     * 错误码	            说明
     * ErrorCode = -1001	配置文件错误，联系即构技术支持
     * ErrorCode > 0	    配置文件获取失败，检查网络状况
     */
}
