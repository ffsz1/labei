//
//  YPBaseGestureSuberController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseGestureSuberController.h"

@interface YPBaseGestureSuberController ()

@end

@implementation YPBaseGestureSuberController

- (instancetype)init {
    
    if (self = [super init]) {
        
        _page = 1;
        _count = 0;
    }
    
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.canScroll = NO;
    [self.view addSubview:self.tableView];
}

- (void)viewDidLayoutSubviews {
    
    [super viewDidLayoutSubviews];
    
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.leading.trailing.bottom.mas_equalTo(0);
    }];
}

- (void)scrollViewDidScroll:(UIScrollView *)scrollView {
    
    if (self.canScroll == NO) {
        scrollView.contentOffset = CGPointZero;
    }
    
    if (scrollView.contentOffset.y <= 0 ) {
        self.canScroll = NO;
        scrollView.contentOffset = CGPointZero;
        !self.block ?:self.block();
    }
}

- (void)handlerBlock:(notiBlock)block {
    self.block = block;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


- (UITableView *)tableView {
    
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectZero style:UITableViewStylePlain];
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        _tableView.backgroundColor = [UIColor whiteColor];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        
    }
    
    return _tableView;
}

@end

