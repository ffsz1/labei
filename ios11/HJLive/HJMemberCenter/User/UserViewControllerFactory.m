//
//  XBDUserViewControllerFactory.m
//  XBD
//
//  Created by 罗兴志 on 2017/5/22.
//  Copyright © 2017年 罗兴志. All rights reserved.
//

#import "XBDUserViewControllerFactory.h"
#import "XCPersonTableViewController.h"

@implementation XBDUserViewControllerFactory
+(instancetype)sharedFactory
{
    static id instance;
    static dispatch_once_t onceToken;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] initWithStoryboard:[UIStoryboard storyboardWithName:@"User" bundle:[NSBundle mainBundle]]];
    });
    return instance;
}

- (UIViewController *)instantiatePersonalViewController
{
    return  [XCPersonTableViewController new];
}

- (UIViewController *)instantiatePersonalEditViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"PersonalEditViewController"];
}

- (UIViewController *)instantiatePersonalVoiceEditViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"PersonalVoiceEditViewController"];
}

- (UIViewController *)instantiateSingleLineTextModifyViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"SingleLineTextModifyViewController"];
}

- (UIViewController *)instantiateMultiLineTextModifyViewController
{
    return [self.storyboard instantiateViewControllerWithIdentifier:@"MultiLineTextModifyViewController"];
}

- (UIViewController *)instantiateEditPersonalPhotosViewController {
    return [self.storyboard instantiateViewControllerWithIdentifier:@"XCEditPersonalPhotosContorller"];
}

@end
