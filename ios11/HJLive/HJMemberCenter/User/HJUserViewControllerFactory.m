//
//  HJUserViewControllerFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJUserViewControllerFactory.h"

@implementation HJUserViewControllerFactory
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
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJPersonalEditViewController"];
}

- (UIViewController *)instantiatePersonalVoiceEditViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJPersonalVoiceEditVC"];
}

- (UIViewController *)instantiateSingleLineTextModifyViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJSingleLineTextModifyVC"];
}

- (UIViewController *)instantiateMultiLineTextModifyViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJMultiLineTextModifyVC"];
}

- (UIViewController *)instantiateEditPersonalPhotosViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"HJEditPersonalPhotosVC"];
}

@end
