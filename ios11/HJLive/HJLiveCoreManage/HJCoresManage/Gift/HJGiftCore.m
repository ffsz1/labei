//
//  HJGiftCore.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//


#import "HJGiftCore.h"
#import "HJHttpRequestHelper+Gift.h"
#import "HJGiftInfoStorage.h"
#import "NSObject+YYModel.h"
#import "HJImMessageCore.h"
#import "HJImMessageCoreClient.h"
#import "Attachment.h"
#import "HJGiftSendInfo.h"
#import "HJGiftCoreClient.h"
#import "NSObject+YYModel.h"
#import "HJAuthCoreHelp.h"
#import "UserInfo.h"
#import "HJUserCoreHelp.h"
#import "HJPurseCoreClient.h"
#import "PurseCore.h"
#import "HJRoomCoreV2Help.h"
#import "HJImRoomCoreV2.h"
#import <SDWebImageDownloader.h>
#import <SDImageCache.h>
#import <SDWebImageManager.h>
#import "HJGiftAllMicroSendInfo.h"
#import "HJVersionCoreHelp.h"
#import "HJImMessageSendCoreClient.h"
#import "HJGiftSecretInfo.H"

@interface HJGiftCore()<HJImMessageCoreClient>
@property (nonatomic, strong)NSMutableArray *giftInfos;

@property (nonatomic, strong)NSMutableArray *orignGiftInfos;

@property (nonatomic, strong) NSMutableArray *mysticGiftInfos;
@property (nonatomic, strong) NSMutableArray *diandianGiftInfos;

@property (nonatomic, strong) dispatch_source_t timer; //计时器

@property (nonatomic, assign) NSInteger failRepeatCount;//请求失败重试次数
@property (nonatomic, assign) NSInteger failRepeatMaxCount;//请求失败重试最大次数
@property (nonatomic, assign) NSInteger failRepeatTime;//请求重试时间

@end
@implementation HJGiftCore
- (instancetype)init
{
    self = [super init];
    if (self) {
        AddCoreClient(HJImMessageCoreClient, self);
        AddCoreClient(HJImMessageSendCoreClient, self);
        _giftInfos = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeNormal];
        _mysticGiftInfos = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeMystic];
        
        if (_mysticGiftInfos == nil) {
            _mysticGiftInfos = [NSMutableArray array];
        }
        _diandianGiftInfos = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeDiandianCoin];
        
        
        _currentGiftMsgArr = [NSMutableArray array];
        _orignGiftInfos = [NSMutableArray array];
        
        
        _failRepeatCount = 0;
        _failRepeatMaxCount = 10;
        _failRepeatTime = 10;
    }
    return self;
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (NSMutableArray *)getGiftInfoListWithType:(NSInteger)type
{
    if (_giftInfos.count == 0 || _giftInfos == nil) {
        NSMutableArray *tempArr = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeNormal];
        for (GiftInfo *item in tempArr) {
            if (item.giftType == type) {
                [_giftInfos addObject:item];
            }
        }
    }
    return _giftInfos;
}

- (NSMutableArray *)getGameRoomGift {
    NSMutableArray *arr = [NSMutableArray array];
    for (GiftInfo *item in self.giftInfos) {
        if (!item.isNobleGift) {

                if (item.giftType == 2) {
                    [arr addObject:item];
                }
        }
        else {
            if (item.giftType == 2 && item.userGiftPurseNum != 0) {
                [arr addObject:item];
            }
        }
    }
    return arr;
}

- (NSMutableArray *)getNormalRoomGift {
    NSMutableArray *arr = [NSMutableArray array];
    for (GiftInfo *item in self.giftInfos) {
        if (!item.isNobleGift) {

                if (item.giftType == 1) {
                    [arr addObject:item];
                }
        }
    }
    return arr;
}

- (NSMutableArray *)getMysticGift {
    
    NSMutableArray *arr = [NSMutableArray array];
    for (GiftInfo *item in self.giftInfos) {
        if (item.giftType == 3 && item.userGiftPurseNum>0) {
            [arr addObject:item];
        }
    }
    
    return arr;
}

- (NSMutableArray *)getDiandianGift
{
    NSMutableArray *arr = [NSMutableArray array];
    for (GiftInfo *item in self.giftInfos) {
//        if (item.giftType == 5) {
         if (item.giftType == 4) {
            [arr addObject:item];
        }
    }
    
    return arr;
}

//- (void)setupDefaltMysticGift {
//    _mysticGiftInfos = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeMystic];
//}
//
//- (void)setupDefaltDiandianCoinGift {
//    _diandianGiftInfos = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeDiandianCoin];
//}

- (void)requestGiftList
{
    @weakify(self)
    [HJHttpRequestHelper requestGiftList:^(NSArray *list) {
        @strongify(self)
        self.giftInfos = [list mutableCopy];
        self.orignGiftInfos = [list mutableCopy];
        [HJGiftInfoStorage saveGiftInfos:[list yy_modelToJSONString] type:GiftTypeNormal];
        NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsRefresh), onGiftIsRefresh);
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}


//- (void)requestGiftListMystic {
//
//    @weakify(self)
//    [HJHttpRequestHelper giftListMysticSuccess:^(NSArray *list) {
//        @strongify(self)
//        self.failRepeatCount = 0;
//
//
//        //辣鸡服务器不排序，自己手动按价格排序
//        NSArray *resultNumArray = [list sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
//            GiftInfo *model1 = obj1;
//            GiftInfo *model2 = obj2;
//            NSNumber *number1 = [NSNumber numberWithInt:model1.goldPrice];
//            NSNumber *number2 = [NSNumber numberWithInt:model2.goldPrice];
//            NSComparisonResult result = [number1 compare:number2];
//
//            return  result == NSOrderedDescending; // 升序
//            //        return  result == NSOrderedSame; // 不变
//            //        return result == NSOrderedAscending;  // 降序
//        }];
//
//        self.mysticGiftInfos = [resultNumArray mutableCopy];
//
//
//
//
//        [HJGiftInfoStorage saveGiftInfos:[resultNumArray yy_modelToJSONString] type:GiftTypeMystic];
//        NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsRefresh), onGiftIsRefresh);
//    } failure:^(NSNumber *resCode, NSString *message) {
//
//
//    }];
//}

//- (void)requestGiftListDiandianCoin
//{
//    @weakify(self)
//    [HJHttpRequestHelper giftListDiandianCoinSuccess:^(NSArray *list) {
//        @strongify(self)
//        self.failRepeatCount = 0;
//
//
//        //辣鸡服务器不排序，自己手动按价格排序
//        NSArray *resultNumArray = [list sortedArrayUsingComparator:^NSComparisonResult(id obj1, id obj2) {
//            GiftInfo *model1 = obj1;
//            GiftInfo *model2 = obj2;
//            NSNumber *number1 = [NSNumber numberWithInt:model1.goldPrice];
//            NSNumber *number2 = [NSNumber numberWithInt:model2.goldPrice];
//            NSComparisonResult result = [number1 compare:number2];
//
//            return  result == NSOrderedDescending; // 升序
//        }];
//
//        self.diandianGiftInfos = [resultNumArray mutableCopy];
//
//        [HJGiftInfoStorage saveGiftInfos:[resultNumArray yy_modelToJSONString] type:GiftTypeDiandianCoin];
//        NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsRefresh), onGiftIsRefresh);
//    } failure:^(NSNumber *resCode, NSString *message) {
//
//
//    }];
//}

- (void)sendAllMicroGiftByUids:(NSString *)uids giftId:(NSInteger)giftId giftNum:(NSInteger)giftNum roomUid:(NSInteger)roomUid isAllMicroSend:(BOOL)isAllMicroSend giftyType:(GiftType)type goldPrice:(double)goldPrice {
    @weakify(self);
//    [MBProgressHUD showMessage:@"请稍后" toView:[UIApplication sharedApplication].keyWindow];
    [HJHttpRequestHelper sendAllMicroGiftByUids:uids giftId:giftId giftNum:giftNum roomUid:roomUid success:^(HJGiftAllMicroSendInfo *info) {
        
        if (isAllMicroSend) {
            Attachment *attachement = [[Attachment alloc]init];
            attachement.first = Custom_Noti_Header_ALLMicroSend;
            attachement.second = Custom_Noti_Sub_AllMicroSend;
            attachement.data = [info model2dictionary];
            NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
            [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
        }
        else {
            for (int i = 0; i < info.targetUids.count; i++) {
                [[GetCore(HJUserCoreHelp) requestUserInfo:[info.targetUids[i] integerValue]] subscribeNext:^(id x) {
                    UserInfo *userInfo = (UserInfo *)x;
                    NSDictionary *dic = [info model2dictionary];
                    HJGiftAllMicroSendInfo *sendInfo = [HJGiftAllMicroSendInfo yy_modelWithJSON:dic];
                    sendInfo.targetUids = nil;
                    sendInfo.targetNick = userInfo.nick;
                    sendInfo.targetUid = userInfo.uid;
                    sendInfo.targetAvatar = userInfo.avatar;
                    
                    Attachment *attachement = [[Attachment alloc]init];
                    attachement.first = Custom_Noti_Header_Gift;
                    attachement.second = Custom_Noti_Sub_Gift_Send;
                    attachement.data = [sendInfo model2dictionary];
                    NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
                    [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
                    
                } error:^(NSError *error) {
                    NSDictionary *dic = [info model2dictionary];
                    HJGiftAllMicroSendInfo *sendInfo = [HJGiftAllMicroSendInfo yy_modelWithJSON:dic];
                    sendInfo.targetUids = nil;
                    sendInfo.targetNick = @"";
                    sendInfo.targetUid = [info.targetUids[i] integerValue];
                    sendInfo.targetAvatar = @"";
                    
                    Attachment *attachement = [[Attachment alloc]init];
                    attachement.first = Custom_Noti_Header_Gift;
                    attachement.second = Custom_Noti_Sub_Gift_Send;
                    attachement.data = [sendInfo model2dictionary];
                    NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
                    [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
                }];
            }
        }
        
        
        
        [self minusGold:info giftyType:type];
        [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];

    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        if ([resCode integerValue] == 8000) {
            [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
            [self requestGiftList];
//            [self requestGiftListMystic];
            NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsOffLine:), onGiftIsOffLine:message);
            
        } else if ([resCode integerValue] == 2103) {
//            [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
            NotifyCoreClient(HJGiftCoreClient, @selector(onSendGiftFail), onSendGiftFail);
        }  else {
            [MBProgressHUD showError:message toView:[UIApplication sharedApplication].keyWindow];
        }
    }];
}


- (void)sendPointWholeMicro:(NSString *)uids giftId:(NSInteger)giftId giftNum:(NSInteger)giftNum roomUid:(NSInteger)roomUid isAllMicroSend:(BOOL)isAllMicroSend giftyType:(GiftType)type goldPrice:(double)goldPrice {
    @weakify(self);
    [MBProgressHUD showMessage:@"请稍后"];
    [HJHttpRequestHelper sendPointWholeMicro:uids giftId:giftId giftNum:giftNum roomUid:roomUid success:^(HJGiftAllMicroSendInfo *info) {
        
        if (isAllMicroSend) {
            Attachment *attachement = [[Attachment alloc]init];
            attachement.first = Custom_Noti_Header_ALLMicroSend;
            attachement.second = Custom_Noti_Sub_AllMicroSend;
            attachement.data = [info model2dictionary];
            NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
            [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
        }
        else {
            for (int i = 0; i < info.targetUids.count; i++) {
                [[GetCore(HJUserCoreHelp) requestUserInfo:[info.targetUids[i] integerValue]] subscribeNext:^(id x) {
                    UserInfo *userInfo = (UserInfo *)x;
                    NSDictionary *dic = [info model2dictionary];
                    HJGiftAllMicroSendInfo *sendInfo = [HJGiftAllMicroSendInfo yy_modelWithJSON:dic];
                    sendInfo.targetUids = nil;
                    sendInfo.targetNick = userInfo.nick;
                    sendInfo.targetUid = userInfo.uid;
                    sendInfo.targetAvatar = userInfo.avatar;
                    
                    Attachment *attachement = [[Attachment alloc]init];
                    attachement.first = Custom_Noti_Header_Gift;
                    attachement.second = Custom_Noti_Sub_Gift_Send;
                    attachement.data = [sendInfo model2dictionary];
                    NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
                    [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
                    
                } error:^(NSError *error) {
                    NSDictionary *dic = [info model2dictionary];
                    HJGiftAllMicroSendInfo *sendInfo = [HJGiftAllMicroSendInfo yy_modelWithJSON:dic];
                    sendInfo.targetUids = nil;
                    sendInfo.targetNick = @"";
                    sendInfo.targetUid = [info.targetUids[i] integerValue];
                    sendInfo.targetAvatar = @"";
                    
                    Attachment *attachement = [[Attachment alloc]init];
                    attachement.first = Custom_Noti_Header_Gift;
                    attachement.second = Custom_Noti_Sub_Gift_Send;
                    attachement.data = [sendInfo model2dictionary];
                    NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
                    [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
                }];
            }
        }
        
        
        
        [self minusGold:info giftyType:type];
        [MBProgressHUD hideHUD];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        if ([resCode integerValue] == 8000) {
            [MBProgressHUD hideHUD];
            [self requestGiftList];
            NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsOffLine:), onGiftIsOffLine:message);
            
        }else {
            [MBProgressHUD hideHUD];
        }
    }];
}



- (void) sendRoomGift:(NSInteger)giftId targetUid:(UserID)targetUid {
    
    [HJHttpRequestHelper sendGift:giftId targetUid:targetUid type:1 success:^{
        
    } failure:^(NSNumber *resCode, NSString *message) {
        
    }];
}

- (void)sendRoomGift:(NSInteger)giftId targetUid:(UserID)targetUid type:(NSInteger)type giftNum:(NSInteger)giftNum giftyType:(GiftType)giftType goldPrice:(double)goldPrice {
    @weakify(self);
    [MBProgressHUD showMessage:@"请稍后" toView:[UIApplication sharedApplication].keyWindow];
    [HJHttpRequestHelper sendGift:giftId targetUid:targetUid giftNum:giftNum type:type success:^(HJGiftAllMicroSendInfo *info) {

        Attachment *attachement = [[Attachment alloc]init];
        attachement.first = Custom_Noti_Header_Gift;
        attachement.second = Custom_Noti_Sub_Gift_Send;
        attachement.data = [info model2dictionary];
        NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
        
        [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
        
        [self minusGold:info giftyType:giftType];
        [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        if ([resCode integerValue] == 8000) {
            [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
            [self requestGiftList];
//            [self requestGiftListMystic];
            NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsOffLine:), onGiftIsOffLine:message);
        } else if ([resCode integerValue] == 2103) {
            [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
            NotifyCoreClient(HJGiftCoreClient, @selector(onSendGiftFail), onSendGiftFail);
        } else {
            [MBProgressHUD showError:message toView:[UIApplication sharedApplication].keyWindow];
        }
    }];
}

- (void)sendPoint:(NSInteger)giftId targetUid:(UserID)targetUid type:(NSInteger)type giftNum:(NSInteger)giftNum giftyType:(GiftType)giftType goldPrice:(double)goldPrice {
    @weakify(self);
    [MBProgressHUD showMessage:@"请稍后" toView:[UIApplication sharedApplication].keyWindow];
    [HJHttpRequestHelper sendPoint:giftId targetUid:targetUid giftNum:giftNum type:type success:^(HJGiftAllMicroSendInfo *info) {
        
        Attachment *attachement = [[Attachment alloc]init];
        attachement.first = Custom_Noti_Header_Gift;
        attachement.second = Custom_Noti_Sub_Gift_Send;
        attachement.data = [info model2dictionary];
        NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
        
        [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:sessionID type:JXIMSessionTypeChatroom];
        
        [self minusGold:info giftyType:giftType];
        [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
        
    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        if ([resCode integerValue] == 8000) {
            [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
            [self requestGiftList];
            NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsOffLine:), onGiftIsOffLine:message);
        }else{
            [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
        }
    }];
}

- (void)sendChatPoint:(NSInteger)giftID info:(UserInfo *)info giftNum:(NSInteger)giftNum targetUid:(UserID)targetUid  giftyType:(GiftType)type {
    __block NSInteger giftId = giftID;
    __block UserID targetUID = targetUid;
    __block UserInfo *userInfo = info;
    @weakify(self);
    [MBProgressHUD showMessage:@"请稍后" toView:[UIApplication sharedApplication].keyWindow];
    [HJHttpRequestHelper sendPoint:giftId targetUid:targetUID giftNum:giftNum type:2 success:^(HJGiftAllMicroSendInfo *info) {
        if (userInfo.nick.length > 0) {
            HJGiftSendInfo * gift = [[HJGiftSendInfo alloc]init];
            gift.uid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
            gift.targetUid = targetUID;
            gift.giftId = giftId;
            gift.nick = userInfo.nick;
            gift.avatar = userInfo.avatar;
            gift.giftNum = giftNum;
            gift.targetNick = userInfo.nick;
            gift.targetAvatar = userInfo.avatar;
            
            Attachment *attachement = [[Attachment alloc]init];
            attachement.first = Custom_Noti_Header_Gift;
            attachement.second = Custom_Noti_Sub_Gift_Send;
            attachement.data = gift.encodeAttachemt;
            
            [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%lld",targetUID] type:JXIMSessionTypeP2P needApns:YES apnsContent:[NSString stringWithFormat:@"%@给你送了%ld个礼物",userInfo.nick,giftNum] yidunEnable:false];
            
            [self minusGold:info giftyType:type];
            
        }
        [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];

    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
        if ([resCode integerValue] == 8000) {
            [self requestGiftList];
            NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsOffLine:), onGiftIsOffLine:message);
        }
    }];
}


- (void)sendChatGift:(NSInteger)giftID info:(UserInfo *)info giftNum:(NSInteger)giftNum targetUid:(UserID)targetUid  giftyType:(GiftType)type {
    __block NSInteger giftId = giftID;
    __block UserID targetUID = targetUid;
    __block UserInfo *userInfo = info;
    @weakify(self);
    [MBProgressHUD showMessage:@"请稍后..." toView:[UIApplication sharedApplication].keyWindow];

    [HJHttpRequestHelper sendGift:giftId targetUid:targetUID giftNum:giftNum type:2 success:^(HJGiftAllMicroSendInfo *info) {
            if (userInfo.nick.length > 0) {
                HJGiftSendInfo * gift = [[HJGiftSendInfo alloc]init];
                gift.uid = [GetCore(HJAuthCoreHelp)getUid].userIDValue;
                gift.targetUid = targetUID;
                gift.giftId = giftId;
                gift.nick = userInfo.nick;
                gift.avatar = userInfo.avatar;
                gift.giftNum = giftNum;
                gift.targetNick = userInfo.nick;
                gift.targetAvatar = userInfo.avatar;
                
                Attachment *attachement = [[Attachment alloc]init];
                attachement.first = Custom_Noti_Header_Gift;
                attachement.second = Custom_Noti_Sub_Gift_Send;
                attachement.data = gift.encodeAttachemt;

                [GetCore(HJImMessageCore)sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%lld",targetUID] type:JXIMSessionTypeP2P needApns:YES apnsContent:[NSString stringWithFormat:@"%@给你送了%ld个礼物",userInfo.nick,giftNum] yidunEnable:false];
                
                [self minusGold:info giftyType:type];

            }
        [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
    } failure:^(NSNumber *resCode, NSString *message) {
        @strongify(self);
        [MBProgressHUD hideHUDForView:[UIApplication sharedApplication].keyWindow];
        if ([resCode integerValue] == 8000) {
            [self requestGiftList];
//            [self requestGiftListMystic];
            NotifyCoreClient(HJGiftCoreClient, @selector(onGiftIsOffLine:), onGiftIsOffLine:message);
        }
    }];
}

- (void)replaceGiftInfoByInfo:(HJGiftAllMicroSendInfo *)ginfo giftyType:(GiftType)type {
//    if (type == GiftTypeNormal) {
    
        GiftInfo * giftInfo = [self findGiftInfoByGiftId:ginfo.giftId giftyType:type];
        if (giftInfo.userGiftPurseNum) {
            giftInfo.userGiftPurseNum = ginfo.userGiftPurseNum;
        }
//    }
    NotifyCoreClient(HJGiftCoreClient, @selector(updateGiftList:), updateGiftList:self.giftInfos);
}

- (GiftInfo *)findGiftInfoByGiftId:(NSInteger)giftId giftyType:(GiftType)type {
    
//    if (type == GiftTypeNormal) {
    
        if (self.orignGiftInfos.count > 0) {
            for (int i=0; i<self.orignGiftInfos.count ; i++) {
                GiftInfo *giftInfo = [self.orignGiftInfos objectAtIndex:i];
                if (giftInfo.giftId == giftId) {
                    return giftInfo;
                }
            }
        }
        return nil;
//    }
//    else if(type == GiftTypeMystic) {
//        if (self.mysticGiftInfos.count > 0) {
//            for (int i=0; i<self.orignGiftInfos.count ; i++) {
//                GiftInfo *giftInfo = [self.mysticGiftInfos objectAtIndex:i];
//                if (giftInfo.giftId == giftId) {
//                    return giftInfo;
//                }
//            }
//        }
//        return nil;
//    }else {
//        if (self.diandianGiftInfos.count > 0) {
//            for (int i=0; i<self.diandianGiftInfos.count ; i++) {
//                GiftInfo *giftInfo = [self.diandianGiftInfos objectAtIndex:i];
//                if (giftInfo.giftId == giftId) {
//                    return giftInfo;
//                }
//            }
//        }
//        return nil;
//    }
}



#pragma mark - ImMessageCoreClient
- (void)onWillSendMessage:(NIMMessage *)msg {
    if (msg.messageType == NIMMessageTypeCustom) {
        NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
            Attachment *attachment = (Attachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Gift) {
                if (attachment.second == Custom_Noti_Sub_Gift_Send) {
                    if (msg.session.sessionType == NIMSessionTypeChatroom) {
                        [self onRecvChatRoomCustomMsg:msg];
                    }else if (msg.session.sessionType == NIMSessionTypeP2P) {
                        [self onRecvP2PCustomMsg:msg];
                    }
                }
            }else if (attachment.first == Custom_Noti_Header_ALLMicroSend) {
                if (attachment.second == Custom_Noti_Sub_AllMicroSend) {
                    if (msg.session.sessionType == NIMSessionTypeChatroom) {
                        [self onRecvChatRoomCustomMsg:msg];
                    }else if (msg.session.sessionType == NIMSessionTypeP2P) {
                        [self onRecvP2PCustomMsg:msg];
                    }
                }
            }
        }
    }
}

- (void)onRecvChatRoomCustomMsg:(HJIMMessage *)msg {
    if (!msg.session.sessionId) {
        JXIMCustomObject *obj = msg.messageObject;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
            Attachment *attachment = (Attachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Gift || attachment.first == Custom_Noti_Header_ALLChannelSend){
                HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data];
                NotifyCoreClient(HJGiftCoreClient, @selector(onReceiveGift:isALLChannelSend:), onReceiveGift:info isALLChannelSend:(attachment.first == Custom_Noti_Header_ALLChannelSend));
                
            }else if (attachment.first == Custom_Noti_Header_Winning){
                HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data[@"params"]];
                info.messagetype = Custom_Noti_Header_Winning;
                NotifyCoreClient(HJGiftCoreClient, @selector(onReceiveGift:isALLChannelSend:), onReceiveGift:info isALLChannelSend:info.isFull);
            }
        }
        return;
    }

    if (GetCore(HJImRoomCoreV2).currentRoomInfo.roomId != [msg.session.sessionId integerValue]) return;
    
    JXIMCustomObject *obj = msg.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        if ((attachment.first == Custom_Noti_Header_Gift || attachment.first == Custom_Noti_Header_ALLMicroSend) && [msg.session.sessionId isEqualToString:[@(GetCore(HJImRoomCoreV2).currentRoomInfo.roomId) description]]) {
            [self.currentGiftMsgArr addObject:msg];
            self.lastGiftIsAll = (attachment.first == Custom_Noti_Header_ALLMicroSend);
            self.lastGiftInfo = [HJGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data];
        }
        if (attachment.first == Custom_Noti_Header_Gift || attachment.first == Custom_Noti_Header_ALLChannelSend){
                HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data];
                NotifyCoreClient(HJGiftCoreClient, @selector(onReceiveGift:isALLChannelSend:), onReceiveGift:info isALLChannelSend:(attachment.first == Custom_Noti_Header_ALLChannelSend));
            
        }
        
        if (attachment.first == Custom_Noti_Header_ALLMicroSend) {
            if (attachment.second == Custom_Noti_Sub_AllMicroSend) {
                    HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data];
                    info.targetUids = attachment.data[@"targetUids"];
                    NotifyCoreClient(HJGiftCoreClient, @selector(onReceiveGift:isALLChannelSend:), onReceiveGift:info isALLChannelSend:NO);
            }
        }
        else if (attachment.first == Custom_Noti_Header_SecretGift) {
            if (attachment.second == Custom_Noti_Sub_Gift_Send) {
                NSDictionary *dic = attachment.data;
                HJGiftSecretInfo *info = [HJGiftSecretInfo yy_modelWithDictionary:dic];
                if (info.uid == [GetCore(HJAuthCoreHelp) getUid].integerValue) {
                    
                    NotifyCoreClient(HJGiftCoreClient, @selector(didReceiveSecretGiftWithInfo:), didReceiveSecretGiftWithInfo:info);
                }
            }
        }else if (attachment.first == Custom_Noti_Header_Winning){
            HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data[@"params"]];
            info.messagetype = Custom_Noti_Header_Winning;
            NotifyCoreClient(HJGiftCoreClient, @selector(onReceiveGift:isALLChannelSend:), onReceiveGift:info isALLChannelSend:info.isFull);
        }
    }
}

//密聊收到礼物
- (void)onRecvP2PCustomMsg:(NIMMessage *)msg {
    NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        if (attachment.first == Custom_Noti_Header_Gift) {
//            HJGiftAllMicroSendInfo *info = [HJGiftAllMicroSendInfo yy_modelWithJSON:attachment.data];
//            NotifyCoreClient(GiftCoreClient, @selector(onReceiveGift:), onReceiveGift:info);
        }
    }
}

#pragma mark - Getter

- (NSMutableArray *)giftInfos {
    if (_giftInfos == nil || _giftInfos.count == 0) {
        _giftInfos =[HJGiftInfoStorage getGiftInfosWithtype:GiftTypeNormal];
    }
    return _giftInfos;
}

- (NSMutableArray *)mysticGiftInfos {
    if (_mysticGiftInfos == nil) {
        _mysticGiftInfos = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeMystic];
    }
    return _mysticGiftInfos;
}

- (NSMutableArray *)diandianGiftInfos
{
    if (_diandianGiftInfos == nil) {
        _diandianGiftInfos = [HJGiftInfoStorage getGiftInfosWithtype:GiftTypeDiandianCoin];
    }
    return _diandianGiftInfos;
}

#pragma mark - Private
- (void)minusGold:(HJGiftAllMicroSendInfo *)allMicroSendInfo giftyType:(GiftType)type {
    
//    if (type == GiftTypeNormal) {
    
        double needMinusGold = 0;
        GiftInfo * giftInfo = [self findGiftInfoByGiftId:allMicroSendInfo.giftId giftyType:type];
        if (allMicroSendInfo.targetUids.count > 0) { //全麦送
            NSInteger count = allMicroSendInfo.targetUids.count * allMicroSendInfo.giftNum;
            if (giftInfo.userGiftPurseNum - count >= 0) {
                needMinusGold = 0;
            } else {
                needMinusGold = (count - giftInfo.userGiftPurseNum) * giftInfo.goldPrice;
            }
        }else { //单人送
            if (giftInfo.userGiftPurseNum - allMicroSendInfo.giftNum >= 0) {
                needMinusGold = 0;
            } else {
                NSLog(@"%@",allMicroSendInfo);
                NSLog(@"%@",giftInfo);
                needMinusGold = (allMicroSendInfo.giftNum - giftInfo.userGiftPurseNum) * giftInfo.goldPrice;
            }
        }
        double oldGoldNum = [GetCore(PurseCore).balanceInfo.goldNum doubleValue];
        if (oldGoldNum == 0) {return;}
        double newGoldNum = oldGoldNum - needMinusGold;
        NSString *newGoldStr = [NSString stringWithFormat:@"%.2f",newGoldNum];
        GetCore(PurseCore).balanceInfo.goldNum = newGoldStr;
        NotifyCoreClient(HJPurseCoreClient, @selector(onBalanceInfoUpdate:), onBalanceInfoUpdate:GetCore(PurseCore).balanceInfo);
//    }
    
//    if(type == GiftTypeMystic){
//        GiftInfo * giftInfo = [self findGiftInfoByGiftId:allMicroSendInfo.giftId giftyType:GiftTypeMystic];
//        if (allMicroSendInfo.targetUids.count > 0) { //全麦送
//            NSInteger count = allMicroSendInfo.targetUids.count * allMicroSendInfo.giftNum;
//            if (giftInfo.userGiftPurseNum - count == 0) {
//
//                if (self.mysticGiftInfos.count >= 2) {
//
//                    [self.mysticGiftInfos removeObject:giftInfo];
//                }
//                else {
//                    self.mysticGiftInfos = [NSMutableArray array];
//                }
//            } else {
//                giftInfo.userGiftPurseNum = giftInfo.userGiftPurseNum - count;
//            }
//        }else { //单人送
//            if (giftInfo.userGiftPurseNum - allMicroSendInfo.giftNum == 0) {
//                if (self.mysticGiftInfos.count >= 2) {
//
//                    [self.mysticGiftInfos removeObject:giftInfo];
//                }
//                else {
//                    self.mysticGiftInfos = [NSMutableArray array];
//                }
//            } else {
//                giftInfo.userGiftPurseNum = giftInfo.userGiftPurseNum - allMicroSendInfo.giftNum;
//            }
//        }
//        [HJGiftInfoStorage saveGiftInfos:[self.mysticGiftInfos yy_modelToJSONString] type:GiftTypeMystic];
//    }
//    else{
//        GiftInfo * giftInfo = [self findGiftInfoByGiftId:allMicroSendInfo.giftId giftyType:GiftTypeDiandianCoin];
//        if (allMicroSendInfo.targetUids.count > 0) { //全麦送
//            NSInteger count = allMicroSendInfo.targetUids.count * allMicroSendInfo.giftNum;
//            if (giftInfo.userGiftPurseNum - count == 0) {
//
//                if (self.diandianGiftInfos.count >= 2) {
//
//                    [self.diandianGiftInfos removeObject:giftInfo];
//                }
//                else {
//                    self.diandianGiftInfos = [NSMutableArray array];
//                }
//            } else {
//                giftInfo.userGiftPurseNum = giftInfo.userGiftPurseNum - count;
//            }
//        }else { //单人送
//            if (giftInfo.userGiftPurseNum - allMicroSendInfo.giftNum == 0) {
//                if (self.diandianGiftInfos.count >= 2) {
//
//                    [self.diandianGiftInfos removeObject:giftInfo];
//                }
//                else {
//                    self.diandianGiftInfos = [NSMutableArray array];
//                }
//            } else {
//                giftInfo.userGiftPurseNum = giftInfo.userGiftPurseNum - allMicroSendInfo.giftNum;
//            }
//        }
//        [HJGiftInfoStorage saveGiftInfos:[self.diandianGiftInfos yy_modelToJSONString] type:GiftTypeDiandianCoin];
//    }
    [self replaceGiftInfoByInfo:allMicroSendInfo giftyType:type];
}

#pragma mark - Test //性能测试

- (void)cancelGiftTimer {
    self.timer = nil;
}

- (void)startGiftTimer {
    // 获得队列
    dispatch_queue_t queue = dispatch_get_main_queue();
    // 创建一个定时器(dispatch_source_t本质还是个OC对象)
    self.timer = dispatch_source_create(DISPATCH_SOURCE_TYPE_TIMER, 0, 0, queue);
    // 设置定时器的各种属性（几时开始任务，每隔多长时间执行一次）
    // GCD的时间参数，一般是纳秒（1秒 == 10的9次方纳秒）
    // 何时开始执行第一个任务
    // dispatch_time(DISPATCH_TIME_NOW, 1.0 * NSEC_PER_SEC) 比当前时间晚3秒
    dispatch_time_t start = dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC));
    uint64_t interval = (uint64_t)(0.5 * NSEC_PER_SEC);
    dispatch_source_set_timer(self.timer, start, interval, 0);
    
    // 设置回调
    @weakify(self);
    dispatch_source_set_event_handler(self.timer, ^{
        @strongify(self);
        
        [self sendRandomRoomGift];
        
    });
    // 启动定时器
    dispatch_resume(self.timer);
    
}

- (void)sendRandomRoomGift {
    NSInteger gifIndex = [self getRandomNumber:0 to:(int)self.giftInfos.count];
//    NSArray *micMembers = [GetCore(ImRoomCoreV2) ]
    NSInteger targetIndex = [self getRandomNumber:0 to:(int)GetCore(HJImRoomCoreV2).micMembers.count];
    ChatRoomMember *memeber = [GetCore(HJImRoomCoreV2).micMembers safeObjectAtIndex:targetIndex];
    GiftInfo *giftInfo = [self.giftInfos safeObjectAtIndex:gifIndex];
    
    [self sendRoomGift:giftInfo.giftId targetUid:[memeber.account longLongValue] type:2 giftNum:66 giftyType:GiftTypeNormal goldPrice:giftInfo.goldPrice];
}

/**
 生成随机数
 
 @param from 最小值
 @param to 最大值
 @return 随机数
 */
- (int)getRandomNumber:(int)from to:(int)to {
    return (int)(from + (arc4random() % (to - from + 1)));
}

- (GiftReceiveInfo *)lastGiftInfo {
    
    if (_lastGiftInfo) {
        return _lastGiftInfo;
    }
    
    if (self.currentGiftMsgArr.count) {
        
        NIMMessage *msg = [self.currentGiftMsgArr lastObject];
        NIMCustomObject *obj = (NIMCustomObject *)msg.messageObject;
        HJGiftAllMicroSendInfo *info = nil;
        if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
            Attachment *attachment = (Attachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Gift || attachment.first == Custom_Noti_Header_ALLMicroSend) {
                info = [HJGiftAllMicroSendInfo yy_modelWithDictionary:attachment.data];
            }
        }
        return info;
    }
    return nil;
}

@end
