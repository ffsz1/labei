//
//  YPGiftContentMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGiftContentMessageView.h"
#import "YPGiftMessageView.h"
#import "YPGiftAttachment.h"
#import "UIView+NTES.h"
#import <UIImageView+WebCache.h>
#import "NSObject+YYModel.h"
#import "YPGiftAllMicroSendInfo.h"
#import "YPGiftCore.h"

NSString *const HJGiftContentMessageViewClick  = @"HJGiftContentMessageViewClick";

@interface YPGiftContentMessageView()
@property (strong, nonatomic) YPGiftMessageView *contentView;
@end

@implementation YPGiftContentMessageView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
//        self.opaque = YES;
        _contentView  = [YPGiftMessageView loadFromNib];

        //        self.bubbleImageView.hidden = YES;
        [self addSubview:_contentView];
    }
    return self;
}

- (void)refresh:(YPNIMMessageModel *)data {
    [super refresh:data];
    NIMCustomObject *object = (NIMCustomObject *)data.message.messageObject;
    YPGiftAttachment *customObject = [[YPGiftAttachment alloc]init];
    if ([object.attachment isKindOfClass:[YPGiftAttachment class]]) {
        customObject =  (YPGiftAttachment *)object.attachment;
    }else if ([object.attachment isKindOfClass:[YPAttachment class]]) {
        YPAttachment * att = (YPAttachment *)object.attachment;
        YPGiftInfo *info = [GetCore(YPGiftCore)findGiftInfoByGiftId:[att.data[@"giftId"] integerValue] giftyType:GiftTypeNormal];
        if (info == nil) {
            info = [GetCore(YPGiftCore)findGiftInfoByGiftId:[att.data[@"giftId"] integerValue] giftyType:GiftTypeMystic];
        }
        customObject.giftPic = info.giftUrl;
        customObject.giftNum = att.data[@"giftNum"];
        customObject.giftName = info.giftName;
    }
    
    [_contentView.giftImageView sd_setImageWithURL:[NSURL URLWithString:customObject.giftPic] placeholderImage:[UIImage imageNamed:default_bg]];
    [_contentView.giftNum setText:[NSString stringWithFormat:@"X%@",customObject.giftNum]];
    [_contentView.giftName setText:[NSString stringWithFormat:@"%@",customObject.giftName]];
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
        YPNIMKitEvent *event = [[YPNIMKitEvent alloc] init];
        event.eventName = HJGiftContentMessageViewClick;
        event.messageModel = self.model;
        event.data = self;
        [self.delegate onCatchEvent:event];
    }
}

@end
