//
//  HJGiftInfoStorage.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef NS_ENUM(NSUInteger, GiftType) {
    GiftTypeNormal = 0, // 房间正常礼物
    GiftTypeMystic,     // 背包礼物
    GiftTypeDiandianCoin,     // 点点币礼物
};

@interface HJGiftInfoStorage : NSObject

+ (NSMutableArray *)getGiftInfosWithtype:(GiftType)type;
+ (void) saveGiftInfos:(NSString *)json type:(GiftType)type;
@end
