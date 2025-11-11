//
//  HJEnterRoomWithPwdView.m
//  HJLive
//
//  Created by feiyin on 2020/6/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJEnterRoomWithPwdView.h"
#import "HJRoomViewControllerCenter.h"
#import "UIView+XCToast.h"

@implementation HJEnterRoomWithPwdView

- (void)awakeFromNib {
    
    [super awakeFromNib];
    
//    self.pwdTextField.layer.cornerRadius = 20;
//    self.pwdTextField.layer.masksToBounds = YES;
}

+ (instancetype)loadFromNib {
    [MBProgressHUD hideHUD];
    return [[NSBundle mainBundle] loadNibNamed:@"HJEnterRoomWithPwdView" owner:self options:nil].lastObject;
}


- (IBAction)enterChatRoomBtnClick:(UIButton *)sender {
    if ([self.pwdTextField.text isEqualToString:self.pwd]) {
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        [_delegate closePwdViewAndNeedPresent:self.roomInfo];
        self.pwdErrTip.hidden = YES;
    }else {
        self.pwdErrTip.hidden = NO;
        [MBProgressHUD showError:NSLocalizedString(XCHudPswError, nil)];
    }
}


- (IBAction)cancelBtnClick:(UIButton *)sender {
//    if ([_delegate respondsToSelector:@selector(closePwdView)]) {
        [_delegate closePwdView];
//    }
}


@end
