//
//  HJSpaceCardView.h
//  HJLive
//
//  Created by feiyin on 2020/5/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^SendGiftBlock)(UserID uid);
typedef void(^RoomOwnerFollow)(BOOL isFoloow);


NS_ASSUME_NONNULL_BEGIN

@interface HJSpaceCardView : UIView

+ (void)show:(UserID)uid sendGiftBlock:(SendGiftBlock)sendGiftBlock roomFollow:(RoomOwnerFollow)roomFollowBlock onView:(UIView *)onView;

@end

NS_ASSUME_NONNULL_END
