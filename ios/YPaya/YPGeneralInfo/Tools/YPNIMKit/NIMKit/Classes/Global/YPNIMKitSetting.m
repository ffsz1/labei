//
//  YPNIMKitSetting.m
//  YPNIMKit
//
//  Created by chris on 2017/10/30.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import "YPNIMKitSetting.h"
#import "UIImage+YPNIMKit.h"

@implementation YPNIMKitSetting

- (instancetype)init:(BOOL)isRight
{
    self = [super init];
    if (self)
    {
        if (isRight)
        {
            _normalBackgroundImage    =  [[[UIImage nim_imageInKit:@"icon_sender_text_node_normal"] imageByTintColor:[UIColor colorWithHexString:@"#FFD4E5"]] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
            _highLightBackgroundImage =  [[[UIImage nim_imageInKit:@"icon_sender_text_node_pressed"] imageByTintColor:[UIColor colorWithHexString:@"#FFD4E5"]] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
            
        }
        else
        {
            _normalBackgroundImage    =  [[[UIImage nim_imageInKit:@"icon_receiver_node_normal"] imageByTintColor:[UIColor colorWithHexString:@"#FFFFFF"]] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
            _highLightBackgroundImage =  [[[UIImage nim_imageInKit:@"icon_receiver_node_pressed"] imageByTintColor:[UIColor colorWithHexString:@"#FFFFFF"]] resizableImageWithCapInsets:UIEdgeInsetsFromString(@"{18,25,17,25}") resizingMode:UIImageResizingModeStretch];
        }
    }
    return self;
}

@end

