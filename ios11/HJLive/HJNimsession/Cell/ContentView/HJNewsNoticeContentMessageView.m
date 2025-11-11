//
//  HJNewsNoticeContentMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNewsNoticeContentMessageView.h"
#import "HJNewsNoticeMessageView.h"
#import "HJNewsInfoAttachment.h"
#import "UIView+NTES.h"
#import <UIImageView+WebCache.h>
//#import <NIMKit.h>
#import "NIMKit.h"

#import "HJWKWebViewController.h"

NSString *const HJNewsNoticeContentMessageViewClick  = @"HJNewsNoticeContentMessageViewClick";

@interface HJNewsNoticeContentMessageView()
@property (strong, nonatomic) HJNewsNoticeMessageView *contentView;
@end

@implementation HJNewsNoticeContentMessageView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
        self.opaque = YES;
        _contentView  = [HJNewsNoticeMessageView loadFromNib];
        //        self.bubbleImageView.hidden = YES;
        [self addSubview:_contentView];
    }
    return self;
}

- (void)refresh:(NIMMessageModel *)data {
    [super refresh:data];
    NIMCustomObject *object = (NIMCustomObject *)data.message.messageObject;
    HJNewsInfoAttachment *customObject = (HJNewsInfoAttachment*)object.attachment;
    [_contentView.picImageView sd_setImageWithURL:[NSURL URLWithString:customObject.pic] placeholderImage:[UIImage imageNamed:default_bg]];
    [_contentView.title setText:customObject.title];
    [_contentView.contentLabel setText:customObject.subTitle];
}

- (void)layoutSubviews{
    [super layoutSubviews];
    UIEdgeInsets contentInsets = self.model.contentViewInsets;
    CGFloat tableViewWidth = self.superview.width;
    CGSize contentSize = [self.model contentSize:tableViewWidth];
    CGRect imageViewFrame = CGRectMake(contentInsets.left - 10, contentInsets.top, contentSize.width, contentSize.height);
    self.contentView.frame  = imageViewFrame;
    CALayer *maskLayer = [CALayer layer];
    maskLayer.cornerRadius = 13.0;
    maskLayer.backgroundColor = [UIColor blackColor].CGColor;
    maskLayer.frame = self.contentView.bounds;
    self.contentView.layer.mask = maskLayer;
}

-(void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {

    if ([self.delegate respondsToSelector:@selector(onCatchEvent:)]) {
        NIMKitEvent *event = [[NIMKitEvent alloc] init];
        event.eventName = HJNewsNoticeContentMessageViewClick;
        event.messageModel = self.model;
        event.data = self;
        [self.delegate onCatchEvent:event];
    }

//    event.eventName = NIMDemoEventNameOpenSnapPicture;
//    event.messageModel = self.model;
//    event.data = self;
    
}

@end
