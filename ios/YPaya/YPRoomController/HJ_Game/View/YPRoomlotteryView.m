//
//  YPRoomlotteryView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomlotteryView.h"
#import "YPRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "YPGiftCore.h"
#import "YPPurseCore.h"
#import "YPPurseViewControllerFactory.h"
#import "YPRoomViewControllerCenter.h"
#import "YPAlertControllerCenter.h"
#import "YPUserCoreHelp.h"
#import "HJPurseCoreClient.h"

#import "YPHttpRequestHelper+Egg.h"
#import "HJPurseCoreClient.h"
#import "YPEggGiftListView.h"
#import "YPEggRecordListView.h"
#import "NSTimer+JXBase.h"
typedef NS_ENUM(NSUInteger, XDBEggPriceType) {
    XDBEggPriceTypeOne = 0,
    XDBEggPriceTypeTen,
    XDBEggPriceTypeFifty,
};


@interface YPRoomlotteryView()<
HJRoomCoreClient,
HJPurseCoreClient
>
@property (nonatomic, assign) BOOL isType;
@property (nonatomic, strong) UserInfo *info;
@property (weak, nonatomic) IBOutlet UIView *showOneView;

@property (nonatomic,assign) XDBEggPriceType type;
@property (weak, nonatomic) IBOutlet UIButton *onceAgainBtn;

@property (nonatomic,assign) BOOL isAgainRequesting;

@end

@implementation YPRoomlotteryView


- (IBAction)rechareClick:(UIButton *)sender {
    [[YPAlertControllerCenter defaultCenter] dismissAlertNeedBlock:false];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [[YPRoomViewControllerCenter defaultCenter].current pushViewController:vc animated:YES];
    });
}

- (void)awakeFromNib {
    [super awakeFromNib];

//        self.managerView.layer.mask = [self setBgRaduisWithHeight:250];
      self.tenView.layer.mask = [self setBgRaduisWithHeight:420];
    
    self.boxImg.contentMode = UIViewContentModeCenter;
    self.closeBtn.layer.borderColor = UIColorHex(ffffff).CGColor;
    self.tenSureBtn.layer.borderColor = UIColorHex(ffffff).CGColor;
    AddCoreClient(HJRoomCoreClient, self);
    AddCoreClient(HJPurseCoreClient, self);
    self.tenView.transform = CGAffineTransformMakeScale(0, 0);
    
    NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
    self.info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[userId intValue]];
    
    [self configMoney];
    
    [self getWalletData];
    
    self.pickConchNumLabel.text =  GetCore(YPPurseCore).balanceInfo.conchNum;
    self.pickGiftNumLabel.text =  self.pickConchNumLabel.text;
    
    //设置规则、排行榜、记录、礼物圆角边框
//    [self setBorderForCall];
//    [self setBgRaduis];
    
}
- (void)setBgRaduis
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, 420);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;
    self.guizeView.layer.mask = maskLayer;
 
}
-(void)setBorderForCall{
//    _guizeBtn.layer.cornerRadius = 8;
//    _guizeBtn.layer.masksToBounds = YES;
//    _guizeBtn.layer.borderWidth = 0.5;
//    _guizeBtn.layer.borderColor = [UIColor whiteColor].CGColor;
    [_guizeBtn setBackgroundImage:[UIImage imageNamed:@"yp_room_jianhailuo_rule"] forState:UIControlStateNormal];
    
//    _liwuBtn.layer.cornerRadius = 8;
//    _liwuBtn.layer.masksToBounds = YES;
//    _liwuBtn.layer.borderWidth = 0.5;
//    _liwuBtn.layer.borderColor = [UIColor whiteColor].CGColor;
    [_liwuBtn setBackgroundImage:[UIImage imageNamed:@"yp_room_jianhailuo_gift"] forState:UIControlStateNormal];
    
//    _jiluBtn.layer.cornerRadius = 8;
//    _jiluBtn.layer.masksToBounds = YES;
//    _jiluBtn.layer.borderWidth = 0.5;
//    _jiluBtn.layer.borderColor = [UIColor whiteColor].CGColor;
    [_jiluBtn setBackgroundImage:[UIImage imageNamed:@"yp_room_jianhailuo_record"] forState:UIControlStateNormal];
    
//    _paihangbangBtn.layer.cornerRadius = 8;
//    _paihangbangBtn.layer.masksToBounds = YES;
//    _paihangbangBtn.layer.borderWidth = 0.5;
//    _paihangbangBtn.layer.borderColor = [UIColor whiteColor].CGColor;
    [_paihangbangBtn setBackgroundImage:[UIImage imageNamed:@"yp_room_jianhailuo_rank"] forState:UIControlStateNormal];
    
    
}







- (CAShapeLayer*)setBgRaduisWithHeight:(CGFloat)height
{
    CGRect frame = CGRectMake(0, 0, kScreenWidth, height);
    UIBezierPath *maskPath = [UIBezierPath bezierPathWithRoundedRect:frame byRoundingCorners:UIRectCornerTopRight | UIRectCornerTopLeft cornerRadii:CGSizeMake(15, 15)];
    
    CAShapeLayer * maskLayer = [[CAShapeLayer alloc]init];
    maskLayer.frame = frame;
    maskLayer.path = maskPath.CGPath;

   return maskLayer;
}



- (void)getWalletData
{
    UserID uid = [GetCore(YPAuthCoreHelp) getUid].userIDValue;
    [GetCore(YPPurseCore) requestBalanceInfo:uid];
}
    

- (void)configMoney {
    
    if ([GetCore(YPPurseCore).balanceInfo.goldNum doubleValue] < 0) {
        [self getWalletData];
        return;
    }
    
    NSString *str2 = [NSString stringWithFormat:@"%@  充值",GetCore(YPPurseCore).balanceInfo.goldNum];
    NSString *str3 = @"充值";
    NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:str2];
    [str addAttribute:NSForegroundColorAttributeName value:UIColorHex(FFFFFF) range:[str2 rangeOfString:GetCore(YPPurseCore).balanceInfo.goldNum]];
    [str addAttribute:NSForegroundColorAttributeName value:UIColorHex(3D95E3) range:[str2 rangeOfString:str3]];
    [self.moneyBtn setAttributedTitle:str forState:UIControlStateNormal];
}



- (void)dealloc {
    RemoveCoreClient(HJRoomCoreClient, self);
    RemoveCoreClient(HJPurseCoreClient, self);
}

- (IBAction)closeC:(UIButton *)sender {
    if (self.closeRoomLotteryView) {
        self.closeRoomLotteryView();
        [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationCloseGame" object:nil userInfo:nil];

    }
}

- (IBAction)closeLotteryOtherClick:(UIButton *)sender {
    self.roomLotteryOtherView.hidden = YES;
}

- (IBAction)helpClick:(UIButton *)sender {
    if (self.helpClickBlock) {
        self.helpClickBlock();
    }
}
- (IBAction)rankClick:(id)sender {
    
    if (self.rankClickBlock) {
        self.rankClickBlock();
    }
}

- (IBAction)onceOrTenceClick:(UIButton *)sender {
    self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled =self.fiftyBtn.userInteractionEnabled =false;
    if (sender.tag == 2) {
        self.isType = false;
        self.type = XDBEggPriceTypeOne;

        [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"1"];
    } else if (sender.tag == 3) {
        self.type = XDBEggPriceTypeTen;

        self.isType = YES;
        [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"2"];
    }
}
- (IBAction)onceAgainBtnAction:(id)sender {
    
//    self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled =self.fiftyBtn.userInteractionEnabled =false;
//
//    self.isAgainRequesting = YES;
//    self.onceAgainBtn.userInteractionEnabled = NO;
//
//    if (self.type == XDBEggPriceTypeTen) {
//        self.isType = YES;
//        [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"2"];
//    }else if (self.type == XDBEggPriceTypeFifty){
//        self.isType = YES;
//        [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"4"];
//    }
    self.type = XDBEggPriceTypeFifty;
     [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"4"];
    
}


- (IBAction)recordeAction:(id)sender {
    
    if (self.recordeClickBlock) {
        self.recordeClickBlock();
    }
}

- (void)userGiftPurseDrawFail:(NSString *)message code:(NSNumber *)code {
    [MBProgressHUD showSuccess:message];
    
    [self getWalletData];
    
    if (self.isAgainRequesting) {
        self.isAgainRequesting = NO;
        self.onceAgainBtn.userInteractionEnabled = YES;
    }
    
    self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled = self.fiftyBtn.userInteractionEnabled = YES;
}

- (void)showOneView:(YPGiftInfo *)info {

    [self.oneImg qn_setImageImageWithUrl:info.picUrl placeholderImage:nil type:ImageTypeRoomGift];
    
    if (self.setAnimationBtn.selected) {
        
        NSArray * imgsArr = @[
                              [UIImage imageNamed:@"yp_room_egg_zd0"],
                              [UIImage imageNamed:@"yp_room_egg_zd1"],
                              [UIImage imageNamed:@"yp_room_egg_zd2"],
                              [UIImage imageNamed:@"yp_room_egg_zd3"],
                              [UIImage imageNamed:@"yp_room_egg_zd4"],
                              [UIImage imageNamed:@"yp_room_egg_zd5"],
                              ];
        // 设置动画图片数组
        [self.boxImg setAnimationImages:imgsArr];
        [self.boxImg setAnimationDuration:1];
        self.boxImg.animationRepeatCount = 1;
        [self.boxImg setImage:[UIImage imageNamed:@"yp_room_egg_zd5"]];
        [self.boxImg startAnimating];
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.oneWidthCons.constant = 200;
            self.oneHeightCons.constant = 300;
            self.showOneView.hidden = NO;
            [UIView animateWithDuration:1 animations:^{
                [self layoutIfNeeded];
            } completion:^(BOOL finished) {
                @weakify(self);
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    @strongify(self);
                    self.boxImg.image = [UIImage imageNamed:@"yp_room_egg_zd0"];
                    self.showOneView.hidden = YES;
                    self.oneWidthCons.constant = 0;
                    self.oneHeightCons.constant = 0;
                    self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled = self.fiftyBtn.userInteractionEnabled = YES;
                });
            }];
        });

    }else{
        
        self.oneWidthCons.constant = 200;
        self.oneHeightCons.constant = 300;
        self.showOneView.hidden = NO;
        
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            self.boxImg.image = [UIImage imageNamed:@"yp_room_egg_zd0"];
            self.showOneView.hidden = YES;
            self.oneWidthCons.constant = 0;
            self.oneHeightCons.constant = 0;
            self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled = self.fiftyBtn.userInteractionEnabled = YES;
        });
        
    }
    
    
}

- (void)showTenView:(NSArray *)infoArr1 {
    NSMutableArray* infoArr = [NSMutableArray array];
    for (YPGiftInfo* infoModel in infoArr1) {
        if (infoModel.giftId != infoModel.ticketId) {
            [infoArr addObject:infoModel];
        }else{
            if (infoModel.giftNum>0) {
                 self.renqipiaoNumLabel.hidden = NO;
                self.renqipiaoNumLabel.text = [NSString stringWithFormat:@"额外获得魔法碎片x%ld",infoModel.giftNum];
            }else{
                self.renqipiaoNumLabel.hidden = YES;
            }
            
        }
    }
    if (infoArr.count > 0) {
        YPGiftInfo *info = [infoArr firstObject];
        [self.tenImg1 qn_setImageImageWithUrl:info.picUrl placeholderImage:nil type:ImageTypeRoomGift];
        self.tenText1.text = [NSString stringWithFormat:@"%@x%zd",info.giftName,info.giftNum];
        self.v1.hidden = false;
        self.v2.hidden = self.v3.hidden = self.v4.hidden = self.v5.hidden = self.v6.hidden = YES;
        if (infoArr.count > 1) {
            YPGiftInfo *info = infoArr[1];
            [self.tenImg2 qn_setImageImageWithUrl:info.picUrl placeholderImage:nil type:ImageTypeRoomGift];
            self.tenText2.text = [NSString stringWithFormat:@"%@x%zd",info.giftName,info.giftNum];
            self.v1.hidden = self.v2.hidden = false;
            self.v3.hidden = self.v4.hidden = self.v5.hidden = self.v6.hidden = YES;
            if (infoArr.count > 2) {
                YPGiftInfo *info = infoArr[2];
                [self.tenImg3 qn_setImageImageWithUrl:info.picUrl placeholderImage:nil type:ImageTypeRoomGift];
                self.tenText3.text = [NSString stringWithFormat:@"%@x%zd",info.giftName,info.giftNum];
                self.v1.hidden = self.v2.hidden = self.v3.hidden = false;
                self.v4.hidden = self.v5.hidden = self.v6.hidden = YES;
                if (infoArr.count > 3) {
                    YPGiftInfo *info = infoArr[3];
                    [self.tenImg4 qn_setImageImageWithUrl:info.picUrl placeholderImage:nil type:ImageTypeRoomGift];
                    self.tenText4.text = [NSString stringWithFormat:@"%@x%zd",info.giftName,info.giftNum];
                    self.v1.hidden = self.v2.hidden = self.v3.hidden = self.v4.hidden = false;
                    self.v5.hidden = self.v6.hidden = YES;
                    if (infoArr.count > 4) {
                        YPGiftInfo *info = infoArr[4];
                        [self.tenImg5 qn_setImageImageWithUrl:info.picUrl placeholderImage:nil type:ImageTypeRoomGift];
                        self.tenText5.text = [NSString stringWithFormat:@"%@x%zd",info.giftName,info.giftNum];
                        self.v1.hidden = self.v2.hidden = self.v3.hidden = self.v4.hidden = self.v5.hidden = false;
                        self.v6.hidden = YES;
                        if (infoArr.count > 5) {
                            YPGiftInfo *info = infoArr[5];
                            [self.tenImg6 qn_setImageImageWithUrl:info.picUrl placeholderImage:nil type:ImageTypeRoomGift];
                            self.tenText6.text = [NSString stringWithFormat:@"%@x%zd",info.giftName,info.giftNum];
                            self.v1.hidden = self.v2.hidden = self.v3.hidden = self.v4.hidden = self.v5.hidden = self.v6.hidden = false;
                        }
                    }
                }
            }
        }
        
        if (self.type == XDBEggPriceTypeFifty) {
//            [self.onceAgainBtn setTitle:@"再砸50次" forState:UIControlStateNormal];
        }else if (self.type == XDBEggPriceTypeTen) {
//            [self.onceAgainBtn setTitle:@"再砸10次" forState:UIControlStateNormal];
        }
        
        
        
        
        
        if (self.setAnimationBtn.selected && !self.isAgainRequesting) {
            
//            NSArray * imgsArr = @[
//                                  [UIImage imageNamed:@"yp_room_egg_zd0"],
//                                  [UIImage imageNamed:@"yp_room_egg_zd1"],
//                                  [UIImage imageNamed:@"yp_room_egg_zd2"],
//                                  [UIImage imageNamed:@"yp_room_egg_zd3"],
//                                  [UIImage imageNamed:@"yp_room_egg_zd4"],
//                                  [UIImage imageNamed:@"yp_room_egg_zd5"],
//                                  ];
//            // 设置动画图片数组
//            [self.boxImg setAnimationImages:imgsArr];
//            [self.boxImg setAnimationDuration:1];
//            self.boxImg.animationRepeatCount = 1;
//            [self.boxImg setImage:[UIImage imageNamed:@"yp_room_egg_zd5"]];
//            [self.boxImg startAnimating];
            
            dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                
                self.tenView.hidden = false;
                [UIView animateWithDuration:0.3 animations:^{
                    self.tenView.transform = CGAffineTransformMakeScale(1, 1);
                } completion:nil];
            });
            
        }else{
            self.tenView.hidden = false;
            [UIView animateWithDuration:0.3 animations:^{
                self.tenView.transform = CGAffineTransformMakeScale(1, 1);
            } completion:nil];
        }
        
        
        if (self.isAgainRequesting) {
            self.isAgainRequesting = NO;
            self.onceAgainBtn.userInteractionEnabled = YES;
        }
        
        
        
    }
}


-(void)remainConchNum:(NSInteger)num conchNum:(NSInteger)conchNum{
    //设置海螺剩余次数
//    NSInteger conchNumber = [self.pickConchNumLabel.text integerValue];
//    if (conchNumber - num<=0) {
//          self.pickConchNumLabel.text = [NSString stringWithFormat:@"%ld",conchNumber];
//    }else{
//          self.pickConchNumLabel.text = [NSString stringWithFormat:@"%ld",conchNumber -num];
//    }
 
    
    self.pickConchNumLabel.text = [NSString stringWithFormat:@"%ld",conchNum];
    self.pickGiftNumLabel.text = self.pickConchNumLabel.text;
    GetCore(YPPurseCore).balanceInfo.conchNum = self.pickConchNumLabel.text;
     [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationUpdateConchNum" object:self.pickConchNumLabel.text];
    
}
- (void)onBalanceInfoUpdate:(YPBalanceInfo *)balanceInfo
{
    NSString *str2 = [NSString stringWithFormat:@"%@  充值",balanceInfo.goldNum];
    NSString *str3 = @"充值";
    NSMutableAttributedString *str = [[NSMutableAttributedString alloc] initWithString:str2];
    [str addAttribute:NSForegroundColorAttributeName value:UIColorHex(FFFFFF) range:[str2 rangeOfString:balanceInfo.goldNum]];
    [str addAttribute:NSForegroundColorAttributeName value:UIColorHex(3D95E3) range:[str2 rangeOfString:str3]];
    [self.moneyBtn setAttributedTitle:str forState:UIControlStateNormal];
    
    GetCore(YPPurseCore).balanceInfo.conchNum = balanceInfo.conchNum;
    self.pickConchNumLabel.text = balanceInfo.conchNum;
    self.pickGiftNumLabel.text =  self.pickConchNumLabel.text;
    
    
    
}
//MARK:- 捡海螺成功回调
- (void)userGiftPurseDrawSuccess:(id)data {
    
    self.setAnimationBtn.userInteractionEnabled = YES;

    if (data) {
        NSArray *dataArr = data;
        if (dataArr.count) {
             NSInteger conchNum = [data[@"conchNum"] integerValue];
            if (self.type == XDBEggPriceTypeFifty) {
                
               
                //查询哪个单价最高
                NSArray *infoArr = [NSArray yy_modelArrayWithClass:[YPGiftInfo class] json:data[@"giftList"]];
                NSInteger ticketId = [data[@"ticketId"] integerValue];
                
                [self remainConchNum:50 conchNum:conchNum];
                              for (YPGiftInfo* infoModel in infoArr) {
                                  infoModel.ticketId = ticketId;
                              }
                [self checkPriceWhoBigWith:infoArr];
                GetCore(YPPurseCore).balanceInfo.goldNum = [NSString stringWithFormat:@"%ld",[GetCore(YPPurseCore).balanceInfo.goldNum integerValue] - 20*50];

                [self configMoney];
                [self showTenView:infoArr];
                
            }else if (self.type == XDBEggPriceTypeTen){
                 [self remainConchNum:10 conchNum:conchNum];
                
                //查询哪个单价最高
                NSArray *infoArr = [NSArray yy_modelArrayWithClass:[YPGiftInfo class] json:data[@"giftList"]];
                NSInteger ticketId = [data[@"ticketId"] integerValue];
                for (YPGiftInfo* infoModel in infoArr) {
                    infoModel.ticketId = ticketId;
                }
                
                [self checkPriceWhoBigWith:infoArr];
                GetCore(YPPurseCore).balanceInfo.goldNum = [NSString stringWithFormat:@"%ld",[GetCore(YPPurseCore).balanceInfo.goldNum integerValue] - 200];

                [self configMoney];
                [self showTenView:infoArr];
                
            }else if (self.type == XDBEggPriceTypeOne){
//                YPGiftInfo *info = [YPGiftInfo yy_modelWithJSON:[dataArr firstObject]];
                
//                  NSArray *infoArr = [NSArray yy_modelArrayWithClass:[YPGiftInfo class] json:data[@"giftList"]];
//                NSInteger ticketId = [data[@"ticketId"] integerValue];
//                 NSMutableArray* selectArray = [NSMutableArray array];
//                  for (YPGiftInfo* infoModel in infoArr) {
//                      infoModel.ticketId = ticketId;
//                      if (infoModel.giftId != ticketId) {
//                          [selectArray addObject:infoModel];
//                      }
//                  }
//
//                YPGiftInfo *info= selectArray.firstObject;
//
//                if (info.goldPrice >= 520) {
//                    [GetCore(YPRoomCoreV2Help) sendLotteryMessageWithGiftInfo:info nick:self.info.nick];
//                }
//                GetCore(YPPurseCore).balanceInfo.goldNum = [NSString stringWithFormat:@"%ld",[GetCore(YPPurseCore).balanceInfo.goldNum integerValue] - 20];
//
//                [self configMoney];
//                [self showOneView:info];
                 [self remainConchNum:1 conchNum:conchNum];
                NSArray *infoArr = [NSArray yy_modelArrayWithClass:[YPGiftInfo class] json:data[@"giftList"]];
                NSInteger ticketId = [data[@"ticketId"] integerValue];
                for (YPGiftInfo* infoModel in infoArr) {
                    infoModel.ticketId = ticketId;
                }
                
                [self checkPriceWhoBigWith:infoArr];
                GetCore(YPPurseCore).balanceInfo.goldNum = [NSString stringWithFormat:@"%ld",[GetCore(YPPurseCore).balanceInfo.goldNum integerValue] - 200];

                [self configMoney];
                [self showTenView:infoArr];
            }
                

        }
    }
}

- (void)checkPriceWhoBigWith:(NSArray *)infoArr {
    dispatch_async(dispatch_get_global_queue(0, 0),^{
        //进入另一个线程
        NSMutableArray *arr = [NSMutableArray array];
        for (int i = 0; i < infoArr.count; i ++) {
            YPGiftInfo *info = infoArr[i];
            if (info.goldPrice >= 520) {
                [arr addObject:info];
            }
        }
        
//        __block float xmax = -MAXFLOAT;
//        YPGiftInfo *info2;
//        for (YPGiftInfo *info in arr) {
//            float x = info.goldPrice;
//            if (x > xmax) {
//                xmax = x;
//                info2 = info;
//            }
//        }
//
//        dispatch_async(dispatch_get_main_queue(),^{
//            //返回主线程
//            if (info2.goldPrice >= 520) {
//                [GetCore(YPRoomCoreV2Help) sendLotteryMessageWithGiftInfo:info2 nick:self.info.nick];
//            }
//        });
        for (YPGiftInfo *model in arr) {
             dispatch_async(dispatch_get_main_queue(),^{
                             //返回主线程
                             if (model.goldPrice >= 520) {
                                 [GetCore(YPRoomCoreV2Help) sendLotteryMessageWithGiftInfo:model nick:self.info.nick];
                             }
                         });
        }
       
    });
}

- (IBAction)tenDoneBtnClick:(UIButton *)sender {
    self.tenView.hidden = YES;
      self.tenView.transform = CGAffineTransformMakeScale(0, 0);
   
  
    self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled = self.fiftyBtn.userInteractionEnabled = YES;
    self.boxImg.image = [UIImage imageNamed:@"yp_room_egg_zd0"];
}

//中奖记录
- (IBAction)recordBtnAction:(id)sender {
    
    [YPEggRecordListView showRecord];
}

//本期奖池
- (IBAction)giftBtnAction:(id)sender {
    
    [YPEggGiftListView showGift];
    
}

//动画开关
- (IBAction)animationBtnAction:(id)sender {
    
    self.setAnimationBtn.selected = !self.setAnimationBtn.selected;
    
}

//50抽
- (IBAction)fiftyBtnAction:(id)sender {
    
    self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled = self.fiftyBtn.userInteractionEnabled = NO;
    
    self.type = XDBEggPriceTypeFifty;

    self.isType = YES;
    [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"4"];
    
    
}



//礼物里获取按钮
- (IBAction)pickGiftObtainBtnAction:(UIButton *)sender {
    if (self.closeRoomLotteryView) {
        self.closeRoomLotteryView();
        [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationCloseGame" object:nil userInfo:nil];
    }
}
//捡海螺1次
- (IBAction)pickOneGiftBtnAction:(UIButton *)sender {
    
//    self.oneoneBtn.userInteractionEnabled = self.tentenBtn.userInteractionEnabled =self.fiftyBtn.userInteractionEnabled =false;
//
//    self.isAgainRequesting = YES;
//    self.onceAgainBtn.userInteractionEnabled = NO;
//
//    if (self.type == XDBEggPriceTypeTen) {
//        self.isType = YES;
//        [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"2"];
//    }else if (self.type == XDBEggPriceTypeFifty){
//        self.isType = YES;
//        [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"4"];
//    }
    self.type = XDBEggPriceTypeOne;
    [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"1"];
    
}
//捡海螺10次  （onceAgainBtnAction50次）
- (IBAction)pickTenGiftBtnAction:(UIButton *)sender {
    self.type = XDBEggPriceTypeTen;
    [GetCore(YPRoomCoreV2Help) userGiftPurseDraw:@"2"];
}
//捡海螺里获取按钮
- (IBAction)pickConchObtainBtnAction:(id)sender
{
    if (self.closeRoomLotteryView) {
        self.closeRoomLotteryView();
        [[NSNotificationCenter defaultCenter] postNotificationName:@"NotificationCloseGame" object:nil userInfo:nil];
    }
}


@end
