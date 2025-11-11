//
//  YPNIMSessionUnknowContentView.h
//  YPNIMKit
//
//  Created by chris on 15/3/9.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "YPNIMSessionUnknowContentView.h"
#import "M80AttributedLabel+YPNIMKit.h"
#import "UIView+NIM.h"
#import "YPNIMMessageModel.h"
#import "YPNIMKit.h"

@interface YPNIMSessionUnknowContentView()

@property (nonatomic,strong) UILabel *label;

@end

@implementation YPNIMSessionUnknowContentView

-(instancetype)initSessionMessageContentView
{
    if (self = [super initSessionMessageContentView]) {
        _label = [[UILabel alloc] initWithFrame:CGRectZero];
        _label.backgroundColor = [UIColor clearColor];
        _label.userInteractionEnabled = NO;
        [self addSubview:_label];
    }
    return self;
}

- (void)refresh:(YPNIMMessageModel *)data{
    [super refresh:data];
    NSString *text = @"未知类型消息";
    
    YPNIMKitSetting *setting = [[YPNIMKit sharedKit].config setting:data.message];

    self.label.textColor = setting.textColor;
    self.label.font = setting.font;
    [self.label setText:text];
    [self.label sizeToFit];
}


- (void)layoutSubviews{
    [super layoutSubviews];
    _label.nim_centerX = self.nim_width  * .5f;
    _label.nim_centerY = self.nim_height * .5f;
}

@end
