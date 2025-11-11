//
//  ErrorInfo.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

@interface ErrorInfo : NSObject
@property(nonatomic, strong) NSNumber *code;
@property(nonatomic, strong) NSString *message;
@end
