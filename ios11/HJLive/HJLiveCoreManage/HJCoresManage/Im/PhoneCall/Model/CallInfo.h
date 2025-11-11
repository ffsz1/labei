//
//  CallInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "BaseObject.h"

@interface CallInfo : BaseObject

@property(assign, nonatomic)UserID uid;
@property(copy, nonatomic)NSString *nick;
@property(copy, nonatomic)NSDictionary *encodeAttchment;
@end
