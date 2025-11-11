//
//  YPLayout.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//  卡片滑动布局

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPLayout : UICollectionViewLayout
//cell间距
@property (nonatomic, assign) CGFloat spacing;
//cell的尺寸
@property (nonatomic, assign) CGSize itemSize;
//缩放率
@property (nonatomic, assign) CGFloat scale;
//边距
@property (nonatomic, assign) UIEdgeInsets edgeInset;
//滚动方向
@property (nonatomic, assign) UICollectionViewScrollDirection scrollDirection;

@end

NS_ASSUME_NONNULL_END
