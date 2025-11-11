//
//  HJSendGoldContentMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/7/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
#import "HJSendGoldMessageView.h"
#import "HJSendGoldContentMessageView.h"
#import "HJSendGoldAttachment.h"
@interface HJSendGoldContentMessageView()
@property (strong, nonatomic) HJSendGoldMessageView *contentView;
@end
@implementation HJSendGoldContentMessageView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
//        self.opaque = YES;
        _contentView  = [HJSendGoldMessageView loadFromNib];

        //        self.bubbleImageView.hidden = YES;
        [self addSubview:_contentView];
    }
    return self;
}

- (void)refresh:(NIMMessageModel *)data {
    [super refresh:data];
    self.bubbleImageView.hidden = YES;
    NIMCustomObject *object = (NIMCustomObject *)data.message.messageObject;
    HJSendGoldAttachment *customObject = [[HJSendGoldAttachment alloc]init];
    if ([object.attachment isKindOfClass:[HJSendGoldAttachment class]]) {
        customObject =  (HJSendGoldAttachment *)object.attachment;
    }else if ([object.attachment isKindOfClass:[Attachment class]]) {
        Attachment * att = (Attachment *)object.attachment;
        
        customObject.goldNum = att.data[@"goldNum"];
        customObject.recvName = att.data[@"recvName"];
    }
    
    _contentView.sendGoldImageView.image = [UIImage imageNamed:@"yp_message_zhuanzhang"];
     NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    if ([self.model.message.from isEqualToString:uid]) {
         [_contentView.desLabel setText:[NSString stringWithFormat:@"发红包给%@",customObject.recvName]];
    }else{
        [_contentView.desLabel setText:[NSString stringWithFormat:@"发红包给你"]];
    }
    [_contentView.desLabel setText:[NSString stringWithFormat:@""]];

  
    [_contentView.goldLabel setText:[NSString stringWithFormat:@" %@开心",customObject.goldNum]];
}

- (void)layoutSubviews{
    [super layoutSubviews];
    UIEdgeInsets contentInsets = self.model.contentViewInsets;
    CGFloat tableViewWidth = self.superview.width;

    CGSize contentSize = [self.model contentSize:tableViewWidth];
    NSString *uid = [GetCore(HJAuthCoreHelp)getUid];
    CGFloat margeLeft = 0;
    if ([self.model.message.from isEqualToString:uid]) {
        margeLeft = contentInsets.left +12;
    }else{
        margeLeft = contentInsets.left -12;
    }
    CGRect imageViewFrame = CGRectMake(margeLeft, contentInsets.top, contentSize.width, contentSize.height);
    self.contentView.frame  = imageViewFrame;
    CALayer *maskLayer = [CALayer layer];
    maskLayer.cornerRadius = 8.0;
    maskLayer.backgroundColor = [UIColor blackColor].CGColor;
    maskLayer.frame = self.contentView.bounds;
    self.contentView.layer.mask = maskLayer;
}

@end
