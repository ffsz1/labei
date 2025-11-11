//
//  YPRoomMsgHeaderView.h
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPGiftReceiveInfo.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomMsgHeaderView : UIView
@property (strong, nonatomic) UILabel *tipLabel;
@property (strong, nonatomic) UIView *giftView;

@property (strong, nonatomic) UILabel *giftLabel;
@property (strong, nonatomic) UILabel *numLabel;
@property (strong, nonatomic) UIImageView *giftImageView;

@property (nonatomic, assign) BOOL isAll;
@property (nonatomic, strong) YPGiftReceiveInfo *model;//礼物模型


@end

NS_ASSUME_NONNULL_END
