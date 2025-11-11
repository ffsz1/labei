//
//  YPRoomManagerListVC.m
//  HJLive
//
//  Created by apple on 2019/7/8.
//

#import "YPRoomManagerListVC.h"

#import "YPRoomManagerCell.h"

#import "YPImRoomCoreV2.h"
#import "YPIMRequestManager+Room.h"

@interface YPRoomManagerListVC ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong,nonatomic) NSMutableArray *dataArr;

@end

@implementation YPRoomManagerListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.tableFooterView = [UIView new];
    
    [self getData];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    YPLightStatusBar
    
}

#pragma mark <UITableViewDelegate,UITableViewDataSource>

- (CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 10;
}

- (CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return CGFLOAT_MIN;
}

- (UIView *)tableView:(UITableView *)tableView viewForHeaderInSection:(NSInteger)section
{
    UIView *view = [UIView new];
    view.backgroundColor = [UIColor clearColor];
    return view;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.dataArr.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    YPRoomManagerCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPRoomManagerCell" forIndexPath:indexPath];
    if (indexPath.row<self.dataArr.count) {
        YPChatRoomMember *model = [self.dataArr objectAtIndex:indexPath.row];
        cell.nameLabel.text = model.nick;
        [cell.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        cell.sexImageView.image = [UIImage imageNamed:model.gender==1?@"yp_home_attend_man":@"yp_home_attend_woman"];
        cell.indexPath = indexPath;
        __weak typeof(self)weakSelf = self;
        cell.removeBlock = ^(NSIndexPath *index) {
            [weakSelf removeManager:index];
        };
    }
    
    return cell;
}

- (IBAction)backAction:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

- (void)getData
{
    __weak typeof(self)weakSelf = self;
    [YPIMRequestManager getRoomManagersWithRoomId:[NSString stringWithFormat:@"%ld", GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] success:^(NSArray<YPChatRoomMember *> *roomMembers) {
        
        weakSelf.dataArr = [NSMutableArray arrayWithArray:roomMembers];
        
        [weakSelf.tableView reloadData];
        
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        
    }];
}

- (void)removeManager:(NSIndexPath *)indexPath
{
    YPChatRoomMember *user = [self.dataArr safeObjectAtIndex:indexPath.row];
    [GetCore(YPImRoomCoreV2) markManagerList:[user.account longLongValue]enable:NO];
    [self.dataArr removeObjectAtSafeIndex:indexPath.row];
    
    [self.tableView reloadData];
    
}


@end
