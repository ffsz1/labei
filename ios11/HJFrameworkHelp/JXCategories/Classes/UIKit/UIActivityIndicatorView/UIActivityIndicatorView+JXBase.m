//
//  UIActivityIndicatorView+JXBase.m
//  JXCategories
//
//  Created by Colin on 2019/2/1.
//

#import "UIActivityIndicatorView+JXBase.h"

@implementation UIActivityIndicatorView (JXBase)

#pragma mark - Base
- (instancetype)initWithActivityIndicatorStyle:(UIActivityIndicatorViewStyle)style size:(CGSize)size {
    self = [self initWithActivityIndicatorStyle:style];
    if (self) {
        CGSize initialSize = self.bounds.size;
        CGFloat scale = size.width / initialSize.width;
        self.transform = CGAffineTransformMakeScale(scale, scale);
    }
    return self;
}

@end
