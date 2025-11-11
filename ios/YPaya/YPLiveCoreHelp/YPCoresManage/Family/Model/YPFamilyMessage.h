//
//  XCFamilyMessage.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "FamilyDefines.h"

@interface YPFamilyMessage : NSObject


//uid
@property (nonatomic, assign) NSInteger uid;
//果果号
@property (nonatomic, assign) NSInteger erbanNo;
//魅力等级
@property (nonatomic, assign) NSInteger charm;
//logo
@property (nonatomic, copy) NSString *avatar;
//名称
@property (nonatomic, copy) NSString *nick;
//财富等级
@property (nonatomic, assign) NSInteger level;
//类型(1.加入 ,退出)
@property (nonatomic, assign) NSInteger type;
/// 状态
@property (nonatomic, assign) XCFamilyApplicationStatus status;

@end
