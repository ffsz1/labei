//
//  HJEggRecordModel.h
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJEggRecordModel : NSObject

@property (nonatomic, assign)NSInteger giftId;
@property (nonatomic, copy)NSString *giftName;
@property (nonatomic, assign)NSInteger giftNum;
@property (nonatomic, assign)NSInteger goldCost;
@property (nonatomic, assign)NSInteger goldPrice;
@property (nonatomic, copy)NSString* createTime;
@property (nonatomic, copy)NSString *picUrl;

@end

NS_ASSUME_NONNULL_END
