//
//  HJPlayingFaceMessageCell2.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPlayingFaceMessageCell2.h"
#import "Attachment.h"
#import "HJFacePlayInfo.h"
#import "HJFaceReceiveInfo.h"
#import "HJFaceSendInfo.h"
#import "NSObject+YYModel.h"
#import "YYDefaultTheme.h"
#import "HJFaceCore.h"
#import <YYAnimatedImageView.h>

@implementation HJPlayingFaceMessageCell2

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
