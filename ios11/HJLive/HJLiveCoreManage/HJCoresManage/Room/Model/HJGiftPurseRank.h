//
//  HJGiftPurseRank.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface HJGiftPurseRank : NSObject

@property (nonatomic, assign) NSInteger erbanNo;
@property (nonatomic, copy) NSString *nick;
@property (nonatomic, copy) NSString *tol;
@property (nonatomic, assign) UserID uid;
@property (nonatomic, assign) NSInteger gender;
@property (nonatomic, assign) NSInteger charmLevel;
@property (nonatomic, assign) NSInteger experLevel;
@property (nonatomic, copy) NSString *avatar;

@end
