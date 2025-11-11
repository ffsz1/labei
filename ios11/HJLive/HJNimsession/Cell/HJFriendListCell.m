//
//  HJFriendListCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFriendListCell.h"
#import "HJUserViewControllerFactory.h"
#import "HJMySpaceVC.h"

@implementation HJFriendListCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.avatar.layer.cornerRadius = 30;
    self.avatar.layer.masksToBounds = YES;
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(pushToUserInfoVC)];
    self.avatar.userInteractionEnabled = YES;
    [self.avatar addGestureRecognizer:tap];
}

- (IBAction)sendClick:(UIButton *)sender {
    if (self.sendBlock) {
        self.sendBlock();
    }
}

- (void)pushToUserInfoVC {
    
    
    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
    vc.userID = self.info.uid;
    [self.navigationController pushViewController:vc animated:YES];
    
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

}

@end
