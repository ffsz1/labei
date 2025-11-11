//
//  UISlider+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UISlider+JXBase.h"

@implementation UISlider (JXBase)

#pragma mark - Base
- (CGRect)jx_trackRect {
    return [self trackRectForBounds:self.bounds];
}

- (CGRect)jx_thumbRect {
    return [self thumbRectForBounds:self.bounds trackRect:self.jx_trackRect value:self.value];
}

@end
