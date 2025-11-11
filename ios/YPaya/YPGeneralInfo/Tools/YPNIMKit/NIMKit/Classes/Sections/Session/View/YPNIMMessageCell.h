//
//  YPNIMMessageCell.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NIMMessageCellProtocol.h"
#import "YPNIMTimestampModel.h"

@class YPNIMSessionMessageContentView;
@class YPNIMAvatarImageView;
@class YPNIMBadgeView;

@interface YPNIMMessageCell : UITableViewCell

@property (nonatomic, strong) YPNIMAvatarImageView *headImageView;
@property (nonatomic, strong) UILabel *nameLabel;                                 //姓名
@property (nonatomic, strong) YPNIMSessionMessageContentView *bubbleView;           //内容区域
@property (nonatomic, strong) UIActivityIndicatorView *traningActivityIndicator;  //发送loading
@property (nonatomic, strong) UIButton *retryButton;                              //重试
@property (nonatomic, strong) YPNIMBadgeView *audioPlayedIcon;                      //语音未读红点
@property (nonatomic, strong) UILabel *readLabel;                                 //已读

@property (nonatomic, weak)   id<NIMMessageCellDelegate> delegate;

- (void)refreshData:(YPNIMMessageModel *)data;

@end
