//
//  HJGiftContentMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGiftContentMessageView.h"
#import "HJGiftMessageView.h"
#import "HJGiftAttachment.h"
#import "UIView+NTES.h"
#import <UIImageView+WebCache.h>
#import "NSObject+YYModel.h"
#import "HJGiftAllMicroSendInfo.h"
#import "HJGiftCore.h"

NSString *const HJGiftContentMessageViewClick  = @"HJGiftContentMessageViewClick";

@interface HJGiftContentMessageView()
@property (strong, nonatomic) HJGiftMessageView *contentView;
@end

@implementation HJGiftContentMessageView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
//        self.opaque = YES;
        _contentView  = [HJGiftMessageView loadFromNib];

        //        self.bubbleImageView.hidden = YES;
        [self addSubview:_contentView];
    }
    return self;
}

- (void)refresh:(NIMMessageModel *)data {
    [super refresh:data];
    NIMCustomObject *object = (NIMCustomObject *)data.message.messageObject;
    HJGiftAttachment *customObject = [[HJGiftAttachment alloc]init];
    if ([object.attachment isKindOfClass:[HJGiftAttachment class]]) {
        customObject =  (HJGiftAttachment *)object.attachment;
    }else if ([object.attachment isKindOfClass:[Attachment class]]) {
        Attachment * att = (Attachment *)object.attachment;
        GiftInfo *info = [GetCore(HJGiftCore)findGiftInfoByGiftId:[att.data[@"giftId"] integerValue] giftyType:GiftTypeNormal];
        if (info == nil) {
            info = [GetCore(HJGiftCore)findGiftInfoByGiftId:[att.data[@"giftId"] integerValue] giftyType:GiftTypeMystic];
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
        NIMKitEvent *event = [[NIMKitEvent alloc] init];
        event.eventName = HJGiftContentMessageViewClick;
        event.messageModel = self.model;
        event.data = self;
        [self.delegate onCatchEvent:event];
    }
}

@end
