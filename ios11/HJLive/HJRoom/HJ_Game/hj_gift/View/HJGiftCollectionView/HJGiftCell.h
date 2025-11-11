//
//  HJGiftCell.h
//  HJLive
//
//  Created by apple on 2019/7/10.
//

#import <UIKit/UIKit.h>

#import "GiftInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJGiftCell : UICollectionViewCell

@property (nonatomic,strong) GiftInfo *giftModel;

- (void)setSelStytle:(BOOL)isSel;

@end

NS_ASSUME_NONNULL_END
