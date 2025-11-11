//
//  HJRoomBounsListInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "UserInfo.h"
#import "BaseObject.h"

@interface HJRoomBounsListInfo : BaseObject
@property (assign, nonatomic) UserID uid;
@property (assign, nonatomic) UserID ctrbUid;
@property (copy, nonatomic) NSString *sumGold;
@property (copy, nonatomic) NSString *nick;
@property (copy, nonatomic) NSString *avatar;
@property (assign, nonatomic) UserGender gender;
/** 财富等级*/
@property (assign, nonatomic) NSInteger experLevel;
/** 魅力等级*/
@property (assign, nonatomic) NSInteger charmLevel;
@end
