//
//  HJBaseGestureSubColloctionVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YYViewController.h"
#import "HJBaseGestureCollectionView.h"
NS_ASSUME_NONNULL_BEGIN

typedef void (^notiBlock)(void);//通知主控制器页面可以滑动了

@interface HJBaseGestureSubColloctionVC : YYViewController<UICollectionViewDelegate,UICollectionViewDataSource,UICollectionViewDelegateFlowLayout>

@property (nonatomic, strong) HJBaseGestureCollectionView *collectionView;

@property (nonatomic, assign) NSInteger headerSelectType;

@property (nonatomic, assign) BOOL canScroll;
@property (nonatomic, copy) notiBlock block;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger count;

- (void)handlerBlock:(notiBlock)block;


@end
NS_ASSUME_NONNULL_END
