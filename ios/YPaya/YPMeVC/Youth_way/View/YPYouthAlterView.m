//
//  YPYouthAlterView.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYouthAlterView.h"

#import "UIView+getTopVC.h"

#import "YPWKWebViewController.h"
//#import <NSDate+JXBase.h>
#import "NSDate+JXBase.h"
#import "YPYYViewControllerCenter.h"
#import "YPHttpRequestHelper+Youth.h"

@interface YPYouthAlterView ()

@property (weak, nonatomic) IBOutlet UIButton *knowBtn;

//@property (nonatomic, strong) NSTimer *timer;

@property (nonatomic,assign) int count;


@end

@implementation YPYouthAlterView

+ (void)updateYouthData
{
    NSString *uidKey = [NSString stringWithFormat:@"%@:isOnYouthSet",[GetCore(YPAuthCoreHelp) getUid]];
    
    [YPHttpRequestHelper getUsersTeensMode:^(BOOL hadSet) {

        [[NSUserDefaults standardUserDefaults]setObject:@(hadSet) forKey:uidKey];
        [[NSUserDefaults standardUserDefaults]synchronize];

    } failure:^(NSNumber *resCode, NSString *message) {
        [[NSUserDefaults standardUserDefaults]setObject:@(NO) forKey:uidKey];
        [[NSUserDefaults standardUserDefaults]synchronize];
    }];
}

+ (void)check
{
    [YPYouthAlterView updateYouthData];
    
    NSString *uidKey = [NSString stringWithFormat:@"%@:isOnYouthSet",[GetCore(YPAuthCoreHelp) getUid]];
    
    BOOL isSet = [[[NSUserDefaults standardUserDefaults]objectForKey:uidKey] boolValue];
    if (isSet) {
        return;
    }
    
    
    UIViewController *cla = [YPYYViewControllerCenter currentViewController];
    if ([cla.className isEqualToString:@"YPLoginViewController"]||[cla.className isEqualToString:@"YPLoginFillDataVC"]) {
        return;
    }
    
    UIWindow *window = [UIApplication sharedApplication].delegate.window;
    UIView *signView = [window viewWithTag:30000];
    if (signView) {
        return;
    }
    
    NSString *dayKey = [NSString stringWithFormat:@"%@:showYouthAlter",[GetCore(YPAuthCoreHelp) getUid]];
    NSString *todayShow = [[NSUserDefaults standardUserDefaults]objectForKey:dayKey];
    
//    NSDate *nowDate = [NSDate date];
//    BOOL isOnTime = nowDate.hour<=6||nowDate.hour>=22;
//    if (!isOnTime) {
//        return;
//    }
    
    //判断记录日期是否未今天
    if (todayShow) {
        NSDate *recordDate = [NSDate dateWithString:todayShow format:@"yyyy-MM-dd HH:mm:ss"];
        NSDate *todayDate = [NSDate date];
        if (![NSDate jx_isSameDay:recordDate date2:todayDate]) {
            [self checkHadShowAlter];
        }
    }else{
        [self checkHadShowAlter];
    }
}

+ (void)checkHadShowAlter
{
    UIWindow *window = [UIApplication sharedApplication].delegate.window;
    UIView *adView = [window viewWithTag:9999];
    UIView *lastSignView = [window viewWithTag:20000];
    
    if (lastSignView) {
        return;
    }
    
    if (adView) {
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            [YPYouthAlterView showAnimation];
        });
        
    }else{
        [YPYouthAlterView showAnimation];
    }

}

+ (void)showAnimation
{
    YPYouthAlterView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPYouthAlterView" owner:self options:nil].lastObject;
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    shareView.tag = 30000;
    UIWindow *window = [UIApplication sharedApplication].delegate.window;
    
    [window addSubview:shareView];
//    shareView.model = model;
    
    [shareView show];
    
    shareView.alpha = 0;
    [UIView animateWithDuration:0.3 animations:^{
        shareView.alpha = 1;
    } completion:^(BOOL finished) {
        shareView.alpha = 1;
    }];
    
    //保存签到日期
    NSDate *today = [NSDate date];
    NSString *dayStr = [today jx_stringWithFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSString *dayKey = [NSString stringWithFormat:@"%@:showYouthAlter",[GetCore(YPAuthCoreHelp) getUid]];
    [[NSUserDefaults standardUserDefaults]setObject:dayStr forKey:dayKey];
    [[NSUserDefaults standardUserDefaults]synchronize];
}


- (void)show
{
    self.hidden = NO;
    self.alpha = 0;
    [UIView animateWithDuration:0.3 animations:^{
        self.alpha = 1;
    } completion:^(BOOL finished) {
//        [self setTimer];
    }];
}

//- (void)setTimer
//{
//    self.timer = [NSTimer timerWithTimeInterval:1 target:self selector:@selector(updateBtnNum) userInfo:nil repeats:YES];
//    [[NSRunLoop currentRunLoop] addTimer:self.timer forMode:NSDefaultRunLoopMode];
//}

//- (void)updateBtnNum
//{
//    self.count += 1;
//
//    if (self.count >5) {
//        [self close];
//    }else{
//        [self.knowBtn setTitle:[NSString stringWithFormat:@"我知道了（%ds）",5-self.count] forState:UIControlStateNormal];
//    }
//}

- (void)close
{
//    [self.timer invalidate];
//    self.timer = nil;
    [self removeFromSuperview];
}

//点击-我知道了-按钮
- (IBAction)closeYouthBtnAction:(id)sender {
    
//    [self loadWebView:@"/front/teenager/index.html"];
    [self close];
    
}

//点击开启青少年模式按钮
- (IBAction)knowBtnAction:(id)sender {
    [self loadWebView:@"/front/teenager/index.html"];
    [self close];
    
}

- (void)loadWebView:(NSString *)url
{
    NSString *urlSting = [NSString stringWithFormat:@"%@%@",[YPHttpRequestHelper getHostUrl],url];
    
    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:urlSting];
    [[self topViewController].navigationController pushViewController:webView animated:YES];
}

@end
