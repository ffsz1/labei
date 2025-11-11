//
//  YYImageViewControllerFactory.m
//  YYMobile
//
//  Created by James Pend on 14-8-27.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//
#import "YYImageViewControllerFactory.h"

@implementation YYImageViewControllerFactory

+ (instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"YYMessageImageViewController" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

- (UIViewController *)imageViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YYMessageImageViewController"];
}


@end
