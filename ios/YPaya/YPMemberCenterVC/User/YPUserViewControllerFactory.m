//
//  YPUserViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPUserViewControllerFactory.h"

@implementation YPUserViewControllerFactory
+(instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"User" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}



- (UIViewController *)instantiatePersonalEditViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPPersonalEditViewController"];
}

- (UIViewController *)instantiatePersonalVoiceEditViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPPersonalVoiceEditVC"];
}

- (UIViewController *)instantiateSingleLineTextModifyViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPSingleLineTextModifyVC"];
}

- (UIViewController *)instantiateMultiLineTextModifyViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPMultiLineTextModifyVC"];
}

- (UIViewController *)instantiateEditPersonalPhotosViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"YPEditPersonalPhotosVC"];
}

@end
