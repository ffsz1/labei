//
//  HJGameRoomVC+LongZhu.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+LongZhu.h"

#import "HJGameRoomPeiDuiSuPeiView.h"
#import "HJGameRoomPeiDuiChooseView.h"
#import "HJGameRoomPeiDuiRuleView.h"

#import "HJRoomLongZhuMsgModel.h"
#import "NSObject+AutoCoding.h"
#import "HJImMessageCore.h"
#import "HJLongZhuCore.h"
#import "HJLongZhuCoreClient.h"
#import "HJVersionCoreHelp.h"
#import "HJUserCoreHelp.h"

#import "HJRoomLongZhuMsgModel.h"

@implementation HJGameRoomVC (LongZhu)

- (void)updateLongZhu {
    
    if (GetCore(HJImRoomCoreV2).currentRoomInfo.roomId != 0) {
        
        if ([GetCore(HJAuthCoreHelp) getUid].integerValue == self.roomInfo.uid) {
            [GetCore(HJLongZhuCore) getStateWithRoomId:GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
        }
        else {
            
            if (![GetCore(HJRoomQueueCoreV2Help) isOnMicro:[GetCore(HJAuthCoreHelp)getUid].userIDValue]) {
                //下麦了
                if (!self.longZhuView.hidden) {
                    [self showLongZhuViewWihtHinden:YES];
                }
            }
            else {
                
                [GetCore(HJLongZhuCore) getStateWithRoomId:GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
                
            }
        }
    }
}

- (void)setupLongZhuView {
    
    GetCore(HJLongZhuCore);
    
    HJGameRoomPeiDuiEnterView *longZhuView = [[[NSBundle mainBundle] loadNibNamed:@"HJGameRoomPeiDuiEnterView" owner:self options:nil] lastObject];
    
    longZhuView.hidden = YES;
    
    @weakify(self);
    [longZhuView setPeiduiBtnActionBlock:^(BOOL isOpen) {
        @strongify(self);
        if (self.longZhuViewStatus == 1) {
            [self showSupeiView];
        }
        else if (self.longZhuViewStatus == 2) {
            [self showChooseView];
        }
        else if (self.longZhuViewStatus == 0) {
            
            self.isLongZhuViewOpen = isOpen;
            [self setupLongViewConstraint];
        }
        else {
            [self showLongZhuViewWihtHinden:YES];
        }
    }];
    
    // 速配
    [longZhuView setSupeiBtnActionBlock:^{
        @strongify(self);
        [self showSupeiView];
    }];
    
    [longZhuView setChooseBtnActionBlock:^{
        @strongify(self);
        [self showChooseView];
    }];
    
    [self.view addSubview:longZhuView];
    
    self.longZhuView = longZhuView;
    
}

- (void)showLongZhuViewWihtHinden:(BOOL)hinden {
    
//        self.longZhuView.hidden = YES;
//        self.activityView.hidden = YES;
    
        self.longZhuView.hidden = hinden;
        if (iPhone5s || iPhone4s) {
            self.activityView.hidden = !hinden;
        }
        else {
            self.activityView.hidden = NO;
        }
    
}

- (void)setupLongViewConstraint {
    
    @weakify(self);
    if (self.isLongZhuViewOpen) {
        [self.longZhuView mas_updateConstraints:^(MASConstraintMaker *make) {
            @strongify(self);
            make.width.mas_equalTo(150);
            make.height.mas_equalTo(60);
            make.top.equalTo(self.messageTableView);
            make.trailing.mas_equalTo(0);
        }];
    }
    else {
        [self.longZhuView mas_updateConstraints:^(MASConstraintMaker *make) {
            @strongify(self);
            make.width.mas_equalTo(60);
            make.height.mas_equalTo(60);
            make.top.equalTo(self.messageTableView);
            make.trailing.mas_equalTo(0);
        }];
    }
}

- (void)showSupeiView {
    HJGameRoomPeiDuiSuPeiView *view = [[NSBundle mainBundle] loadNibNamed:@"HJGameRoomPeiDuiSuPeiView" owner:nil options:nil][0];
    
    view.selectedNum = self.didSeletedNum;
//    view.backgroundColor = [UIColor redColor];
    @weakify(self);
    @weakify(view);
    [view setRuleBtnActionBlock:^{
        @strongify(view);
        HJGameRoomPeiDuiRuleView *ruleView = [[NSBundle mainBundle] loadNibNamed:@"HJGameRoomPeiDuiRuleView" owner:nil options:nil][0];
        ruleView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
        [view addSubview:ruleView];
    }];
    
    [view setSupeiBtnActionBlock:^{
        @strongify(self);
        [MBProgressHUD showMessage:@""];
        [GetCore(HJLongZhuCore) getChooseResultWithRoomId:self.roomInfo.roomId type:1 result:nil];
    }];
    
    [view setCancelBtnActionBlock:^{
        @strongify(self);
        [MBProgressHUD showMessage:@""];
        [GetCore(HJLongZhuCore) cancelChooseResultWithRoomId:self.roomInfo.roomId type:1 result:[@(self.didSeletedNum) description]];
        
    }];
    
    [view setShowBtnActionBlock:^{
        @strongify(self);
        [MBProgressHUD showMessage:@""];
        [GetCore(HJLongZhuCore) confirmResult:[@(self.didSeletedNum) description] roomId:self.roomInfo.roomId type:1];
    }];
    
    view.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);

    [HJAlertControllerCenter defaultCenter].alertViewOriginY = 0;
    [[HJAlertControllerCenter defaultCenter]presentAlertWith:self view:view preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
    
    [view addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        @strongify(view)
        if (!view.isAnimation) {
            
            [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
        }
    }]];

}

- (void)showChooseView {
    
    HJGameRoomPeiDuiChooseView *view = [[NSBundle mainBundle] loadNibNamed:@"HJGameRoomPeiDuiChooseView" owner:nil options:nil][0];
    
    view.selectedNum = self.didSeletedNum;
    
    @weakify(view);
    @weakify(self);
    [view setRuleBtnActionBlock:^{
        @strongify(view);
        HJGameRoomPeiDuiRuleView *ruleView = [[NSBundle mainBundle] loadNibNamed:@"HJGameRoomPeiDuiRuleView" owner:nil options:nil][0];
        ruleView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
        [view addSubview:ruleView];
    }];
    
    [view setChooseBtnActionBlock:^{
        @strongify(view);
        @strongify(self);
        self.didSeletedNum = view.selectedNum;
        
        [MBProgressHUD showMessage:@""];
        [GetCore(HJLongZhuCore) getChooseResultWithRoomId:GetCore(HJImRoomCoreV2).currentRoomInfo.roomId type:2 result:[@(self.didSeletedNum) description]];
    }];
    
    [view setShowBtnActionBlock:^{
        @strongify(view);
        @strongify(self);
        [MBProgressHUD showMessage:@""];
        [GetCore(HJLongZhuCore) confirmResult:[@(self.didSeletedNum) description] roomId:GetCore(HJImRoomCoreV2).currentRoomInfo.roomId type:2];
    }];
    
    view.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [HJAlertControllerCenter defaultCenter].alertViewOriginY = 0;
    [[HJAlertControllerCenter defaultCenter]presentAlertWith:self view:view preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
    
    [view addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    }]];
}

- (void)sendMsgWithNumArr:(NSArray *)numArr isShowed:(BOOL)isShowed {
    
    ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;;
    HJRoomLongZhuMsgModel *model = [HJRoomLongZhuMsgModel new];
    model.uid = [GetCore(HJAuthCoreHelp) getUid];
    model.nick = myMember.nick.length ? myMember.nick : @" ";
    model.isShowd = isShowed;
    model.numArr = numArr == nil ? @[] : numArr;
    model.level = [GetCore(HJUserCoreHelp) getUserInfoInDB:[GetCore(HJAuthCoreHelp) getUid].integerValue].experLevel;
    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_LongZhu;
    attachement.data = [model dictionaryRepresentation];
    attachement.second = numArr.count == 3 ? Custom_Noti_Sub_LongZhu_Supei : Custom_Noti_Sub_LongZhu_Choose;

    
    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:JXIMSessionTypeChatroom];
}

#pragma mark - LongZhuCoreClient
// 获取速配状态
- (void)getStateSuccessWithResult:(NSDictionary *)result {
    [MBProgressHUD hideHUD];
    
    if ([GetCore(HJAuthCoreHelp) getUid].integerValue == self.roomInfo.uid) {
        [self setupLongZhuVIewWithResult:result];
    }
    else {
        
        if ([GetCore(HJRoomQueueCoreV2Help) isOnMicro:[GetCore(HJAuthCoreHelp)getUid].userIDValue]) {
            // 在麦位
            [self setupLongZhuVIewWithResult:result];
        }
        else {
            if (!self.longZhuView.hidden) {
                [self showLongZhuViewWihtHinden:YES];
            }
        }
    }
}

- (void)setupLongZhuVIewWithResult:(NSDictionary *)result {
    
    NSInteger status = [result[@"status"] integerValue];
    
    self.longZhuViewStatus = status;
    
    if (status == -1) {
        // 没有权限
        if (!self.longZhuView.hidden) {
            [self showLongZhuViewWihtHinden:YES];
        }
    }
    else if (status == 0 || status == 1 || status == 2) {
        if (self.longZhuView.hidden) {
            [self showLongZhuViewWihtHinden:NO];
        }
        
        if (status == 1) {
            
            NSArray *arr = [[result[@"result"] description] componentsSeparatedByString:@","];
            
            if (arr.count >= 3) {
                NSInteger num1 = [arr[0] integerValue];
                NSInteger num2 = [arr[1] integerValue];
                NSInteger num3 = [arr[2] integerValue];
                self.didSeletedNum = num1 * 100 + num2 * 10 + num3;
            }
            else {
                self.didSeletedNum = 0;
            }
        }
        else if (status == 2) {
            self.didSeletedNum = [result[@"result"] integerValue];
        }
        else {
            self.didSeletedNum = 0;
        }
    }
    else {
        // 异常
        if (!self.longZhuView.hidden) {
            [self showLongZhuViewWihtHinden:YES];
        }
    }
}

- (void)getStateFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

// 获取速配随机数/保存自己选择的数
- (void)getChooseResultSuccessWithResult:(NSInteger)result type:(NSInteger)type {
    
    [self.longZhuView close];
    self.longZhuView.didChoose = YES;
    self.longZhuViewStatus = type;
    
    if (type == 1) {
        [MBProgressHUD hideHUD];
        self.didSeletedNum = result;
        NSInteger num1 = result / 100;
        NSInteger num2 = (result - num1 * 100) / 10;
        NSInteger num3 = result - num1 * 100 - num2 * 10;
        
        NSMutableArray *numArr = [NSMutableArray arrayWithObjects:@(num1),@(num2),@(num3), nil];
//         [numArr sortUsingSelector:@selector(compare:)];
        
        @weakify(self);
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.8f * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            @strongify(self);
            [self sendMsgWithNumArr:numArr isShowed:NO];
        });
    }
    else if (type == 2) {
        [MBProgressHUD hideHUD];
        [self sendMsgWithNumArr:@[@(self.didSeletedNum)] isShowed:NO];
    }
}
- (void)getChooseResultFailedWithMessage:(NSString *)message type:(NSInteger)type {
    
    [MBProgressHUD hideHUD];
    self.didSeletedNum = 0;
}

//放弃解签
- (void)cancelChooseResultSuccessWithResult:(NSInteger)result type:(NSInteger)type
{
    
    self.longZhuView.didChoose = NO;
    self.longZhuViewStatus = 0;
    
    [MBProgressHUD hideHUD];
    self.didSeletedNum = 0;
    [self sendCancelMsg];
    [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
}

- (void)cancelChooseResultFailedWithMessage:(NSString *)message type:(NSInteger)type
{
    [MBProgressHUD hideHUD];
    [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
}

- (void)sendCancelMsg
{
    ChatRoomMember *myMember = GetCore(HJImRoomCoreV2).myMember;
    HJRoomLongZhuMsgModel *model = [HJRoomLongZhuMsgModel new];
    model.uid = [GetCore(HJAuthCoreHelp) getUid];
    model.nick = myMember.nick.length ? myMember.nick : @" ";
    model.isShowd = NO;
    model.numArr = @[];
    model.level = [GetCore(HJUserCoreHelp) getUserInfoInDB:[GetCore(HJAuthCoreHelp) getUid].integerValue].experLevel;

    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_LongZhu;
    attachement.data = [model dictionaryRepresentation];
    attachement.second = Custom_Noti_Sub_LongZhu_cancel;
    attachement.experLevel = [GetCore(HJUserCoreHelp) getUserInfoInDB:[GetCore(HJAuthCoreHelp) getUid].integerValue].experLevel;
    
    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%ld",(long)GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:NIMSessionTypeChatroom];
}

// 展示结果
- (void)confirmResultSuccessWithType:(NSInteger)type {
    
    self.longZhuView.didChoose = NO;
    self.longZhuViewStatus = 0;
    
    if (type == 1) {
        [MBProgressHUD hideHUD];
        
        NSInteger num1 = self.didSeletedNum / 100;
        NSInteger num2 = (self.didSeletedNum - num1 * 100) / 10;
        NSInteger num3 = self.didSeletedNum - num1 * 100 - num2 * 10;
        
        NSMutableArray *numArr = [NSMutableArray arrayWithObjects:@(num1),@(num2),@(num3), nil];
//        [numArr sortUsingSelector:@selector(compare:)];
        
        [self sendMsgWithNumArr:numArr isShowed:YES];
        self.didSeletedNum = 0;
        
        [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    }
    else if (type == 2) {
        [MBProgressHUD hideHUD];
        [self sendMsgWithNumArr:@[@(self.didSeletedNum)] isShowed:YES];
        self.didSeletedNum = 0;
        
        [[HJAlertControllerCenter defaultCenter] dismissAlertNeedBlock:NO];
    }
}
- (void)confirmResultFailedWithMessage:(NSString *)message type:(NSInteger)type {
    [MBProgressHUD hideHUD];
}


@end
