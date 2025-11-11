//
//  ContactPickedView.h
//  NIM
//
//  Created by ios on 10/23/13.
//  Copyright (c) 2013 Netease. All rights reserved.
//

#import <UIKit/UIKit.h>
@class YPNIMKitInfo;

@protocol NIMContactPickedViewDelegate <NSObject>

- (void)removeUser:(NSString *)userId;

@end

@interface YPNIMContactPickedView : UIView <UIScrollViewDelegate>

@property (nonatomic, weak) id<NIMContactPickedViewDelegate> delegate;

- (void)removeMemberInfo:(YPNIMKitInfo *)info;

- (void)addMemberInfo:(YPNIMKitInfo *)info;

@end
