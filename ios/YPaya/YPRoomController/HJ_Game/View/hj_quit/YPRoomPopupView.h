//
//  YPRoomPopupView.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPRoomPopupItem.h"

NS_ASSUME_NONNULL_BEGIN

typedef void(^roomPopupViewCancelBlcok)(void);

@interface YPRoomPopupView : UIView

@property (nonatomic, copy) roomPopupViewCancelBlcok cancelAction;
@property (nonatomic, strong) NSArray<YPRoomPopupItem *> *items;

- (void)showWithContentView:(UIView * _Nullable)contenView;

- (void)dismiss;

@end

NS_ASSUME_NONNULL_END
