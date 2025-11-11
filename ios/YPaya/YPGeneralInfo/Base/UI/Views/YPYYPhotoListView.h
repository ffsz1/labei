//
//  YPYYPhotoListView.h
//  YYMobile
//
//  Created by James Pend on 15/3/18.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//


#import <UIKit/UIKit.h>
#import "YPYYPhotoListDataProvider.h"

@class YPYYPhotoListView;
@protocol YYPhotoListViewDelegate <NSObject>

@optional

/**
 *  图片列表
 *
 *  @param yyphotoListView 图片列表视图
 *  @param page            图片列表当前的页数
 */
- (void)yyphotolistView:(YPYYPhotoListView *)yyphotoListView page:(NSInteger)page allPage:(NSInteger)allPage;

/**
 *  图片列表
 *
 *  @param yyphotoListView 图片列表视图
 *  @param index           所选图片的下标
 */

- (void)yyphotolistView:(YPYYPhotoListView *)yyphotoListView didSelecteIndex:(NSInteger)index;

/**
 *  图片列表
 *
 *  @param yyphotoListView 图片列表视图
 *  @param index           所选图片的下标
 */
- (void)yyphotolistView:(YPYYPhotoListView *)yyphotoListView didLongPressAtIndex:(NSInteger)index;

@end

@interface YPYYPhotoListView : UIView

@property (nonatomic, strong) YPYYPhotoListDataProvider *dataProvider;

/**
 *  当前显示的图片image
 */
@property (nonatomic, strong,readonly) UIImage *currentImage;

//@property (nonatomic, strong) YYWebImageManager *imageManager;

@property (weak, nonatomic) id<YYPhotoListViewDelegate> delegate;

@property (weak,nonatomic) UIViewController* parentViewController;

- (void)setCurrentImage:(UIImage *)currentImage;

@end
