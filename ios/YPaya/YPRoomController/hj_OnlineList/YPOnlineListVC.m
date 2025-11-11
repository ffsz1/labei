//
//  YPOnlineListVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPOnlineListVC.h"
#import "YPRoomMemberListCell.h"

#import "YPAuthCoreHelp.h"
#import "YPImRoomCoreV2.h"
#import "HJImRoomCoreClient.h"
#import "HJImRoomCoreClientV2.h"
#import "YPRoomQueueCoreV2Help.h"
#import "HJRoomCoreClient.h"

#import "YPRoomViewControllerFactory.h"
#import "YPUserViewControllerFactory.h"
#import "YPAlertControllerCenter.h"
#import "YPRoomViewControllerCenter.h"


#import "YPYYActionSheetViewController.h"

#import "UITableView+Refresh.h"
#import "YPOnlineListCell.h"
#import "MMAlertView.h"
#import "MMSheetView.h"

//拍卖相关



#import "UIView+XCToast.h"
@interface YPOnlineListVC ()

<
    UITableViewDelegate,
    UITableViewDataSource,
    HJImRoomCoreClientV2
>

//@property (weak, nonatomic) IBOutlet UILabel *onlineNumLabel;
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (nonatomic, strong) YPYYActionSheetViewController* startAuctionSheet;//开启拍卖
@property (strong, nonatomic) NSMutableArray<YPChatRoomMember *> *onlineUserList;
@property (assign, nonatomic) NSInteger currentPage;
@property (strong, nonatomic) NSIndexPath *currentIndex;
@property(nonatomic, strong)YPChatRoomInfo *roomInfo;
@end

@implementation YPOnlineListVC

#pragma mark - lift cycle

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    [self addCore];
    [self initView];
    [self updateView];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

#pragma mark - UITableViewDataSource

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.onlineUserList.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPChatRoomMember *member = [self.onlineUserList safeObjectAtIndex:indexPath.row];
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
        YPOnlineListCell *cell = (YPOnlineListCell *)[tableView dequeueReusableCellWithIdentifier:@"YPOnlineListCell"];
        [cell.avatar qn_setImageImageWithUrl:member.avatar placeholderImage:default_avatar type:(ImageType)ImageTypeUserIcon];
        cell.nameLabel.text = member.nick;
        [[[GetCore(YPImRoomCoreV2) rac_fetchMemberUserInfoByUid:member.account] takeUntil:cell.rac_prepareForReuseSignal] subscribeNext:^(id x) {
            NIMUser *user  = (NIMUser *)x;
            if (user.userInfo.gender == NIMUserGenderMale) {
                cell.genderImageView.image = [UIImage imageNamed:@"yp_sex_male_logo"];
            }else if (user.userInfo.gender == NIMUserGenderFemale) {
                cell.genderImageView.image = [UIImage imageNamed:@"yp_sex_female_logo"];
            }
            cell.nameLabel.textColor = [UIColor blackColor];
            cell.avatar.layer.cornerRadius = cell.avatar.frame.size.width / 2;
            cell.avatar.layer.masksToBounds = YES;
        }];
        return cell;
    }else {
        YPRoomMemberListCell *cell = (YPRoomMemberListCell *)[tableView dequeueReusableCellWithIdentifier:@"YPRoomMemberListCell"];
        cell.roleImageView.hidden = YES;
        cell.mamagerTag.hidden = YES;
        cell.arrow.hidden = YES;
        cell.nameLabel.text = member.nick;
        cell.nameLabel.textColor = [UIColor blackColor];
        cell.avatar.layer.cornerRadius = cell.avatar.frame.size.width / 2;
        cell.avatar.layer.masksToBounds = YES;
        [cell.avatar qn_setImageImageWithUrl:member.avatar placeholderImage:default_avatar type:(ImageType)ImageTypeUserIcon];
        [[[GetCore(YPImRoomCoreV2) rac_fetchMemberUserInfoByUid:member.account] takeUntil:cell.rac_prepareForReuseSignal] subscribeNext:^(id x) {
            NIMUser *user  = (NIMUser *)x;
            if (user.userInfo.gender == NIMUserGenderMale) {
                cell.genderImageView.image = [UIImage imageNamed:@"yp_sex_male_logo"];
            }else if (user.userInfo.gender == NIMUserGenderFemale) {
                cell.genderImageView.image = [UIImage imageNamed:@"yp_sex_female_logo"];
            }
        }];
        
        
        if (member.is_creator) {
            cell.roleImageView.hidden = NO;
            [cell.roleImageView setImage:[UIImage imageNamed:@"room_game_owner_tag"]];
            cell.mamagerTag.hidden = YES;
            
            cell.onMicroWidthCOnstraint.constant = 25;
        }else {
            if ([GetCore(YPRoomQueueCoreV2Help) isOnMicro:[member.account longLongValue]]) {
                cell.roleImageView.hidden = NO;
                cell.onMicroWidthCOnstraint.constant = 25;
                [cell.roleImageView setImage:[UIImage imageNamed:@"room_game_mic_tag"]];
                if (member.is_manager) {
                    cell.managerMarginLeadingTag.constant = 8;
                }else {
                    cell.managerMarginLeadingTag.constant = 0;
                }
            }else{
                if ([GetCore(YPRoomQueueCoreV2Help) isOnMicro:[member.account longLongValue]]) {
                    cell.roleImageView.hidden = YES;
                    cell.onMicroWidthCOnstraint.constant = 0;
                    cell.managerMarginLeadingTag.constant = 8;
                }else {
                    cell.roleImageView.hidden = YES;
                    cell.onMicroWidthCOnstraint.constant = 0;
                    cell.managerMarginLeadingTag.constant = 0;
                }
                
                
            }
            
            if (member.is_manager) {
                cell.mamagerTag.hidden = NO;
            }else {
                cell.mamagerTag.hidden = YES;
            }
        }
        
        [[[GetCore(YPImRoomCoreV2) rac_fetchMemberUserInfoByUid:member.account] takeUntil:cell.rac_prepareForReuseSignal] subscribeNext:^(id x) {
            NIMUser *user  = (NIMUser *)x;
            if (user) {
                if (user.userInfo.gender == NIMUserGenderMale) {
                    cell.genderImageView.hidden = NO;
                    [cell.genderImageView setImage:[UIImage imageNamed:@"room_game_online_male"]];
                }else if (user.userInfo.gender == NIMUserGenderFemale) {
                    cell.genderImageView.hidden = NO;
                    [cell.genderImageView setImage:[UIImage imageNamed:@"room_game_online_female"]];
                }else {
                    cell.genderImageView.hidden = YES;
                }
            }
        }];
        
        if (indexPath.row % 2 == 0) {
            cell.backView.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:0.02];
        } else {
            cell.backView.backgroundColor = [UIColor colorWithRed:255/255.0 green:255/255.0 blue:255/255.0 alpha:0];
        }
        
        return cell;

    }

}


#pragma mark - UITableViewDelegate

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 60;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
     [tableView deselectRowAtIndexPath:indexPath animated:NO];
    self.currentIndex = indexPath;
    @weakify(self);
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
        YPChatRoomMember *member = [self.onlineUserList safeObjectAtIndex:(int)indexPath.row];
        if ([member.account longLongValue] == [GetCore(YPAuthCoreHelp) getUid].userIDValue) {
            [GetCore(YPRoomQueueCoreV2Help) upMic:self.pos];
        }else {
            [GetCore(YPRoomQueueCoreV2Help) inviteUpMic:[member.account longLongValue] postion:[NSString stringWithFormat:@"%d",self.pos]];
        }
        [self.navigationController popViewControllerAnimated:YES];
    }
}


#pragma mark - RoomUIClient
- (void)roomVCWillDisappear {
    [[YPAlertControllerCenter defaultCenter]dismissAlertNeedBlock:NO];
}

//获取房间信息
- (void)updateInfo {
    self.roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
}

#pragma mark - RoomCoreClient
- (void)onManagerAdd:(YPChatRoomMember *)member{
    [self pullDownRefresh:1];
}
- (void)onManagerRemove:(YPChatRoomMember *)member{
     [self pullDownRefresh:1];
}
//用户被加入黑名单
- (void)userBeAddBlack:(YPChatRoomMember *)member{
    [self pullDownRefresh:1];
}
#pragma mark - ImRoomCoreClient
//user被踢 exit
- (void)onUserExitChatRoom:(NSString *)roomId uid:(NSString *)uid{
    [self pullDownRefresh:1];
}

#pragma mark - ImRoomCoreClientV2
//获取队列
- (void)onGetRoomQueueSuccessV2:(NSMutableArray<YPIMQueueItem *> *)info{
    [self pullDownRefresh:1];
}
- (void)fetchRoomUserListSuccess:(int)state {
    [self.onlineUserList removeAllObjects];
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
        [self.onlineUserList addObjectsFromArray:GetCore(YPImRoomCoreV2).noMicMembers];
        if (self.onlineUserList.count == 0) {
            [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCRoomNoUserUp, nil) andImage:[UIImage imageNamed:@"blank"]];
        }else {
            [self.tableView hideToastView];
        }
    }else {
        [self.onlineUserList addObjectsFromArray:GetCore(YPImRoomCoreV2).displayMembers];
        if (self.onlineUserList.count == 0) {
            [self.tableView showEmptyContentToastWithTitle:NSLocalizedString(XCRoomNoUser, nil) andImage:[UIImage imageNamed:@"blank"]];

        }else {
            [self.tableView hideToastView];
        }
    }
    
    [self.tableView reloadData];
    
    [self.tableView endRefreshStatus:state hasMoreData:self.onlineUserList.count];
}

#pragma mark - Private Method
- (void)addCore {
    AddCoreClient(HJImRoomCoreClient, self);
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJRoomCoreClient, self);
}

- (void)initView {
    self.title = @"在线听众";
    if (@available(iOS 11.0, *)) {
        self.tableView.contentInsetAdjustmentBehavior = UIScrollViewContentInsetAdjustmentNever;
    } else {
        self.automaticallyAdjustsScrollViewInsets = NO;
    }
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
        [self.tableView registerNib:[UINib nibWithNibName:@"YPOnlineListCell" bundle:nil] forCellReuseIdentifier:@"YPOnlineListCell"];
    }else {
        [self.tableView registerNib:[UINib nibWithNibName:@"YPRoomMemberListCell" bundle:nil] forCellReuseIdentifier:@"YPRoomMemberListCell"];
    }
    
    [self updateInfo];
}

- (void)updateView {
    [self pullDownRefresh:1];
}










//add black
- (void)showAlertWithAddBlackList:(UserID)uid {
    
    [[GetCore(YPUserCoreHelp) getUserInfoByUid:uid refresh:NO] subscribeNext:^(id x) {
        UserInfo *info = (UserInfo *)x;
        NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),info.nick];
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            [GetCore(YPImRoomCoreV2) markBlackList:uid enable:YES];
        }];
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        [alert addAction:enter];
        [alert addAction:cancel];
        [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
    }];
    
    
}

//是否在麦上
- (BOOL)userIsOnMicroWith:(UserID)uid {
    
    NSArray *micMembers = [GetCore(YPImRoomCoreV2).micMembers copy];
    if (micMembers != nil && micMembers.count > 0) {
        for (int i = 0; i < micMembers.count; i ++) {
            YPChatRoomMember *chatRoomMember = micMembers[i];
            if ([chatRoomMember.account longLongValue] == uid) {
                return YES;
            }
        }
    }
    return NO;
}



#pragma mark - Refresh

- (void)setupRefreshTarget:(UITableView *)tableView{
    
    [tableView setupRefreshFunctionWith:RefreshTypeHeaderAndFooter];
    
    [tableView pullUpRefresh:^(int page, BOOL isLastPage) {
        
        [self pullUpRefresh:page lastPage:isLastPage];
    }];
    
    [tableView pullDownRefresh:^(int page)
     {
         [self pullDownRefresh:page];
     }];
}

- (void)pullDownRefresh:(int)page{
    [self.onlineUserList removeAllObjects];
    self.currentPage = page;
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
        [GetCore(YPImRoomCoreV2) queryNoMicChatRoomMembersWithPage:self.currentPage state:0];

    }else {
        [GetCore(YPImRoomCoreV2) queryChatRoomMembersWithPage:self.currentPage state:0];
    }
}

- (void)pullUpRefresh:(int)page lastPage:(BOOL)isLastPage{
    self.currentPage = page;
    if (GetCore(YPImRoomCoreV2).currentRoomInfo.type == RoomType_Game) {
        [GetCore(YPImRoomCoreV2) queryNoMicChatRoomMembersWithPage:self.currentPage state:1];

    }else {
        [GetCore(YPImRoomCoreV2) queryChatRoomMembersWithPage:self.currentPage state:1];
    }
}

#pragma mark - Getter

- (NSMutableArray<YPChatRoomMember *> *)onlineUserList{
    if (_onlineUserList == nil) {
        _onlineUserList = [NSMutableArray array];
    }
    return _onlineUserList;
}



@end
