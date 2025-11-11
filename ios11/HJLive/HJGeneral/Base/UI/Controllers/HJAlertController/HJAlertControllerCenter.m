//
//  HJAlertControllerCenter.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAlertControllerCenter.h"

@implementation HJAlertControllerCenter

+ (instancetype)defaultCenter
{
    static dispatch_once_t onceToken = 0;
    static id instance;
    dispatch_once(&onceToken, ^{
        instance = [[self alloc] init];
    });
    
    return instance;
}

- (void)presentAlertWith:(UIViewController *)alertParaents view:(UIView *)view dismissBlock:(void (^)(void))dismissBlock {
//    [self presentAlertWith:alertParaents view:view dismissBlock:dismissBlock completionBlock:nil];
    [self presentAlertWith:alertParaents view:view preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:dismissBlock completionBlock:nil];
}

- (void)presentAlertWith:(UIViewController *)alertParaents view:(UIView *)view preferredStyle:(TYAlertControllerStyle)preferredStyle dismissBlock:(void (^)(void))dismissBlock completionBlock:(void (^)(void))completionBlock{
    
    UIView *superview;
    if (preferredStyle == TYAlertControllerStyleActionSheet && iPhoneX) {
        superview = [[UIView alloc] initWithFrame:CGRectMake(0, 0, view.frame.size.width, view.frame.size.height+34)];
        view.frame = CGRectMake(0, 0, view.frame.size.width, view.frame.size.height);
        [superview addSubview:view];
    }else {
        superview = view;
    }
    
    self.alert = [TYAlertController alertControllerWithAlertView:superview preferredStyle:preferredStyle transitionAnimation:(TYAlertTransitionAnimation)TYAlertTransitionAnimationFade];
    self.alert.backgoundTapDismissEnable = YES;
    
    if (self.alertViewOriginY > 0) {
        self.alert.alertViewOriginY = self.alertViewOriginY;
    }
    @weakify(self);
//    self.alert.dismissComplete = ^(){
//        @strongify(self);
//        dismissBlock();
//        self.alert = nil;
//    };
    [self.alert setDismissComplete:^{
        @strongify(self);
        self.alert = nil;
        self.alertViewOriginY = 0;
//        self.dismissComplete();
    }];
    [alertParaents presentViewController:self.alert animated:YES completion:completionBlock];
}

- (void)dismissAlertNeedBlock:(BOOL)needBlock {

    @weakify(self);
    self.alertViewOriginY = 0;
    [self.alert setDismissComplete:^{
        @strongify(self);
        self.alert = nil;
        if (needBlock && self.dismissComplete) {
            self.dismissComplete();
        }else {
            self.dismissComplete = nil;
        }
        
    }];

    [self.alert dismissViewControllerAnimated:YES completion:nil];
}

@end
