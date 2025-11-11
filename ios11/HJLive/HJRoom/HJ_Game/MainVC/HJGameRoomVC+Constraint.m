//
//  HJGameRoomVC+Constraint.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+Constraint.h"
#import "HJVersionCoreHelp.h"
#import "HJGameRoomVC+LongZhu.h"

@implementation HJGameRoomVC (Constraint)

- (void)initConstraint {


    //麦序
    [self.positionView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.top.mas_equalTo(self.topicLabel.mas_bottom).offset(15);
        make.top.mas_equalTo(self.view).offset(125 +(XC_Height_NavBar-64));
        make.leading.mas_equalTo(self.view.mas_leading).offset(0);
        make.trailing.mas_equalTo(self.view.mas_trailing).offset(0);
        make.height.mas_equalTo(210);
    }];

//    //活动小窗
//    [self.activityView mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.bottom.mas_equalTo((kScreenWidth>380)?-170:-140);
//        make.trailing.mas_equalTo(self.view.mas_trailing).offset(-8);
//        make.width.mas_equalTo(70);
//        make.height.mas_equalTo(70);
//    }];
    [self.banaCommonView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo((kScreenWidth>380)?-180:-170);
        make.trailing.mas_equalTo(self.view.mas_trailing).offset(-8);
        make.width.mas_equalTo(65);
        make.height.mas_equalTo(65);
    }];


    [self.giftRecordBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.positionView).mas_offset(-10);
        make.trailing.mas_equalTo(self.positionView);
        make.width.mas_equalTo(23);
        make.height.mas_equalTo(91);
    }];
    
    //聊天按钮
//    [self.talkMessageSelectBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.centerY.mas_equalTo(self.view).mas_offset(-10);
//        make.trailing.mas_equalTo(self.positionView);
//        make.width.mas_equalTo(64);
//        make.height.mas_equalTo(24);
//    }];

        //公屏
        [self.messageTableView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.leading.mas_equalTo(self.view.mas_leading).offset(8);
            make.trailing.mas_equalTo(self.banaCommonView.mas_leading).offset(-8);
            make.bottom.mas_equalTo(self.view.mas_bottom).offset(-57);
            //        make.height.mas_equalTo(self.view.frame.size.height / 3);
            make.top.mas_equalTo(self.positionView.mas_bottom).offset(8);
        }];
    
    [self.enterTipView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.leading.mas_equalTo(self.view.mas_trailing);
        
        make.top.mas_equalTo(self.messageTableView.mas_top).offset(0);
        make.width.mas_equalTo(181);
        make.height.mas_equalTo(24);
    }];
    
    CGSize textSize = [self.headerViewTip.text boundingRectWithSize:CGSizeMake(XC_SCREE_W - 94 - 45, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont systemFontOfSize:14.f]} context:nil].size;
    if (iphonePlus) {
        self.messageTableViewHeader.height = textSize.height + 30;
    }
    else {

        self.messageTableViewHeader.height = textSize.height + 12;
    }
    //礼物显示框
    [self.giftContainer mas_makeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo(self.messageTableView.mas_top).offset(-25);
        make.leading.mas_equalTo(self.view.mas_leading);
        make.trailing.mas_equalTo(self.view.mas_trailing);
        make.top.mas_equalTo(self.ownerNameLabel.mas_bottom).offset(30);
    }];
    
//    //贡献榜按钮
//    [self.roomContributionBtn mas_makeConstraints:^(MASConstraintMaker *make) {
//        make.centerX.mas_equalTo(self.followBtn);
//        make.width.mas_equalTo(82);
//        make.height.mas_equalTo(28);
//        make.top.mas_equalTo(self.followBtn.mas_bottom).offset(14);
//        make.right.mas_equalTo(self.view);
//    }];
    
    //gif播放
    [self.gifImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.view.mas_top);
        make.leading.mas_equalTo(self.view.mas_leading);
        make.trailing.mas_equalTo(self.view.mas_trailing);
        make.bottom.mas_equalTo(self.view.mas_bottom);
    }];
    
    
    
    [self setupLongViewConstraint];

}

@end
