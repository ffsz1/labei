//
//  HJGameRoomPositionView.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

// 房间 麦序view
#import <UIKit/UIKit.h>

#import "HJRoomMemberCharmInfoModel.h"

@protocol HJGameRoomPositionViewDelegate <NSObject>

- (void)positionViewShowUserInfoCardWithUid:(UserID)uid;

@end

@interface HJGameRoomPositionView : UIView

@property (weak,nonatomic) UINavigationController *navigationController;

@property(nonatomic, weak) id<HJGameRoomPositionViewDelegate> delegate;

- (void)awakeFromNib;
+ (instancetype)loadFromNib;
- (CGPoint)findThePositionCenterByPosition:(NSString *)position;


@end
