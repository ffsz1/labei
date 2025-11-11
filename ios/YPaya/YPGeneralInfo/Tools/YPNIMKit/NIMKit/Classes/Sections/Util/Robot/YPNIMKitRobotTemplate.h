//
//  YPNIMKitRobotTemplate.h
//  YPNIMKit
//
//  Created by chris on 2017/6/25.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPNIMKitRobotTemplateLayout.h"

@interface YPNIMKitRobotTemplate : NSObject<NIMKitRobotTemplateContainer>

@property (nonatomic, copy) NSString *templateId;

@property (nonatomic, copy) NSString *param;

@property (nonatomic, copy) NSString *version;

@property (nonatomic, strong) NSMutableArray<YPNIMKitRobotTemplateLayout *> *items;

@end
