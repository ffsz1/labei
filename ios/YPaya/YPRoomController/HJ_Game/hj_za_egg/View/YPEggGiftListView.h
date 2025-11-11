//
//  YPEggGiftListView.h
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "GGMaskView.h"


NS_ASSUME_NONNULL_BEGIN

@interface YPEggGiftListView : GGMaskView<UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_BgView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *liwu_height_layout;
@property (strong,nonatomic) NSArray *dataArr;

+ (void)showGift;

@end

NS_ASSUME_NONNULL_END
