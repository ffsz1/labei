//
//  UIView+JXAnimation.m
//  JXCategories
//
//  Created by Colin on 2019/1/25.
//

#import "UIView+JXAnimation.h"

@implementation UIView (JXAnimation)

#pragma mark - Shake
- (void)jx_shake {
    CGFloat t =4.0;
    CGAffineTransform translateRight = CGAffineTransformTranslate(CGAffineTransformIdentity, t, 0.0);
    CGAffineTransform translateLeft = CGAffineTransformTranslate(CGAffineTransformIdentity, -t, 0.0);
    self.transform = translateLeft;
    [UIView animateWithDuration:0.07 delay:0.0 options:UIViewAnimationOptionAutoreverse | UIViewAnimationOptionRepeat animations:^{
        [UIView setAnimationRepeatCount:2.0];
        self.transform = translateRight;
    } completion:^(BOOL finished) {
        if(finished) {
            [UIView animateWithDuration:0.05 delay:0.0 options:UIViewAnimationOptionBeginFromCurrentState animations:^{
                self.transform =CGAffineTransformIdentity;
            } completion:NULL];
        }
    }];
}

@end
