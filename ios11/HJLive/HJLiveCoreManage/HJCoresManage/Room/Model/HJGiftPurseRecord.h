//
//  HJGiftPurseRecord.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HJGiftPurseRecord : NSObject

@property (nonatomic, assign) NSInteger createTime;
@property (nonatomic, assign) NSInteger giftId;
@property (nonatomic, assign) NSInteger giftNum;
@property (nonatomic, copy) NSString *giftName;
@property (nonatomic, copy) NSString *picUrl;
@property (nonatomic, assign) NSInteger goldCost;

@end
