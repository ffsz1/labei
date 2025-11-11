//
//  HJRoomMsgHeaderView.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "GiftReceiveInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomMsgHeaderView : UIView
@property (strong, nonatomic) UILabel *tipLabel;
@property (strong, nonatomic) UIView *giftView;

@property (strong, nonatomic) UILabel *giftLabel;
@property (strong, nonatomic) UILabel *numLabel;
@property (strong, nonatomic) UIImageView *giftImageView;

@property (nonatomic, assign) BOOL isAll;
@property (nonatomic, strong) GiftReceiveInfo *model;//礼物模型


@end

NS_ASSUME_NONNULL_END
