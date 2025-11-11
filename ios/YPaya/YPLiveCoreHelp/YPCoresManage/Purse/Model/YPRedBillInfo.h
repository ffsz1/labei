//
//  YPRedBillInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/5.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, RedBillType) {
    RedBillType_Register = 1, //申请成为分享小能手
    RedBillType_OtherRegisterAfterShare = 3,//分享后用户注册
    RedBillType_RechargeDevide = 4//充值
};

@interface YPRedBillInfo : NSObject
@property (strong, nonatomic) NSString * typeStr;
@property (assign, nonatomic) UserID uid;
@property (strong, nonatomic) NSString * packetNum;
@property (assign, nonatomic) NSInteger recordTime;
@property (assign, nonatomic) NSInteger date;
@end
