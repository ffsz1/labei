//
//  YPRoomSettingVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomSettingVC.h"
#import "YPManagerSettingController.h"
#import "YPRoomBackgroundSettingVC.h"
#import "YPRoomViewControllerFactory.h"

//core
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPRoomQueueCoreV2Help.h"
#import "YPImRoomCoreV2.h"
#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"

#import "YPHomeCore.h"
#import "HJHomeCoreClient.h"

#import "UIView+XCToast.h"
#import "YPYYTheme.h"
#import <NIMSDK/NIMSDK.h>

#import "YPRoomTagCell.h"
#import "YPHomeTag.h"
#import "YPRoomSettingTopicVC.h"
#import "YPRoomQueueCustomAttachment.h"
#import "YPImMessageCore.h"

@interface YPRoomSettingVC ()
<
    HJRoomCoreClient,
    HJHomeCoreClient,
    UICollectionViewDelegate,
    UICollectionViewDataSource,
    UICollectionViewDelegateFlowLayout,
    UITextFieldDelegate
>

@property (strong, nonatomic) YPChatRoomInfo *roomInfo;
@property (strong, nonatomic) UIButton *selectedButton;
@property (weak, nonatomic) IBOutlet UITextField *roomnameTextField;
@property (weak, nonatomic) IBOutlet UITextField *roomPasswordTextField;
@property (weak, nonatomic) IBOutlet UICollectionView *tagCollectionView;
@property (weak, nonatomic) IBOutlet UITextField *pwdTextField;

@property (strong, nonatomic) NSMutableArray *tagList;
@property (weak, nonatomic) IBOutlet UISwitch *springGiftSwith;
@property (weak, nonatomic) IBOutlet UISwitch *giftEffectSwitch;
@property (weak, nonatomic) IBOutlet UISwitch *carEffectSwitch;

@property (nonatomic, assign) BOOL isChangeiftEffect;
@property (nonatomic, assign) BOOL isChangeiftCar;

@property (nonatomic, assign) BOOL isCanSendMsg;
@property (nonatomic, assign) BOOL isCanChaneRoomName;//是否改变了房间名字
@property (nonatomic, assign) BOOL isCanChaneRoomLock;//是否改变房间密码
@property (nonatomic, assign) BOOL isCanChaneRoomNoLock;//是否解锁房间
@property (nonatomic, assign) NSInteger selectedBackgroundImageId;

@end

@implementation YPRoomSettingVC
{
    int selectedChildenId;
}

- (NSMutableDictionary *)dic {
    if (!_dic) {
        _dic = [NSMutableDictionary dictionary];
    }
    return _dic;
}

#pragma mark - life cycle
- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = @"房间设置";
    [self addCore];
    [self initView];
    [self updateView];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)initView {
    self.tableView.tableFooterView = [[UIView alloc]initWithFrame:CGRectZero];
    UIBarButtonItem *rightBarButtonItem = [[UIBarButtonItem alloc]initWithTitle:NSLocalizedString(XCRoomSave, nil) style:UIBarButtonItemStylePlain target:self action:@selector(save)];
    [rightBarButtonItem setTintColor:UIColorHex(9F62FB)];
    self.navigationItem.rightBarButtonItem = rightBarButtonItem;
    self.tableView.backgroundColor = [[YPYYTheme defaultTheme]colorWithHexString:@"#F5F5F5" alpha:1];
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}
#pragma mark - UITextFieldDelegate

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if (textField == self.pwdTextField) {
        NSString *regex =@"[0-9]*";
        NSPredicate *pred = [NSPredicate predicateWithFormat:@"SELF MATCHES %@",regex];
        if ([pred evaluateWithObject:string]) {
            return YES;
        }
        return NO;

    }
    return YES;
    
}

#pragma mark - tableViewDelegate
-(CGFloat)tableView:(UITableView *)tableView heightForFooterInSection:(NSInteger)section
{
    return 0.01;
}
-(CGFloat)tableView:(UITableView *)tableView heightForHeaderInSection:(NSInteger)section
{
    return 0.01;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    if (indexPath.section == 0) {
        return 51;
    }else if (indexPath.section == 2) {
        NSInteger eachLineCount = (([UIScreen mainScreen].bounds.size.width - 30) - 5) / (60 + 5);
        NSInteger lineCount = self.tagList.count % eachLineCount > 0 ? self.tagList.count / eachLineCount + 1: self.tagList.count / eachLineCount;
        return lineCount * 34 + (lineCount - 1) * 10 + 44 + 8;
    }else {
        YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;
        if (myMember.is_manager) {
            if (indexPath.row == 3) {
                return CGFLOAT_MIN;
            }
            return 50;
        }
        return 50;
    }
    return CGFLOAT_MIN;
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView {
    return 3;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:NO];
    if (indexPath.section == 1) {
        if (indexPath.row == 6) {
            //话题
            YPRoomSettingTopicVC *vc = (YPRoomSettingTopicVC *)[[YPRoomViewControllerFactory sharedFactory] instantiateGameHJRoomSettingTopicVC];
            [self.navigationController pushViewController:vc animated:YES];
        }
        else if (indexPath.row == 5) {
            //进房提示
            UIViewController *vc = [[YPRoomViewControllerFactory sharedFactory] instantiateHJRoomSettingPlayInfoVC];
            [self.navigationController pushViewController:vc animated:YES];
        }
        else if (indexPath.row == 0) {
            //背景            
            YPRoomBackgroundSettingVC *viewController = [YPRoomBackgroundSettingVC new];
            @weakify(self);
            viewController.itemId = self.selectedBackgroundImageId;
            viewController.didSelectedItemHandler = ^(NSInteger itemId) {
                @strongify(self);
                self.selectedBackgroundImageId = itemId;
            };
            [self.navigationController pushViewController:viewController animated:YES];
            
//            if (GetCore(ImRoomCoreV2).currentRoomInfo.isPermitRoom == 3 || GetCore(ImRoomCoreV2).currentRoomInfo.isPermitRoom == 1) {
//            } else {
//                [MBProgressHUD showSuccess:@"只有牌照房才能换背景哦~"];
//            }
        } else if (indexPath.row == 3) {
            //管理员
//            YPManagerSettingController *vc = (YPManagerSettingController *)[[YPRoomViewControllerFactory sharedFactory]instantiateManagerSettingViewController];
//            vc.type = 2;
//            [self.navigationController pushViewController:vc animated:YES];
            
            UIViewController *vc = YPRoomStoryBoard(@"YPRoomManagerListVC");
            [self.navigationController pushViewController:vc animated:YES];
            
        } else if (indexPath.row == 4) {
            //黑名单
            
            UIViewController *vc = YPRoomStoryBoard(@"YPRoomBlackListVC");
            [self.navigationController pushViewController:vc animated:YES];
            
//            YPManagerSettingController *vc = (YPManagerSettingController *)[[YPRoomViewControllerFactory sharedFactory]instantiateManagerSettingViewController];
//            vc.type = 1;
//            [self.navigationController pushViewController:vc animated:YES];
        }
    }
}

#pragma mark - UICollectionViewDelegate
- (NSInteger)numberOfSectionsInCollectionView:(UICollectionView *)collectionView {
    return 1;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.tagList.count;
}

- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    YPHomeTag *tag = [self.tagList safeObjectAtIndex:indexPath.row];
    selectedChildenId = tag.id;
    [self.tagCollectionView reloadData];
}

#pragma mark - UICollectionViewDataSource
- (UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    YPHomeTag *tag = [self.tagList safeObjectAtIndex:indexPath.row];
    YPRoomTagCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"YPRoomTagCell" forIndexPath:indexPath];
    cell.roomTagLabel.text = tag.name;
    cell.bottomView.layer.cornerRadius = 17;
    cell.bottomView.layer.masksToBounds = YES;
    
    if (tag.id == selectedChildenId) {
        cell.bottomView.backgroundColor = [[YPYYTheme defaultTheme] colorWithHexString:@"#9F62FB" alpha:1.0];
        cell.bottomView.layer.borderWidth = 0;
        cell.roomTagLabel.textColor = UIColorHex(ffffff);
    }else {
        cell.roomTagLabel.textColor = UIColorHex(FFFFFF);
        cell.bottomView.backgroundColor = UIColorHex(E5E5E5);
        cell.bottomView.layer.borderWidth = 0.5;
        cell.bottomView.layer.borderColor = UIColorHex(E5E5E5).CGColor;
    }
    return cell;
}

#pragma mark - UICollectionViewDelegateFlowLayout
- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {
    return CGSizeMake(60, 34);
}

- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return 10.0;
}



#pragma mark - RoomCoreClient
- (void)onGameRoomInfoUpdateSuccess:(YPChatRoomInfo *)info isFromMessage:(BOOL)isFromMessage {
    
    if (self.isCanSendMsg && (self.isChangeiftEffect || self.isChangeiftCar)) {
        
        YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
        
        YPRoomQueueCustomAttachment *roomQueueAttachment = [[YPRoomQueueCustomAttachment alloc]init];
        roomQueueAttachment.uid = [myMember.account longLongValue];
        
        if (self.isChangeiftEffect) {
            YPAttachment *attachement = [[YPAttachment alloc]init];
            attachement.first = Custom_Noti_Header_Queue;
            if (info.giftEffectSwitch) {
                attachement.second = Custom_Noti_Sub_Gift_Effect_Open;
                
            }
            else {
                attachement.second = Custom_Noti_Sub_Gift_Effect_Close;
              
            }
            attachement.data = roomQueueAttachment.encodeAttachment;
            
            [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
        }
        
        if (self.isChangeiftCar) {
            YPAttachment *attachement = [[YPAttachment alloc]init];
            attachement.first = Custom_Noti_Header_Queue;
            if (info.giftCardSwitch) {
                 attachement.second = Custom_Noti_Sub_Car_Effect_Close;
//                attachement.second = Custom_Noti_Sub_Car_Effect_Open;
                
            }
            else {
                attachement.second = Custom_Noti_Sub_Car_Effect_Open;
               
            }
            attachement.data = roomQueueAttachment.encodeAttachment;
            
            [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
        }
        
        self.isCanSendMsg = NO;
    }
    
    
    if (self.isCanSendMsg && self.isCanChaneRoomName){
        [self sendChargeRoomNameMessageForChangeInfo:info.title];
        
        self.isCanSendMsg = NO;
        self.isCanChaneRoomName = NO;
    }
    if (self.isCanSendMsg && self.isCanChaneRoomLock){//
        [self sendRoomLockNameMessageForLock:self.isCanChaneRoomNoLock];
           
           self.isCanSendMsg = NO;
           self.isCanChaneRoomLock = NO;
       }
    
    
    
    
    
    
    [MBProgressHUD hideHUD];
    [UIView showToastInKeyWindow:NSLocalizedString(XCEditChangeSuccess, nil) duration:2 position:YYToastPositionCenter];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)onGameRoomInfoUpdateFailth:(NSString *)message {
    [MBProgressHUD hideHUD];
}
//发送公屏消息for房间更新名字
- (void)sendChargeRoomNameMessageForChangeInfo:(NSString *)roomName{
    
    
    YPAttachment *attachement = [[YPAttachment alloc]init];
    attachement.first = Custom_Noti_Header_ChargeRoomName;
    attachement.second = Custom_Noti_Header_ChargeRoomName;

   
    NSMutableDictionary *buffer = [NSMutableDictionary dictionary];
    [buffer setObject:roomName forKey:@"roomName"];
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];

    [buffer setObject:userId forKey:@"uid"];
     [buffer setObject:[NSString stringWithFormat:@"%lld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] forKey:@"roomId"];
    NSDictionary *attMessageDic = @{@"params": buffer};
    attachement.data = attMessageDic;
    
    [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%lld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:(JXIMSessionType) NIMSessionTypeChatroom];
}


- (void)sendRoomLockNameMessageForLock:(BOOL)lock
{
    YPChatRoomMember *myMember = GetCore(YPImRoomCoreV2).myMember;;
    
    YPRoomQueueCustomAttachment *roomQueueAttachment = [[YPRoomQueueCustomAttachment alloc]init];
    roomQueueAttachment.uid = [myMember.account longLongValue];
    
    YPAttachment *attachement = [[YPAttachment alloc]init];
    attachement.first = Custom_Noti_Header_Queue;
    if (lock) {

         attachement.second = Custom_Noti_Header_CHANGE_ROOM_NO_LOCK;
    }
    else {
        attachement.second = Custom_Noti_Header_CHANGE_ROOM_LOCK;
        
    }
    attachement.data = roomQueueAttachment.encodeAttachmentForRoomLock;
    
    [GetCore(YPImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(YPImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
}












#pragma mark - HomeCoreClient
- (void)requestRoomAllTagListSuccess:(NSArray *)list {
    self.tagList = [list mutableCopy];
    [self.tagCollectionView reloadData];
    [self.tableView reloadData];
    [MBProgressHUD hideHUD];
}

- (void)requestRoomAllTagListFailth:(NSString *)msg {
//    [MBProgressHUD showError:msg];
    [MBProgressHUD hideHUD];
}

#pragma mark - Event Response
- (IBAction)tagButtonClick:(UIButton *)sender {
    if (sender != self.selectedButton) {
        [self.selectedButton setSelected:NO];
        self.selectedButton = sender;
        [sender setSelected:YES];
    }
}


#pragma mark - private method
- (void)addCore{
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJHomeCoreClient, self);
}

- (void)updateView {
    [MBProgressHUD showMessage:@"请稍后"];
    self.roomInfo = GetCore(YPImRoomCoreV2).currentRoomInfo;
    if (self.roomInfo != nil) {
        [self initData];
    }
}

- (void)initData {
    [GetCore(YPHomeCore)requestRoomAllTag];
    self.roomnameTextField.text = self.roomInfo.title;
    self.roomPasswordTextField.text = self.roomInfo.roomPwd;
    
    if (self.roomInfo.canSpringGift) {
        [self.springGiftSwith setOn:YES];
    }else {
        [self.springGiftSwith setOn:NO];
    }
    
    if (!self.roomInfo.giftEffectSwitch) {
        [self.giftEffectSwitch setOn:YES];
    }
    else {
        [self.giftEffectSwitch setOn:NO];
    }
    
    if (!self.roomInfo.giftCardSwitch) {
        [self.carEffectSwitch setOn:YES];
    }
    else {
        [self.carEffectSwitch setOn:NO];
    }
    
    if (self.roomInfo.tagId > 0) {
        selectedChildenId = self.roomInfo.tagId;
    }else {
        selectedChildenId = self.roomInfo.tagId;
    }
    
    self.selectedBackgroundImageId = self.roomInfo.backPic.integerValue;
}


- (void)save {
    
    
    if (self.roomnameTextField.text.length > 19) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomTheNameCantLeaf15, nil) duration:2 position:YYToastPositionCenter];
        return;
    }
    if (self.roomnameTextField.text.length == 0) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomNameCantBeEmpty, nil) duration:2 position:YYToastPositionCenter];
        return;
    }
    
    if (selectedChildenId == 0) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomChooseLabel, nil) duration:2 position:YYToastPositionCenter];
        return;
    }
    
    if (self.roomPasswordTextField.text.length > 8) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomPswCantOut8, nil) duration:2 position:YYToastPositionCenter];
        return;
    }
    
    if (![self.roomPasswordTextField.text isEqualToString:self.roomInfo.roomPwd] || ![self.roomnameTextField.text isEqualToString:self.roomInfo.title] || ![self.selectedButton.titleLabel.text isEqualToString:self.roomInfo.roomTag]) {
        
        self.isCanSendMsg = YES;
        if (self.roomInfo.giftEffectSwitch == self.giftEffectSwitch.isOn) {
            self.isChangeiftEffect = YES;
        }
        else {
            self.isChangeiftEffect = NO;
        }
        
        if (self.roomInfo.giftEffectSwitch == self.giftEffectSwitch.isOn) {
            self.isChangeiftEffect = YES;
        }
        else {
            self.isChangeiftEffect = NO;
        }
        
        
        if (self.roomInfo.giftCardSwitch == self.carEffectSwitch.isOn) {
            self.isChangeiftCar = YES;
        }
        else {
            self.isChangeiftCar = NO;
        }
        
        if ([self.roomnameTextField.text isEqualToString:self.roomInfo.title]) {
            self.isCanChaneRoomName = NO;
        }else{
            self.isCanChaneRoomName = YES;
        }
        
        if ([self.roomPasswordTextField.text isEqualToString:self.roomInfo.roomPwd]) {//是否改变密码
            self.isCanChaneRoomLock = NO;
        }else{
            self.isCanChaneRoomLock = YES;
            if (self.roomPasswordTextField.text.length <1) {
                self.isCanChaneRoomNoLock = YES;//解锁
            }else{
                 self.isCanChaneRoomNoLock = NO;
            }
        }
        
        
        
        
        
        
        
        
        NSString *valueStr = [NSString stringWithFormat:@"%ld", self.selectedBackgroundImageId];
        [MBProgressHUD showMessage:@"请稍后"];
        if (GetCore(YPImRoomCoreV2).myMember.is_creator) {
            [GetCore(YPRoomCoreV2Help) updateGameRoomInfo:self.roomInfo.uid backPic:valueStr title:self.roomnameTextField.text  roomTopic:nil roomNotice:nil roomPassword:self.roomPasswordTextField.text tag:selectedChildenId playInfo:GetCore(YPImRoomCoreV2).currentRoomInfo.playInfo giftEffectSwitch:self.giftEffectSwitch.isOn giftCardSwitch:self.carEffectSwitch.isOn publicChatSwitch:GetCore(YPImRoomCoreV2).currentRoomInfo.publicChatSwitch];
        }else if (GetCore(YPImRoomCoreV2).myMember.is_manager) {
            [GetCore(YPRoomCoreV2Help) managerUpdateGameRoomInfo:[GetCore(YPAuthCoreHelp)getUid].userIDValue backPic:valueStr  title:self.roomnameTextField.text roomTopic:nil roomNotice:nil roomPassword:self.roomPasswordTextField.text tag:selectedChildenId playInfo:GetCore(YPImRoomCoreV2).currentRoomInfo.playInfo giftEffectSwitch:self.giftEffectSwitch.isOn giftCardSwitch:self.carEffectSwitch.isOn publicChatSwitch:GetCore(YPImRoomCoreV2).currentRoomInfo.publicChatSwitch];
        }
    } else {
        [MBProgressHUD showError:NSLocalizedString(XCRoomNotChange, nil)];
    }
}
@end
