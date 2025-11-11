//
//  YPIMReceiveMessageInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/6.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface YPIMReceiveMessageInfo : NSObject

@property (nonatomic, assign) NSInteger id;           ///< Id
@property (nonatomic, copy) NSString *route;          ///< 路由名
@property (nonatomic, strong) NSDictionary *req_data; ///< 应答业务参数

@end
