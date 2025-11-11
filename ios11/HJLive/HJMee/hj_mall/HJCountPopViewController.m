//
//  HJCountPopViewController.m
//  HJLive
//
//  Created by MacBook on 2020/8/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJCountPopViewController.h"

@interface HJCountPopViewController ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *masterTableView;
@property (nonatomic, strong) NSArray *masterDataSource;
@end

@implementation HJCountPopViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    self.preferredContentSize = CGSizeMake(130, 260);
    self.masterTableView.separatorStyle = UITableViewCellSeparatorStyleNone;
}

- (NSArray *)masterDataSource {
    if (_masterDataSource == nil) {
        _masterDataSource = @[@"自定义数量", @" 100", @" 50", @" 20", @" 10",@" 1"];
    }
    return _masterDataSource;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.masterDataSource.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"master"];
    if (cell == nil) {
        cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"master"];
    }
    cell.textLabel.text = self.masterDataSource[indexPath.row];
    cell.textLabel.textAlignment = NSTextAlignmentCenter;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    if (self.selectedCountAction) {
        self.selectedCountAction(indexPath.row, self.masterDataSource[indexPath.row]);
    }
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 40;
}

@end
