//
//  YPAboutViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPAboutViewControllerFactory.h"

@implementation YPAboutViewControllerFactory

+ (instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"YPSetting" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

- (UIViewController *)instantiateAboutController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPAboutViewController"];
}

@end
