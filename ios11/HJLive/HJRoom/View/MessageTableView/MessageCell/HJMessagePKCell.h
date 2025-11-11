//
//  HJMessagePKCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJRoomPKMsgModel.h"
#import "Attachment.h"



NS_ASSUME_NONNULL_BEGIN

@interface HJMessagePKCell : UITableViewCell
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *levelImageView;
@property (weak, nonatomic) IBOutlet UILabel *resultLabel;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel2;
@property (weak, nonatomic) IBOutlet UIImageView *giftImageView;
@property (weak, nonatomic) IBOutlet UILabel *giftNumLabel;
@property (weak, nonatomic) IBOutlet UIButton *pkBtn;

@property (strong,nonatomic) Attachment *attachment;



@end

NS_ASSUME_NONNULL_END
