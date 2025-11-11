//
//  HJRoomManagerListVC.m
//  HJLive
//
//  Created by apple on 2019/7/8.
//

#import "HJRoomManagerListVC.h"

#import "HJRoomManagerCell.h"

#import "HJImRoomCoreV2.h"
#import "HJIMRequestManager+Room.h"

@interface HJRoomManagerListVC ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (strong,nonatomic) NSMutableArray *dataArr;

@end

@implementation HJRoomManagerListVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.tableView.tableFooterView = [UIView new];
    
    [self getData];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];
    
    HJLightStatusBar
    
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
    HJRoomManagerCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJRoomManagerCell" forIndexPath:indexPath];
    if (indexPath.row<self.dataArr.count) {
        ChatRoomMember *model = [self.dataArr objectAtIndex:indexPath.row];
        cell.nameLabel.text = model.nick;
        [cell.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        cell.sexImageView.image = [UIImage imageNamed:model.gender==1?@"hj_home_attend_man":@"hj_home_attend_woman"];
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
    [HJIMRequestManager getRoomManagersWithRoomId:[NSString stringWithFormat:@"%ld", GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] success:^(NSArray<ChatRoomMember *> *roomMembers) {
        
        weakSelf.dataArr = [NSMutableArray arrayWithArray:roomMembers];
        
        [weakSelf.tableView reloadData];
        
    } failure:^(NSInteger code, NSString * _Nonnull errorMessage) {
        
    }];
}

- (void)removeManager:(NSIndexPath *)indexPath
{
    ChatRoomMember *user = [self.dataArr safeObjectAtIndex:indexPath.row];
    [GetCore(HJImRoomCoreV2) markManagerList:[user.account longLongValue]enable:NO];
    [self.dataArr removeObjectAtSafeIndex:indexPath.row];
    
    [self.tableView reloadData];
    
}


@end
