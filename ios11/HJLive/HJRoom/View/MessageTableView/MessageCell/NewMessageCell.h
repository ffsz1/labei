//
//  NewMessageCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <YYText.h>

@interface NewMessageCell : UITableViewCell

@property (nonatomic, strong) YYLabel *userLabel;
@property (strong, nonatomic) YYLabel *messageLabel;
@property (strong, nonatomic) UIView *labelContentView;
@property (strong, nonatomic) UIImageView *bgImage;

@property (nonatomic, assign) CGFloat userTopOffset;

- (void)updatePreferredMaxLayoutWidth;

@end
