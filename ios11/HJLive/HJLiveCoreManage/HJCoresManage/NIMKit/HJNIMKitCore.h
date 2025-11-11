//
//  NIMKitCore.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "BaseCore.h"

@interface HJNIMKitCore : BaseCore

//注入布局
- (void)initLayoutConfig;

//注入解码器
- (void)initCustomerMsgDecoder;

@end
