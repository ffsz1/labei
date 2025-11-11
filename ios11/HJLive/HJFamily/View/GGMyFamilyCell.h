//
//  GGMyFamilyCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>



#import "HJFamilyInfoDetail.h"
#import "HJRoomCoreV2Help.h"

typedef void(^HeadTap)(NSInteger uid,NSInteger tagRole);
typedef void(^FingHimTap)(NSInteger uid);

NS_ASSUME_NONNULL_BEGIN

@interface GGMyFamilyCell : UITableViewCell

@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UIImageView *roleImageView;
@property (weak, nonatomic) IBOutlet UIImageView *levelImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *tipLabel;

@property (strong, nonatomic) HJFamilyInfoDetail *model;

@property (copy,nonatomic) HeadTap headTapBlock;

@property (copy,nonatomic) FingHimTap findBlock;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *name_width;

@end

NS_ASSUME_NONNULL_END
