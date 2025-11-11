//
//  NTESContactInfoCell.h
//  NIM
//
//  Created by chris on 15/2/26.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "NIMContactDefines.h"
#import "YPNIMKitInfo.h"

@protocol NIMContactDataCellDelegate <NSObject>

- (void)onPressAvatar:(NSString *)memberId;

@end

@class YPNIMAvatarImageView;

@interface YPNIMContactDataCell : UITableViewCell

@property (nonatomic,copy) NSString *memberId;

@property (nonatomic,strong) YPNIMAvatarImageView * avatarImageView;

@property (nonatomic,strong) UIButton *accessoryBtn;

@property (nonatomic,weak) id<NIMContactDataCellDelegate> delegate;

- (void)refreshUser:(id<NIMGroupMemberProtocol>)member;

- (void)refreshTeam:(id<NIMGroupMemberProtocol>)member;

- (void)refreshItem:(id<NIMGroupMemberProtocol>)member withMemberInfo:(YPNIMKitInfo *)info;

@end
