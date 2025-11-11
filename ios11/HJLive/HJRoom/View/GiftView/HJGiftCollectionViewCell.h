//
//  HJGiftCollectionViewCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJGiftCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *giftBgImageView;
@property (weak, nonatomic) IBOutlet UIImageView *giftLogo;
@property (weak, nonatomic) IBOutlet UILabel *gitftNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *giftPriceLabel;
@property (weak, nonatomic) IBOutlet UIView *collectionViewBg;
@property (weak, nonatomic) IBOutlet UIImageView *secondTag;
@property (weak, nonatomic) IBOutlet UIImageView *firstTag;
@property (weak, nonatomic) IBOutlet UIImageView *thirdTag;
@property (weak, nonatomic) IBOutlet UILabel *gitfreeLabel;

@end
