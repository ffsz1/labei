//
//  YPXCTurntableAlertView.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPXCTurntableAlertView.h"
#import "YPRoomViewControllerCenter.h"
#import "TYAlertController.h"
#import "HJAdUIClient.h"

@implementation YPXCTurntableAlertView

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPXCTurntableAlertView" owner:self options:nil].lastObject;
}

- (IBAction)goBtnClick:(UIButton *)sender {
    NotifyCoreClient(HJAdUIClient, @selector(onGoBtnClick), onGoBtnClick);
}

- (IBAction)closeBtnClick:(UIButton *)sender {
    UIViewController *vc = [[YPRoomViewControllerCenter defaultCenter]getCurrentVC];
    if ([vc isKindOfClass:[TYAlertController class]]) {
        [(TYAlertController *)vc dismissViewControllerAnimated:NO completion:^{
            
        }];
    }
}

@end
