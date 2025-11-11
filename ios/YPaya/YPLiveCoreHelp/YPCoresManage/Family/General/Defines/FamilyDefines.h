//
//  FamilyDefines.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#ifndef FamilyDefines_h
#define FamilyDefines_h

typedef NS_ENUM(NSInteger, XCFamilyRoleStatus) { ///< 家族角色状态
    XCFamilyRoleStatusLeader  = 1,               ///< 族长
    XCFamilyRoleStatusManager = 2,               ///< 管理员
    XCFamilyRoleStatusMember  = 3,               ///< 成员
};

typedef NS_ENUM(NSInteger, XCFamilyMemberLevelType) { ///< 家族成员级别
    XCFamilyMemberLevelTypeLeader,                    ///< 族长
    XCFamilyMemberLevelTypeViceLeader,                ///< 副族长
    XCFamilyMemberLevelTypeLord,                      ///< 王者
    XCFamilyMemberLevelTypeBroze,                     ///< 青铜
};

typedef NS_ENUM(NSInteger, XCFamilyManageItemType) { ///< 家族管理Item类型
    XCFamilyManageItemTypeManageFamily,              ///< 管理家族
    XCFamilyManageItemTypeApplicationRecord,         ///< 申请记录
    XCFamilyManageItemTypeExit,                      ///< 退出家族
    XCFamilyManageItemTypeMessageNotice,             ///< 消息免打扰
};

typedef NS_ENUM(NSInteger, XCFamilyManageSettingItemType) { ///< 家族管理设置Item类型
    XCFamilyManageSettingItemTypeRemoveMembers,             ///< 踢出成员
    XCFamilyManageSettingItemTypeSetupManager,              ///< 设置职位
    XCFamilyManageSettingItemTypeJoinWay,                   ///< 加入方式
};

typedef NS_ENUM(NSInteger, XCFamilyJoinWayType) { ///< 家族加入方式
    XCFamilyJoinWayTypeNormal = 0,                 ///< 无需验证
    XCFamilyJoinWayTypeSH  = 1,                 ///<  通过
    XCFamilyJoinWayTypeReject = 2,                 ///< 拒绝任何人加入
};

typedef NS_ENUM(NSInteger, XCFamilyApplicationType) { ///< 家族申请类型
    XCFamilyApplicationTypeJoin = 1,                  ///< 加入
    XCFamilyApplicationTypeExit = 2,                  ///< 退出
};

typedef NS_ENUM(NSInteger, XCFamilyApplicationStatus) { ///< 家族 状态
    XCFamilyApplicationStatusUndefie  = 0,              ///<  中
    XCFamilyApplicationStatusSuccess  = 1,              ///<  成功
    XCFamilyApplicationStatusFailure  = 2,              ///<  失败( 失败7天自动退出）
    XCFamilyApplicationStatusAutoExit = 3,              ///< 处理自动退出成功
};

typedef NS_ENUM(NSInteger, XCFamilyNoticeSettingType) { ///< 家族消息通知设置类型
    XCFamilyNoticeSettingTypeClose = 1,                 ///< 关闭
    XCFamilyNoticeSettingTypeOpen  = 2,                 ///< 打开
};

#endif /* FamilyDefines_h */
