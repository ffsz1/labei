//
//  NIMCellConfig.h
//  YPNIMKit
//
//  Created by chris.
//  Copyright (c) 2015年 NetEase. All rights reserved.
//

#import <UIKit/UIKit.h>

@class YPNIMSessionMessageContentView;
@class YPNIMMessageModel;

@protocol YPNIMCellLayoutConfig <NSObject>

@optional

/**
 * @return 返回message的内容大小
 */
- (CGSize)contentSize:(YPNIMMessageModel *)model cellWidth:(CGFloat)width;


/**
 *  需要构造的cellContent类名
 */
- (NSString *)cellContent:(YPNIMMessageModel *)model;

/**
 *  左对齐的气泡，cell气泡距离整个cell的内间距
 */
- (UIEdgeInsets)cellInsets:(YPNIMMessageModel *)model;

/**
 *  左对齐的气泡，cell内容距离气泡的内间距，
 */
- (UIEdgeInsets)contentViewInsets:(YPNIMMessageModel *)model;

/**
 *  是否显示头像
 */
- (BOOL)shouldShowAvatar:(YPNIMMessageModel *)model;


/**
 *  左对齐的气泡，头像控件的 origin 点
 */
- (CGPoint)avatarMargin:(YPNIMMessageModel *)model;

/**
 *  左对齐的气泡，头像控件的 size
 */
- (CGSize)avatarSize:(YPNIMMessageModel *)model;

/**
 *  是否显示姓名
 */
- (BOOL)shouldShowNickName:(YPNIMMessageModel *)model;

/**
 *  左对齐的气泡，昵称控件的 origin 点
 */
- (CGPoint)nickNameMargin:(YPNIMMessageModel *)model;


/**
 *  消息显示在左边
 */
- (BOOL)shouldShowLeft:(YPNIMMessageModel *)model;


/**
 *  需要添加到Cell上的自定义视图
 */
- (NSArray *)customViews:(YPNIMMessageModel *)model;


/**
 *  是否开启重试叹号 
 */
- (BOOL)disableRetryButton:(YPNIMMessageModel *)model;


@end
