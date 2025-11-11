//
//  UITableView+MJRefresh.m
//  HJLive
//
//  Created by feiyin on 2020/6/30.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UITableView+MJRefresh.h"

#import <objc/message.h>

@implementation UITableView (MJRefresh)

- (void)headerAction
{
    self.page = 0;
    if (self.headerBlock) {
        self.headerBlock();
    }
}

- (void)footerAction
{
    self.page += 1;
    if (self.footerBlock) {
        self.footerBlock();
    }
}

#pragma mark - setter/getter
- (NSInteger)page
{
    return [objc_getAssociatedObject(self, @"MJRefreshPage") integerValue];
}

- (void)setPage:(NSInteger)page
{
    objc_setAssociatedObject(self, @"MJRefreshPage", [NSNumber numberWithInteger:page], OBJC_ASSOCIATION_ASSIGN);
}

- (FreshBlock)headerBlock
{
    return objc_getAssociatedObject(self, @"MJRefreshHeaderBlock");
}

- (void)setHeaderBlock:(FreshBlock)headerBlock
{
    objc_setAssociatedObject(self, @"MJRefreshHeaderBlock", headerBlock, OBJC_ASSOCIATION_COPY_NONATOMIC);
    
    if (headerBlock != nil) {
        self.mj_header = [MJRefreshNormalHeader headerWithRefreshingTarget:self refreshingAction:@selector(headerAction)];
    }else{
        self.mj_header = nil;
    }
}

- (FreshBlock)footerBlock
{
    return objc_getAssociatedObject(self, @"MJRefreshFooterBlock");
}

- (void)setFooterBlock:(FreshBlock)footerBlock
{
    objc_setAssociatedObject(self, @"MJRefreshFooterBlock", footerBlock, OBJC_ASSOCIATION_COPY_NONATOMIC);
    
    if (footerBlock != nil) {
        self.mj_footer = [MJRefreshBackNormalFooter footerWithRefreshingTarget:self refreshingAction:@selector(footerAction)];
    }else{
        self.mj_footer = nil;
    }
    

}




@end
