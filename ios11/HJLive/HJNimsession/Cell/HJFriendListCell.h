//
//  HJFriendListCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UserInfo.h"

@interface HJFriendListCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *usernameLabel;
@property (weak, nonatomic) UINavigationController *navigationController;
@property (strong, nonatomic) UserInfo *info;
@property (weak, nonatomic) IBOutlet UIButton *sendBtn;
@property (copy, nonatomic) void(^sendBlock)();

@property (nonatomic, assign) UserID uid;

@end
