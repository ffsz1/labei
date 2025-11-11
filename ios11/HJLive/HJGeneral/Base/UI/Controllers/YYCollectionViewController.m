//
//  YYCollectionViewController.m
//  YYMobile
//
//  Created by wuwei on 14/6/11.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYCollectionViewController.h"
#import "UIViewController+YYViewControllers.h"


#define YYLoggerTag TBase

@interface YYCollectionViewController ()

@end

@implementation YYCollectionViewController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
//    if (self) {
        // Custom initialization
//    }
    return self;
}

- (void)viewDidLoad
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [super viewDidLoad];

    [self yy_viewDidLoad];
}

- (void)viewWillAppear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
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
    
    self.collectionView.dataSource = nil;
    self.collectionView.delegate = nil;
    self.collectionView = nil;
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
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

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender
{
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
