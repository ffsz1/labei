//
//  GiftInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface GiftInfo : BaseObject
@property (nonatomic, assign)NSInteger giftId;
@property (nonatomic, strong)NSString *giftName;
@property (nonatomic, assign)double goldPrice;
@property (nonatomic, strong)NSString *giftUrl;
@property (nonatomic, copy)NSString *gifUrl;
@property (nonatomic, assign) BOOL hasGifPic;
@property (nonatomic, assign) NSInteger giftType;//2普通礼物、3砸蛋礼物、5点点币礼物
@property (assign, nonatomic) BOOL hasVggPic;
@property (copy, nonatomic) NSString *vggUrl;
@property (assign, nonatomic) BOOL hasLatest; //是否最新
@property (assign, nonatomic) BOOL hasTimeLimit; //是否限时
@property (assign, nonatomic) BOOL hasEffect; //是否特效
@property (assign, nonatomic) BOOL isNobleGift; //是否是贵族礼物
@property (assign, nonatomic) NSInteger userGiftPurseNum;
@property (assign, nonatomic) NSInteger userGiftPurseGold;

@property (assign, nonatomic) NSInteger giftNum;
@property (nonatomic, strong)NSString *picUrl;
@property (nonatomic, assign)NSInteger ticketId;
@end
