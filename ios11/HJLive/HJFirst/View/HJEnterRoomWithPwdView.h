//
//  HJEnterRoomWithPwdView.h
//  HJLive
//
//  Created by feiyin on 2020/6/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ChatRoomInfo.h"

@protocol HJEnterRoomWithPwdViewDelegate <NSObject>
@optional
- (void)closePwdView;
- (void)closePwdViewAndNeedPresent:(ChatRoomInfo *)roomInfo;
@end

@interface HJEnterRoomWithPwdView : UIView
@property (weak, nonatomic) IBOutlet UITextField *pwdTextField;
@property (weak, nonatomic) IBOutlet UILabel *pwdErrTip;
@property (assign, nonatomic) UserID uid;
@property (copy, nonatomic) NSString *pwd;
@property (strong, nonatomic) ChatRoomInfo *roomInfo;
@property (weak, nonatomic) id<HJEnterRoomWithPwdViewDelegate> delegate;
+ (instancetype)loadFromNib;
@end
