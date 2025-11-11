//
//  YPMyFamilyVC.m
//  HJLive
//
//  Created by feiyin on 2020/6/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMyFamilyVC.h"
#import "YPFamilyAnnouncementViewController.h"
#import "YPFamilyMembersViewController.h"

#import "YPFamilyManageViewController.h"
#import "YPFamilyRankListVC.h"

#import "YPAlertControllerCenter.h"
#import "YPFamilyAlertView.h"

#import "YPGGMyFamilyCell.h"
#import "YPGGMyFamilySectionHeader.h"
#import "YPGGMyFamilyQuitView.h"
#import "YPFamilyMembersView.h"

#import "YPFamilyInfoDetail.h"
#import "YPFamilyMemberModel.h"

#import "YPFamilyCore.h"
#import "HJFamilyCoreClient.h"

#import "UIImage+ImageEffects.h"

#import "YPAlertControllerCenter.h"
#import "UIView+getNavigationController.h"
#import "YPRoomViewControllerCenter.h"

#import "MBProgressHUD+YY.h"
#import "MMSheetView.h"
#import "YPFamilyEditTableController.h"

#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"

#import "YPRefreshFactory.h"

#import "NSString+YPNIMKit.h"


#import "GGMaskView.h"
#import "YPVersionCoreHelp.h"
#import "FamilyDefines.h"

#define MyFamilyTableViewBottom iPhoneX? -90 :-56
#define MyFamilyShareBottomHeihtFixIphoneX (iPhoneX?-20:0)


@interface YPMyFamilyVC ()<HJFamilyCoreClient>

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
@property (nonatomic, strong) YPFamilyMembersView *memebersView;


@end

@implementation YPMyFamilyVC

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
    [GetCore(YPFamilyCore) familyCheckFamilyJoin];
}

- (void)skipToFamilyManage {
    YPFamilyManageViewController *viewController = [YPFamilyManageViewController new];
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToFamilyAnnouncement {
    YPFamilyAnnouncementViewController *viewController = [YPFamilyAnnouncementViewController new];
    viewController.familyName = self.familyInfoModel.familyName;
    viewController.familyId = self.familyInfoModel.familyId;
    viewController.familyLogo = self.familyInfoModel.familyLogo;
    viewController.familyBackground = self.familyInfoModel.bgimg;
    viewController.familyNotice = self.familyInfoModel.familyNotice;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToFamilyMembers {
    YPFamilyMembersViewController *viewController = [YPFamilyMembersViewController new];
    viewController.familyId = self.familyInfoModel.familyId;
    [self.navigationController pushViewController:viewController animated:YES];
}

- (void)skipToFamilyRankList {
    YPFamilyRankListVC *viewController = [[YPFamilyRankListVC alloc] init];
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
    YPFamilyAlertView *alertView  = [[YPFamilyAlertView alloc] initWithFrame:CGRectMake(0, 0, 290, 199)];
    alertView.title = @"申请加入家族";
    alertView.message = [NSString stringWithFormat:@"1.每个人仅可以进入一个家族\n\n2.加入家族后享受家族带来的权限"];
    alertView.actionTitle = @"确认申请";
    @weakify(self);
    YPAlertControllerCenter *alertCenter = [YPAlertControllerCenter defaultCenter];
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
    [GetCore(YPFamilyCore) applyJoinFamilyWithFamilyId:self.familyInfoModel.familyId];
}

#pragma mark - <FamilyCoreClient>
- (void)applyJoinFamilySuccessWithData:(id)data {
    [MBProgressHUD showSuccess:@"申请成功"];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)applyJoinFamilyFailedWithDataFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

- (void)familyCheckFamilyJoinSuccess:(YPFamilyModel *)data {
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
- (YPFamilyMembersView *)memebersView {
    if (!_memebersView) {
        _memebersView = [YPFamilyMembersView new];
        @weakify(self);
        _memebersView.didTapHandler = ^{
            @strongify(self);
            [self skipToFamilyMembers];
        };
    }
    return _memebersView;
}

@end
