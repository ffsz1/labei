//
//  YPZJContentView.h
//  YPZJScrollPageView
//
//  Created by jasnig on 16/5/6.
//  Copyright © 2016年 ZeroJ. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "ZJScrollPageViewDelegate.h"
#import "YPZJCollectionView.h"
#import "YPZJScrollSegmentView.h"
#import "UIViewController+ZJScrollPageController.h"

@interface YPZJContentView : UIView

/** 必须设置代理和实现相关的方法*/
@property(weak, nonatomic)id<ZJScrollPageViewDelegate> delegate;
@property (strong, nonatomic, readonly) YPZJCollectionView *collectionView;

/**初始化方法
 *
 */
- (instancetype)initWithFrame:(CGRect)frame segmentView:(YPZJScrollSegmentView *)segmentView parentViewController:(UIViewController *)parentViewController delegate:(id<ZJScrollPageViewDelegate>) delegate;

/** 给外界可以设置ContentOffSet的方法 */
- (void)setContentOffSet:(CGPoint)offset animated:(BOOL)animated;
/** 给外界 重新加载内容的方法 */
- (void)reload;
@end
