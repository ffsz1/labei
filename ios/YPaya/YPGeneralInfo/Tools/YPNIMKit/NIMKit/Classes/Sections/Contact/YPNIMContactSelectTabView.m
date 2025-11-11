//
//  YPNIMContactSelectTabView.m
//  YPNIMKit
//
//  Created by chris on 15/9/15.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import "YPNIMContactSelectTabView.h"
#import "YPNIMContactPickedView.h"
#import "UIView+NIM.h"
#import "UIImage+YPNIMKit.h"

@implementation YPNIMContactSelectTabView

- (instancetype)initWithFrame:(CGRect)frame{
    self = [super initWithFrame:frame];
    if (self) {
        _pickedView = [[YPNIMContactPickedView alloc] initWithFrame:CGRectMake(0, 0, 40, 40)];
        [self addSubview:_pickedView];
        _doneButton = [UIButton buttonWithType:UIButtonTypeCustom];
        UIImage *doneButtonNormal      = [UIImage nim_imageInKit:@"icon_cell_blue_normal"];
        UIImage *doneButtonHighlighted = [UIImage nim_imageInKit:@"icon_cell_blue_normal"];
        [_doneButton setBackgroundImage:doneButtonNormal forState:UIControlStateNormal];
        [_doneButton setBackgroundImage:doneButtonHighlighted forState:UIControlStateHighlighted];
        [_doneButton setTitle:NSLocalizedString(NIMKitConfirm, nil) forState:UIControlStateNormal];
        _doneButton.nim_size = doneButtonNormal.size;
        [self addSubview:_doneButton];
        self.backgroundColor = [UIColor colorWithPatternImage:[UIImage nim_imageInKit:@"contact_bg.png"]];
    }
    return self;
}

- (void)layoutSubviews{
    [super layoutSubviews];
    CGFloat spacing = 15.f;
    _pickedView.nim_height  = self.nim_height;
    _pickedView.nim_width   = self.nim_width - _doneButton.nim_width - spacing;
    CGFloat doneButtonRight = 15.f;
    _doneButton.nim_right   = self.nim_width - doneButtonRight;
    _doneButton.nim_centerY = self.nim_height * .5f;
}

@end
