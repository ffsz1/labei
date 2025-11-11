//
//  ZJScrollView.h
//  YPZJScrollPageView
//
//  Created by ZeroJ on 16/10/24.
//  Copyright © 2016年 ZeroJ. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPZJCollectionView : UICollectionView

typedef BOOL(^ZJScrollViewShouldBeginPanGestureHandler)(YPZJCollectionView *collectionView, UIPanGestureRecognizer *panGesture);

- (void)setupScrollViewShouldBeginPanGestureHandler:(ZJScrollViewShouldBeginPanGestureHandler)gestureBeginHandler;

@end
