//
//  YYEmptyContentToastView.m
//  YYMobile
//
//  Created by wuwei on 14/7/30.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import "YYEmptyContentToastView.h"

@interface YYEmptyContentToastView ()

@property(weak, nonatomic) IBOutlet UILabel *titleLabel;
@property(weak, nonatomic) IBOutlet UIImageView *imageView;

@end

@implementation YYEmptyContentToastView

@synthesize titleLabel = _titleLabel;

+ (instancetype)instantiateEmptyContentToast
{
    YYEmptyContentToastView *view = [[[NSBundle mainBundle] loadNibNamed:@"YYEmptyContentToast" owner:nil options:nil] firstObject];
    view.imageView.image = [UIImage imageNamed:@"icon_neirongkong"];
    
    return view;
}

+ (instancetype)instantiateNetworkErrorToast
{
    YYEmptyContentToastView *view = [[[NSBundle mainBundle] loadNibNamed:@"YYEmptyContentToast"
                                                                   owner:nil
                                                                 options:nil] firstObject];
    view.imageView.image = [UIImage imageNamed:@"network_error.png"];

    return view;
}

+ (instancetype)instantiateEmptyContentToastWithImage:(UIImage*)image
{
    YYEmptyContentToastView *view = [[[NSBundle mainBundle] loadNibNamed:@"YYEmptyContentToast"
                                                                   owner:nil
                                                                 options:nil] firstObject];
    view.imageView.image = image;
    view.titleLabel.text = @"";
    
    return view;
}

- (id)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
//    if (self) {
        // Initialization code
//    }
    return self;
}

/*
// Only override drawRect: if you perform custom drawing.
// An empty implementation adversely affects performance during animation.
- (void)drawRect:(CGRect)rect
{
    // Drawing code
}
*/

@end
