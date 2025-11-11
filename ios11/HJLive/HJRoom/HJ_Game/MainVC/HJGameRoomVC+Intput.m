//
//  HJGameRoomVC+Intput.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+Intput.h"

@implementation HJGameRoomVC (Intput)

- (void)keyboardWillShow:(NSNotification *)notification {
    [self.messageTableView reloadData];
    self.keyboardIsShow = YES;
    [self.view bringSubviewToFront:self.editView];
    
    NSDictionary *info = [notification userInfo];
    CGFloat duration = [[info objectForKey:UIKeyboardAnimationDurationUserInfoKey] floatValue];
//    CGRect beginKeyboardRect = [[info objectForKey:UIKeyboardFrameBeginUserInfoKey] CGRectValue];
    CGRect endKeyboardRect = [[info objectForKey:UIKeyboardFrameEndUserInfoKey] CGRectValue];
    CGFloat h = endKeyboardRect.size.height;
    
    @weakify(self);
    [UIView animateWithDuration:duration animations:^{
        @strongify(self);
        if (iPhoneX) {
//            self.editViewBottomConstraint.constant = h - 34; //34为iphoneX home indicator的高度
            self.editViewBottomConstraint.constant = h;
        }else {
            self.editViewBottomConstraint.constant = h;
        }
        
        self.editView.hidden = NO;
        [self.view layoutIfNeeded];
    }];
}

//键盘隐藏
- (void)keyboardWillHidden:(NSNotification *)notification {
    
    NSDictionary *info = [notification userInfo];
    CGFloat duration = [[info objectForKey:UIKeyboardAnimationDurationUserInfoKey] floatValue];
    
    self.keyboardIsShow = NO;
    self.editViewBottomConstraint.constant = 0;
    
    @weakify(self);
    [UIView animateWithDuration:duration animations:^{
        @strongify(self);
        self.editView.hidden = YES;
        [self.view layoutIfNeeded];
    }];
}

- (void)hideKeyboard {
    [self.editText resignFirstResponder];
}


@end
