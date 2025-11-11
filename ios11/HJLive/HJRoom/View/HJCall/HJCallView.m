//
//  HJCallView.m
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJCallView.h"
#import "HJRoomQueueCoreV2Help.h"
#import "HJImRoomCoreV2.h"
#import "HJPickupConchView.h"
#import "GiftInfo.h"
#import "HJDaCallModel.h"
#import "HJRoomViewControllerCenter.h"
#import "HJPurseViewControllerFactory.h"
#import "PurseCore.h"
#import "HJPlayRulesView.h"
#import "HJPurseCoreClient.h"
//关闭捡海螺的回调代理要导入
#import "HJRoomlotteryView.h"

@interface HJCallView()

@end

@implementation HJCallView
- (void)awakeFromNib
{
    [super awakeFromNib];
    
      AddCoreClient(HJPurseCoreClient, self);
    [self setBgRaduis];

    self.micArr = [NSMutableArray array];
    self.giftList = [NSMutableArray array];
    _btnSet = [NSMutableSet set];
       self.ownAvetarView.image = [UIImage imageNamed:@"hj_room_call_touxiang_default"];
    //设置刷新数据
    [self setDataAndUpdate];
   
     self.jinbiNumLabel.text =  GetCore(PurseCore).balanceInfo.goldNum;
    self.numRemainLabel.text =  [NSString stringWithFormat:@"剩余次数：%@",GetCore(PurseCore).balanceInfo.conchNum];//GetCore(PurseCore).balanceInfo.conchNum;
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(notificationCallModelAction:) name:@"NotificationCallModel" object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(notificationUpdateConchNumAction:) name:@"NotificationUpdateConchNum" object:nil];
    
    //收到捡海螺view关闭
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(notificationCloseGameAction) name:@"NotificationCloseGame" object:nil];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.jinbiNumLabel.text =  GetCore(PurseCore).balanceInfo.goldNum;
        self.numRemainLabel.text =[NSString stringWithFormat:@"剩余次数：%@",GetCore(PurseCore).balanceInfo.conchNum];  //GetCore(PurseCore).balanceInfo.conchNum;
  
          [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(closeForPlayCallAction) name:@"closeForPlayCall" object:nil];
    });
}


//设置刷新数据
-(void)setDataAndUpdate{
    //获取麦上数据
      self.micArr = [self getArr];
      //隐藏用户按钮
      [self isHiddenBtn];
      //设置数据
      [self setDataModel];
    //点击默认按钮
    [self setClickBtn];
}


//MARK:- getData
-(void)notificationUpdateConchNumAction:(NSNotification*)noti {
    if (noti) {
        if (noti.object) {
            self.numRemainLabel.text = [NSString stringWithFormat:@"剩余次数：%@",(NSString*)noti.object];//(NSString*)noti.object;
        }
        
    }
}


- (void)onBalanceInfoUpdate:(BalanceInfo *)balanceInfo
{
    self.jinbiNumLabel.text = balanceInfo.goldNum;
    GetCore(PurseCore).balanceInfo.conchNum = balanceInfo.conchNum;
    self.numRemainLabel.text =  [NSString stringWithFormat:@"剩余次数：%@",balanceInfo.conchNum];//balanceInfo.conchNum;
    
 
}
-(void)notificationCallModelAction:(NSNotification*)noti {
    if (noti) {
        if (noti.object) {
            HJDaCallModel* model = (HJDaCallModel*)noti.object;
            //打call剩余次数
            self.numRemainLabel.text = [NSString stringWithFormat:@"剩余次数：%ld",model.conchNum];
            //更新开心数量
            NSString* price = self.jinbiNumLabel.text;
            NSInteger gold = [price integerValue];
            if (model.goldPrice==20) {
                gold -= 20;
            }else if(model.goldPrice==200){
                 gold -= 200;
            }else if (model.goldPrice==1000){
                gold -= 1000;
            }
             self.jinbiNumLabel.text = [NSString stringWithFormat:@"%ld",gold];
        }
    }
  
}

- (void)notificationCloseGameAction{
    self.jianhailuoBtn.hidden = NO;
    self.numRemainLabel.hidden = NO;
    
}

-(void)closeForPlayCallAction{
    [self close];
}

-(void)setClickBtn{
//    UserInfo *ownerInfo = GetCore(HJImRoomCoreV2).roomOwnerInfo;//房主信息
//          UserInfo *myInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:GetCore(HJAuthCoreHelp).getUid.userIDValue];//个人信息
       //判断是否是自己的房间
//       if (myInfo.uid != ownerInfo.uid) {
//              self.intoModel.type = 1;//赠送礼物类型：1，赠送给房主；2，私聊发礼物；3，房间内赠送给个人
//              [self ownBtnAction:nil];
//          }else{
//              self.intoModel.type = 3;
//              if (self.micArr.count>0){
//                  [self mai1Action:nil];
//              }
//          }
    self.intoModel.type = 1;//赠送礼物类型：1，赠送给房主；2，私聊发礼物；3，房间内赠送给个人
    [self ownBtnAction:nil];
}

- (NSMutableArray *)getArr
{
    NSMutableArray *temp = [GetCore(HJRoomQueueCoreV2Help) findOnMicMember];
    NSMutableArray *resultArr = [[NSMutableArray alloc] init];
    
    ChatRoomInfo *roomInfo = GetCore(HJImRoomCoreV2).currentRoomInfo;//房间信息
    UserInfo *ownerInfo = GetCore(HJImRoomCoreV2).roomOwnerInfo;//房主信息
    UserInfo *myInfo = [GetCore(HJUserCoreHelp) getUserInfoInDB:GetCore(HJAuthCoreHelp).getUid.userIDValue];//个人信息
  
    [self.ownAvatarImgView qn_setImageImageWithUrl:ownerInfo.avatar placeholderImage:nil type:ImageTypeUserIcon];
    self.intoModel.uid = myInfo.uid;
    self.intoModel.roomUid = roomInfo.uid;
    self.intoModel.sendName = myInfo.nick;
    self.intoModel.targetName = ownerInfo.nick;
     self.intoModel.targetUid = [NSString stringWithFormat:@"%lld",ownerInfo.uid];
    //筛选掉自己的麦位
    BOOL hadOwner = NO;
    for (int i = 0; i<temp.count; i++) {
        HJIMQueueItem *model = temp[i];
        if (model.queueInfo.chatRoomMember.account.userIDValue != myInfo.uid) {
            
            if (model.queueInfo.chatRoomMember.account.userIDValue == ownerInfo.uid) {
                hadOwner = YES;
            }
            
            [resultArr addObject:model];
        }
    }
    

//       [self.mai1btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
//            [self.mai2Btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
//           [self.mai3Btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
//           [self.mai4Btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
//           [self.mai5Btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
//           [self.mai6Btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
//           [self.mai7Btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
//           [self.mai8Btn setImage:[self getImageFromURL:ownerInfo.avatar] forState:UIControlStateNormal];
    
    return resultArr;
}




+ (void)showCall:(TapCallTypeBlock)tapCallTypeBlock list:(nonnull NSMutableArray *)list
{
    
    HJCallView *shareView = [[NSBundle mainBundle]loadNibNamed:@"HJCallView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    CGFloat height = UIApplication.sharedApplication.statusBarFrame.size.height;
    if (height >= 44.0) {
          // 是机型iPhoneX/iPhoneXR/iPhoneXS/iPhoneXSMax
        shareView.mangerViewHeight.constant = 280;
        shareView.bottom_item_layout.constant = 110;
        shareView.bottom_lowest_layout.constant = 40;
        shareView.top_hailuoBtn_layout.constant = 0;//原20
        shareView.top_remainLab_layout.constant = 60;//原90
    }else{
        shareView.mangerViewHeight.constant = 252;
        shareView.bottom_item_layout.constant = 90;
        shareView.bottom_lowest_layout.constant = 20;
        shareView.top_hailuoBtn_layout.constant = 40;//原20
        shareView.top_remainLab_layout.constant = 100;//原90
    }
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    [list reverse];
    shareView.giftList = list;
    if (list.count>0) {
        if (list.count >2) {
          GiftInfo* giftModel =  list[0];
            GiftInfo* giftModel2 =  list[1];
            GiftInfo* giftModel3 =  list[2];
            
            [shareView.smallValueImageView sd_setImageWithURL:[NSURL URLWithString:giftModel.giftUrl]];
            [shareView.middelValueImageView sd_setImageWithURL:[NSURL URLWithString:giftModel2.giftUrl]];
            [shareView.bigValueImageView sd_setImageWithURL:[NSURL URLWithString:giftModel3.giftUrl]];
            shareView.smallValueLabel.text = [NSString stringWithFormat:@"%.0f",giftModel.goldPrice];
            shareView.middelValueLabel.text = [NSString stringWithFormat:@"%.0f",giftModel2.goldPrice];
            shareView.bigValueLabel.text = [NSString stringWithFormat:@"%.0f",giftModel3.goldPrice];
            shareView.smallValueNameLabel.text = giftModel.giftName;
            shareView.middelValueNameLabel.text = giftModel2.giftName;
            shareView.bigValueNameLabel.text = giftModel3.giftName;
            [shareView mufaAction:nil];
        }
    }
    shareView.tapCallTypeBlock = tapCallTypeBlock;
    shareView.bottom_manager_layout.constant = -248;
   shareView.bottom_view_layout.constant = -448;
    
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.7 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        shareView.bottom_manager_layout.constant = 0;
        shareView.bottom_view_layout.constant = 0;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
    }];
}

//MARK: - 私有方法
- (UIImage *)getImageFromURL:(NSString *)fileURL
{
    UIImage *result;
    NSData *data = [NSData dataWithContentsOfURL:[NSURL URLWithString:fileURL]];
    result = [UIImage imageWithData:data];
    return result;
  
}


- (void)setBgRaduis
{
    CGFloat height = UIApplication.sharedApplication.statusBarFrame.size.height;
    CGFloat mangerHeight = 252;
    if (height >= 44.0) {
          // 是机型iPhoneX/iPhoneXR/iPhoneXS/iPhoneXSMax
        mangerHeight = 280;
    }
    CGRect frame = CGRectMake(0, 0, kScreenWidth, mangerHeight);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.managerView.layer.mask = maskLayer;
 
}

- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.3 animations:^{
        
        self.bottom_manager_layout.constant = -248;
        self.bottom_view_layout.constant = -448;
        
       
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}
// MARK: - Action
- (IBAction)tapCloseGesture:(id)sender {
    [self close];
}

//设置选中用户显示背景圆环
-(void)setImgForSelectState:(UIImageView*)selImageView{
    if (_btnSet.count>0) {
            UIImageView* temp = [_btnSet allObjects][0];
        if ([selImageView isEqual:temp]) {
           temp.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
        }else{
            temp.image = [UIImage imageNamed:@"hj_room_call_touxiang_select_no"];
        }
           
            [_btnSet removeAllObjects];
       }
     [_btnSet addObject:selImageView];
}


//大头
- (IBAction)ownBtnAction:(id)sender {
    if (self.intoModel.type == 3) {
        return;
    }
    
    
   UserInfo *ownerInfo = GetCore(HJImRoomCoreV2).roomOwnerInfo;
     self.intoModel.targetUid = [NSString stringWithFormat:@"%lld",ownerInfo.uid];
//    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
//        self.ownAvetarView.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
//    });
 self.ownAvetarView.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
    [self setImgForSelectState:self.ownAvetarView];
   
    
   
}
- (IBAction)mai1Action:(id)sender {
    self.mai1Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
    [self setImgForSelectState:self.mai1Img];
   

    _intoModel.targetUid = _queueItem.queueInfo.chatRoomMember.account;
}

- (IBAction)mai2Action:(id)sender {
     _intoModel.targetUid = _queueItem2.queueInfo.chatRoomMember.account;
    self.mai2Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
   [self setImgForSelectState:self.mai2Img];
  
}

- (IBAction)mai3Action:(id)sender {
      self.mai3Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
       [self setImgForSelectState:self.mai3Img];
     
     _intoModel.targetUid = _queueItem3.queueInfo.chatRoomMember.account;
}
- (IBAction)mai4Action:(id)sender {
     _intoModel.targetUid = _queueItem4.queueInfo.chatRoomMember.account;
    self.mai4Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
      [self setImgForSelectState:self.mai4Img];
     
}

- (IBAction)mai5Action:(id)sender {
     _intoModel.targetUid = _queueItem5.queueInfo.chatRoomMember.account;
     self.mai5Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
       [self setImgForSelectState:self.mai5Img];
      
}

- (IBAction)mai6Action:(id)sender {
     _intoModel.targetUid = _queueItem6.queueInfo.chatRoomMember.account;
    self.mai6Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
      [self setImgForSelectState:self.mai6Img];
     
}

- (IBAction)mai7Action:(id)sender {
     _intoModel.targetUid = _queueItem7.queueInfo.chatRoomMember.account;
    self.mai7Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
      [self setImgForSelectState:self.mai7Img];
   
}



- (IBAction)mai8Action:(id)sender {
     _intoModel.targetUid = _queueItem8.queueInfo.chatRoomMember.account;
    self.mai8Img.image = [UIImage imageNamed:@"hj_room_call_touxiang_select"];
      [self setImgForSelectState:self.mai8Img];
    
}


//打call
- (IBAction)dacallAction:(id)sender {
    if (_tapCallTypeBlock) {
        _tapCallTypeBlock(HJCallTypeDaCall,self.intoModel);
    }
}

//规则
- (IBAction)guizeAction:(id)sender {
    [HJPlayRulesView showPlayRulesView];
    
    
}
//充值
- (IBAction)chongzhiAction:(id)sender {
    UIViewController *vc = [[HJPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
          [[HJRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];
    [self close];
}

//木筏
- (IBAction)mufaAction:(id)sender {
    GiftInfo* giftModel = self.giftList[0];
    _intoModel.giftId = giftModel.giftId;
    _intoModel.giftNum = 1;
    _intoModel.callName = giftModel.giftName;
     _intoModel.giftUrl = giftModel.giftUrl;
    [_mufaBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select"] forState:UIControlStateNormal];
    [_xiaochuanBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select_no"] forState:UIControlStateNormal];
    [_youtingBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select_no"] forState:UIControlStateNormal];
    
}
//小船
- (IBAction)xiaochuanAction:(id)sender {
    GiftInfo* giftModel = self.giftList[1];
       _intoModel.giftId = giftModel.giftId;
     _intoModel.giftNum = 1;
    _intoModel.callName = giftModel.giftName;
     _intoModel.giftUrl = giftModel.giftUrl;
     [_xiaochuanBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select"] forState:UIControlStateNormal];
    [_youtingBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select_no"] forState:UIControlStateNormal];
    [_mufaBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select_no"] forState:UIControlStateNormal];
}
//游艇
- (IBAction)youtingAction:(id)sender {
    GiftInfo* giftModel = self.giftList[2];
       _intoModel.giftId = giftModel.giftId;
     _intoModel.giftNum = 1;
    _intoModel.callName = giftModel.giftName;
    _intoModel.giftUrl = giftModel.giftUrl;
     [_youtingBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select"] forState:UIControlStateNormal];
    [_xiaochuanBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select_no"] forState:UIControlStateNormal];
    [_mufaBtn setBackgroundImage:[UIImage imageNamed:@"hj_room_mufa_select_no"] forState:UIControlStateNormal];
}
//捡海螺入口
- (IBAction)jianhailuoAction:(id)sender {
    if (_tapCallTypeBlock) {
           _tapCallTypeBlock(HJCallTypePickupConch,self.intoModel);
        self.jianhailuoBtn.hidden = YES;
        self.numRemainLabel.hidden = YES;
       }
}

-(void)hiddenBtn12:(BOOL)isHidden{
    _mai1Img.hidden = isHidden;
    _mai1btn.hidden = isHidden;
    _mai2Img.hidden = isHidden;
    _mai2Btn.hidden = isHidden;
}

-(void)hiddenBtn34:(BOOL)isHidden{
      _mai3Img.hidden = isHidden;
      _mai3Btn.hidden = isHidden;
      _mai4Img.hidden = isHidden;
      _mai4Btn.hidden = isHidden;
 
}
-(void)hiddenBtn56:(BOOL)isHidden{
    _mai5Img.hidden = isHidden;
    _mai5Btn.hidden = isHidden;
    _mai6Img.hidden = isHidden;
    _mai6Btn.hidden = isHidden;
}
-(void)hiddenBtn78:(BOOL)isHidden{
     _mai7Img.hidden = isHidden;
     _mai7Btn.hidden = isHidden;
     _mai8Img.hidden = isHidden;
     _mai8Btn.hidden = isHidden;
}


-(void)setDataModel{
    switch (self.micArr.count) {
        case 1:
            [self setbtnData1];
            break;
            case 2:
        [self setbtnData1];
             [self setbtnData2];
            break;
            case 3:
             [self setbtnData1];
                        [self setbtnData2];
             [self setbtnData3];
            break;
            case 4:
            [self setbtnData1];
                        [self setbtnData2];
            [self setbtnData3];
                        [self setbtnData4];
               
            case 5:
              [self setbtnData1];
                          [self setbtnData2];
            [self setbtnData3];
                        [self setbtnData4];
             [self setbtnData5];
            break;
            case 6:
              [self setbtnData1];
            [self setbtnData2];
            [self setbtnData3];
             [self setbtnData4];
             [self setbtnData5];
             [self setbtnData6];
            break;
            case 7:
          [self setbtnData1];
               [self setbtnData2];
               [self setbtnData3];
                [self setbtnData4];
               [self setbtnData5];
            [self setbtnData6];
            [self setbtnData7];
            break;
            case 8:
               [self setbtnData1];
                [self setbtnData2];
                [self setbtnData3];
                [self setbtnData4];
            [self setbtnData5];
            [self setbtnData6];
            [self setbtnData7];
            [self setbtnData8];
            break;
            
        default:
            break;
    }
    
   
}

-(void)setbtnData1{
    
    _queueItem  = self.micArr[0];
                [self.mai1btn setImage:[self getImageFromURL:_queueItem.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}
-(void)setbtnData2{
    _queueItem2  = self.micArr[1];
        [self.mai2Btn setImage:[self getImageFromURL:_queueItem2.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}
-(void)setbtnData3{
    _queueItem3  = self.micArr[2];
           [self.mai2Btn setImage:[self getImageFromURL:_queueItem3.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}
-(void)setbtnData4{
    _queueItem4  = self.micArr[3];
           [self.mai2Btn setImage:[self getImageFromURL:_queueItem4.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}
-(void)setbtnData5{
    _queueItem5  = self.micArr[4];
           [self.mai2Btn setImage:[self getImageFromURL:_queueItem5.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}
-(void)setbtnData6{
    _queueItem6  = self.micArr[5];
           [self.mai6Btn setImage:[self getImageFromURL:_queueItem6.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}
-(void)setbtnData7{
    _queueItem7  = self.micArr[6];
           [self.mai7Btn setImage:[self getImageFromURL:_queueItem7.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}
-(void)setbtnData8{
    _queueItem8  = self.micArr[7];
           [self.mai8Btn setImage:[self getImageFromURL:_queueItem8.queueInfo.chatRoomMember.avatar] forState:UIControlStateNormal];
}

-(void)isHiddenBtn{
    
    self.manage_height_layout.constant = 200;
    [self hiddenBtn12:YES];
    [self hiddenBtn34:YES];
    [self hiddenBtn56:YES];
    [self hiddenBtn78:YES];
//    if (_micArr.count<1) {
//        self.manage_height_layout.constant = 200;
//    }else{
//          self.manage_height_layout.constant = 248;
//    }
    //隐藏dacall对象按钮
//    switch (_micArr.count) {
//        case 0:
//            [self hiddenBtn12:YES];
//             [self hiddenBtn34:YES];
//             [self hiddenBtn56:YES];
//             [self hiddenBtn78:YES];
//            break;
//            case 1:
//            _mai1Img.hidden = NO;
//            _mai1btn.hidden = NO;
//            _mai2Img.hidden = YES;
//            _mai2Btn.hidden = YES;
//            [self hiddenBtn34:YES];
//            [self hiddenBtn56:YES];
//            [self hiddenBtn78:YES];
//            break;
//            case 2:
//
//            [self hiddenBtn12:NO];
//           [self hiddenBtn34:YES];
//           [self hiddenBtn56:YES];
//           [self hiddenBtn78:YES];
//
//            break;
//            case 3:
//            [self hiddenBtn12:NO];
//            [self hiddenBtn56:YES];
//            [self hiddenBtn78:YES];
//            _mai3Img.hidden = NO;
//            _mai3Btn.hidden = NO;
//            _mai4Img.hidden = YES;
//            _mai4Btn.hidden = YES;
//
//            break;
//            case 4:
//            [self hiddenBtn12:NO];
//            [self hiddenBtn56:YES];
//            [self hiddenBtn78:YES];
//            [self hiddenBtn34:NO];
//
//            break;
//            case 5:
//            [self hiddenBtn12:NO];
//            [self hiddenBtn34:NO];
//            [self hiddenBtn78:YES];
//            _mai6Img.hidden = YES;
//            _mai6Btn.hidden = YES;
//            _mai5Img.hidden = NO;
//            _mai5Btn.hidden = NO;
//            break;
//            case 6:
//            [self hiddenBtn12:NO];
//            [self hiddenBtn34:NO];
//            [self hiddenBtn78:YES];
//             [self hiddenBtn56:NO];
//            break;
//            case 7:
//            [self hiddenBtn12:NO];
//            [self hiddenBtn34:NO];
//            [self hiddenBtn56:NO];
//            _mai8Img.hidden = YES;
//            _mai8Btn.hidden = YES;
//            _mai7Img.hidden = NO;
//            _mai7Btn.hidden = NO;
//            break;
//            case 8:
//            [self hiddenBtn12:NO];
//            [self hiddenBtn34:NO];
//            [self hiddenBtn56:NO];
//            [self hiddenBtn78:NO];
//            break;
//
//        default:
//
//            break;
//    }
}
-(HJDaCallIntoModel*)intoModel{
    if (!_intoModel) {
        _intoModel = [[HJDaCallIntoModel alloc] init];
    }
    
    return _intoModel;
}






@end

