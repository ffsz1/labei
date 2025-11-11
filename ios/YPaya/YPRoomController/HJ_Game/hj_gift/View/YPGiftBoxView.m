//
//  YPGiftBoxView.m
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import "YPGiftBoxView.h"

#import "YPGiftMicView.h"
#import "YPGiftCollectionView.h"

#import "YPPointCoinWalletModel.h"

#import "YPPurseCore.h"
#import "YPHttpRequestHelper+Sign.h"
#import "HJPurseCoreClient.h"
#import "YPGiftCore.h"

#import "YPPurseViewControllerFactory.h"
#import "YPRoomViewControllerCenter.h"
#import "YPMySpaceVC.h"
#import "UIView+getTopVC.h"

#import "YPRoomQueueCoreV2Help.h"

#import "YPImRoomCoreV2.h"

#import "YPSpaceCardView.h"
#import "YPKeyBoardView.h"
#import "YPRenqipiaoIntrodutionView.h"


@interface YPGiftBoxView ()

@property (weak, nonatomic) IBOutlet UIView *cardView;

//个人信息
@property (weak, nonatomic) IBOutlet UIView *personView;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;

//普通、背包、点点币 btn
@property (weak, nonatomic) IBOutlet UIButton *normalBtn;
@property (weak, nonatomic) IBOutlet UIButton *bagBtn;
@property (weak, nonatomic) IBOutlet UIButton *pointBtn;

//礼物
@property (weak, nonatomic) IBOutlet UIView *giftContentView;
@property (weak, nonatomic) IBOutlet UIPageControl *pageView;

//底部栏
@property (weak, nonatomic) IBOutlet UIImageView *iconImageView;
@property (weak, nonatomic) IBOutlet UILabel *coinNumLabel;
@property (weak, nonatomic) IBOutlet UIButton *numBtn;
@property (weak, nonatomic) IBOutlet UIImageView *upArrowImageView;
@property (weak, nonatomic) IBOutlet UILabel *addCoinLabel;
@property (weak, nonatomic) IBOutlet UIImageView *addCoinArrow;

//礼物数量按钮
@property (weak, nonatomic) IBOutlet UIButton *numBtn_1;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_10;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_66;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_99;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_188;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_520;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_666;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_1314;
@property (weak, nonatomic) IBOutlet UIButton *numBtn_define;

@property (nonatomic,assign) NSInteger freeNum;


//礼物数量框底部 约束
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_numView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_cardView;

//麦上用户
@property (nonatomic,strong) YPGiftMicView *micView;

@property (nonatomic,strong) YPGiftCollectionView *giftView;

@property (assign,nonatomic) XBDGiftBoxType type;

@property (strong,nonatomic) YPPointCoinWalletModel *pointCoinModel;

@property (strong, nonatomic) NSArray *selectedUids;

@property (nonatomic,assign) UserID uid;

@property (assign,nonatomic) BOOL isChat;
@property (weak, nonatomic) IBOutlet UIButton *senddBtn;
@property (weak, nonatomic) IBOutlet UILabel *backPackNumLabel;


@property (weak, nonatomic) IBOutlet UIImageView *renqipiaoImgView;

@property (weak, nonatomic) IBOutlet UILabel *huodongGiftLabel;

@property (weak, nonatomic) IBOutlet UILabel *activityGiftDesLabel;

@property (weak, nonatomic) IBOutlet UIButton *playGiftIntroBtn;
@property (nonatomic, copy) void(^CustomQuantityBlock)(void);
@end

@implementation YPGiftBoxView

//+ (instancetype)shareGiftBoxView
//{
////    static YPGiftBoxView *_sharedSingleton = nil;
////    static dispatch_once_t onceToken;
////    dispatch_once(&onceToken, ^{
////        _sharedSingleton = [[NSBundle mainBundle]loadNibNamed:@"YPGiftBoxView" owner:self options:nil][0];
////    });
//    return [[NSBundle mainBundle]loadNibNamed:@"YPGiftBoxView" owner:self options:nil][0];
//
//}

+ (void)show:(UserID)tagUID
{
    [YPGiftBoxView show:tagUID isAllMic:NO isChat:NO];
}

+ (void)showAllMic:(UserID)tagUID
{
    [YPGiftBoxView show:tagUID isAllMic:YES isChat:NO];
}

+ (void)showChat:(UserID)tagUID
{
    [YPGiftBoxView show:tagUID isAllMic:NO isChat:YES];
}

+ (void)show:(UserID)tagUID isAllMic:(BOOL)isAllMic isChat:(BOOL)isChat
{
    YPGiftBoxView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPGiftBoxView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];
    
    shareView.bottom_numView.constant = -298;
    shareView.bottom_cardView.constant = -440;
    
    shareView.coinNumLabel.text = [NSString stringWithFormat:@"%.0f",[GetCore(YPPurseCore).balanceInfo.goldNum doubleValue]];
    
    shareView.isChat = isChat;
    
    [shareView updateDiandianCoinData];
    [GetCore(YPGiftCore) requestGiftList];
    [GetCore(YPPurseCore) requestBalanceInfo:GetCore(YPAuthCoreHelp).getUid.userIDValue];
    if (isAllMic) {
        [shareView setOnMicUids:tagUID];
    }else{
        [shareView setPersonInfo:tagUID];
    }
    
    
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.3 animations:^{
        shareView.bottom_cardView.constant = 0;
        
        [shareView layoutIfNeeded];
    } completion:^(BOOL finished) {
        
    }];
    
    [shareView.giftView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(shareView.giftContentView);
    }];
    
}



- (void)close
{
    [UIView animateWithDuration:0.3 animations:^{
        self.bottom_cardView.constant = -440;
        [self layoutIfNeeded];
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    AddCoreClient(HJPurseCoreClient, self);
    
    [self setBgRaduis];
    
    
}

- (void)dealloc
{
    RemoveCoreClientAll(self);
}

- (void)setBgRaduis
{
    //背景图左上、右上圆角
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 440);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.cardView.layer.mask = maskLayer;
    
}

- (void)resetTypeBtn
{
    self.normalBtn.selected = NO;
    self.bagBtn.selected = NO;
    self.pointBtn.selected = NO;

}

//人气票介绍玩法
- (IBAction)renqipiaoIntroduBtnAction:(id)sender {
//    HJRenqipiaoIntrodutionVC* vc = [[HJRenqipiaoIntrodutionVC alloc] init];
    
    [YPRenqipiaoIntrodutionView showRenqipiaoIntrodutionView];
    if (_CustomQuantityBlock) {
        _CustomQuantityBlock();
    }
    
    
}


- (IBAction)closeBtnAction:(id)sender {
    
    [self close];
}
//普通
- (IBAction)normalBtnAction:(id)sender {
    _backPackNumLabel.hidden = YES;
    [self resetTypeBtn];
    self.renqipiaoImgView.hidden = YES;
    self.huodongGiftLabel.hidden = YES;
        self.activityGiftDesLabel.hidden = YES;
        self.playGiftIntroBtn.hidden = YES;
    self.type = XBDGiftBoxTypeNormal;
    self.normalBtn.selected = YES;
    //发送显示隐藏
     self.senddBtn.hidden = NO;
    self.numBtn.hidden = NO;
    self.upArrowImageView.hidden = NO;
    //选中标识
    self.normalItem.hidden = NO;
    self.bagItem.hidden = YES;
    self.pointItem.hidden = YES;
}
//背包
- (IBAction)bagBtnAction:(id)sender {
    [self resetTypeBtn];
      self.renqipiaoImgView.hidden = YES;
    self.huodongGiftLabel.hidden = YES;
     self.activityGiftDesLabel.hidden = YES;
     self.playGiftIntroBtn.hidden = YES;
    
 _backPackNumLabel.hidden = YES;
    self.type = XBDGiftBoxTypeBag;
    self.bagBtn.selected = YES;
    
    self.senddBtn.hidden = NO;
    self.numBtn.hidden = NO;
    self.upArrowImageView.hidden = NO;
    
    self.normalItem.hidden = YES;
    self.bagItem.hidden = NO;
    self.pointItem.hidden = YES;

}
//活动
- (IBAction)pointBtnAction:(id)sender {
    [self resetTypeBtn];
    self.renqipiaoImgView.hidden = NO;
    self.huodongGiftLabel.hidden = NO;
    self.activityGiftDesLabel.hidden = NO;
    self.playGiftIntroBtn.hidden = NO;
    
 _backPackNumLabel.hidden = YES;
    self.type = XBDGiftBoxTypePoint;
    self.pointBtn.selected = YES;
    
    self.senddBtn.hidden = NO;
    self.numBtn.hidden = NO;
    self.upArrowImageView.hidden = NO;
    
    
    self.normalItem.hidden = YES;
    self.bagItem.hidden = YES;
    self.pointItem.hidden = NO;
}

//选择数量
- (IBAction)numBtnAction:(id)sender {
    
    self.numBtn.userInteractionEnabled = NO;
    
    CGAffineTransform endAngle = CGAffineTransformMakeRotation(180 * (M_PI / -180.0f));

    
    [self layoutIfNeeded];
    
    
    [UIView animateWithDuration:0.6 animations:^{
        self.bottom_numView.constant = 52;
        self.upArrowImageView.transform = endAngle;

        [self layoutIfNeeded];
    } completion:^(BOOL finished) {
        self.numBtn.userInteractionEnabled = YES;
    }];
    
}



- (IBAction)numAction_1:(id)sender {
    
    [self hidenNumView];
    [self.numBtn setTitle:@"1" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn.selected = YES;
    
}

- (IBAction)numAction_10:(id)sender {
    [self hidenNumView];
    [self.numBtn setTitle:@"10" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn_10.selected = YES;
}

- (IBAction)numAction_66:(id)sender {
    [self hidenNumView];
    [self.numBtn setTitle:@"66" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn_66.selected = YES;
}

- (IBAction)numAction_99:(id)sender {
    [self hidenNumView];
    [self.numBtn setTitle:@"99" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn_99.selected = YES;
}

- (IBAction)numAction_188:(id)sender {
    [self hidenNumView];
    [self.numBtn setTitle:@"188" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn_188.selected = YES;
}

- (IBAction)numAction_520:(id)sender {
    [self hidenNumView];
    [self.numBtn setTitle:@"520" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn_520.selected = YES;
}

- (IBAction)numAction_666:(id)sender {
    [self hidenNumView];
    [self.numBtn setTitle:@"666" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn_666.selected = YES;
}

- (IBAction)numAction_1314:(id)sender {
    [self hidenNumView];
    [self.numBtn setTitle:@"1314" forState:UIControlStateNormal];
    
    [self resetNumBtn];
    self.numBtn_1314.selected = YES;
}
//自定义数量
- (IBAction)numAction_define:(id)sender {
    
    [YPKeyBoardView showKeyboardWithSureBtnImgStr:@"yp_prop_icon_goumai" sureBtnTitle:@"确定" sureActionHandler:^(NSInteger count) {
//        [self.bottomView.sendBtn setTitle:[NSString stringWithFormat:@"%zd",count] forState:UIControlStateNormal];
        [self hidenNumView];
        [self.numBtn setTitle:[NSString stringWithFormat:@"%zd",count] forState:UIControlStateNormal];
        [self resetNumBtn];
        self.freeNum = count;
    }];
    
}


- (IBAction)sendAction:(id)sender {
    
    
    YPGiftInfo *giftModel = [self.giftView getGiftModel];
    
    NSInteger giftNum = 1;
    if (self.numBtn_10.selected) giftNum = 10;
    if (self.numBtn_66.selected) giftNum = 66;
    if (self.numBtn_99.selected) giftNum = 99;
    if (self.numBtn_188.selected) giftNum = 188;
    if (self.numBtn_520.selected) giftNum = 520;
    if (self.numBtn_666.selected) giftNum = 666;
    if (self.numBtn_1314.selected) giftNum = 1314;
    
    if (self.freeNum > 0) giftNum = self.freeNum;
    
    if (giftModel == nil) {
        [MBProgressHUD showError:@"请选择赠送的礼物" toView:[UIApplication sharedApplication].keyWindow];
        return;
    }
    
    //私聊送礼物
    if (self.isChat) {
        [self setChatGift:giftModel giftNum:giftNum];
        return;
    }
    
    //房间单人送
    if (self.uid>0) {
        [self setSingleGift:giftModel giftNum:giftNum];
        return;
    }
    
    //房间多人送
    [self sendOnMicGift:giftModel giftNum:giftNum];
    
}

- (IBAction)addCoinBtnAction:(id)sender {
    
//    if (_type == XBDGiftBoxTypePoint) {
//        return;
//    }
    
//    [self close];
    
    [self removeFromSuperview];
    
    
    UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
    [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];
    
}
- (IBAction)infoBtnAction:(id)sender {
    
    [self showInfoView:nil];
    
}

- (IBAction)showInfoView:(UITapGestureRecognizer *)sender {
    
    [self close];
    
    UserID uid = self.uid;
    
    if (uid == 0) {
        uid = [self.micView getFirstSelUID];
    }
    
    if (uid == 0) {
        [MBProgressHUD showSuccess:@"请选择需要查看资料的用户" toView:[UIApplication sharedApplication].keyWindow];
        return;
    }
    
//    [YPSpaceCardView show];
    
    YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
    vc.userID = uid;
    [[self topViewController].navigationController pushViewController:vc animated:YES];
    
}

- (void)hidenNumView
{
    
    CGAffineTransform endAngle = CGAffineTransformMakeRotation(0);
    [self layoutIfNeeded];
    [UIView animateWithDuration:0.6 animations:^{
        self.bottom_numView.constant = -298;
        self.upArrowImageView.transform = endAngle;

        [self layoutIfNeeded];
    } completion:^(BOOL finished) {
    }];
}

- (void)resetNumBtn
{
    self.numBtn_1.selected = NO;
    self.numBtn_10.selected = NO;
    self.numBtn_66.selected = NO;
    self.numBtn_99.selected = NO;
    self.numBtn_188.selected = NO;
    self.numBtn_520.selected = NO;
    self.numBtn_666.selected = NO;
    self.numBtn_1314.selected = NO;
    self.freeNum = 0;
}



- (void)updateDiandianCoinData
{
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper requestDiandianCoinNum:^(id  _Nonnull data) {
        
        weakSelf.pointCoinModel = data;
        
        if (weakSelf.type == XBDGiftBoxTypePoint) {
            weakSelf.coinNumLabel.text = JX_STR_AVOID_nil(weakSelf.pointCoinModel.mcoinNum);
        }
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        
    }];
}

//发送私聊礼物消息
- (void)setChatGift:(YPGiftInfo *)giftModel giftNum:(NSInteger)giftNum
{
    UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:self.uid];
    
    if (self.type == XBDGiftBoxTypeNormal) {
        
        [GetCore(YPGiftCore)sendChatGift:giftModel.giftId info:info giftNum:giftNum targetUid:self.uid  giftyType:GiftTypeNormal];;
    }
    
    if (self.type == XBDGiftBoxTypeBag) {
        [GetCore(YPGiftCore)sendChatGift:giftModel.giftId info:info giftNum:giftNum targetUid:self.uid  giftyType:GiftTypeMystic];
    }
    
    if (self.type == XBDGiftBoxTypePoint) {
        [GetCore(YPGiftCore)sendChatPoint:giftModel.giftId info:info giftNum:giftNum targetUid:self.uid  giftyType:GiftTypeNormal];
    }
    
}


//发送单人礼物消息
- (void)setSingleGift:(YPGiftInfo *)giftModel giftNum:(NSInteger)giftNum
{
    
    if (self.type == XBDGiftBoxTypeNormal) {
        [GetCore(YPGiftCore) sendRoomGift:giftModel.giftId targetUid:self.uid type:3 giftNum:giftNum giftyType:GiftTypeNormal goldPrice:giftModel.goldPrice];
    }
    
    if (self.type == XBDGiftBoxTypeBag) {
        [GetCore(YPGiftCore) sendRoomGift:giftModel.giftId targetUid:self.uid type:3 giftNum:giftNum giftyType:GiftTypeMystic goldPrice:giftModel.goldPrice];
    }
    
    if (self.type == XBDGiftBoxTypePoint) {
        [GetCore(YPGiftCore) sendPoint:giftModel.giftId targetUid:self.uid type:3 giftNum:giftNum giftyType:GiftTypeDiandianCoin goldPrice:giftModel.goldPrice];
    }
    
}

- (void)sendOnMicGift:(YPGiftInfo *)giftModel giftNum:(NSInteger)giftNum
{
    NSString *uidsStr = [self.micView getSelUidsString];
    BOOL isAllMic = [self.micView isAllMic];
    
    if (uidsStr.length == 0) {
        [MBProgressHUD showError:@"请选择送礼物的人" toView:[UIApplication sharedApplication].keyWindow];
        return;
    }
    
    if (self.type == XBDGiftBoxTypeNormal) {
        [GetCore(YPGiftCore) sendAllMicroGiftByUids:uidsStr giftId:giftModel.giftId giftNum:giftNum roomUid:GetCore(YPImRoomCoreV2).currentRoomInfo.uid isAllMicroSend:isAllMic giftyType:GiftTypeNormal goldPrice:giftModel.goldPrice];
    }
    
    if (self.type == XBDGiftBoxTypeBag) {
        [GetCore(YPGiftCore) sendAllMicroGiftByUids:uidsStr giftId:giftModel.giftId giftNum:giftNum roomUid:GetCore(YPImRoomCoreV2).currentRoomInfo.uid isAllMicroSend:isAllMic giftyType:GiftTypeMystic goldPrice:giftModel.goldPrice];
    }
    
    if (self.type == XBDGiftBoxTypePoint) {
        [GetCore(YPGiftCore) sendPointWholeMicro:uidsStr giftId:giftModel.giftId giftNum:giftNum roomUid:GetCore(YPImRoomCoreV2).currentRoomInfo.uid isAllMicroSend:isAllMic giftyType:GiftTypeDiandianCoin goldPrice:giftModel.goldPrice];
    }
    
}

#pragma mark - HJPurseCoreClient
- (void)onBalanceInfoUpdate:(YPBalanceInfo *)balanceInfo
{
    if ([balanceInfo.goldNum doubleValue] < 0) {
        UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
        [GetCore(YPPurseCore) requestBalanceInfo:uid];
    } else {
        self.coinNumLabel.text = [NSString stringWithFormat:@"%.0f",balanceInfo.goldNum.doubleValue];
    }
}

//设置麦上用户
- (void)setOnMicUids:(UserID)uid
{
    self.personView.hidden = YES;
    
    
//    self.selectedUids = temp;
    
    self.micView.targetUID = uid;
    self.micView.hidden = NO;
    
//    self.micView.micArr = self.selectedUids;
}

//设置单人样式
- (void)setPersonInfo:(UserID)uid
{
    self.micView.hidden = YES;
    self.uid = uid;
    UserInfo * userInfo = [GetCore(YPUserCoreHelp) getUserInfoInDB:uid];
    
    if (userInfo.uid == 0) {
        [GetCore(YPUserCoreHelp) getUserInfo:uid refresh:YES success:^(UserInfo *info) {
            [self.avatarImageView qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
            self.nameLabel.text = info.nick;
        }];
    }else{
        [self.avatarImageView qn_setImageImageWithUrl:userInfo.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        self.nameLabel.text = userInfo.nick;
    }
    
    
}

#pragma mark setter/getter

- (void)setType:(XBDGiftBoxType)type
{
    _type = type;
    
    if (type == XBDGiftBoxTypePoint) {
//        self.iconImageView.image = [UIImage imageNamed:@"yp_sign_alter_coin"];
//        self.coinNumLabel.text = JX_STR_AVOID_nil(self.pointCoinModel.mcoinNum);
//        if (self.coinNumLabel.text.length == 0) self.coinNumLabel.text = @"0";
//
//        self.addCoinArrow.hidden = YES;
//        self.addCoinLabel.hidden = YES;
    }else{
        self.iconImageView.image = [UIImage imageNamed:@"yp_room_gift_icon_jinbii"];
        self.coinNumLabel.text = [NSString stringWithFormat:@"%.0f",[GetCore(YPPurseCore).balanceInfo.goldNum doubleValue]];
        self.addCoinArrow.hidden = NO;
        self.addCoinLabel.hidden = NO;
    }
    
    self.giftView.type = _type;
    
}



- (YPGiftMicView *)micView
{
    if (!_micView) {
        _micView = [[YPGiftMicView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 85)];
    
        [self.cardView addSubview:_micView];
    }
  
    return _micView;
}

- (YPGiftCollectionView *)giftView
{
     __weak typeof(self) weakSelf = self;
    if (!_giftView) {
        _giftView = [[YPGiftCollectionView alloc] initWithFrame:CGRectZero];
        _giftView.allBagNumberBlack = ^(double allBagNumber) {
                       NSInteger allBagNumberr = (NSInteger)allBagNumber;
                       weakSelf.backPackNumLabel.text = [NSString stringWithFormat:@"总：%ld 金币",allBagNumberr];
                   };
//             _giftView.selectCellBlack = ^(YPGiftInfo * _Nonnull selectInfo) {
//                       if (weakSelf.numBtn_all.selected) {
//                           weakSelf.numBtn_all.selected = NO;
//                            [weakSelf.numBtn setTitle:@"1" forState:UIControlStateNormal];
//                            weakSelf.uid = 0;
//                       }
//                       
//                   };
        [self.giftContentView addSubview:_giftView];
    }
    return _giftView;
}

@end
