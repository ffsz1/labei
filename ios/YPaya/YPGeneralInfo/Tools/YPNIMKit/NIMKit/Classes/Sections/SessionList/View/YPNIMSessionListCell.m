//
//  NTESSessionListCell.m
//  NIMDemo
//
//  Created by chris on 15/2/10.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "YPNIMSessionListCell.h"
#import "YPNIMAvatarImageView.h"
#import "UIView+NIM.h"
#import "YPNIMKitUtil.h"
#import "YPNIMBadgeView.h"

@implementation YPNIMSessionListCell
#define AvatarWidth 40
- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    if (self) {
        _avatarImageView = [[YPNIMAvatarImageView alloc] initWithFrame:CGRectMake(0, 0, 44, 44)];
        
        [self addSubview:_avatarImageView];
        
        _avatarImageView.cornerRadius = 15;

        
        
        _nameLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _nameLabel.backgroundColor = [UIColor whiteColor];
//        _nameLabel.font            = [UIFont boldSystemFontOfSize:15.f];
        _nameLabel.font            = [UIFont boldSystemFontOfSize:14];
        _nameLabel.textColor       = UIColorHex(000000);
        [self addSubview:_nameLabel];
        
        _messageLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _messageLabel.backgroundColor = [UIColor whiteColor];
//        _messageLabel.font            = [UIFont boldSystemFontOfSize:12.f];
        _messageLabel.font            = [UIFont systemFontOfSize:13];
        _messageLabel.textColor       = UIColorHex(666666);
        [self addSubview:_messageLabel];
        
        
        _sexImageView = [[UIImageView alloc] initWithFrame:CGRectZero];
        [self addSubview:_sexImageView];
        
        
        _timeLabel = [[UILabel alloc] initWithFrame:CGRectZero];
        _timeLabel.backgroundColor = [UIColor whiteColor];
//        _timeLabel.font            = [UIFont boldSystemFontOfSize:12.f];
        _timeLabel.font            = [UIFont systemFontOfSize:10];
        _timeLabel.textColor       = UIColorHex(999999);
        [self addSubview:_timeLabel];
        
        _badgeView = [YPNIMBadgeView viewWithBadgeTip:@""];
        [self addSubview:_badgeView];
    }
    return self;
}


#define NameLabelMaxWidth    160.f
#define MessageLabelMaxWidth 200.f
- (void)refresh:(NIMRecentSession*)recent{
    self.nameLabel.nim_width = self.nameLabel.nim_width > NameLabelMaxWidth ? NameLabelMaxWidth : self.nameLabel.nim_width;
    self.messageLabel.nim_width = self.messageLabel.nim_width > MessageLabelMaxWidth ? MessageLabelMaxWidth : self.messageLabel.nim_width;
    if (recent.unreadCount) {
        self.badgeView.hidden = NO;
        self.badgeView.badgeValue = @(recent.unreadCount).stringValue;
    }else{
        self.badgeView.hidden = YES;
    }
}


- (void)layoutSubviews{
    [super layoutSubviews];
    //Session List
    NSInteger sessionListAvatarLeft             = 15;
    NSInteger sessionListNameTop                = 10;
    NSInteger sessionListNameLeftToAvatar       = 10;
    NSInteger sessionListMessageLeftToAvatar    = 10;
    NSInteger sessionListMessageBottom          = 15;
    NSInteger sessionListTimeRight              = 15;
    NSInteger sessionListTimeTop                = 15;
    NSInteger sessionBadgeTimeBottom            = 17;
    NSInteger sessionBadgeTimeRight             = 15;
    
    _avatarImageView.nim_left    = sessionListAvatarLeft;
    _avatarImageView.nim_centerY = self.nim_height * .5f;
    _nameLabel.nim_top           = sessionListNameTop;
    _nameLabel.nim_left          = _avatarImageView.nim_right + sessionListNameLeftToAvatar;
    _messageLabel.nim_left       = _avatarImageView.nim_right + sessionListMessageLeftToAvatar;
    _messageLabel.nim_bottom     = self.nim_height - sessionListMessageBottom;
    _timeLabel.nim_right         = self.nim_width - sessionListTimeRight;
    _timeLabel.nim_top           = sessionListTimeTop;
    
    _badgeView.nim_right         = self.nim_width - sessionBadgeTimeRight;
    _badgeView.nim_bottom        = self.nim_height - sessionBadgeTimeBottom;
    
//    _badgeView.nim_left          = _avatarImageView.nim_left + 43;
//    _badgeView.nim_top           = _avatarImageView.nim_top;
    
    _sexImageView.nim_left = _nameLabel.nim_right+5;
    _sexImageView.nim_centerY = _nameLabel.nim_centerY;
    _sexImageView.nim_size = CGSizeMake(13, 13);
    
}



@end
