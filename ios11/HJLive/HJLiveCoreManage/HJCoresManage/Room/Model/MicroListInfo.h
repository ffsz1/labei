//
//  MicroListInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "HJMicroUserListInfo.h"
#import "NSObject+YYModel.h"
#import "BaseObject.h"

@interface MicroListInfo : BaseObject
@property(nonatomic, assign)UserID uid;
@property(nonatomic, strong)NSArray<HJMicroUserListInfo *> *data;
@end
