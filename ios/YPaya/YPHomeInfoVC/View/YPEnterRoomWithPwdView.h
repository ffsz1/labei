//
//  YPEnterRoomWithPwdView.h
//  HJLive
//
//  Created by feiyin on 2020/6/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPChatRoomInfo.h"

@protocol HJEnterRoomWithPwdViewDelegate <NSObject>
@optional
- (void)closePwdView;
- (void)closePwdViewAndNeedPresent:(YPChatRoomInfo *)roomInfo;
@end

@interface YPEnterRoomWithPwdView : UIView
@property (weak, nonatomic) IBOutlet UITextField *pwdTextField;
@property (weak, nonatomic) IBOutlet UILabel *pwdErrTip;
@property (assign, nonatomic) UserID uid;
@property (copy, nonatomic) NSString *pwd;
@property (strong, nonatomic) YPChatRoomInfo *roomInfo;
@property (weak, nonatomic) id<HJEnterRoomWithPwdViewDelegate> delegate;
+ (instancetype)loadFromNib;
@end
