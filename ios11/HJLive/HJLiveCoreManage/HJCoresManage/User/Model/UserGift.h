//
//  UserGift.h
//  HJLive
//
//  Created by feiyin on 2020/6/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

typedef enum {
    OrderType_Number = 1,
    OrderType_Price = 2
}OrderType;


@interface UserGift : NSObject
@property (assign, nonatomic) UserID uid;
@property (copy, nonatomic) NSString * giftId;
@property (copy, nonatomic) NSString * reciveCount;
@property (nonatomic, strong)NSString *picUrl;
@property (nonatomic, strong)NSString *giftName;
@end


