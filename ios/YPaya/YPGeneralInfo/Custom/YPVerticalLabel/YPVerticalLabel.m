//
//  YPVerticalLabel.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPVerticalLabel.h"

@implementation YPVerticalLabel


-(instancetype)initWithFrame:(CGRect)frame{
    if (self = [super initWithFrame:frame]) {
        self.verticalAlignment = HJVerticalAlignmentMiddle;
    }
    return self;
}

-(void)setVerticalAlignment:(HJVerticalAlignment)verticalAlignment{
    _verticalAlignment = verticalAlignment;
    [self setNeedsDisplay];
}

-(CGRect)textRectForBounds:(CGRect)bounds limitedToNumberOfLines:(NSInteger)numberOfLines{
    CGRect textRect = [super textRectForBounds:bounds   limitedToNumberOfLines:numberOfLines];
    switch (self.verticalAlignment) {
        case HJVerticalAlignmentTop:
            textRect.origin.y = self.bounds.origin.y;
            break;
            
        case HJVerticalAlignmentMiddle:
            break;
            
        case HJVerticalAlignmentBottom:
            textRect.origin.y = bounds.origin.y +       bounds.size.height - textRect.size.height-5;
            break;
            
        default:
            textRect.origin.y = bounds.origin.y + (bounds.size.height - textRect.size.height) / 2.0;
            break;
    }
    return textRect;
}
-(void)drawTextInRect:(CGRect)rect{
    CGRect actualRect = [self textRectForBounds:rect limitedToNumberOfLines:self.numberOfLines];
    [super drawTextInRect:actualRect];
}


@end
