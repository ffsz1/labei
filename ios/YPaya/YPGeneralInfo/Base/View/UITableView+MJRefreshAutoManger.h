//
//  UITableView+MJRefreshAutoManger.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//
//  管理刷新状态

#import <UIKit/UIKit.h>

typedef NS_ENUM(NSInteger, MJFooterRefreshState) {
    MJFooterRefreshStateNormal,
    MJFooterRefreshStateLoadMore,
    MJFooterRefreshStateNoMore
};


@interface UITableView (MJRefreshAutoManger)
@property (nonatomic,assign)MJFooterRefreshState footRefreshState;
@end
