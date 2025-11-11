//
//  YPBaseTableViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseTableViewController.h"
#import "UIColor+UIColor_Hex.h"

@interface YPBaseTableViewController ()
@property (nonatomic, weak)UITableView * targetTableView;
@end

@implementation YPBaseTableViewController

-(UITableView *)tableView{
    
    if (!_tableView) {
        _tableView = [[UITableView alloc] initWithFrame:CGRectMake(0,0,[UIScreen mainScreen].bounds.size.width,[UIScreen mainScreen].bounds.size.height) style:UITableViewStylePlain];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.separatorColor = [UIColor colorWithHexString:@"#ebebeb"];
        _tableView.backgroundColor = [UIColor colorWithHexString:@"#f5f5f5"];
        [self.view addSubview:_tableView];
        
    }
    
    return _tableView;
    
    
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    if (@available(iOS 11.0, *)) {
        self.tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }

}

#pragma mark - UITableViewDataSource


- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return 0;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    return nil;
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}


#pragma mark -- fresh
- (void)setupRefreshTarget:(UITableView *)tableView
{
    //设置tableView默认 对象为lybaseviewcontroller的tableview
    if (tableView==nil) tableView = self.tableView;
    
    self.targetTableView = tableView;
    
    [tableView setupRefreshFunctionWith:RefreshTypeHeaderAndFooter];
    
    [tableView pullUpRefresh:^(int page, BOOL isLastPage) {
        
        [self pullUpRefresh:page lastPage:isLastPage];
    }];
    
    [tableView pullDownRefresh:^(int page)
     {
         [self pullDownRefresh:page];
     }];
}

- (void)setupRefreshTarget:(UITableView *)tableView With:(RefreshType)type
{
    //设置tableView默认 对象为lybaseviewcontroller的tableview
    if (tableView==nil) tableView = self.tableView;
    
    self.targetTableView = tableView;
    
    [tableView setupRefreshFunctionWith:type];
    
    if (type == RefreshTypeHeader)
    {
        [tableView pullDownRefresh:^(int page)
         {
             [self pullDownRefresh:page];
         }];
    }
    else if (type==RefreshTypeFooter)
    {
        [tableView pullUpRefresh:^(int page, BOOL isLastPage) {
            
            [self pullUpRefresh:page lastPage:isLastPage];
        }];
    }
    else
    {
        [self setupRefreshTarget:tableView];
    }
}

- (void)pullDownRefresh:(int)page
{
    
}

// 进入页面，建议在此处添加
- (void)viewDidAppear:(BOOL)animated {
    [super viewDidAppear:animated];
    
}

// 退出页面，建议在此处添加
- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
}

- (void)pullUpRefresh:(int)page lastPage:(BOOL)isLastPage
{
    
}
//请求成功结束刷新状态
- (void)successEndRefreshStatus:(int)status totalPage:(int)totalPage
{
    [self.targetTableView endRefreshStatus:status totalPage:totalPage];
}
//请求成功结束刷新状态
- (void)successEndRefreshStatus:(int)status hasMoreData:(BOOL)hasMore
{
    [self.targetTableView endRefreshStatus:status hasMoreData:hasMore];
}


//请求失败结束刷新状态
- (void)failEndRefreshStatus:(int)status
{
    [self.targetTableView endRefreshStatus:status];
}

////允许接受多个手势
//- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer{
//
//    return YES;
//}

@end
