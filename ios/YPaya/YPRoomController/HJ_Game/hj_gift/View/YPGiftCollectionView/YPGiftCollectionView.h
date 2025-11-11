//
//  YPGiftCollectionView.h
//  HJLive
//
//  Created by apple on 2019/7/10.
//

#import <UIKit/UIKit.h>

#import "YPGiftInfo.h"

typedef NS_ENUM(NSUInteger, XBDGiftBoxType) {
    XBDGiftBoxTypeNormal = 0,//普通
    XBDGiftBoxTypeBag,//背包
    XBDGiftBoxTypePoint,//点点币
};

NS_ASSUME_NONNULL_BEGIN

@interface YPGiftCollectionView : UIView

@property (nonatomic,assign) XBDGiftBoxType type;
@property (nonatomic,assign) double allBagNum ;
@property (nonatomic,copy) void(^allBagNumberBlack)(double allBagNumber);
@property (nonatomic,copy) void(^selectCellBlack)(YPGiftInfo *selectInfo);
- (YPGiftInfo *)getGiftModel;

@end

NS_ASSUME_NONNULL_END
