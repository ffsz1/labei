//
//  HJMallBottomView.h
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJMallBottomView : UIView

@property (copy, nonatomic) void(^sendGiftButtonAction)();
@property (copy, nonatomic) void(^buyGiftButtonAction)();

- (void)setupGold:(NSString *)gold day:(NSString *)day;

- (void)setupBuyButtonText:(NSString *)text;

- (void)setupSendStyle;//只有赠送的的模式

@property (assign, nonatomic) BOOL isPopularitySel;

@property (nonatomic, strong) UIButton *sendBtn;
@property (assign, nonatomic) BOOL isBackpack;
@end

NS_ASSUME_NONNULL_END
