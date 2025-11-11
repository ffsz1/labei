//
//  YPMicroListInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPMicroUserListInfo.h"
#import "NSObject+YYModel.h"
#import "YPBaseObject.h"

@interface YPMicroListInfo : YPBaseObject
@property(nonatomic, assign)UserID uid;
@property(nonatomic, strong)NSArray<YPMicroUserListInfo *> *data;
@end
