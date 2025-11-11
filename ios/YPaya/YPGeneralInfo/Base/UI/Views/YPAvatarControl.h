//
//  YPAvatarControl.h
//  YYMobile
//
//  Created by wuwei on 14/6/20.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "UserID.h"
#import "YYWebImage.h"
typedef NS_ENUM(NSInteger, AvatarControlRenderMode)
{
    AvatarControlRenderNormal,      // 正常显示
    AvatarControlRenderGrayscale    // 灰度化显示
};

typedef NS_ENUM(NSInteger, AvatarControlSize)
{
    AvatarControlOriginal = 0,
    AvatarControlX60 = 1,
    AvatarControlX90 = 2
};

@interface YPAvatarControl : UIControl

@property (nonatomic, assign) UserID userId;
@property (nonatomic, assign) AvatarControlRenderMode renderMode;
@property (nonatomic, assign) CGFloat borderWidth;      // default is 0.0f;
@property (nonatomic, strong) UIColor *borderColor;
@property (nonatomic, strong) UIImage *avatarImage;
@property (nonatomic, strong) UIImageView *imageView;
- (void)setImage:(UIImage *)image;
- (UIImage *)image;

- (void)setImageURL:(NSURL *)url;
- (void)setImageURL:(NSURL *)url placeholderImage:(UIImage *)placeholderImage;

- (void)setImageURL:(NSURL *)url
       placeholderImage:(UIImage *)placeholderImage
              completed:(YYWebImageCompletionBlock)completed;

- (void)setCornerRadius:(CGFloat)cornerRadius;
@end

@interface YPAvatarControl (User)

- (void)setImageWithUserID:(UserID)userId size:(AvatarControlSize)size;

@end
