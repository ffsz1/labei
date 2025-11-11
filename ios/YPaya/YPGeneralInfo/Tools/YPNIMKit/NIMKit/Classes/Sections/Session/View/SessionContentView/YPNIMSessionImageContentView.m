//
//  YPNIMSessionImageContentView.m
//  YPNIMKit
//
//  Created by chris on 15/1/28.
//  Copyright (c) 2015年 Netease. All rights reserved.
//

#import "YPNIMSessionImageContentView.h"
#import "YPNIMMessageModel.h"
#import "UIView+NIM.h"
#import "YPNIMLoadProgressView.h"

@interface YPNIMSessionImageContentView()

@property (nonatomic,strong,readwrite) UIImageView * imageView;

@property (nonatomic,strong) YPNIMLoadProgressView * progressView;

@end

@implementation YPNIMSessionImageContentView

- (instancetype)initSessionMessageContentView{
    self = [super initSessionMessageContentView];
    if (self) {
        self.opaque = YES;
        _imageView  = [[UIImageView alloc] initWithFrame:CGRectZero];
        _imageView.backgroundColor = [UIColor blackColor];
        _imageView.contentMode = UIViewContentModeScaleAspectFill;
        [self addSubview:_imageView];
        _progressView = [[YPNIMLoadProgressView alloc] initWithFrame:CGRectMake(0, 0, 44, 44)];
        _progressView.maxProgress = 1.0f;
        [self addSubview:_progressView];
    }
    return self;
}

- (void)refresh:(YPNIMMessageModel *)data
{
    [super refresh:data];
    NIMImageObject * imageObject = (NIMImageObject*)self.model.message.messageObject;
    UIImage * image              = [UIImage imageWithContentsOfFile:imageObject.thumbPath];
    self.imageView.image         = image;
    self.progressView.hidden     = self.model.message.isOutgoingMsg ? (self.model.message.deliveryState != NIMMessageDeliveryStateDelivering) : (self.model.message.attachmentDownloadState != NIMMessageAttachmentDownloadStateDownloading);
    if (!self.progressView.hidden) {
        [self.progressView setProgress:[[[NIMSDK sharedSDK] chatManager] messageTransportProgress:self.model.message]];
    }
}

- (void)layoutSubviews{
    [super layoutSubviews];
    UIEdgeInsets contentInsets = self.model.contentViewInsets;
    CGFloat tableViewWidth = self.superview.nim_width;
    CGSize contentSize = [self.model contentSize:tableViewWidth];

    if (self.model.message.isOutgoingMsg) {
           CGRect imageViewFrame = CGRectMake(contentInsets.left+4, contentInsets.top, contentSize.width, contentSize.height);
           self.imageView.frame  = imageViewFrame;
       }else{
           CGRect imageViewFrame = CGRectMake(contentInsets.left-4, contentInsets.top, contentSize.width, contentSize.height);
           self.imageView.frame  = imageViewFrame;
       }
   
    _progressView.frame   = self.bounds;
    
    CALayer *maskLayer = [CALayer layer];
    maskLayer.cornerRadius = 13.0;
    maskLayer.backgroundColor = [UIColor blackColor].CGColor;
    maskLayer.frame = self.imageView.bounds;
    self.imageView.layer.mask = maskLayer;
}


- (void)onTouchUpInside:(id)sender
{
    YPNIMKitEvent *event = [[YPNIMKitEvent alloc] init];
    event.eventName = NIMKitEventNameTapContent;
    event.messageModel = self.model;
    [self.delegate onCatchEvent:event];
}

- (void)updateProgress:(float)progress
{
    if (progress > 1.0) {
        progress = 1.0;
    }
    self.progressView.progress = progress;
}

@end
