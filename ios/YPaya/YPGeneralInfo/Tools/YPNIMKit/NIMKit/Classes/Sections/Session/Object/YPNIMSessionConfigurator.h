//
//  NIMSessionViewConfigurator.h
//  YPNIMKit
//
//  Created by chris on 2016/11/7.
//  Copyright © 2016年 NetEase. All rights reserved.
//

#import "YPNIMSessionViewController.h"
#import "NIMSessionConfigurateProtocol.h"

@class YPNIMSessionViewController;

@interface YPNIMSessionConfigurator : NSObject

- (void)setup:(YPNIMSessionViewController *)vc;

@end
