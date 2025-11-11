//
//  HJRoomCoreClient.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "ChatRoomInfo.h"
#import "MicroListInfo.h"
#import "Attachment.h"
#import <NIMSDK/NIMSDK.h>
#import "ChatRoomMember.h"
#import "HJIMMessage.h"

#import "HJRoomMemberCharmInfoModel.h"

@protocol HJRoomCoreClient <NSObject>
@optional
- (void)onOpenRoomSuccess:(ChatRoomInfo *)roomInfo;
- (void)onOpenRoomFailth:(NSNumber *)resCode message:(NSString *)message;



- (void)onUpdateRoomInfoSuccess:(ChatRoomInfo *)roomInfo;
- (void)onUpdateRoomInfoFailth:(NSString *)message;

- (void)onCurrentRoomMsgUpdate:(NSMutableArray *)messages;

- (void)onSpeakUsersReport:(NSArray *)userInfos;
- (void)onMySpeakStateUpdate:(BOOL)speaking;

- (void)mySelfIsInBalckList:(BOOL)state;//当前用户是否在黑名单里面

- (void)onGetRoomBounsListSuucess:(NSMutableArray *)arr type:(NSString *)type dateType:(NSString *)dateType; //获取房间贡献榜成功
- (void)onGetRoomBounsListFailth:(NSString *)message type:(NSString *)type dateType:(NSString *)dateType; //获取房间贡献榜失败

- (void)userInterRoomWith:(ChatRoomMember *)member;//用户进入
- (void)onUserEnterChatRoomWith:(HJIMMessage *)message;//用户进入聊天室
- (void)userExitChatRoomWith:(NSString *)userId;//用户退出

- (void)requestUserRoomInterInfo:(ChatRoomInfo *)info uid:(UserID)uid; //用户当前进入房间信息成功
- (void)requestUserRoomInterInfoFailth:(NSString *)message; //获取用户当前进入房间信息失败
- (void)thereIsNoMicoPrivacy;//没有麦克风权限

- (void)onManagerAdd:(ChatRoomMember *)member;//添加管理员
- (void)onManagerRemove:(ChatRoomMember *)member;//移除管理员

- (void)userBeAddBlack:(ChatRoomMember *)member; //用户被加入黑名单
- (void)userBeRemoveBlack:(ChatRoomMember *)member; //用户被移除黑名单
- (void)bekillSuccess;

- (void)onGameRoomInfoUpdateSuccess:(ChatRoomInfo *)info isFromMessage:(BOOL)isFromMessage; //房间信息更新成功
- (void)onGameRoomInfoUpdateSuccessV2:(ChatRoomInfo *)info; //房间信息更新成功
- (void)onGameRoomInfoUpdateFailth:(NSString *)message; //房间信息更新失败
- (void)onHJRoomMicInfoChange; //麦序改变

//不需要
- (void)onGetRoomInfoSuccess:(ChatRoomInfo *)roomInfo;
- (void)onGetRoomInfoFailth:(NSString *)message;

- (void)onCloseRoomSuccess;
- (void)onCloseRoomFailth:(NSString *)message code:(NSNumber*)code;

- (void)onApplyMicroSuccess;
- (void)onApplyMicroFailth:(NSString *)message;

- (void)onUpMicroSuccess;
- (void)onUpMicroFailth:(NSString *)message;

- (void)onLeftMicroSuccess;
- (void)onLeftMicroFailth:(NSString *)message;

- (void)onRequestRoomInfosSuccess:(NSArray<ChatRoomInfo *> *)roomsInfo;
- (void)onRequestRoomInfosFailth:(NSString *)message;

- (void)onCurrentMicroListUpdateApplyList:(NSMutableArray *)applyList;

- (void)onRoomInfoUpdateSuccess; //房间信息有更新
- (void)fetchMembersInfoSuccess:(NSArray<NIMUser *> *)users;//批量获取用户信息成功
- (void)fetchMembersInfoFailth:(NSString *)message;//批量获取用户信息失败
- (void)fetchAllRegularMemberSuccess; //获取所有固定成员成功
- (void)fetchAllRegulatMemberFailth:(NSString *)message; //获取所有固定成员失败
- (void)userInfoUpdateWithInfo:(ChatRoomMember *)member; //用户信息修改（上下麦，跳坑）

- (void)updateMemberInBlackListSuccess;
- (void)updateMemberInBlackListFailth:(NSString *)message;

- (void)fetchMembersSuccess:(NSMutableArray *)members;//获取房间成员
- (void)fetchRegularMembersSuccess:(NSMutableArray *)members;

- (void)onDenyApplyMicroSuccess;
- (void)onDenyApplyMicroFailth:(NSString *)message;

- (void)onBeInviteByOwner:(NSString *)uid; //房主邀请上麦
- (void)onAgreeUpMicro:(NSString *)uid; //用户同意上麦
- (void)userLeftMicro; //用户离开麦序
- (void)onRecvBeKickedMicroMessage:(NSString *)uid; //接受被踢通知

- (void)userGiftPurseDrawSuccess:(id)data;
- (void)userGiftPurseDrawFail:(NSString *)message code:(NSNumber*)code;

// 砸蛋排行榜
- (void)userGiftPurseGetRankSuccessWithType:(NSInteger)type list:(NSMutableArray *)list;
- (void)userGiftPurseGetRankFailWithType:(NSInteger)type message:(NSString *)message;

// 砸蛋中奖纪录
- (void)userGiftPurseRecordSuccessWithList:(NSMutableArray *)list;
- (void)userGiftPurseRecordFailWithMessage:(NSString *)message;

//更新麦位魅力值
- (void)updateMicCharm:(NSMutableArray <HJRoomMemberCharmInfoModel *> *)memberCharmInfoList;
//清除魅力值
- (void)userReceiveRoomMicMsgSuccess;
- (void)userReceiveRoomMicMsgFail;
@end
