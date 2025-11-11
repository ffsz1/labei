//
//  YPTopAlertViewTool.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPTopAlertViewTool.h"
#import "YPNewUserDianChnagAlertView.h"
#import "YPAlertControllerCenter.h"
#import "YPRoomViewControllerCenter.h"

@interface YPTopAlertViewTool ()

@property (nonatomic, assign) BOOL hadShow;

@end

@implementation YPTopAlertViewTool

+ (instancetype)shareHJTopAlertViewTool
{
    static dispatch_once_t onceToken = 0;
    static id instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    return instance;
}


- (void)showUpdateViewWithDesc:(NSString *)desc version:(NSString *)version {
//    [self hidenAlertView];
    NSUserDefaults *userDefault = [NSUserDefaults standardUserDefaults];
    //    NSLog(@"之前时间：%@", [userDefault objectForKey:@"nowDate"]);//之前存储的时间
    //    NSLog(@"现在时间%@",[NSDate date]);//现在的时间
    NSDate *now = [NSDate date];
    NSDate *agoDate = [userDefault objectForKey:@"nowDate"];
    
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy-MM-dd"];
    
    NSString *ageDateString = [dateFormatter stringFromDate:agoDate];
    NSString *nowDateString = [dateFormatter stringFromDate:now];
    
    if  (![ageDateString isEqualToString:nowDateString]){
        if (!self.updateView) {
            NSDate *nowDate = [NSDate date];
            
            [userDefault setObject:nowDate forKey:@"nowDate"];
            [userDefault synchronize];
            
            
            NSString *message = [NSString stringWithFormat:@"%@:%@",NSLocalizedString(XCAlertUpdateTip, nil),version];
            self.updateView = [YPUpdateView loadFromNib];
            self.updateView.message = desc;
            self.updateView.title = message;
            @weakify(self);
            self.updateView.updateBlock = ^{
                @strongify(self);
                [self.updateView removeFromSuperview];
                NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://itunes.apple.com/cn/app/id%d",1457187790]];
                if (iOS10) {
                    
                    if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                        [[UIApplication sharedApplication]openURL:url options:@{}completionHandler:^(BOOL        success) {
                        }];
                    }
                }else {
                    if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                        [[UIApplication sharedApplication]openURL:url];
                    }
                    
                }
            };
            self.updateView.frame = CGRectMake(0, 0,  [UIApplication sharedApplication].keyWindow.frame.size.width, [UIApplication sharedApplication].keyWindow.frame.size.height);
            self.hadShow = YES;
            [[UIApplication sharedApplication].keyWindow addSubview:self.updateView];
            
        }
    }
    
}


- (void)showNoticeViewWithDesc:(NSString *)desc version:(NSString *)version{
    if (!self.noticeView) {
        NSString *message = [NSString stringWithFormat:@"%@:%@",NSLocalizedString(XCAlertUpdateTip, nil),version];
        @weakify(self);
        self.noticeView = [YPNoticeView loadFromNib];
        self.noticeView.message = desc;
        self.noticeView.title = message;
        self.noticeView.noticeBlock = ^{
            @strongify(self);
            NSURL *url = [NSURL URLWithString:[NSString stringWithFormat:@"https://itunes.apple.com/cn/app/id%d",1405457893]];
            
            if (iOS10) {
                
                if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                    [[UIApplication sharedApplication]openURL:url options:@{}completionHandler:^(BOOL        success) {
                    }];
                }
            }else {
                if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                    [[UIApplication sharedApplication]openURL:url];
                }
            }
            [self showNoticeViewWithDesc:desc version:version];
            
        };
        self.noticeView.frame = CGRectMake(0, 0, [UIApplication sharedApplication].keyWindow.frame.size.width, [UIApplication sharedApplication].keyWindow.frame.size.height);
        [[UIApplication sharedApplication].keyWindow addSubview:self.noticeView];
        
    }

}

- (void)showBadNetworkAlertView {
//    [self hidenAlertView];
    @weakify(self);
    if (!self.badNetworkAlertView) {
        self.badNetworkAlertView = [YPBadNetworkAlertView loadFromNib];
        [self.badNetworkAlertView setTitle:NSLocalizedString(XCAlertNiceTip, nil)];
        [self.badNetworkAlertView setDesc:NSLocalizedString(XCAlertCheckNetTip, nil)];
        self.badNetworkAlertView.rightBlock = ^{
            NSURL *url = [NSURL URLWithString:UIApplicationOpenSettingsURLString];
            if (iOS10) {
                
                if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                    [[UIApplication sharedApplication]openURL:url options:@{}completionHandler:^(BOOL        success) {
                    }];
                }
            }else {
                if( [[UIApplication sharedApplication]canOpenURL:url] ) {
                    [[UIApplication sharedApplication]openURL:url];
                }
            }
        };
        self.badNetworkAlertView.leftBlock = ^{
            @strongify(self);
            [self hidenAlertView];
            self.badNetworkAlertView = nil;
        };
        self.badNetworkAlertView.frame =  CGRectMake(0, 0, [UIApplication sharedApplication].keyWindow.frame.size.width, [UIApplication sharedApplication].keyWindow.frame.size.height);
        self.hadShow = YES;
        [[UIApplication sharedApplication].keyWindow addSubview:self.badNetworkAlertView];

    }
}

- (void)hidenAlertView {
    self.hadShow = NO;
    for (UIView *item in [UIApplication sharedApplication].keyWindow.subviews) {
        if ([item isKindOfClass:[YPUpdateView class]]) {
            [item removeFromSuperview];
        }
        if ([item isKindOfClass:[YPBadNetworkAlertView class]]) {
            [item removeFromSuperview];
        }
    }
}

- (void)showNewUserAlertWithViewController:(UIViewController *)vc
                                    roomId:(NSInteger)roomId
                                       uid:(NSInteger)uid
                                     title:(NSString *)title
                                    avatar:(NSString *)avatar {
    YPNewUserDianChnagAlertView *view = [[NSBundle mainBundle] loadNibNamed:@"YPNewUserDianChnagAlertView" owner:nil options:nil][0];
    view.title = title;
    view.avatar = avatar;
    
    @weakify(view);
    [view setCloseBtnActionBlock:^{
        [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    }];
    
    [view setDianchangActionBlock:^{
        [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:uid succ:^(YPChatRoomInfo *roomInfo) {
            if (roomInfo != nil && roomInfo.title.length > 0) {
                //根据房间信息开房
                [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
            }else {
                [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
            }
        } fail:^(NSString *errorMsg) {
            [MBProgressHUD showError:errorMsg];
        }];
    }];
    
    view.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [YPAlertControllerCenter defaultCenter].alertViewOriginY = 0;
    [[YPAlertControllerCenter defaultCenter]presentAlertWith:vc view:view preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
    
    [view addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
//        [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    }]];
}

@end
