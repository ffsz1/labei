//
//  YPFamilyMembersView.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@class UserInfo;

typedef void(^HJFamilyMembersViewDidTapHandler)(void);

@interface YPFamilyMembersView : UIView

@property (nonatomic, strong) NSArray<UserInfo *> *items;
@property (nonatomic, copy) HJFamilyMembersViewDidTapHandler didTapHandler;

@end

NS_ASSUME_NONNULL_END
