//
//  CUIWaterfallView.h
//  
//
//  Created by daixiang on 13-12-26.
//  Copyright (c) 2013年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

#define WATERFALL_USE_COLLECTIONVIEW_IF_AVAILABLE      1
extern BOOL waterfallShouldUseCollectionView();

@class CUIWaterfallViewCell;
@class CUIWaterfallView;

@protocol CUIWaterfallDataSource <NSObject>

- (NSUInteger)numberOfColumnsInWaterfallView:(CUIWaterfallView *)waterfallView;
- (NSUInteger)numberOfItemsInWaterfallView:(CUIWaterfallView *)waterfallView;
- (CUIWaterfallViewCell *)waterfallView:(CUIWaterfallView *)waterfallView cellForItemAtIndex:(NSUInteger)index;

@end

@protocol CUIWaterfallDelegate <UIScrollViewDelegate>

@optional

- (CGFloat)waterfallView:(CUIWaterfallView *)waterfallView heightForItemAtIndex:(NSUInteger)index;
- (void)waterfallView:(CUIWaterfallView *)waterfallView didSelectItemAtIndex:(NSUInteger)index;

@end

@interface CUIWaterfallView : UIView

@property (nonatomic, weak) id<CUIWaterfallDataSource> dataSource;
@property (nonatomic, weak) id<CUIWaterfallDelegate> delegate;
@property (nonatomic) UIEdgeInsets contentPaddingInset;
@property (nonatomic) CGFloat columnPadding;
@property (nonatomic) CGFloat rowPadding;

//注意：请勿设置此scrollView的delegate，它被waterfallview内部使用了
@property (nonatomic, readonly) UIScrollView *scrollView;

- (void)reloadData;

//如果没有可重用cell的话,此函数会新建一个cell,因此不需要自己init cell
- (id)dequeueReusableCellWithIdentifier:(NSString *)identifier forIndex:(NSUInteger)index;

- (CUIWaterfallViewCell *)cellAtIndex:(NSUInteger)index;

- (NSArray *)visibleCells;

@end
