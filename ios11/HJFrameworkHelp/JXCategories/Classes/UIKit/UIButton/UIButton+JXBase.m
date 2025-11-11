//
//  UIButton+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/12.
//
//

#import "UIButton+JXBase.h"
#import "UIView+JXBase.h"

@implementation UIButton (JXBase)

#pragma mark - Base
- (instancetype)jx_initWithImage:(UIImage *)image title:(NSString *)title {
#pragma clang diagnostic push
#pragma clang diagnostic ignored "-Wunused-value"
    [self init];
#pragma clang diagnostic pop
    [self setImage:image forState:UIControlStateNormal];
    [self setTitle:title forState:UIControlStateNormal];
    return self;
}

#pragma mark - Count Down
- (void)jx_changeWithCountDown:(NSInteger)seconds title:(NSString *)title backgroundColor:(UIColor *)backgroundColor finishedTitle:(NSString *)finishedTitle finishedBackgroundColor:(UIColor *)finishedBackgroundColor {
    [self jx_changeWithCountDown:seconds countDownHandler:^(UIButton *sender, NSInteger second, BOOL finished) {
        if (finished) {
            sender.backgroundColor = finishedBackgroundColor;
            [sender setTitle:finishedTitle forState:UIControlStateNormal];
            sender.enabled = YES;
        } else {
            sender.backgroundColor = backgroundColor;
            NSString *timeStr = [NSString stringWithFormat:@"%0.2ld", (long)second];
            [sender setTitle:[NSString stringWithFormat:@"%@(%@S)",title, timeStr] forState:UIControlStateDisabled];
            sender.enabled = NO;
        }
    }];
}

@end
