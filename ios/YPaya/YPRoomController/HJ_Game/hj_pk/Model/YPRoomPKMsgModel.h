//
//  YPRoomPKMsgModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomPKMsgModel : NSObject
@property (nonatomic, assign)NSInteger recordId;
@property (nonatomic, assign)NSString *uid;
@property (nonatomic, copy)NSString *erbanNo;
@property (nonatomic, copy)NSString *nick;
@property (nonatomic, copy)NSString *avatar;
@property (nonatomic, copy)NSString *subject;
@property (nonatomic, assign)NSInteger experienceLevel;
@property (nonatomic, assign)NSInteger charmLevel;
@property (nonatomic, assign)NSInteger giftId;
@property (nonatomic, assign)NSInteger giftNum;
@property (nonatomic, copy)NSString *giftName;
@property (nonatomic, copy)NSString *giftUrl;
@property (nonatomic, copy)NSString *opponentNick;


@end

NS_ASSUME_NONNULL_END
