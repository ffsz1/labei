//
//  GGFamilyHomeCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface GGFamilyHomeCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *rankImageView;
@property (weak, nonatomic) IBOutlet UILabel *rankLabel;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet GGLabel *popularLabel;
@property (weak, nonatomic) IBOutlet UILabel *peopleLabel;


@property (weak, nonatomic) IBOutlet UIImageView *rankImageView2;
@property (weak, nonatomic) IBOutlet UILabel *rankLabel2;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView2;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel2;
@property (weak, nonatomic) IBOutlet GGLabel *popularLabel2;
@property (weak, nonatomic) IBOutlet UILabel *peopleLabel2;
@property (weak, nonatomic) IBOutlet UIView *view2;


@property (copy,nonatomic) void (^tap1)(void);
@property (copy,nonatomic) void (^tap2)(void);


@end

NS_ASSUME_NONNULL_END
