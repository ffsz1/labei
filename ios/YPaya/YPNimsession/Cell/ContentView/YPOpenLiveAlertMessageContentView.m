//
//  YPOpenLiveAlertMessageContentView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPOpenLiveAlertMessageContentView.h"
#import "UIView+NTES.h"
#import "YPOpenLiveAlertMessageView.h"
#import "YPOpenLiveAttachment.h"
#import <UIImageView+WebCache.h>
#import "YPRoomViewControllerCenter.h"

@interface YPOpenLiveAlertMessageContentView()
@property (strong, nonatomic) YPOpenLiveAlertMessageView *contentView;
@end

@implementation YPOpenLiveAlertMessageContentView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
        self.opaque = YES;
        _contentView  = [YPOpenLiveAlertMessageView loadFromNib];
//        self.bubbleImageView.hidden = YES;
        [self addSubview:_contentView];
    }
    return self;
}

- (void)refresh:(YPNIMMessageModel *)data {
    [super refresh:data];
    NIMCustomObject *object = (NIMCustomObject *)data.message.messageObject;
    YPOpenLiveAttachment *customObject = (YPOpenLiveAttachment*)object.attachment;
    if (customObject.nick && customObject.nick.length > 0) {
        [_contentView.avatar sd_setImageWithURL:[NSURL URLWithString:customObject.avatar] placeholderImage:[UIImage imageNamed:default_avatar]];
        [_contentView.subTitle setText:[NSString stringWithFormat:@"%@上线啦",customObject.nick]];
    }else {
        __block NIMUser *user = [[NIMUser alloc]init];
        NSArray *uids = @[[NSString stringWithFormat:@"%lld",customObject.uid]];
        @weakify(self);
        [[NIMSDK sharedSDK].userManager fetchUserInfos:uids completion:^(NSArray<NIMUser *> * _Nullable users, NSError * _Nullable error) {
            @strongify(self);
            if (error == nil) {
                user = users[0];
                [self.contentView.avatar sd_setImageWithURL:[NSURL URLWithString:user.userInfo.avatarUrl] placeholderImage:[UIImage imageNamed:default_avatar]];
                [self.contentView.subTitle setText:[NSString stringWithFormat:@"%@上线啦",user.userInfo.nickName]];
            }
        }];
    }
    
}

- (void)layoutSubviews{
    [super layoutSubviews];
    UIEdgeInsets contentInsets = self.model.contentViewInsets;
    CGFloat tableViewWidth = self.superview.width;
    CGSize contentSize = [self.model contentSize:tableViewWidth];
    CGRect imageViewFrame = CGRectMake(contentInsets.left - 10, contentInsets.top, contentSize.width, contentSize.height);
    self.contentView.frame  = imageViewFrame;
    CALayer *maskLayer = [CALayer layer];
    maskLayer.cornerRadius = 13.0;
    maskLayer.backgroundColor = [UIColor blackColor].CGColor;
    maskLayer.frame = self.contentView.bounds;
    self.contentView.layer.mask = maskLayer;
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    NIMCustomObject *object = (NIMCustomObject *)self.model.message.messageObject;
    YPOpenLiveAttachment *customObject = (YPOpenLiveAttachment*)object.attachment;
    [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:customObject.uid succ:^(YPChatRoomInfo *roomInfo) {
        if (roomInfo != nil) {
            [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
        }
    } fail:^(NSString *errorMsg) {
        [MBProgressHUD showError:errorMsg];
    }];
}

@end
