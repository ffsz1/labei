//
//  YPRoomPKRecordModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPKRecordModel : NSObject

@property (nonatomic, assign)NSString *uid;
@property (nonatomic, copy)NSString *avatar;
@property (nonatomic, assign)NSInteger createTime;


@property (nonatomic, copy)NSString *erBanNo;
@property (nonatomic, assign)NSInteger giftId;
@property (nonatomic, assign)NSInteger num;
@property (nonatomic, copy)NSString *giftUrl;
@property (nonatomic, copy)NSString *giftName;

@property (nonatomic, copy)NSString *nick;
@property (nonatomic, copy)NSString *subject;


@end

NS_ASSUME_NONNULL_END
