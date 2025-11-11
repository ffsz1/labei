//
//  HJSignnAlterView.m
//  HJLive
//
//  Created by apple on 2019/5/23.
//

#import "HJSignnAlterView.h"

#import "NSDate+JXBase.h"

#import "JXMCDefines.h"
#import "UIView+getTopVC.h"
#import "YYViewControllerCenter.h"

#define mark

@implementation HJSignnAlterView

+ (void)show
{
    
    UIViewController *cla = [YYViewControllerCenter currentViewController];
    if ([cla.className isEqualToString:@"HJLoginViewController"]||[cla.className isEqualToString:@"HJLoginFillDataVC"]) {
        return;
    }
    
    
    UIWindow *window = [UIApplication sharedApplication].delegate.window;
    UIView *signView = [window viewWithTag:20000];
    if (signView) {
        return;
    }
    
    NSString *todayShow = [[NSUserDefaults standardUserDefaults]objectForKey:@"showSignDay"];
    
    //判断记录日期是否未今天
    if (todayShow) {
        
        NSDate *recordDate = [NSDate dateWithString:todayShow format:@"yyyy-MM-dd HH:mm:ss"];
        
        if (!recordDate.jx_isToday) {
            [self checkHadShowSign:nil];
        }
    }else{
        [self checkHadShowSign:nil];
    }
}

+ (void)showFromHome:(FinishBlock)finishBlock
{
    [self checkHadShowSign:finishBlock];
}

+ (void)checkHadShowSign:(FinishBlock)finishBlock
{
    [HJHttpRequestHelper requestMengCoinListSuccess:^(HJMMHomeInfoModel * _Nonnull data) {
        
        
        UIWindow *window = [UIApplication sharedApplication].delegate.window;
        UIView *adView = [window viewWithTag:9999];
        UIView *lastSignView = [window viewWithTag:20000];
        
        if (lastSignView) {
            if (finishBlock) {
                finishBlock();
            }
            return;
        }
        
        if (finishBlock) {
            finishBlock();
        }
        
        if (adView) {
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//                [HJSignnAlterView showAnimation:data];
            });
            
        }else{
//            [HJSignnAlterView showAnimation:data];
        }
        
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        if (finishBlock) {
            finishBlock();
        }
    }];
}



+ (void)showAnimation:(HJMMHomeInfoModel *)model
{
    HJSignnAlterView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJSignnAlterView" owner:self options:nil].lastObject;
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    shareView.tag = 20000;
    UIWindow *window = [UIApplication sharedApplication].delegate.window;

    [window addSubview:shareView];
    shareView.model = model;
    
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
    [[NSUserDefaults standardUserDefaults]setObject:dayStr forKey:@"showSignDay"];
    [[NSUserDefaults standardUserDefaults]synchronize];
    
    
    
}



- (IBAction)knowAction:(id)sender {
    
    UIViewController *vc = HJSignStoryBoard(@"HJSignHomeViewController");
    [[self topViewController].navigationController pushViewController:vc animated:YES];
    
    [self dismiss];
}

- (IBAction)closeAction:(id)sender {
    [self dismiss];
}

- (void)setModel:(HJMMHomeInfoModel *)model
{
    _model = model;
    
    
    if (model) {
        
        int signDay = 0;
        NSString *coin = @"";
        
        if (model.weeklyMissions.count==7) {
            for (int i = 0; i<model.weeklyMissions.count; i++) {
                HJMMHomeItemModel *dayModel = model.weeklyMissions[i];
                JXMCHomeItemStatus status = [dayModel.missionStatus integerValue];
                
                UIButton *btn = [self viewWithTag:3000+i];
                
                if (status == JXMCHomeItemStatusFinished) {
                    signDay = i;
                    coin = dayModel.mcoinAmount;
                    btn.selected = YES;
                }else{
                    btn.selected = NO;
                }
            }
        }
        
        self.dayLabel.text = [NSString stringWithFormat:@"已连续签到 %d 天",signDay+1];
        self.coinLabel.text = [NSString stringWithFormat:@"+%@",coin];
        
    }
    
}


@end
