//
//  XCManagerSettingControllerViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJManagerSettingController.h"
#import "HJManagerSettingCell.h"

#import "HJImRoomCoreV2.h"
#import "HJImRoomCoreClientV2.h"
#import "HJImRoomCoreClient.h"

#import <UIImageView+WebCache.h>
#import <NIMSDK/NIMSDK.h>
#import "UIView+XCToast.h"

#import "HJRefreshFactory.h"

@interface HJManagerSettingController ()

<
    UITableViewDelegate,
    UITableViewDataSource,
    XCManagerSettingCellDelegate,
    HJImRoomCoreClient,
    HJImRoomCoreClientV2
>

@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (strong, nonatomic) NSMutableArray<ChatRoomMember *> *list;
@property (weak, nonatomic) IBOutlet UILabel *titleLabel;
@property (assign, nonatomic) NSInteger page;

@end

@implementation HJManagerSettingController

- (void)viewDidLoad {
    [super viewDidLoad];
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    [self initView];

}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];

}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)initView {
    if (@available(iOS 11.0, *)) {
        self.tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }

    if (self.type == RoomBlackList) {
        self.title = NSLocalizedString(XCBlackTitle, nil);
        self.tableView.mj_footer = [HJRefreshFactory footerRefreshWithTarget:self refreshingAction:@selector(requestForMoreData)];
    }else if (self.type == RoomManager) {
        self.title = NSLocalizedString(XCRoomManager, nil);
    }
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    
    [self requestForNewData];
}

- (void)requestForNewData {
    
    [self.tableView hideToastView];
    
    if (self.type == RoomBlackList) {
        self.page = 1;
        [GetCore(HJImRoomCoreV2) queryManagerorBackList];
        self.tableView.mj_footer.hidden = YES;
    }else if (self.type == RoomManager) {
        [GetCore(HJImRoomCoreV2) getManagerList];
    }
}

- (void)requestForMoreData {
    self.page++;
    [GetCore(HJImRoomCoreV2) queryManagerorBackList];
}


#pragma mark - UITableViewDataSource
- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.list.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    HJManagerSettingCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJManagerSettingCell" forIndexPath:indexPath];
    
    [self configureCell:cell forRowAtIndexPath:indexPath];
    
    return cell;
}

#pragma mark - XCManagerSettingCellDelegate
- (void)removeBy:(NSIndexPath *)indexPath {
    if (self.type == RoomBlackList) {
        ChatRoomMember *user = [self.list safeObjectAtIndex:indexPath.row];
        
        [GetCore(HJImRoomCoreV2) markBlackList:[user.account longLongValue] enable:NO];
        [self.list removeObjectAtSafeIndex:indexPath.row];
        [self.titleLabel setText:[NSString stringWithFormat:@"%@%ld%@",NSLocalizedString(XCBlackTitle, nil),(long)self.list.count,NSLocalizedString(NIMKitPerson, nil)]];
        [self.tableView reloadData];
        
        if (!self.list.count) {
            [self.tableView showEmptyContentToastWithTitle:@"你还没有黑名单噢！" andImage:[UIImage imageNamed:@"blank"]];
        }
    }else if (self.type == RoomManager)  {
        ChatRoomMember *user = [self.list safeObjectAtIndex:indexPath.row];
        [GetCore(HJImRoomCoreV2) markManagerList:[user.account longLongValue]enable:NO];
        [self.list removeObjectAtSafeIndex:indexPath.row];
        [self.titleLabel setText:[NSString stringWithFormat:@"%@%ld%@",NSLocalizedString(XCRoomManager, nil),(long)self.list.count,NSLocalizedString(NIMKitPerson, nil)]];
        [self.tableView reloadData];
        
        if (!self.list.count) {
            [self.tableView showEmptyContentToastWithTitle:@"你还没有管理员噢！" andImage:[UIImage imageNamed:@"blank"]];
        }
    }
}


#pragma mark - ImRoomCoreClientV2
/**
 获取黑名单列表
 */
- (void)fetchBlackMemberSuccess:(NSArray<ChatRoomMember *> *)member page:(NSInteger)page {
    [self dealListWithMember:member page:page];
}

- (void)fetchBlackMemberNoMoreData {
    [self.tableView.mj_footer endRefreshing];
    self.tableView.mj_footer.hidden = YES;
}

- (void)fetchBlackMemberfailure {
    
    [self handleMembers];
}


- (void)dealListWithMember:(NSArray<ChatRoomMember *> *)member page:(NSInteger)page {
    if (page == 1) {
        self.list = [NSMutableArray array];
    }
    
    [self.list addObjectsFromArray:member];
    [self.tableView reloadData];
    [self handleMembers];
    
    if (self.list.count) {
        self.tableView.mj_footer.hidden = NO;
        self.tableView.mj_footer.state = MJRefreshStateIdle;
    }
}

/**
 获取管理员列表
 @param member 成员
 */
- (void)fetchManagerMemberSuccess:(NSArray<ChatRoomMember *> *)member {
    [self dealListWithMember:member page:1];
}

- (void)fetchManagerMemberfailure {
    [self handleMembers];
}


- (void)handleMembers {
    
    if (self.type == RoomBlackList) {
        [self.titleLabel setText:[NSString stringWithFormat:@"黑名单%ld人",(long)self.list.count]];
        if (!self.list.count) {
            [self.tableView showEmptyContentToastWithTitle:@"你还没有黑名单噢！" andImage:[UIImage imageNamed:@"blank"]];
        }
    }
    else {
        [self.titleLabel setText:[NSString stringWithFormat:@"管理员%ld人",(long)self.list.count]];
        if (!self.list.count) {
            [self.tableView showEmptyContentToastWithTitle:@"你还没有管理员噢！" andImage:[UIImage imageNamed:@"blank"]];
        }
    }
}

#pragma mark - ImRoomCoreClient

//manager
- (void)managerAdd:(NSString *)uid {
    [self requestForNewData];
}
- (void)managerRemove:(NSString *)uid {
    [self requestForNewData];
}
//black
- (void)onUserBeAddBlack:(NSString *)uid {
    [self requestForNewData];
}
- (void)onUserBeRemoveBlack:(NSString *)uid {
    [self requestForNewData];
}


#pragma mark - private method
- (void)configureCell:(HJManagerSettingCell *)cell forRowAtIndexPath:(NSIndexPath *)indexPath{
    cell.delegate = self;
    ChatRoomMember *member = self.list[indexPath.row];
    [cell.avatar sd_setImageWithURL:[NSURL URLWithString:member.avatar] placeholderImage:[UIImage imageNamed:default_avatar]];
    if (self.type == RoomBlackList) {
        cell.roleTagImageView.hidden = YES;
    }else if (self.type == RoomManager) {
        cell.roleTagImageView.hidden = NO;
        [cell.roleTagImageView setImage:[UIImage imageNamed:@"room_game_mamager_tag"]];
    }
    cell.indexPath = indexPath;
    
    [[GetCore(HJImRoomCoreV2) rac_fetchMemberUserInfoByUid:member.account] subscribeNext:^(id x) {
        NIMUser *user  = (NIMUser *)x;
        if (user.userInfo.gender == NIMUserGenderMale) {
            cell.sexImageView.image = [UIImage imageNamed:@"hj_sex_male_logo"];
        }else if (user.userInfo.gender == NIMUserGenderFemale) {
            cell.sexImageView.image = [UIImage imageNamed:@"hj_sex_female_logo"];
        }
    }];

    
    [cell.nameLabel setText:member.nick];
    
}


@end
