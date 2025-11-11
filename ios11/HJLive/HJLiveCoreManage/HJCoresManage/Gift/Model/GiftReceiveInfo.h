//
//  GiftReceiveInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface GiftReceiveInfo : BaseObject

//{
//    avatar = "https://nos.netease.com/nim/NDI3OTA4NQ==/bmltYV83NzcyMDExMDdfMTUwNjY3NTQ4OTg2OV9mODIyMTkzZi01OTIxLTQ5MDUtOWM4YS0yY2Q0Yzc4ZDA2YTY=";
//    giftId = 1010;
//    giftNum = 1;
//    nick = "\U5b88\U8b77\U3042\U98a8";
//    targetAvatar = "http://nim.nos.netease.com/NDI3OTA4NQ==/bmltd18wXzE1MDQxNzAxNzUxODJfOWIwYzczZTUtOGJkMC00NGFmLTliOTktNGU3OGNlNTcxMTNm";
//    targetNick = "\U5b88\U8b77\U3044\U3049\U5929\U4f7f";
//    targetUid = 90020;
//    uid = 90018;
//}

@property(nonatomic, assign)UserID uid;
@property(nonatomic, assign)UserID targetUid;
@property(nonatomic, assign)NSInteger giftId;
@property(nonatomic, strong)NSString *nick;
@property(nonatomic, strong)NSString *avatar;
@property (assign, nonatomic) NSInteger giftNum;
@property (copy, nonatomic) NSString *targetAvatar;
@property (copy, nonatomic) NSString *targetNick;
@property(nonatomic, assign) NSInteger experLevel;
@property(nonatomic, assign) NSInteger userNo;
@property(nonatomic, assign) NSInteger roomId;
@property(nonatomic, assign) NSInteger roomUid;
@property(nonatomic, assign) NSInteger userGiftPurseNum;
@property(nonatomic, assign) NSInteger userGiftPurseGold;
@property(nonatomic, assign) NSInteger giftSendTime;

@property(nonatomic, assign) NSInteger messagetype;
@property (assign, nonatomic) NSInteger count;
@property(nonatomic, strong)NSString *giftName;
@property (assign, nonatomic) NSInteger giftType;
@property(nonatomic, strong)NSString *giftUrl;
@property (assign, nonatomic) NSInteger goldPrice;
@property (assign, nonatomic) BOOL isFull;
@end

@interface MangheLiwuInfo : BaseObject



@end
