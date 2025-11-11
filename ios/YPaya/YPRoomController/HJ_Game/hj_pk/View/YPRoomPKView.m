//
//  YPRoomPKView.m
//  HJLive
//
//  Created by apple on 2019/6/18.
//

#import "YPRoomPKView.h"
#import "YPRoomPKSureView.h"
#import "YPRoomPKRecordModelView.h"

#import "YPRoomPKGiftModel.h"
#import "YPRoomPKProbability.h"

#import "YPHttpRequestHelper+PK.h"
#import "UIButton+WebCache.h"

#import "YPPurseCore.h"


#define XBDRoomAnimationTime 0.5

@interface YPRoomPKView ()
@property (weak, nonatomic) IBOutlet UIButton *closeBtn;

@property (weak, nonatomic) IBOutlet UIImageView *levelLogoImageView;
@property (weak, nonatomic) IBOutlet UIButton *lowBtn;
@property (weak, nonatomic) IBOutlet UIButton *midBtn;
@property (weak, nonatomic) IBOutlet UIButton *highBtn;

@property (weak, nonatomic) IBOutlet UIButton *pkBtn1;
@property (weak, nonatomic) IBOutlet UIButton *pkBtn2;
@property (weak, nonatomic) IBOutlet UIButton *pkBtn3;

@property (weak, nonatomic) IBOutlet UILabel *timesLabel;

@property (weak, nonatomic) IBOutlet UIButton *giftBtn1;
@property (weak, nonatomic) IBOutlet UIButton *giftBtn2;
@property (weak, nonatomic) IBOutlet UIButton *giftBtn3;

@property (weak, nonatomic) IBOutlet UILabel *priceLabel1;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel2;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel3;

@property (weak, nonatomic) IBOutlet UILabel *numLabel1;
@property (weak, nonatomic) IBOutlet UILabel *numLabel2;
@property (weak, nonatomic) IBOutlet UILabel *numLabel3;
@property (weak, nonatomic) IBOutlet UIView *sendPkView;
@property (weak, nonatomic) IBOutlet UIView *failTipView;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *centerX_level;

@property (strong,nonatomic) YPRoomPKSureView *pkSureView;
@property (strong,nonatomic) YPRoomPKRecordModelView *recordView;

@property (assign,nonatomic) XBDRoomPKType type;

@property (nonatomic,strong) NSArray *giftArr;
@property (nonatomic,strong) NSArray *probabilityArr;



@end

@implementation YPRoomPKView

+ (void)show
{
    [YPRoomPKView loadXib:XBDRoomPKTypeSend joinModel:nil];
}

+ (void)showSurePKView:(YPRoomPKJoinModel *)joinModel
{
    [YPRoomPKView loadXib:XBDRoomPKTypeSure joinModel:joinModel];
}

+ (void)showFailView
{
    [YPRoomPKView loadXib:XBDRoomPKTypeFail joinModel:nil];
}

+ (void)loadXib:(XBDRoomPKType)type joinModel:(YPRoomPKJoinModel *)joinModel
{
    YPRoomPKView *shareView = [[NSBundle mainBundle]loadNibNamed:@"YPRoomPKView" owner:self options:nil].lastObject;
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];

    shareView.alpha = 0;
    shareView.type = type;
    
    if (joinModel) {
        shareView.pkSureView.joinModel = joinModel;
    }


    [UIView animateWithDuration:XBDRoomAnimationTime animations:^{
        shareView.alpha = 1;
    }];
}

- (instancetype)initWithCoder:(NSCoder *)aDecoder
{
    self = [super initWithCoder:aDecoder];
    if (self) {
//        [self getProbabilityData];
    }
    return self;
}


- (IBAction)closeBtnAction:(id)sender {
    
    [UIView animateWithDuration:XBDRoomAnimationTime animations:^{
        
        self.alpha = 0;
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
}

//低
- (IBAction)lowBtnAction:(id)sender {
    
    if (self.lowBtn.selected) {
        return;
    }
    
    self.lowBtn.selected = YES;
    self.midBtn.selected = NO;
    self.highBtn.selected = NO;
    self.centerX_level.constant = -32;
    
    [self getGiftData];
}

//中
- (IBAction)midBtnAction:(id)sender {
    
    if (self.midBtn.selected) {
        return;
    }
    
    self.lowBtn.selected = NO;
    self.midBtn.selected = YES;
    self.highBtn.selected = NO;
    self.centerX_level.constant = 7;
    
    [self getGiftData];
}

//高
- (IBAction)highBtnAction:(id)sender {
    
    if (self.highBtn.selected) {
        return;
    }
    
    self.lowBtn.selected = NO;
    self.midBtn.selected = NO;
    self.highBtn.selected = YES;
    self.centerX_level.constant = 47;
    
    [self getGiftData];
}

//猜拳记录
- (IBAction)recordBtnAction:(id)sender {
    
    self.sendPkView.hidden = YES;
    self.recordView.hidden = NO;
    self.closeBtn.hidden = YES;
}

//石头
- (IBAction)pkBtnAction1:(id)sender {
    
    self.pkBtn1.selected = YES;
    self.pkBtn2.selected = NO;
    self.pkBtn3.selected = NO;
    
}

//剪刀
- (IBAction)pkBtnAction2:(id)sender {
    
    self.pkBtn1.selected = NO;
    self.pkBtn2.selected = YES;
    self.pkBtn3.selected = NO;
}

//布
- (IBAction)pkBtnAction3:(id)sender {
    
    self.pkBtn1.selected = NO;
    self.pkBtn2.selected = NO;
    self.pkBtn3.selected = YES;
}

- (IBAction)giftBtnAction:(id)sender {
    
    self.giftBtn1.selected = YES;
    self.giftBtn2.selected = NO;
    self.giftBtn3.selected = NO;
    
}

- (IBAction)giftBtnAction2:(id)sender {
    
    self.giftBtn1.selected = NO;
    self.giftBtn2.selected = YES;
    self.giftBtn3.selected = NO;
}

- (IBAction)giftBtnAction3:(id)sender {
    
    self.giftBtn1.selected = NO;
    self.giftBtn2.selected = NO;
    self.giftBtn3.selected = YES;
}

//确定
- (IBAction)surebtnAction:(id)sender {
    
    if (self.giftArr.count<3) {
        return;
    }
    
    NSString * probability = @"3";
    if (self.midBtn.selected) probability = @"2";
    if (self.highBtn.selected) probability = @"1";
    
    NSInteger choose = 2;
    if (self.pkBtn2.selected) choose = 1;
    if (self.pkBtn3.selected) choose = 3;
    
    YPRoomPKGiftModel *giftModel = self.giftArr[0];
    if (self.giftBtn2.selected) giftModel = self.giftArr[1];
    if (self.giftBtn3.selected) giftModel = self.giftArr[2];

    [MBProgressHUD showMessage:@"发起猜拳中..."];
    
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper pk_confirmPk:probability choose:choose giftId:giftModel.giftId giftNum:giftModel.giftNum success:^(id  _Nonnull data) {
        
        [MBProgressHUD hideHUD];
        [weakSelf closeBtnAction:nil];
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        
        [MBProgressHUD showError:message];
        
    }];
    
    [self closeBtnAction:nil];
}
- (IBAction)retryBtnAction:(id)sender {
    
    self.failTipView.hidden = YES;
    self.closeBtn.hidden = NO;
    self.type = XBDRoomPKTypeSend;
}

#pragma mark - setter/getter
- (void)setType:(XBDRoomPKType)type
{
    _type = type;
    
    if (type == XBDRoomPKTypeSure) {
        self.pkSureView.hidden = NO;
        self.closeBtn.hidden = YES;
        self.sendPkView.hidden = YES;
        
    }else if (type == XBDRoomPKTypeSend){
        self.sendPkView.hidden = NO;
        [self getProbabilityData];
    }else if (type == XBDRoomPKTypeFail){
        self.failTipView.hidden = NO;
        self.sendPkView.hidden = YES;
        self.closeBtn.hidden = YES;
    }
    
}

- (YPRoomPKSureView *)pkSureView
{
    if (!_pkSureView) {
        _pkSureView = [[NSBundle mainBundle]loadNibNamed:@"YPRoomPKSureView" owner:self options:nil].lastObject;
        _pkSureView.frame = CGRectMake(0, 0, XC_SCREE_W-30, 350);
        _pkSureView.center = self.center;
        
        __weak typeof(self)weakSelf = self;
        _pkSureView.closeBlock = ^{
            [weakSelf closeBtnAction:nil];
        };
        
        [self addSubview:_pkSureView];
    }
    return _pkSureView;
}

- (YPRoomPKRecordModelView *)recordView
{
    if (!_recordView) {
        _recordView = [[NSBundle mainBundle]loadNibNamed:@"YPRoomPKRecordModelView" owner:self options:nil].lastObject;
        _recordView.frame = CGRectMake(0, 0, XC_SCREE_W-30, 415);
        _recordView.center = self.center;
        
        __weak typeof(self)weakSelf = self;
        _recordView.closeBlock = ^{
            [weakSelf closeBtnAction:nil];
        };
        
        [self addSubview:_recordView];
    }
    return _recordView;
}

- (void)getProbabilityData
{
    
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper pk_getProbability:^(NSArray * _Nonnull arr) {
        
        weakSelf.probabilityArr = arr;
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        [MBProgressHUD showError:message];
    }];
}

- (void)getGiftData
{
    NSInteger idnex = 0;
    if (self.midBtn.selected) idnex =1;
    if (self.highBtn.selected) idnex =2;
    
    YPRoomPKProbability *model = _probabilityArr[idnex];
    __weak typeof(self)weakSelf = self;
    [YPHttpRequestHelper pk_getMoraInfo:model.probability success:^(NSArray* arr,int num,int moraTime) {
        weakSelf.giftArr = arr;
        weakSelf.timesLabel.text = [NSString stringWithFormat:@"%d/50",num];
        weakSelf.timeLabel.text = [NSString stringWithFormat:@"若无人参与,金币将在%d分钟内退还",moraTime];
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        
    }];
}

- (void)setProbabilityArr:(NSArray *)probabilityArr
{
    _probabilityArr = probabilityArr;
    if (probabilityArr.count>0) {
        
        YPRoomPKProbability *model = _probabilityArr[0];
        [self.lowBtn setTitle:model.name forState:UIControlStateNormal];
        
        self.lowBtn.hidden = NO;
        self.levelLogoImageView.hidden = NO;
        
        [self getGiftData];
    }
    
    if (probabilityArr.count>1) {
        self.midBtn.hidden = NO;
        YPRoomPKProbability *model = _probabilityArr[1];
        [self.midBtn setTitle:model.name forState:UIControlStateNormal];
    }
    
    if (probabilityArr.count>2) {
        self.highBtn.hidden = NO;
        YPRoomPKProbability *model = _probabilityArr[2];
        [self.highBtn setTitle:model.name forState:UIControlStateNormal];
    }
    
}

- (void)setGiftArr:(NSArray *)giftArr
{
    _giftArr = giftArr;
    
    if (giftArr.count>0) {
        YPRoomPKGiftModel *model = self.giftArr[0];
        [self.giftBtn1 sd_setImageWithURL:[NSURL URLWithString:model.giftUrl] forState:UIControlStateNormal];
        self.priceLabel1.text = [NSString stringWithFormat:@"%ld",(long)model.giftGold];
        self.numLabel1.text = [NSString stringWithFormat:@"X %ld",(long)model.giftNum];
    }
    
    if (giftArr.count>1) {
        YPRoomPKGiftModel *model = self.giftArr[1];
        [self.giftBtn2 sd_setImageWithURL:[NSURL URLWithString:model.giftUrl] forState:UIControlStateNormal];
        self.priceLabel2.text = [NSString stringWithFormat:@"%ld",(long)model.giftGold];
        self.numLabel2.text = [NSString stringWithFormat:@"X %ld",(long)model.giftNum];
    }
    
    if (giftArr.count>2) {
        YPRoomPKGiftModel *model = self.giftArr[2];
        [self.giftBtn3 sd_setImageWithURL:[NSURL URLWithString:model.giftUrl] forState:UIControlStateNormal];
        self.priceLabel3.text = [NSString stringWithFormat:@"%ld",(long)model.giftGold];
        self.numLabel3.text = [NSString stringWithFormat:@"X %ld",(long)model.giftNum];
    }
    
}

@end
