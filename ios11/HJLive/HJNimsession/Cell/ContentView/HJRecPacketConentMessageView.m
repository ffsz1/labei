//
//  HJRecPacketConentMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRecPacketConentMessageView.h"
#import "HJRedPacketInfoAttachment.h"
#import "HJRedPackeMessagetView.h"
#import "UIView+NTES.h"

@interface HJRecPacketConentMessageView ()
@property (strong, nonatomic) HJRedPackeMessagetView *contentView;
@end

@implementation HJRecPacketConentMessageView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
        self.opaque = YES;
        _contentView  = [HJRedPackeMessagetView loadFromNib];
        //        self.bubbleImageView.hidden = YES;
        [self addSubview:_contentView];
    }
    return self;
}

- (void)refresh:(NIMMessageModel *)data {
    [super refresh:data];
    NIMCustomObject *object = (NIMCustomObject *)data.message.messageObject;
    HJRedPacketInfoAttachment *customObject = (HJRedPacketInfoAttachment*)object.attachment;
    if (customObject.title > 0) {
        _contentView.redIcon.image = [UIImage imageNamed:@"hj_message_redPacket"];
        _contentView.redLabel.text = [NSString stringWithFormat:@"%@%@%@,%@",@"收到",customObject.title,@"福利",@"快去看吧！"];
    }
    
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

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {

    if ([self.delegate respondsToSelector:@selector(onCatchEvent:)]) {
        NIMKitEvent *event = [[NIMKitEvent alloc] init];
        event.eventName = @"XCOnRedPacketNoticClick";
        event.messageModel = self.model;
        event.data = self;
        [self.delegate onCatchEvent:event];
    }
}

@end
