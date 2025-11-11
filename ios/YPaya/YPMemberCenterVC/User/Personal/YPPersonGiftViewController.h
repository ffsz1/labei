//
//  YPPersonGiftViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseGestureSubColloctionVC.h"

NS_ASSUME_NONNULL_BEGIN
@interface YPPersonGiftViewController : YPBaseGestureSubColloctionVC
@property (nonatomic, assign) UserID userID;
- (void)updateData;
- (void)setupscrollDirection:(UICollectionViewScrollDirection)scrollDirection;
@end
NS_ASSUME_NONNULL_END
