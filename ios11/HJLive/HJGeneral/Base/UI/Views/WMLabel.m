//
//  WMLabel.m
//  HJLive
//
//  Created by feiyin on 2020/6/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "WMLabel.h"

@implementation WMLabel

- (instancetype)init {
    if (self = [super init]) {
        _textInsets = UIEdgeInsetsZero;
    }
    return self;
}

- (instancetype)initWithFrame:(CGRect)frame {
    if (self = [super initWithFrame:frame]) {
        _textInsets = UIEdgeInsetsZero;
    }
    return self;
}

- (CGRect)textRectForBounds:(CGRect)bounds limitedToNumberOfLines:(NSInteger)numberOfLines {
    UIEdgeInsets insets = self.textInsets;
    CGRect rect = [super textRectForBounds:UIEdgeInsetsInsetRect(bounds, insets)
                    limitedToNumberOfLines:numberOfLines];
    
    rect.origin.x    -= insets.left;
    rect.origin.y    -= insets.top;
    rect.size.width  += (insets.left + insets.right);
    rect.size.height += (insets.top + insets.bottom);
    
    return rect;
}

- (void)drawTextInRect:(CGRect)rect {
//    [super drawTextInRect:UIEdgeInsetsInsetRect(rect, _textInsets)];
    [super drawTextInRect:UIEdgeInsetsInsetRect(rect, self.textInsets)];
}

@end
