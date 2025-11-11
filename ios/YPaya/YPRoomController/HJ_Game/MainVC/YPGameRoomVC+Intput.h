//
//  YPGameRoomVC+Intput.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomVC.h"

@interface YPGameRoomVC (Intput)

- (void)keyboardWillShow:(NSNotification *)notification;
- (void)keyboardWillHidden:(NSNotification *)notification;
- (void)hideKeyboard;

@end
