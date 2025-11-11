//
//  HJHJPushVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "YPHomeIcons.h"
NS_ASSUME_NONNULL_BEGIN

@interface YPPushVC : NSObject
+ (instancetype)shared;

- (void)pushWithJson:(id)json withvc:(UIViewController *)vc;

- (void)pushWithModel:(YPHomeIcons *)icon withvc:(UIViewController *)vc;

@end

NS_ASSUME_NONNULL_END
