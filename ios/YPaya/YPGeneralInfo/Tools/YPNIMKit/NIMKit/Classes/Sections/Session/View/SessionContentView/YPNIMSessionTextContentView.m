//
//  YPNIMSessionTextContentView.m
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "YPNIMSessionTextContentView.h"
#import "M80AttributedLabel+YPNIMKit.h"
#import "YPNIMMessageModel.h"
#import "NIMGlobalMacro.h"
#import "UIView+NIM.h"
#import "YPNIMKit.h"

NSString *const NIMTextMessageLabelLinkData = @"NIMTextMessageLabelLinkData";

@interface YPNIMSessionTextContentView()<M80AttributedLabelDelegate>

@end

@implementation YPNIMSessionTextContentView

- (instancetype)initSessionMessageContentView
{
    if (self = [super initSessionMessageContentView]) {
        _textLabel = [[M80AttributedLabel alloc] initWithFrame:CGRectZero];
        _textLabel.delegate = self;
        _textLabel.numberOfLines = 0;
        _textLabel.lineBreakMode = NSLineBreakByWordWrapping;
        _textLabel.backgroundColor = [UIColor clearColor];
        [self addSubview:_textLabel];
    }
    return self;
}

- (void)refresh:(YPNIMMessageModel *)data{
    [super refresh:data];
    NSString *text = self.model.message.text;
    
    YPNIMKitSetting *setting = [[YPNIMKit sharedKit].config setting:data.message];

    self.textLabel.textColor = setting.textColor;
    self.textLabel.font = setting.font;
    [self.textLabel nim_setText:text];
}

- (void)layoutSubviews{
    [super layoutSubviews];
    UIEdgeInsets contentInsets = self.model.contentViewInsets;
    
    CGFloat tableViewWidth = self.superview.nim_width;
    CGSize contentsize         = [self.model contentSize:tableViewWidth];
    CGRect labelFrame = CGRectMake(contentInsets.left-(self.model.message.isOutgoingMsg?-5:5), contentInsets.top, contentsize.width, contentsize.height);
    self.textLabel.frame = labelFrame;
}


#pragma mark - M80AttributedLabelDelegate
- (void)m80AttributedLabel:(M80AttributedLabel *)label
             clickedOnLink:(id)linkData{
    YPNIMKitEvent *event = [[YPNIMKitEvent alloc] init];
    event.eventName = NIMKitEventNameTapLabelLink;
    event.messageModel = self.model;
    event.data = linkData;
    [self.delegate onCatchEvent:event];
}

@end
