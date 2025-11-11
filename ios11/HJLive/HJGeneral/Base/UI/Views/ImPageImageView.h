//
//  ImPageImageView.h
//  YYMobile
//
//  Created by James Pend on 15/1/19.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class ImPageImageView;

@protocol ImPageDelegate <NSObject>
@optional
- (void)imPageDelegete_didTapImageView;

- (void)onPageImageLongPress:(ImPageImageView *)imageView;

@end

@interface ImPageImageView : UIScrollView

/**
 *  要显示的图片数据源
 */
@property (nonatomic,strong) UIImage *imageData;

/**
 *  要显示的图片链接
 */
@property (nonatomic,copy) NSString *imageUrl;

@property (nonatomic,weak) id<ImPageDelegate> pageDelegate;

//@property (nonatomic, strong) YYWebImageManager *imageManager;

+ (ImPageImageView *)view;

- (void)resetScale;
- (BOOL)zoomScaleChanged;
@end
