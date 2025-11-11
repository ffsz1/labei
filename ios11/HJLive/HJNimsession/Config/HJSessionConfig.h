//
//  HJSessionConfig.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NIMSessionConfig.h"
NS_ASSUME_NONNULL_BEGIN

@interface HJSessionConfig : NSObject<NIMSessionConfig>
@property (nonatomic,strong)    NIMSession *session;
@end

NS_ASSUME_NONNULL_END


