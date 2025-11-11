//
//  YPBaseGestureSuberController.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYViewController.h"

NS_ASSUME_NONNULL_BEGIN

typedef void (^notiBlock)(void);//通知主控制器页面可以滑动了

@interface YPBaseGestureSuberController : YPYYViewController<UITableViewDataSource,UITableViewDelegate>

@property (nonatomic, strong) UITableView *tableView;

@property (nonatomic, assign) NSInteger headerSelectType;

@property (nonatomic, assign) BOOL canScroll;
@property (nonatomic, copy) notiBlock block;

@property (nonatomic, assign) NSInteger page;
@property (nonatomic, assign) NSInteger count;

- (void)handlerBlock:(notiBlock)block;

@end
NS_ASSUME_NONNULL_END
