//
//  YPDiscoveryFamilyTableViewCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPDiscoveryFamilyTableViewCell : UITableViewCell
/**
 设置

 @param avatar 头像
 @param title 标题
 @param count 威望值
 @param memberCount 人数
 @param info 描述
 @param index indexpath.row
 */
- (void)setupAvatar:(NSString *)avatar
              title:(NSString *)title
              count:(NSInteger)count
        memberCount:(NSInteger)memberCount
           infoText:(NSString *)info
              index:(NSInteger)index;


/**
 设置的展示头像
 @param avatars 头像链接数组
 */
- (void)setupUserAvatars:(NSArray<NSString*> *)avatars;

@end

NS_ASSUME_NONNULL_END
