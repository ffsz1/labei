//
//  YPRoomPopupItem.h
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

typedef void(^roomPopupItemBlcok)(void);

@interface YPRoomPopupItem : NSObject

@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *icon;
@property (nonatomic, copy) roomPopupItemBlcok blockAction;

+ (instancetype)ItemWithTitle:(NSString *)title icon:(NSString *)icon action:(roomPopupItemBlcok)action;

@end

NS_ASSUME_NONNULL_END
