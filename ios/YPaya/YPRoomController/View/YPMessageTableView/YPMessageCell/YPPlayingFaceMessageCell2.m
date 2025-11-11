//
//  YPPlayingFaceMessageCell2.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPlayingFaceMessageCell2.h"
#import "YPAttachment.h"
#import "YPFacePlayInfo.h"
#import "YPFaceReceiveInfo.h"
#import "YPFaceSendInfo.h"
#import "NSObject+YYModel.h"
#import "YPYYDefaultTheme.h"
#import "YPFaceCore.h"
#import <YYAnimatedImageView.h>

@implementation YPPlayingFaceMessageCell2

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    self.layer.drawsAsynchronously = YES;
    self.messageLabel.displaysAsynchronously = YES;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}


@end
