//
//  HJAnchorSharingContentMessageView.m
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAnchorSharingContentMessageView.h"
#import "HJAnchorSharingMessageView.h"
#import "JSONTools.h"
#import "HJRoomViewControllerCenter.h"

@interface HJAnchorSharingContentMessageView()
@property (strong, nonatomic) HJAnchorSharingMessageView *contentView;
@end

@implementation HJAnchorSharingContentMessageView

- (instancetype)initSessionMessageContentView {
    self = [super initSessionMessageContentView];
    if (self) {
        //        self.opaque = YES;
        _contentView  = [[NSBundle mainBundle] loadNibNamed:@"HJAnchorSharingMessageView" owner:nil options:nil][0];
        //        self.bubbleImageView.hidden = YES;
        [self addSubview:_contentView];
        [_contentView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
            //uid
            [MBProgressHUD showMessage:@"请稍后"];
            [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:_contentView.uid succ:^(ChatRoomInfo *roomInfo) {
                if (roomInfo != nil && roomInfo.title.length > 0) {
                    //            [MBProgressHUD hideHUD];
                    //根据房间信息开房
                    [[HJRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
                }else {
                    [MBProgressHUD showError:@"网络错误"];
                }
            } fail:^(NSString *errorMsg) {
                [MBProgressHUD showError:errorMsg];
            }];
        }]];
    }
    return self;
}

- (void)refresh:(NIMMessageModel *)data {
    [super refresh:data];
    NSString *text = data.message.text;
    NSDictionary *dic = [JSONTools ll_dictionaryWithJSON:text];
//    [_contentView.bgImg sd_setImageWithURL:[NSURL URLWithString:dic[@"bg"]] placeholderImage:[UIImage imageNamed:default_bg]];
    [_contentView.roomImg sd_setImageWithURL:[NSURL URLWithString:dic[@"avatar"]] placeholderImage:[UIImage imageNamed:default_bg]];
    _contentView.title.text = dic[@"title"];
    _contentView.uid = [dic[@"uid"] longLongValue];
}


@end
