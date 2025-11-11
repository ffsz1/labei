//
//  YPRefreshFactory.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRefreshFactory.h"

@implementation YPRefreshFactory

+ (MJRefreshHeader *)headerRefreshWithTarget:(id)target refreshingAction:(SEL)action
{
    MJRefreshNormalHeader *header = [MJRefreshNormalHeader headerWithRefreshingTarget:target refreshingAction:action];
    header.stateLabel.font = [UIFont systemFontOfSize:10.0];
    header.lastUpdatedTimeLabel.font = [UIFont systemFontOfSize:10.0];
    header.arrowView.image = [UIImage imageNamed:@"refreshImage"];
    
    return header;
}

+ (MJRefreshFooter *)footerRefreshWithTarget:(id)target refreshingAction:(SEL)action
{
    MJRefreshAutoNormalFooter *footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:target refreshingAction:action];
//   MJRefreshBackNormalFooter *footer = [MJRefreshBackNormalFooter footerWithRefreshingTarget:target refreshingAction:action];
    footer.stateLabel.font = [UIFont systemFontOfSize:10.0];
    return footer;
}

+ (MJRefreshHeader *)headerRefreshWithTarget:(id)target refreshingAction:(SEL)action color:(UIColor *)color {
    MJRefreshNormalHeader *header = [MJRefreshNormalHeader headerWithRefreshingTarget:target refreshingAction:action];
    
    if (color) {
        header.stateLabel.textColor = color;
        header.lastUpdatedTimeLabel.textColor = color;
    }
    
    header.stateLabel.font = [UIFont systemFontOfSize:10.0];
    header.lastUpdatedTimeLabel.font = [UIFont systemFontOfSize:10.0];
    header.arrowView.image = [UIImage imageNamed:@"refreshImage"];
    
    return header;

}

+ (MJRefreshFooter *)footerRefreshWithTarget:(id)target refreshingAction:(SEL)action color:(UIColor *)color {
    MJRefreshAutoNormalFooter *footer = [MJRefreshAutoNormalFooter footerWithRefreshingTarget:target refreshingAction:action];
    //   MJRefreshBackNormalFooter *footer = [MJRefreshBackNormalFooter footerWithRefreshingTarget:target refreshingAction:action];
    if (color) {
        footer.stateLabel.textColor = color;
    }
    footer.stateLabel.font = [UIFont systemFontOfSize:10.0];
    return footer;

}


@end
