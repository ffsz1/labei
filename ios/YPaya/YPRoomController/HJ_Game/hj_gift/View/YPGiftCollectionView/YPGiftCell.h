//
//  YPGiftCell.h
//  HJLive
//
//  Created by apple on 2019/7/10.
//

#import <UIKit/UIKit.h>

#import "YPGiftInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPGiftCell : UICollectionViewCell

@property (nonatomic,strong) YPGiftInfo *giftModel;

- (void)setSelStytle:(BOOL)isSel;

@end

NS_ASSUME_NONNULL_END
