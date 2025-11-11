//
//  YPRoomMemberListCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPRoomMemberListCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *roleImageView;
@property (weak, nonatomic) IBOutlet UIView *backView;
@property (weak, nonatomic) IBOutlet UIImageView *genderImageView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *managerMarginLeadingTag;
@property (weak, nonatomic) IBOutlet UIImageView *mamagerTag;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *onMicroWidthCOnstraint;
@property (weak, nonatomic) IBOutlet UIImageView *arrow;

@end
