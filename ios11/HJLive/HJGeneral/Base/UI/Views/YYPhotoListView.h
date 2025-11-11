//
//  YYPhotoListView.h
//  YYMobile
//
//  Created by James Pend on 15/3/18.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "YYPhotoListDataProvider.h"

@class YYPhotoListView;
@protocol YYPhotoListViewDelegate <NSObject>

@optional

/**
 *  图片列表
 *
 *  @param yyphotoListView 图片列表视图
 *  @param page            图片列表当前的页数
 */
- (void)yyphotolistView:(YYPhotoListView *)yyphotoListView page:(NSInteger)page allPage:(NSInteger)allPage;

/**
 *  图片列表
 *
 *  @param yyphotoListView 图片列表视图
 *  @param index           所选图片的下标
 */

- (void)yyphotolistView:(YYPhotoListView *)yyphotoListView didSelecteIndex:(NSInteger)index;

/**
 *  图片列表
 *
 *  @param yyphotoListView 图片列表视图
 *  @param index           所选图片的下标
 */
- (void)yyphotolistView:(YYPhotoListView *)yyphotoListView didLongPressAtIndex:(NSInteger)index;

@end

@interface YYPhotoListView : UIView

@property (nonatomic, strong) YYPhotoListDataProvider *dataProvider;

/**
 *  当前显示的图片image
 */
@property (nonatomic, strong,readonly) UIImage *currentImage;

//@property (nonatomic, strong) YYWebImageManager *imageManager;

@property (weak, nonatomic) id<YYPhotoListViewDelegate> delegate;

@property (weak,nonatomic) UIViewController* parentViewController;

- (void)setCurrentImage:(UIImage *)currentImage;

@end
