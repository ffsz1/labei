//
//  YPTurntableContentView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPTurntableContentView.h"
#import "YPTurntableAttachment.h"
#import "YPTurntableMessageView.h"
#import "UIView+NTES.h"

NSString *const HJTurntableMessageViewClick = @"HJTurntableMessageViewClick";

@interface YPTurntableContentView ()

@property (strong, nonatomic) YPTurntableMessageView *contentView;

@end

@implementation YPTurntableContentView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
        self.opaque = YES;
        _contentView = [YPTurntableMessageView loadFromNib];
        [self addSubview:_contentView];
    }
    return self;
}

- (void)layoutSubviews{
    [super layoutSubviews];
    UIEdgeInsets contentInsets = self.model.contentViewInsets;
    CGFloat tableViewWidth = self.superview.width;
    CGSize contentSize = [self.model contentSize:tableViewWidth];
    CGRect imageViewFrame = CGRectMake(contentInsets.left, contentInsets.top, contentSize.width, contentSize.height);
    self.contentView.frame  = imageViewFrame;
//    CALayer *maskLayer = [CALayer layer];
//    maskLayer.cornerRadius = 13.0;
//    maskLayer.backgroundColor = [UIColor blackColor].CGColor;
//    maskLayer.frame = self.contentView.bounds;
//    self.contentView.layer.mask = maskLayer;
}

- (void)refresh:(YPNIMMessageModel *)data {
    [super refresh:data];
}


-(void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    
    if ([self.delegate respondsToSelector:@selector(onCatchEvent:)]) {
        YPNIMKitEvent *event = [[YPNIMKitEvent alloc] init];
        event.eventName = HJTurntableMessageViewClick;
        event.messageModel = self.model;
        event.data = self;
        [self.delegate onCatchEvent:event];
    }
    
    //    event.eventName = NIMDemoEventNameOpenSnapPicture;
    //    event.messageModel = self.model;
    //    event.data = self;
    
}


@end
