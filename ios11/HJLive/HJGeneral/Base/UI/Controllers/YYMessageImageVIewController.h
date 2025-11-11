//
//  YYMessageImageViewController.h
//  YYMobile
//
//  Created by James Pend on 14-8-27.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol YYMessageImageDelegate <NSObject>

- (void)onMoreBtnClicked;

@end

@interface YYMessageImageViewController : YYViewController
@property (nonatomic,strong) id<YYMessageImageDelegate> delegate;

@property (nonatomic,strong) NSString *imageUrl;

@property (nonatomic,assign) NSUInteger imageIndex;

@property (nonatomic, strong) NSArray *imageList;

@property (nonatomic, strong) UIImage *defaultImage;

@property (nonatomic, assign) BOOL pageHidden;
@property (nonatomic, assign) BOOL rightBarHidden;

- (void)saveCurrentImage;
- (void)resetImageProvider;
- (void)setCurrentImage:(UIImage *)image;

@end
