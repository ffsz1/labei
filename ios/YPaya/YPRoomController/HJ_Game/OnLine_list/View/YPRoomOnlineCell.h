//
//  YPRoomOnlineCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPChatRoomMember.h"

typedef void(^XBDRoomOnlineUpMic)(NSIndexPath* _Nullable index);


NS_ASSUME_NONNULL_BEGIN

@interface YPRoomOnlineCell : UITableViewCell

@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *richLevelImageView;
@property (weak, nonatomic) IBOutlet UIImageView *charmLevelImageView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_name;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *left_charm;

@property (strong,nonatomic) NSIndexPath *indexPath;
@property (copy,nonatomic) XBDRoomOnlineUpMic upMicBlock;
@property (strong,nonatomic) YPChatRoomMember *model;



@end

NS_ASSUME_NONNULL_END
