//
//  YPNIMKitRobotDefaultTemplateParser.h
//  YPNIMKit
//
//  Created by chris on 2017/6/25.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "NIMKitRobotTemplateParserProtocol.h"
#import "YPNIMKitRobotTemplate.h"

@class NIMMessage;

@interface YPNIMKitRobotDefaultTemplateParser : NSObject<NIMKitRobotTemplateParser>

- (void)clean;

- (YPNIMKitRobotTemplate *)robotTemplate:(NIMMessage *)message;

@end
