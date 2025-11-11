//
//  YPOnlineListTwoView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//



#import "YPOnlineListTwoView.h"
#import "YPRoomMemberListCell.h"
#import "HJImRoomCoreClientV2.h"
#import <NIMSDK/NIMSDK.h>
#import "YPImRoomCoreV2.h"
#import "YPRoomQueueCoreV2Help.h"
#import "UITableView+Refresh.h"
#import "UIView+XCToast.h"
#import "HJRoomCoreClient.h"
#import <MJRefresh.h>
#import "YPYYActionSheetViewController.h"
#import "YPOnlineCore.h"
#import "YPRoomViewControllerCenter.h"

#import "UIView+getTopVC.h"


@interface YPOnlineListTwoView()<
UITableViewDelegate,
UITableViewDataSource,
HJImRoomCoreClientV2,
HJRoomCoreClient
>
@property (strong, nonatomic) NSMutableArray<YPChatRoomMember *> *onlineUserList;
@property (weak, nonatomic) IBOutlet UIView *online;
@end

@implementation YPOnlineListTwoView
{
    int pageNo;
}
#pragma mark - Private Method
- (void)addCore {
    AddCoreClient(HJImRoomCoreClientV2, self);
    AddCoreClient(HJRoomCoreClient, self);
}

- (void)removeCore {
    RemoveCoreClientAll(self);
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    YPChatRoomMember *member = [self.onlineUserList safeObjectAtIndex:(int)indexPath.row];
//    UserID uid = [member.account longLongValue];
//    if (self.alertUserInformation) {
//        self.alertUserInformation(uid);
//    }
    [self showAlertViewWithMember:member];
}

- (void)showAlertViewWithMember:(YPChatRoomMember *)member {
    UserID uid = [member.account longLongValue];
    if (self.alertUserInformation) {
        self.alertUserInformation(uid);
    }
    return;
/*
    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    
        if (myMember.account == member.account || member.is_creator)  {
        if (self.alertUserInformation) {
            self.alertUserInformation(uid);
        }
        return;
    }
    
    //普通用户对麦下用户，直接打开该用户的资料卡片
    if (!myMember.is_creator && !myMember.is_manager){
        if (self.alertUserInformation) {
            self.alertUserInformation(uid);
        }
        return;
    }
    
    @weakify(self);
    
    
    UIAlertController *alter = [UIAlertController alertControllerWithTitle:nil message:nil preferredStyle:UIAlertControllerStyleActionSheet];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"送礼物" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        
        if (self.sendGiftBlock) {
            self.sendGiftBlock(uid, member.nick);
        }
        
//        [GetCore(YPOnlineCore) showGitToUidFromOnline:uid withName:member.nick];
//        [self removeCore];
//        [self removeFromSuperview];

    }]];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        
    }]];
    
//    YPYYActionSheetViewController *sheet = [[YPYYActionSheetViewController alloc]init];
//    [sheet addButtonWithTitle:NSLocalizedString(XCRoomSendGift, nil) block:^(YPYYActionSheetViewController *controller) {
//        @strongify(self);
//        [GetCore(YPOnlineCore) showGitToUidFromOnline:uid withName:member.nick];
//    }];
    
    [alter addAction:[UIAlertAction actionWithTitle:@"查看资料" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        if (self.alertUserInformation) {
            self.alertUserInformation(uid);
        }
    }]];
    
//    [sheet addButtonWithTitle:NSLocalizedString(XCRoomCheckInfo, nil) block:^(YPYYActionSheetViewController *controller) {
//        @strongify(self);
//        if (self.alertUserInformation) {
//            self.alertUserInformation(uid);
//        }
//    }];
    
    if (myMember.is_creator || myMember.is_manager) {
        if ([GetCore(YPRoomQueueCoreV2Help) isOnMicro:uid]) {
            
            [alter addAction:[UIAlertAction actionWithTitle:@"抱Ta下麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                @strongify(self);
                NSString *positon = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:uid];
                if (positon) {
                    [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:positon.intValue];
                }
            }]];
            
//            [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickHimDownMic, nil) block:^(YPYYActionSheetViewController *controller) {
//                @strongify(self);
//                NSString *positon = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:uid];
//                if (positon) {
//                    [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:positon.intValue];
//                }
//            }];
            
            [alter addAction:[UIAlertAction actionWithTitle:@"封锁此座位" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                @strongify(self);
                YPChatRoomMember *seq = [GetCore(YPRoomQueueCoreV2Help) findTheMemberByUserId:uid];
                NSString *positon = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:uid];
                if (seq) {
                    if (positon) {
                        [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:positon.intValue];
                    }
                }
                if (positon) {
                    [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:positon.intValue];
                }
            }]];
            
//            [sheet addButtonWithTitle:NSLocalizedString(XCRoomLockThatSeat, nil) block:^(YPYYActionSheetViewController *controller) {
//                @strongify(self);
//                YPChatRoomMember *seq = [GetCore(YPRoomQueueCoreV2Help) findTheMemberByUserId:uid];
//                NSString *positon = [GetCore(YPRoomQueueCoreV2Help) findThePositionByUid:uid];
//                if (seq) {
//                    if (positon) {
//                        [GetCore(YPRoomQueueCoreV2Help) kickDownMic:uid position:positon.intValue];
//                    }
//                }
//                if (positon) {
//                    [GetCore(YPRoomQueueCoreV2Help) lockMicPlace:positon.intValue];
//                }
//            }];
        } else {
            
            if(myMember.is_creator){
//                [alter addAction:[UIAlertAction actionWithTitle:@"抱Ta上麦" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
//                    @strongify(self);
//                    [GetCore(YPRoomQueueCoreV2Help) inviteUpFreeMic:uid];
//
//                }]];
                [alter addAction:[UIAlertAction actionWithTitle:@"踢出房间" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    @strongify(self);
                    [YPMidSure kickUser:uid didKickFinish:^{
                        [self.onlineUserList removeObject:member];
                        [self.tableView reloadData];
                    }];
                }]];
            }
            
//            [sheet addButtonWithTitle:NSLocalizedString(XCRoomBringHimUpMic, nil) block:^(YPYYActionSheetViewController *controller) {
//                @strongify(self);
//                [GetCore(YPRoomQueueCoreV2Help) inviteUpFreeMic:uid];
//            }];
        }
        
        if (!member.is_manager && !member.is_creator) {
            
            [alter addAction:[UIAlertAction actionWithTitle:@"踢出房间" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                @strongify(self);
                [YPMidSure kickUser:uid didKickFinish:^{
                    [self.onlineUserList removeObject:member];
                    [self.tableView reloadData];
                }];
            }]];
            
//            [sheet addButtonWithTitle:NSLocalizedString(XCRoomKickOutTheRoom, nil) block:^(YPYYActionSheetViewController *controller) {
//                @strongify(self);
//                [YPMidSure kickUser:uid didKickFinish:^{
//                    [self.onlineUserList removeObject:member];
//                    [self.tableView reloadData];
//                }];
//            }];
        }
        
//        (myMember.is_creator && (!member.is_creator && !member.is_manager)) 原条件
        if (myMember.is_creator && !member.is_creator) {
            if(member.is_manager){
                [alter addAction:[UIAlertAction actionWithTitle:@"移除管理员" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    @strongify(self);
                    [GetCore(YPImRoomCoreV2) markManagerList:uid enable:NO];

                }]];
            }else{
                [alter addAction:[UIAlertAction actionWithTitle:@"设置管理员" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                    @strongify(self);
                    [GetCore(YPImRoomCoreV2) markManagerList:uid enable:YES];

                }]];
            }
            
//            [sheet addButtonWithTitle:NSLocalizedString(XCRoomSetToBeManager, nil) block:^(YPYYActionSheetViewController *controller) {
//                @strongify(self);
//                [GetCore(YPImRoomCoreV2) markManagerList:uid enable:YES];
//            }];
        }

        if (!(myMember.is_manager && member.is_manager)) {
            
            
            [alter addAction:[UIAlertAction actionWithTitle:@"加入房间黑名单" style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
                @strongify(self);
                [self showAlertWithAddBlackListWithName:member.nick withUid:uid];

            }]];
            
//            [sheet addButtonWithTitle:NSLocalizedString(XCPersonalInfoInBlackList, nil) block:^(YPYYActionSheetViewController *controller) {
//                @strongify(self);
//                [self showAlertWithAddBlackListWithName:member.nick withUid:uid];
//            }];
        }
    }
    
    [[self topViewController] presentViewController:alter animated:YES completion:nil];
 
*/
}

- (void)showAlertWithAddBlackListWithName:(NSString *)name withUid:(UserID)uid {
    NSString *title = [NSString stringWithFormat:@"%@%@",NSLocalizedString(XCRoomMoveToBlackTitle, nil),name];
    UIAlertController *alert = [UIAlertController alertControllerWithTitle:title message:NSLocalizedString(XCRoomMoveToBlackMsg, nil) preferredStyle:UIAlertControllerStyleAlert];
    @weakify(self);
    UIAlertAction *enter = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomConfirm, nil) style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
        @strongify(self);
        [GetCore(YPImRoomCoreV2) markBlackList:uid enable:YES];
    }];
    UIAlertAction *cancel = [UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
    }];
    [alert addAction:enter];
    [alert addAction:cancel];
    [[YPRoomViewControllerCenter defaultCenter].current presentViewController:alert animated:YES completion:nil];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    YPRoomMemberListCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPRoomMemberListCell"];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    YPChatRoomMember *member = [self.onlineUserList safeObjectAtIndex:indexPath.row];
    cell.roleImageView.hidden = YES;
    cell.mamagerTag.hidden = YES;
    cell.arrow.hidden = YES;
    cell.nameLabel.text = member.nick;
//    cell.nameLabel.textColor = [UIColor blackColor];
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
    
    if (member.account.integerValue == GetCore(YPImRoomCoreV2).roomOwner.account.integerValue && member.account.integerValue != 0) {
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
    
    return cell;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.onlineUserList.count;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    pageNo = 1;

    [self addCore];
    
    self.backgroundColor = [UIColorHex(FFFFFF) colorWithAlphaComponent:1];
    self.tableView.backgroundColor = [UIColorHex(FFFFFF) colorWithAlphaComponent:1];
    self.online.backgroundColor = [UIColorHex(FFFFFF) colorWithAlphaComponent:1];
    
    self.layer.cornerRadius = 8;
    self.layer.masksToBounds = YES;
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPRoomMemberListCell" bundle:nil] forCellReuseIdentifier:@"YPRoomMemberListCell"];
    
    [self pullDownRefresh];

    @weakify(self);
    MJRefreshNormalHeader *header = [MJRefreshNormalHeader headerWithRefreshingBlock:^{
        @strongify(self);
        pageNo = 1;
        [GetCore(YPImRoomCoreV2) queryChatRoomMembersWithPage:pageNo state:0];
    }];
    header.lastUpdatedTimeLabel.hidden = YES;
    header.stateLabel.hidden = YES;
    
    MJRefreshAutoNormalFooter *footer = [MJRefreshAutoNormalFooter footerWithRefreshingBlock:^{
        @strongify(self);
        
        [GetCore(YPImRoomCoreV2) queryChatRoomMembersWithPage:pageNo state:1];
    }];
    
    self.tableView.mj_footer = footer;
    self.tableView.mj_header = header;
}

- (void)onGetRoomQueueSuccessV2:(NSMutableArray<YPIMQueueItem *> *)info {
//    [self pullDownRefresh];
}

- (void)pullDownRefresh {
    pageNo = 1;
    [GetCore(YPImRoomCoreV2) queryChatRoomMembersWithPage:pageNo state:0];
}

- (NSMutableArray<YPChatRoomMember *> *)onlineUserList{
    if (_onlineUserList == nil) {
        _onlineUserList = [NSMutableArray array];
    }
    return _onlineUserList;
}

#pragma mark - RoomCoreClient
- (void)onManagerAdd:(YPChatRoomMember *)member{
//    [self pullDownRefresh];
}

- (void)onManagerRemove:(YPChatRoomMember *)member{
//    [self pullDownRefresh];
}

//用户被加入黑名单
- (void)userBeAddBlack:(YPChatRoomMember *)member{
//    [self pullDownRefresh];
}

#pragma mark - ImRoomCoreClient
//user被踢 exit
- (void)onUserExitChatRoom:(NSString *)roomId uid:(NSString *)uid{
//    [self pullDownRefresh];
}

- (void)fetchRoomUserListNoMoreData {
    [self.tableView.mj_footer endRefreshingWithNoMoreData];
}

#pragma mark - ImRoomCoreClientV2
- (void)fetchRoomUserListSuccess:(int)state {
    
//    if (pageNo == 1) {
//        [self.onlineUserList removeAllObjects];
//    }
    
    pageNo++;
    self.onlineUserList = GetCore(YPImRoomCoreV2).displayMembers;
//    [self.onlineUserList addObjectsFromArray:GetCore(YPImRoomCoreV2).displayMembers];
    [self.tableView reloadData];
    [self.tableView.mj_header endRefreshing];
    [self.tableView.mj_footer endRefreshing];

//    [self.tableView endRefreshStatus:state hasMoreData:self.onlineUserList.count];
}

@end
