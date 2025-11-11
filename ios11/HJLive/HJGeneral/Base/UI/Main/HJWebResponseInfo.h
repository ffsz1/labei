//
//  HJWebResponseInfo.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJWebResponseInfo : NSObject
@property (nonatomic, copy) NSString *Id;
@property (nonatomic, strong) NSDictionary *data;

@end

NS_ASSUME_NONNULL_END
