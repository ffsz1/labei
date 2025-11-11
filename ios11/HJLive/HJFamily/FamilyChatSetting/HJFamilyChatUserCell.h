//
//  HJFamilyChatUserCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJFamilyChatUserCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *roleImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;

@end

NS_ASSUME_NONNULL_END
