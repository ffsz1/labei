//
//  HJRoomPopupView.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJRoomPopupItem.h"

NS_ASSUME_NONNULL_BEGIN

typedef void(^roomPopupViewCancelBlcok)(void);

@interface HJRoomPopupView : UIView

@property (nonatomic, copy) roomPopupViewCancelBlcok cancelAction;
@property (nonatomic, strong) NSArray<HJRoomPopupItem *> *items;

- (void)showWithContentView:(UIView * _Nullable)contenView;

- (void)dismiss;

@end

NS_ASSUME_NONNULL_END
