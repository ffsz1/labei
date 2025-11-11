//
//  YPNIMSessionRobotContentView.h
//  YPNIMKit
//
//  Created by chris on 2017/6/27.
//  Copyright © 2017年 NetEase. All rights reserved.
//

#import "YPNIMSessionMessageContentView.h"

@interface YPNIMSessionRobotContentView : YPNIMSessionMessageContentView

// 参与 cell 行高的接口
- (void)setupRobot:(YPNIMMessageModel *)data;

+ (CGFloat)itemSpacing;

@end
