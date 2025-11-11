//
//  YPMySpaceGiftWallVC.h
//  HJLive
//
//  Created by feiyin on 2020/5/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^UpdateGiftNum)(NSInteger giftNum,NSInteger giftTypeNum);

NS_ASSUME_NONNULL_BEGIN

@interface YPMySpaceGiftWallVC : UICollectionViewController

@property (nonatomic, assign) UserID userID;
@property (nonatomic,strong) UpdateGiftNum updateBlock;

@property (nonatomic, assign) BOOL canScroll;
@property (nonatomic, assign) BOOL isShow;

@property (nonatomic,copy) void (^updateScrollBlock)(void);

@property (nonatomic,assign) NSInteger type;


@end

NS_ASSUME_NONNULL_END
