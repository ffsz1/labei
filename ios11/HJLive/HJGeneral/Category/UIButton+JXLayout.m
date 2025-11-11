//
//  UIButton+JXLayout.m
//  HJLive
//
//  Created by feiyin on 2020/6/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UIButton+JXLayout.h"

@implementation UIButton (JXLayout)

- (void)verticalImageAndTitle:(CGFloat)spacing
{
    CGSize imageSize = self.imageView.frame.size;
    CGSize titleSize = self.titleLabel.frame.size;
    CGSize textSize = [self.titleLabel.text sizeWithAttributes:@{NSFontAttributeName:self.titleLabel.font}];
    CGSize frameSize = CGSizeMake(ceilf(textSize.width), ceilf(textSize.height));
    if (titleSize.width + 0.5 < frameSize.width) {
        titleSize.width = frameSize.width;
    }
    CGFloat totalHeight = (imageSize.height + titleSize.height + spacing);
    self.imageEdgeInsets = UIEdgeInsetsMake(- (totalHeight - imageSize.height), 0.0, 0.0, - titleSize.width);
    self.titleEdgeInsets = UIEdgeInsetsMake(0, - imageSize.width, - (totalHeight - titleSize.height), 0);
    
}

- (void)verticalImageAndTitle
{
    const int DEFAULT_SPACING = 6.0f;
    [self verticalImageAndTitle:DEFAULT_SPACING];
}

- (void)invertedImageAndTitle:(CGFloat)spacing {
    CGSize imageSize = self.imageView.frame.size;
    CGSize titleSize = self.titleLabel.frame.size;
    self.imageEdgeInsets = UIEdgeInsetsMake(0, titleSize.width + spacing/2, 0, -(titleSize.width + spacing/2));
    self.titleEdgeInsets = UIEdgeInsetsMake(0, -(imageSize.width + spacing/2), 0, imageSize.width + spacing/2);
}

- (void)invertedImageAndTitle {
    const int DEFAULT_SPACING = 6.0f;
    [self invertedImageAndTitle:DEFAULT_SPACING];
}


- (void)alignmentLeftImageAndTitle
{
    //button文字的偏移量
    self.titleEdgeInsets = UIEdgeInsetsMake(0,  -(self.imageView.frame.origin.x+self.imageView.frame.size.width)-10, 0, 0);
    //button图片的偏移量
    self.imageEdgeInsets = UIEdgeInsetsMake(0, -(self.imageView.frame.origin.x )+40, 0, self.imageView.frame.origin.x);
}

//待优化
- (void)alignmentLeftImageAndTitle:(CGFloat)imageSpacing titleSpacing:(CGFloat)titleSpacing
{
    
    //button文字的偏移量
    self.titleEdgeInsets = UIEdgeInsetsMake(0,  -(self.imageView.frame.origin.x+self.imageView.frame.size.width+imageSpacing*2)-titleSpacing-50, 0, 0);
    
    //button图片的偏移量
    self.imageEdgeInsets = UIEdgeInsetsMake(0, -(self.imageView.frame.origin.x )+imageSpacing*2, 0, self.imageView.frame.origin.x);
    
    
    
}

@end
