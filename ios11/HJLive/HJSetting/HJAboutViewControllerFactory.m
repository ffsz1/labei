//
//  HJAboutViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAboutViewControllerFactory.h"

@implementation HJAboutViewControllerFactory

+ (instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"HJSetting" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

- (UIViewController *)instantiateAboutController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJAboutViewController"];
}

@end
