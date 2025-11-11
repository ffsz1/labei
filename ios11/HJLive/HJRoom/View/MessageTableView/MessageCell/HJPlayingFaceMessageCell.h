//
//  HJPlayingFaceMessageCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <NIMSDK/NIMSDK.h>
#import "HJIMMessage.h"

@interface HJPlayingFaceMessageCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *oneLabel;
@property (weak, nonatomic) IBOutlet UIImageView *oneImageView;

@property (weak, nonatomic) IBOutlet UILabel *twoLabel;
@property (weak, nonatomic) IBOutlet UIImageView *twoImageView;

@property (weak, nonatomic) IBOutlet UILabel *threeLabel;
@property (weak, nonatomic) IBOutlet UIImageView *threeImageView;

@property (weak, nonatomic) IBOutlet UILabel *fourLabel;
@property (weak, nonatomic) IBOutlet UIImageView *fourImageView;

@property (weak, nonatomic) IBOutlet UILabel *fiveLabel;
@property (weak, nonatomic) IBOutlet UIImageView *fiveImageView;

@property (weak, nonatomic) IBOutlet UILabel *sixLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sixImageView;

@property (weak, nonatomic) IBOutlet UILabel *sevenLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sevenImageView;

@property (weak, nonatomic) IBOutlet UILabel *eightLabel;
@property (weak, nonatomic) IBOutlet UIImageView *eightImageView;
@property (weak, nonatomic) IBOutlet UILabel *nineLabel;
@property (weak, nonatomic) IBOutlet UIImageView *nineImageView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *containerHeightConstraint;

@property (strong, nonatomic) HJIMMessage *message;
@property (weak, nonatomic) IBOutlet UIView *containerView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *containerViewWidthConstraint;

@end
