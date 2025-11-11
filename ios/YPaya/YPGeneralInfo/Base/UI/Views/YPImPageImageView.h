//
//  YPImPageImageView.h
//  YYMobile
//
//  Created by James Pend on 15/1/19.
//  Copyright (c) 2015年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@class YPImPageImageView;

@protocol ImPageDelegate <NSObject>
@optional
- (void)imPageDelegete_didTapImageView;

- (void)onPageImageLongPress:(YPImPageImageView *)imageView;

@end

@interface YPImPageImageView : UIScrollView

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

+ (YPImPageImageView *)view;

- (void)resetScale;
- (BOOL)zoomScaleChanged;
@end
