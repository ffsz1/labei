//
//  HJAdUIClient.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPAdInfo.h"

@protocol HJAdUIClient <NSObject>

@optional

- (void)onAdTap:(YPAdInfo *)adInfo;
- (void)onGoBtnClick;
@end
