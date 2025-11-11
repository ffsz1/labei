//
//  YPNIMPageView.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>
@class YPNIMPageView;

@protocol NIMPageViewDataSource <NSObject>
- (NSInteger)numberOfPages: (YPNIMPageView *)pageView;
- (UIView *)pageView: (YPNIMPageView *)pageView viewInPage: (NSInteger)index;
@end

@protocol NIMPageViewDelegate <NSObject>
@optional
- (void)pageViewScrollEnd: (YPNIMPageView *)pageView
             currentIndex: (NSInteger)index
               totolPages: (NSInteger)pages;

- (void)pageViewDidScroll: (YPNIMPageView *)pageView;
- (BOOL)needScrollAnimation;
@end


@interface YPNIMPageView : UIView<UIScrollViewDelegate>
@property (nonatomic,strong)    UIScrollView   *scrollView;
@property (nonatomic,weak)    id<NIMPageViewDataSource>  dataSource;
@property (nonatomic,weak)    id<NIMPageViewDelegate>    pageViewDelegate;
- (void)scrollToPage: (NSInteger)pages;
- (void)reloadData;
- (UIView *)viewAtIndex: (NSInteger)index;
- (NSInteger)currentPage;


//旋转相关方法,这两个方法必须配对调用,否则会有问题
- (void)willRotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                duration:(NSTimeInterval)duration;

- (void)willAnimateRotationToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation
                                         duration:(NSTimeInterval)duration;
@end
