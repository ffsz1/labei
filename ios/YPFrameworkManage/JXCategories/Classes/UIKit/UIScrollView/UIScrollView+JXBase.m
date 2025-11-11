//
//  UIScrollView+JXBase.m
//  Pods
//
//  Created by Colin on 17/1/11.
//
//

#import "UIScrollView+JXBase.h"
#import "JXCoreGraphicHelper.h"

@implementation UIScrollView (JXBase)

#pragma mark - Base
- (UIEdgeInsets)jx_contentInset {
    if (@available(iOS 11, *)) {
        return self.adjustedContentInset;
    }
    return self.contentInset;
}

- (void)setJx_showsScrollIndicator:(BOOL)jx_showsScrollIndicator {
    self.showsHorizontalScrollIndicator = jx_showsScrollIndicator;
    self.showsVerticalScrollIndicator = jx_showsScrollIndicator;
}

- (BOOL)jx_showsScrollIndicator {
    return self.showsHorizontalScrollIndicator || self.showsVerticalScrollIndicator;
}

- (BOOL)jx_isScrolledToTop {
    if (JXCGFloatEqualeToFloat(self.contentOffset.y, -self.jx_contentInset.top)) return YES;
    
    return NO;
}
        
- (BOOL)jx_isScrolledToLeft {
if (JXCGFloatEqualeToFloat(self.contentOffset.x, -self.jx_contentInset.left)) return YES;

    return NO;
}

- (BOOL)jx_isScrolledToBottom {
    if (!self.jx_canScroll) return YES;
    if (JXCGFloatEqualeToFloat(self.contentOffset.y, self.contentSize.height + self.jx_contentInset.bottom - CGRectGetHeight(self.bounds))) return YES;
    
    return NO;
}
    
- (BOOL)jx_isScrolledToRight {
    if (!self.jx_canScroll) return YES;
    if (JXCGFloatEqualeToFloat(self.contentOffset.x, self.contentSize.width + self.jx_contentInset.right - CGRectGetWidth(self.bounds))) return YES;
     
    return NO;
}

- (BOOL)jx_canScroll {
    return [self jx_canScroll:JXUIScrollViewScrollDirectionVertical] || [self jx_canScroll:JXUIScrollViewScrollDirectionHorizontal];
}

- (BOOL)jx_canScroll:(JXUIScrollViewScrollDirection)direction {
    if (JXCGSizeIsEmpty(self.bounds.size)) return NO;
   
    BOOL canScroll = NO;
    switch (direction) {
        case JXUIScrollViewScrollDirectionVertical:
        {
            canScroll = self.contentSize.height + JXUIEdgeInsetsGetValuesInVertical(self.jx_contentInset) > CGRectGetHeight(self.bounds);
        }
            break;
        case JXUIScrollViewScrollDirectionHorizontal:
        {
            canScroll = self.contentSize.width + JXUIEdgeInsetsGetValuesInHorizontal(self.jx_contentInset) > CGRectGetWidth(self.bounds);
        }
            break;
    }
    return NO;
}

- (void)jx_scrollToTop {
    [self jx_scrollToTop:YES];
}

- (void)jx_scrollToBottom {
    [self jx_scrollToBottom:YES];
}

- (void)jx_scrollToLeft {
    [self jx_scrollToLeft:YES];
}

- (void)jx_scrollToRight {
    [self jx_scrollToRight:YES];
}

- (void)jx_scrollToTop:(BOOL)animated {
    CGPoint off = self.contentOffset;
    off.y = 0 - self.jx_contentInset.top;
    [self jx_setContentOffset:off animated:animated];
}

- (void)jx_scrollToBottom:(BOOL)animated {
    CGPoint off = self.contentOffset;
    off.y = self.contentSize.height - self.bounds.size.height + self.jx_contentInset.bottom;
    [self jx_setContentOffset:off animated:animated];
}

- (void)jx_scrollToLeft:(BOOL)animated {
    CGPoint off = self.contentOffset;
    off.x = 0 - self.jx_contentInset.left;
    [self jx_setContentOffset:off animated:animated];
}

- (void)jx_scrollToRight:(BOOL)animated {
    CGPoint off = self.contentOffset;
    off.x = self.contentSize.width - self.bounds.size.width + self.jx_contentInset.right;
    [self jx_setContentOffset:off animated:animated];
}

- (void)jx_setContentOffset:(CGPoint)contentOffset animated:(BOOL)animated {
    if (JXCGFloatEqualeToFloat(self.contentOffset.x, contentOffset.x) && JXCGFloatEqualeToFloat(self.contentOffset.y, contentOffset.y)) return; // CGPointEqualToPoint Not Work
    [self setContentOffset:contentOffset animated:animated];
}

- (void)jx_endDecelerating {
    if (!self.decelerating) return;
    
    [self jx_setContentOffset:self.contentOffset animated:NO];
}

@end
