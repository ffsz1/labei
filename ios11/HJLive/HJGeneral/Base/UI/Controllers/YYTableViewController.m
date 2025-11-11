//
//  YYTableViewController.m
//  YYMobile
//
//  Created by wuwei on 14/6/11.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYTableViewController.h"
#import "FBKVOController.h"
#import "UIViewController+YYViewControllers.h"
#import "YYDefaultTheme.h"

#define YYLoggerTag TControlers

@interface YYTableViewController ()

@end

@implementation YYTableViewController
{
    UIImageView *navBarHairlineImageView;
@private
    FBKVOController *_navigationKVOController;
}

- (id)initWithStyle:(UITableViewStyle)style
{
    self = [super initWithStyle:style];
//    if (self) {
        // Custom initialization
//    }
    return self;
}

- (void)viewDidLoad
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [super viewDidLoad];
    
    // Uncomment the following line to preserve selection between presentations.
    // self.clearsSelectionOnViewWillAppear = NO;
    
    // Uncomment the following line to display an Edit button in the navigation bar for this view controller.
    // self.navigationItem.rightBarButtonItem = self.editButtonItem;
    
    [self yy_viewDidLoad];
    UIImageView *navigationImageView = [self findHairlineImageViewUnder:self.navigationController.navigationBar];
    navBarHairlineImageView = navigationImageView;
    self.tableView.backgroundColor = [YYTheme defaultTheme].viewControllerBackgroundColor;
    self.navigationController.navigationBar.titleTextAttributes = @{UITextAttributeTextColor: [[YYDefaultTheme defaultTheme] colorWithHexString:@"#333333" alpha:1.0],
                                                                    UITextAttributeFont : [UIFont boldSystemFontOfSize:18]};
//    self.view.backgroundColor= [UIColor whiteColor];
}

- (void)viewWillAppear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    navBarHairlineImageView.hidden = YES;
    [super viewWillAppear:animated];
    // Hiido 页面访问统计 - 进入

}

- (void)viewDidAppear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [super viewDidAppear:animated];
    
    [self yy_viewDidAppear];
}

- (void)viewWillDisappear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [super viewWillDisappear:animated];
    // Hiido 页面访问统计 - 离开

    
    [self yy_viewWillDisappear];
}

- (void)viewDidDisappear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [super viewDidDisappear:animated];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)dealloc
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [self yy_dealloc];
}

- (UIStatusBarStyle)preferredStatusBarStyle {
    
    return [self yy_preferredStatusBarStyle];
}

- (BOOL)shouldAutorotate
{
    return NO;
}

- (UIInterfaceOrientationMask)supportedInterfaceOrientations NS_AVAILABLE_IOS(6_0)
{
    return UIInterfaceOrientationMaskPortrait;
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation NS_AVAILABLE_IOS(6_0)
{
    return UIInterfaceOrientationPortrait;
}

- (UIImageView *)findHairlineImageViewUnder:(UIView *)view {
    if ([view isKindOfClass:UIImageView.class] && view.bounds.size.height <= 1.0) {
        return (UIImageView *)view;
    }
    for (UIView *subview in view.subviews) {
        UIImageView *imageView = [self findHairlineImageViewUnder:subview];
        if (imageView) {
            return imageView;
        }
    }
    return nil;
}

@end
