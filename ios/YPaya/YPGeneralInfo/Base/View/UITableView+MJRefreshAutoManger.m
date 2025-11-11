//
//  UITableView+MJRefreshAutoManger.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UITableView+MJRefreshAutoManger.h"
#import <MJRefresh.h>

@implementation UITableView (MJRefreshAutoManger)
static char stateKey;
- (void)setFootRefreshState:(MJFooterRefreshState)footRefreshState {
    UIWindow *window = [UIApplication sharedApplication].keyWindow;
    
    [RACObserve(self.mj_footer, frame)subscribeNext:^(id x) {
        CGPoint point = [self convertPoint:self.mj_footer.frame.origin toView:window];
        if (point.y < window.frame.size.height) {
            [(MJRefreshAutoNormalFooter *)self.mj_footer setTitle:@"" forState:MJRefreshStateNoMoreData];
            [self.mj_footer endRefreshingWithNoMoreData];
        }
    }];
    
    [self handleFooterRefresh:footRefreshState];
    NSString *value = [NSString stringWithFormat:@"%ld", (long)footRefreshState];
    objc_setAssociatedObject(self, &stateKey, value, OBJC_ASSOCIATION_COPY_NONATOMIC);
}

- (MJFooterRefreshState)footRefreshState {
    NSString *refreshState =objc_getAssociatedObject(self, &stateKey);
    if ([refreshState isEqualToString:@"MJFooterRefreshStateLoadMore"]) {
        return MJFooterRefreshStateNoMore;
    }
    else {
        return MJFooterRefreshStateLoadMore;
    }
}

- (void) handleFooterRefresh: (MJFooterRefreshState)footRefreshState {
    MJRefreshAutoNormalFooter *footer = (MJRefreshAutoNormalFooter*)self.mj_footer;
    switch (footRefreshState) {
        case MJFooterRefreshStateNormal: {
            [footer setTitle:@"" forState:MJRefreshStateIdle];
            break;
        }
        case MJFooterRefreshStateLoadMore: {
            [self.mj_footer endRefreshing];
            break;
        }
        case MJFooterRefreshStateNoMore: {
            [footer setTitle:MJRefreshBackFooterNoMoreDataText forState:MJRefreshStateNoMoreData];
            [self.mj_footer endRefreshingWithNoMoreData];
            break;
        }
        default:
            break;
    }
}

@end
