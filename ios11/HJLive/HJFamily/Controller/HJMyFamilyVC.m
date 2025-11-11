//
//  HJMyFamilyVC.m
//  HJLive
//
//  Created by feiyin on 2020/6/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMyFamilyVC.h"
#import "HJFamilyAnnouncementViewController.h"
#import "HJFamilyMembersViewController.h"

#import "HJFamilyManageViewController.h"
#import "HJFamilyRankListVC.h"

#import "HJAlertControllerCenter.h"
#import "HJFamilyAlertView.h"

#import "GGMyFamilyCell.h"
#import "GGMyFamilySectionHeader.h"
#import "GGMyFamilyQuitView.h"
#import "HJFamilyMembersView.h"

#import "HJFamilyInfoDetail.h"
#import "HJFamilyMemberModel.h"

#import "HJFamilyCore.h"
#import "HJFamilyCoreClient.h"

#import "UIImage+ImageEffects.h"

#import "HJAlertControllerCenter.h"
#import "UIView+getNavigationController.h"
#import "HJRoomViewControllerCenter.h"

#import "MBProgressHUD+YY.h"
#import "MMSheetView.h"
#import "HJFamilyEditTableController.h"

#import "HJRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"

#import "HJRefreshFactory.h"

#import "NSString+NIMKit.h"


#import "GGMaskView.h"
#import "HJVersionCoreHelp.h"
#import "FamilyDefines.h"

#define MyFamilyTableViewBottom iPhoneX? -90 :-56
#define MyFamilyShareBottomHeihtFixIphoneX (iPhoneX?-20:0)


@interface HJMyFamilyVC ()<HJFamilyCoreClient>

@property (weak, nonatomic) IBOutlet GGImageView *avaterView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *famliyIdLabel;
@property (weak, nonatomic) IBOutlet UILabel *familyNoticeLabel;
@property (weak, nonatomic) IBOutlet UILabel *memberCountLabel;
@property (weak, nonatomic) IBOutlet UIButton *familyChatButton;
@property (weak, nonatomic) IBOutlet UIButton *applyButton;
@property (weak, nonatomic) IBOutlet UIButton *photoButton;
@property (weak, nonatomic) IBOutlet UIImageView *familyBackgroundView;
@property (weak, nonatomic) IBOutlet UIView *noticeContentView;
@property (weak, nonatomic) IBOutlet UIView *membersContentView;
@property (weak, nonatomic) IBOutlet UIView *contentView;
@property (nonatomic, strong) HJFamilyMembersView *memebersView;


@end

@implementation HJMyFamilyVC

#pragma mark - Life cycle
- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    [self addCores];
    [self addControls];
    [self layoutControls];
    [self updateControls];
    [self loadNewInfo];
}

#pragma mark - Event
- (void)loadNewInfo {
    [GetCore(HJFamilyCore) familyCheckFamilyJoin];
}

- (void)skipToFamilyManage {
    HJFamilyManageViewController *viewController = [HJFamilyManageViewController new];
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToFamilyAnnouncement {
    HJFamilyAnnouncementViewController *viewController = [HJFamilyAnnouncementViewController new];
    viewController.familyName = self.familyInfoModel.familyName;
    viewController.familyId = self.familyInfoModel.familyId;
    viewController.familyLogo = self.familyInfoModel.familyLogo;
    viewController.familyBackground = self.familyInfoModel.bgimg;
    viewController.familyNotice = self.familyInfoModel.familyNotice;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToFamilyMembers {
    HJFamilyMembersViewController *viewController = [HJFamilyMembersViewController new];
    viewController.familyId = self.familyInfoModel.familyId;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToFamilyRankList {
    HJFamilyRankListVC *viewController = [[HJFamilyRankListVC alloc] init];
    [self.navigationController pushViewController:viewController animated:YES];
}

- (IBAction)noticeContentViewDidTap:(id)sender {
    [self skipToFamilyAnnouncement];
}

- (IBAction)membersContentViewDidTap:(id)sender {
    [self skipToFamilyMembers];
}

- (IBAction)applyButtonDidTap:(id)sender {
    [self showJoinFamilyAlert];
}

- (void)showJoinFamilyAlert {
    HJFamilyAlertView *alertView  = [[HJFamilyAlertView alloc] initWithFrame:CGRectMake(0, 0, 290, 199)];
    alertView.title = @"申请加入家族";
    alertView.message = [NSString stringWithFormat:@"1.每个人仅可以进入一个家族\n\n2.加入家族后享受家族带来的权限"];
    alertView.actionTitle = @"确认申请";
    @weakify(self);
    HJAlertControllerCenter *alertCenter = [HJAlertControllerCenter defaultCenter];
    alertView.didTapActionHandler = ^{
        @strongify(self);
        [alertCenter dismissAlertNeedBlock:YES];
        [self applyJoinFamily];
    };
    alertView.didTapCancelHandler = ^{
        [alertCenter dismissAlertNeedBlock:YES];
    };
    [alertCenter presentAlertWith:self.navigationController view:alertView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:^{
    }];
}

//申请加入家族
- (void)applyJoinFamily {
    [MBProgressHUD showMessage:@"申请中"];
    [GetCore(HJFamilyCore) applyJoinFamilyWithFamilyId:self.familyInfoModel.familyId];
}

#pragma mark - <FamilyCoreClient>
- (void)applyJoinFamilySuccessWithData:(id)data {
    [MBProgressHUD showSuccess:@"申请成功"];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)applyJoinFamilyFailedWithDataFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

- (void)familyCheckFamilyJoinSuccess:(HJFamilyModel *)data {
    self.applyButton.hidden = (data != nil);
}

- (void)familyCheckFamilyJoinFailedWithMessage:(NSString *)message {
    
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)updateNavigation {
}

- (void)updateControls {
    if (self.familyInfoModel) {
        [self.avaterView sd_setImageWithURL:[NSURL URLWithString:[self.familyInfoModel.familyLogo cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
        self.nameLabel.text = self.familyInfoModel.familyName;
        self.famliyIdLabel.text = [NSString stringWithFormat:@"ID:%@", self.familyInfoModel.familyId];
        self.familyNoticeLabel.text = self.familyInfoModel.familyNotice;
        self.memberCountLabel.text = [NSString stringWithFormat:@"家族成员/%@", self.familyInfoModel.member];
        [self.familyBackgroundView sd_setImageWithURL:[NSURL URLWithString:self.familyInfoModel.familyLogo] placeholderImage:[UIImage imageNamed:default_avatar]];
        self.memebersView.items = self.familyInfoModel.familyUsersDTOS;
    }
    
    self.familyChatButton.hidden = YES;
    self.photoButton.hidden = YES;
    self.applyButton.hidden = YES;
}

#pragma mark - Layout
- (void)addControls {
    [self.contentView insertSubview:self.memebersView belowSubview:self.memberCountLabel];
}

- (void)layoutControls {
    [self.memebersView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.contentView).offset(15);
        make.right.equalTo(self.contentView).offset(-15);
        make.top.equalTo(self.memberCountLabel.mas_bottom).offset(15);
    }];
}

#pragma mark - setters/getters
- (HJFamilyMembersView *)memebersView {
    if (!_memebersView) {
        _memebersView = [HJFamilyMembersView new];
        @weakify(self);
        _memebersView.didTapHandler = ^{
            @strongify(self);
            [self skipToFamilyMembers];
        };
    }
    return _memebersView;
}

@end
