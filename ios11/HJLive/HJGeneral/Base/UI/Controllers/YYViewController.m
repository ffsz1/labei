//
//  YYViewController.m
//  YYMobile
//
//  Created by wuwei on 14/6/11.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYViewController.h"
#import "FBKVOController.h"
#import "UIViewController+YYViewControllers.h"

#import "YYLogger.h"
#import "YYViewControllerClient.h"
#import "YYDefaultTheme.h"


#define YYLoggerTag TControlers

@interface YYViewController ()
{
    UIImageView *navBarHairlineImageView;
@private
    FBKVOController *_navigationKVOController;
}

@end

@implementation YYViewController

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
    [self findHairlineImageViewUnder:self.navigationController.navigationBar];
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self yy_viewDidLoad];
    UIImageView *navigationImageView = [self findHairlineImageViewUnder:self.navigationController.navigationBar];
    navBarHairlineImageView = navigationImageView;
    self.view.backgroundColor = [YYTheme defaultTheme].viewControllerBackgroundColor;
    self.navigationController.navigationBar.titleTextAttributes = @{UITextAttributeTextColor: [[YYDefaultTheme defaultTheme] colorWithHexString:@"#333333" alpha:1.0],
                                                                    UITextAttributeFont : [UIFont boldSystemFontOfSize:18]};
}

- (void)viewWillAppear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    navBarHairlineImageView.hidden = YES;
    [super viewWillAppear:animated];

}

- (void)viewDidAppear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [super viewDidAppear:animated];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    navBarHairlineImageView.hidden = NO;
    [super viewWillDisappear:animated];
    
    [self yy_viewWillDisappear];
    
}

- (void)viewDidDisappear:(BOOL)animated
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [super viewDidDisappear:animated];
    

}

- (void)dealloc
{
    [YYLogger info:YYLoggerTag message:@"%@ %s", NSStringFromClass([self class]), __FUNCTION__];
    
    [self yy_dealloc];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (UIStatusBarStyle)preferredStatusBarStyle
{
    
    return [self yy_preferredStatusBarStyle];
}

//- (BOOL)prefersStatusBarHidden
//{
//    return NO;
//}

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

- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation duration:(NSTimeInterval)duration {
    
    [super willRotateToInterfaceOrientation:toInterfaceOrientation duration:duration];
    
    NotifyCoreClient(YYViewControllerClient, @selector(viewContrllerWillTrunOrientation:), viewContrllerWillTrunOrientation:self);
}

- (void)didRotateFromInterfaceOrientation:(UIInterfaceOrientation)fromInterfaceOrientation {
    
    [super didRotateFromInterfaceOrientation:fromInterfaceOrientation];
    
    NotifyCoreClient(YYViewControllerClient, @selector(viewContrller:orientation:), viewContrller:self orientation:fromInterfaceOrientation)
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

- (void)setupHairlineImageViewHidden:(BOOL)hidden {
    
    navBarHairlineImageView.hidden = hidden;
}


@end
