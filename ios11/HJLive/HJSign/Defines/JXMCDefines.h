//
//  JXMCDefines.h
//  XChat
//
//  Created by Colin on 2019/1/16.
//

#ifndef JXMCDefines_h
#define JXMCDefines_h

typedef NS_ENUM(NSInteger, JXMCHomeItemType) { ///< 首页任务类型
    JXMCHomeItemTypeUploadImages = 1,         ///< 上传4张照片
    JXMCHomeItemTypeAttentedActor = 2,        ///< 关注一个主播
    JXMCHomeItemTypeSendPublic = 3,           ///< 在大厅发言一次
    JXMCHomeItemTypeBindingPhone = 4,         ///< 绑定手机号
    JXMCHomeItemTypeSetupUserDescription = 5, ///< 设置个性签名
    JXMCHomeItemTypeShareVChatTimeline = 6,   ///< 分享朋友圈
    JXMCHomeItemTypeShareQZone = 7,           ///< 分享QQ空间
    JXMCHomeItemTypeRecharge = 8,             ///< 充值一次
    JXMCHomeItemTypeSendGift = 9,             ///< 送礼物一次
    JXMCHomeItemTypeDrawEgg = 10,              ///< 砸蛋一次
    JXMCHomeItemTypeStayRoom = 11,             ///< 房间停留30分钟
    JXMCHomeItemTypeSign1 = 12,               ///< 签到1次
    JXMCHomeItemTypeSign2 = 13,               ///< 签到2次
    JXMCHomeItemTypeSign3 = 14,               ///< 签到3次
    JXMCHomeItemTypeSign4 = 15,               ///< 签到4次
    JXMCHomeItemTypeSign5 = 16,               ///< 签到5次
    JXMCHomeItemTypeSign6 = 17,               ///< 签到6次
    JXMCHomeItemTypeSign7 = 18,               ///< 签到7次
    JXMCHomeItemTypeAuth = 24,               ///< 实名认证
    
};

typedef NS_ENUM(NSInteger, JXMCHomeItemStatus) { ///< 首页Item状态
    JXMCHomeItemStatusToDo     = 1, ///< 未完成
    JXMCHomeItemStatusReceive  = 2, ///< 领取
    JXMCHomeItemStatusFinished = 3, ///< 已完成
};

typedef NS_ENUM(NSInteger, JXMCHomeSectionType) { ///< 萌币首页组类别
    JXMCHomeSectionTypeCompete,  ///< 竞猜
    JXMCHomeSectionTypeBeginner, ///< 新手
    JXMCHomeSectionTypeDaily,    ///< 每日
};

#endif /* JXMCDefines_h */
