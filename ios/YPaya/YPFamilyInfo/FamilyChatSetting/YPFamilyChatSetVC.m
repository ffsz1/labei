//
//  YPFamilyChatSetVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyChatSetVC.h"
#import "YPFamilyChatMemberVC.h"

#import "YPFamilyChatUserCell.h"
#import "YPFamilyCore.h"

#import "UIImage+ImageEffects.h"
#import "YPNIMKitInfo.h"
#import "YPNIMKit.h"

#import "NSString+YPNIMKit.h"
#import "YPHttpRequestHelper+Family.h"

@interface YPFamilyChatSetVC ()<UICollectionViewDataSource,UICollectionViewDelegateFlowLayout,NIMChatManagerDelegate,NIMTeamManagerDelegate>
@property (weak, nonatomic) IBOutlet UIImageView *bgImageView;
@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *familyNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *tipLabel;
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;
@property (weak, nonatomic) IBOutlet UISwitch *msgSwitch;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *familyTipView_height;
@property(nonatomic,copy) NSArray *memberData;

@property(assign,nonatomic) BOOL isSetting;

- (void)refresh;


@end

@implementation YPFamilyChatSetVC

- (void)viewDidLoad {
    [super viewDidLoad];
    
    
    if (NSFoundationVersionNumber>=NSFoundationVersionNumber_iOS_8_0) {        self.edgesForExtendedLayout = UIRectEdgeNone;        self.extendedLayoutIncludesOpaqueBars = NO;        self.modalPresentationCapturesStatusBarAppearance = NO;        self.automaticallyAdjustsScrollViewInsets=NO;
        
        }
    
    [self requestData];
        
}

- (void)setTeam:(NIMTeam *)team
{
    _team = team;
    
    _team = [[NIMSDK sharedSDK].teamManager teamById:team.teamId];
    
    [[NIMSDK sharedSDK].teamManager addDelegate:self];
    
    NIMTeamNotifyState state = [[NIMSDK sharedSDK].teamManager notifyStateForNewMsg:team.teamId];
    if (state == NIMTeamNotifyStateAll) {
        self.msgSwitch.on = YES;
    }else{
        self.msgSwitch.on = NO;
    }
}



- (void)refresh
{
    // team 缓存可能发生改变，需要重新从 SDK 里拿一遍
    _team = [[NIMSDK sharedSDK].teamManager teamById:_team.teamId];
    NSURL *avatarUrl = [NSURL URLWithString:[self changeImageUrl:_team.thumbAvatarUrl]];
    
    [self.avatarImageView sd_setImageWithURL:avatarUrl];
    
    __weak typeof(self)weakSelf = self;
    [self.avatarImageView sd_setImageWithURL:avatarUrl completed:^(UIImage * _Nullable image, NSError * _Nullable error, SDImageCacheType cacheType, NSURL * _Nullable imageURL) {
        if (image) {
            weakSelf.bgImageView.image = [image applyBlurWithRadius:20 tintColor:[UIColor colorWithRed:0 green:0 blue:0 alpha:0.2] saturationDeltaFactor:1.8 maskImage:nil];
        }
    }];
    
    
    self.familyNameLabel.text = self.team.teamName;
    self.timeLabel.text = [self formartCreateTime];
    self.tipLabel.text = self.team.announcement;
    self.numLabel.text = [NSString stringWithFormat:@"共%ld人",self.team.memberNumber];
    
    if (self.team.notifyStateForNewMsg == NIMTeamNotifyStateAll) {
        [self.msgSwitch setOn:YES];
    }else{
        [self.msgSwitch setOn:NO];
    }
    
}

//拦截云信图片链接，去掉后缀
- (NSString *)changeImageUrl:(NSString *)urlStr
{
    NSString *tmpStr = @"";
    if (urlStr != nil) {
        NSRange range = [urlStr rangeOfString:@"thumbnail"];
        
        if (range.location>0 && range.length>0) {
             tmpStr = [urlStr substringToIndex:range.location+range.length];
        }
        return tmpStr;
    }
    return tmpStr;
}

- (NSString*)formartCreateTime{
    NSTimeInterval timestamp = self.team.createTime;
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    [dateFormatter setDateFormat:@"yyyy/MM/dd"];
    NSString *dateString = [dateFormatter stringFromDate:[NSDate dateWithTimeIntervalSince1970:timestamp]];
    if (!dateString.length) {
        return @"未知时间创建";
    }
    return [NSString stringWithFormat:@"于%@创建",dateString];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:YES animated:YES];

}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    

    
}

#pragma mark - UICollectionViewDelegateFlowLayout
- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section
{
    return self.memberData.count>5?5:self.memberData.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath
{
    return CGSizeMake(kScreenWidth/5, 89);
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section
{
    return UIEdgeInsetsMake(0, 0, 0, 0);
}

- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath
{
    YPFamilyChatUserCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPFamilyChatUserCell" forIndexPath:indexPath];
    
    NIMTeamMember *member = [self.memberData safeObjectAtIndex:indexPath.item];
//    cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[self changeImageUrl:member.hea]]
    
    
    YPNIMKitInfo *info            = [[YPNIMKit sharedKit] infoByUser:member.userId option:nil];
    cell.nameLabel.text = info.showName;
    [cell.avatarImageView sd_setImageWithURL:[NSURL URLWithString:[info.avatarUrlString cutAvatarImageSize]]];
    
    NSString *imageName = @"";
    if (member.type == 1) {
        imageName = @"yp_family_owner";
    }else if(member.type == 2){
        imageName = @"yp_family_manager";
    }
    cell.roleImageView.image = [UIImage imageNamed:imageName];
    
    return cell;
}


- (IBAction)backBtnAction:(id)sender {
    
    [self.navigationController popViewControllerAnimated:YES];
    
}

- (void)dealloc
{
    [[NIMSDK sharedSDK].teamManager removeDelegate:self];
}


- (void)requestData{
    __weak typeof(self) wself = self;
    [[NIMSDK sharedSDK].teamManager fetchTeamMembers:self.team.teamId completion:^(NSError *error, NSArray *members) {
        
//        wself.memberData = members;
        
        NSMutableArray *tmpArr = [NSMutableArray arrayWithArray:members];
        
        int times = (tmpArr.count > 5)?5:tmpArr.count;
        
        for (int i = 0; i<times; i++) {
            if (i < tmpArr.count) {
                if (i>0) {
                    NIMTeamMember *member = [tmpArr safeObjectAtIndex:i];
                    if (member.type == 1) {
                        if (i >0) {
                            [tmpArr exchangeObjectAtIndex:i withObjectAtIndex:0];
                        }
                    }
                }
            }
        }
        
        wself.memberData = tmpArr;
        
        
        
        [self.collectionView reloadData];

        [self refresh];
    }];
}

#pragma mark - NIMTeamManagerDelegate
- (void)onTeamMemberChanged:(NIMTeam *)team
{
    __weak typeof(self) weakSelf = self;
    [self requestData];
}


- (IBAction)moreMemberBtnAction:(id)sender {
    
    YPFamilyChatMemberVC *vc = [[UIStoryboard storyboardWithName:@"HJFamily" bundle:[NSBundle mainBundle]] instantiateViewControllerWithIdentifier:@"YPFamilyChatMemberVC"];
    vc.memberData = self.memberData;
    [self.navigationController pushViewController:vc animated:YES];
}
- (IBAction)moreTipTapAction:(id)sender {
    
    CGSize size = [self.tipLabel.text boundingRectWithSize:CGSizeMake(kScreenWidth - 157, 0) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:self.tipLabel.font} context:nil].size;
    
    self.familyTipView_height.constant = size.height + 30;
}

- (IBAction)changeMessageStatusAction:(id)sender {
    if (self.isSetting) {
        return;
    }
    
    //防止重复设置
    self.isSetting = YES;
    //复位 
    self.msgSwitch.on = !self.msgSwitch.isOn;
    
    [MBProgressHUD showMessage:@"设置中..."];
    
    NIMTeamNotifyState state = [[NIMSDK sharedSDK].teamManager notifyStateForNewMsg:self.team.teamId];
    NIMTeamNotifyState changeState = state==NIMTeamNotifyStateAll?NIMTeamNotifyStateNone:NIMTeamNotifyStateAll;
    
    [self changeNikTalkNotifyState:changeState];

    
//    服务器 ope 1：关闭消息提醒，2：打开消息提醒，其他值无效
//    NSInteger ope = changeState == NIMTeamNotifyStateAll?2:1;
//
//    @weakify(self);
//    [HttpRequestHelper setMsgNotifyWithFamilyId:self.familyID ope:ope Success:^(id data) {
//        @strongify(self);
//        [self changeNikTalkNotifyState:changeState];
//    } failure:^(NSNumber *code, NSString *msg) {
//        [MBProgressHUD hideHUD];
//    }];
    
    
}

//改变云信群消息状态
- (void)changeNikTalkNotifyState:(NIMTeamNotifyState)changeState
{
    @weakify(self);
    [[NIMSDK sharedSDK].teamManager updateNotifyState:changeState inTeam:self.team.teamId completion:^(NSError * _Nullable error) {
        @strongify(self);
        [MBProgressHUD hideHUD];
        self.isSetting = NO;
        if (error != nil) {
            self.msgSwitch.on = !self.msgSwitch.isOn;
        }
    }];
}



@end
