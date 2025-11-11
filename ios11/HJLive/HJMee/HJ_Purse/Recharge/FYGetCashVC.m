//
//  FYGetCashVC.m
//  XBD
//
//  Created by feiyin on 2019/10/30.
// 提现页面

#import "FYGetCashVC.h"
#import "HJRechargeViewCollectionViewCell.h"
#import "BalanceInfo.h"
#import "RechargeInfo.h"
#import "PurseCore.h"
#import "HJPurseCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "FYGetCashInfoModel.h"
#import "FYCashBindingView.h"
#import "FYBindingWeixinVC.h"
#import "HJPurseViewControllerFactory.h"
#import "FYSetBankInfoVC.h"
#import "FYRealNameAuthView.h"
#import "HJWKWebViewController.h"
#import "FYBankInfoListModel.h"
#import "FYBankBindInfoModel.h"
#import "FYZfbBindInfoModel.h"
#import "HJZhiFuBaoAndBankInfoVC.h"
#import "HJGetCachSuccessView.h"
#import "HJAlerVerificationCodeView.h"
#import "HJAuthCoreClient.h"
#import "HJHttpRequestHelper+Purse.h"


@interface FYGetCashVC ()<UICollectionViewDelegate, UICollectionViewDataSource, HJPurseCoreClient, HJRechargeViewCellDelegate,UIGestureRecognizerDelegate>
@property (weak, nonatomic) IBOutlet UICollectionView *collectionView;
@property (weak, nonatomic) IBOutlet UIView *desView;

@property (weak, nonatomic) IBOutlet UIButton *immediatelyButton;
@property (weak, nonatomic) IBOutlet UIButton *cashWayButton;

@property (weak, nonatomic) IBOutlet UIView *wxView;

@property (weak, nonatomic) IBOutlet UIImageView *wxIcon;

@property (weak, nonatomic) IBOutlet UILabel *wxNameLabel;



@property (strong, nonatomic) NSArray *productList;
@property (strong, nonatomic) DrawExchangeModel *balanceInfo;
//钻石模型
@property (strong, nonatomic)  FYGetCashInfoModel *selectModel;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *cashBtn_bottom_layout;

@property (nonatomic, assign) BOOL first;

@property (nonatomic, assign) NSInteger failCode;
@property (nonatomic, strong) NSString* failMessage;
@property (nonatomic, assign) BOOL isBindBandCard;
@property (nonatomic, strong) NSString* alipayAccount;//支付宝账户
@property (nonatomic, strong) NSString* alipayAccountName;//支付宝姓名
@property (nonatomic, assign) BOOL isBindAlipay;
@property (assign, nonatomic)  NSInteger accountType;
@property (strong, nonatomic)  NSString* account;
@property (strong, nonatomic)  NSString* accountName;

//很抱歉审核期内暂时无法使用该功能
@end

@implementation FYGetCashVC
{
    NSInteger _index;
}
- (void)viewDidLoad {
    [super viewDidLoad];
    self.first = YES;
    AddCoreClient(HJPurseCoreClient, self);
      AddCoreClient(HJAuthCoreClient, self);
    self.balanceInfo = GetCore(PurseCore).drawExchangeModel;
    self.immediatelyButton.layer.cornerRadius = 8;
    self.immediatelyButton.layer.masksToBounds = YES;
    self.view.backgroundColor = [UIColor colorWithHexString:@"#FAFAFA"];
    self.collectionView.backgroundColor = [UIColor colorWithHexString:@"#FAFAFA"];
    self.selectModel = [[FYGetCashInfoModel alloc] init];
//    [self getWXInfo];
    _desView.hidden = NO;
    _wxView.hidden = YES;
   
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(drawCashNotificationAction:) name:@"drawCashNotification" object:nil];
    
}
//获取微信信息
-(void)getWXInfo{
    __weak typeof(self)weakSelf = self;
       [GetCore(HJUserCoreHelp) getUserInfo:[GetCore(HJAuthCoreHelp).getUid userIDValue] refresh:YES success:^(UserInfo *info) {
           if (info.hasWx) {
               weakSelf.wxView.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
               weakSelf.wxIcon.layer.cornerRadius = 20;
               weakSelf.wxIcon.layer.masksToBounds = YES;
               [weakSelf.wxIcon qn_setImageImageWithUrl:info.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
               weakSelf.wxNameLabel.text = info.nick;
           }
       }];
}


- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    NSString *uid = GetCore(HJAuthCoreHelp).getUid;
//    [GetCore(PurseCore) requestBalanceInfo:uid.userIDValue];
    //MARK: - 提现用户信息
    [GetCore(PurseCore) requestBalanceInfoWithDrawExchange:uid.userIDValue];
    
    [self initView];
    if (iPhoneX) {
        self.cashBtn_bottom_layout.constant = -15;
    }
    //判断实名认证
        [GetCore(PurseCore) requestGetUserRealNameStatus:GetCore(HJAuthCoreHelp).getUid.userIDValue type:@"cash"];
}
- (void)dealloc
{
    RemoveCoreClientAll(self);
}
- (void)initView
{

    UserID myUid = [GetCore(HJAuthCoreHelp) getUid].userIDValue;
//    UserInfo *userInfo = [GetCore(UserCore) getUserInfo:myUid refresh:NO];
//    @weakify(self);
    self.amountLabel.text = [NSString stringWithFormat:@"%.1lf", [self.balanceInfo.diamondNum doubleValue]];
    
    [GetCore(PurseCore) requestGetCashList:myUid];
    [self.collectionView registerNib:[UINib nibWithNibName:@"HJRechargeViewCollectionViewCell" bundle:nil] forCellWithReuseIdentifier:@"RechargeViewCollectionViewCell"];
    [_collectionView registerClass:[UICollectionReusableView class] forSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"HomeViewCollectionViewFooter"];
    
     UICollectionViewFlowLayout *_customLayout = [[UICollectionViewFlowLayout alloc] init]; // 自定义的布局对象
        _customLayout.scrollDirection = UICollectionViewScrollDirectionVertical;
     //设置headerView大小
   
    //设置footerView大小
     _customLayout.footerReferenceSize = CGSizeMake(kScreenWidth, 140);
    self.collectionView.collectionViewLayout = _customLayout;
}


//MARK: - CollectionViewDelegate & Datasource

#pragma mark - 视图内容
-(UICollectionReusableView *)collectionView:(UICollectionView *)collectionView viewForSupplementaryElementOfKind:(NSString *)kind atIndexPath:(NSIndexPath *)indexPath{
    // 视图添加到 UICollectionReusableView 创建的对象中
    if (kind == UICollectionElementKindSectionFooter) {
        // 底部试图
        UICollectionReusableView *headerView = [collectionView dequeueReusableSupplementaryViewOfKind:UICollectionElementKindSectionFooter withReuseIdentifier:@"HomeViewCollectionViewFooter" forIndexPath:indexPath];
        headerView.backgroundColor = [UIColor colorWithHexString:@"#FAFAFA"];
        
        
        UIImageView* iconImage = [[UIImageView alloc] init];
        iconImage.image = [UIImage imageNamed:@"fy_qianbao_up_icon"];
        iconImage.frame = CGRectMake(10, 15, 17, 17);
        UILabel* label1 = [[UILabel alloc] init];
        label1.text = @"温馨提示：";
        label1.textColor = [UIColor colorWithHexString:@"333333"];
        label1.font = [UIFont boldSystemFontOfSize:18];
        label1.frame = CGRectMake(CGRectGetMaxX(iconImage.frame)+6, 15, 150, 16);
        
        
        
        
        UILabel* label2 = [[UILabel alloc] init];
        label2.text = @"提现将在3个工作日内到账";
        label2.textColor = [UIColor colorWithHexString:@"333333"];
        label2.font = [UIFont systemFontOfSize:13];
        label2.frame = CGRectMake(14, CGRectGetMaxY(label1.frame)+6, kScreenWidth-20, 16);
        label2.numberOfLines = 0;
        
        UILabel* label3 = [[UILabel alloc] init];
        label3.text = @"逾期未到账请联系客服微信号：xxxxxx";
        label3.textColor = [UIColor colorWithHexString:@"333333"];
        label3.font = [UIFont systemFontOfSize:13];
        label3.frame = CGRectMake(14, CGRectGetMaxY(label2.frame)+6,kScreenWidth-60, 16);
        
        UILabel* label4 = [[UILabel alloc] init];
        label4.text = @"客服电话：18024561926";
        label4.textColor = [UIColor colorWithHexString:@"333333"];
        label4.font = [UIFont systemFontOfSize:13];
        label4.frame = CGRectMake(30, CGRectGetMaxY(label3.frame)+6,kScreenWidth-60, 16);
        
          [headerView addSubview:iconImage];
          [headerView addSubview:label1];
          [headerView addSubview:label2];
          [headerView addSubview:label3];
//          [headerView addSubview:label4];
        return headerView;
        
    }else {
        return nil;
    }
}



- (__kindof UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath {
    HJRechargeViewCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:@"RechargeViewCollectionViewCell" forIndexPath:indexPath];
    FYGetCashInfoModel *cashInfo = [self.productList safeObjectAtIndex:indexPath.row];
    cell.titleLable.text = cashInfo.cashProdName;
    cell.rmbLabel.text = [NSString stringWithFormat:@"¥%ld元", (long)cashInfo.cashNum];
    cell.goldlabel.text =  [NSString stringWithFormat:@"消耗%ld钻石", (long)cashInfo.diamondNum];
    
//    cell.iconImage.image = [UIImage imageNamed:@"fy_qianbao_zhuanshi"];
    
    
    
    cell.index = indexPath.row;
    cell.delegate = self;
    
//    cell.titleLable.hidden = YES;
    if (self.first) {
        cell.titleLable.hidden = YES;
    }
    else {
        cell.titleLable.hidden = NO;
    }
    cell.bgView.backgroundColor = [UIColor clearColor];
    if (indexPath.row == _index) {
        cell.bgImageView.image = [UIImage imageNamed:@"hj_zuanshi_bg_yes"];
        cell.goldlabel.textColor = [UIColor whiteColor];
        cell.rmbLabel.textColor = [UIColor whiteColor];
//        cell.bgView.layer.borderWidth = 1;
//        cell.bgView.layer.borderColor = [UIColor colorWithHexString:@"#A940FD"].CGColor;
    } else {
//        cell.bgView.layer.borderWidth = 1;
//         cell.bgView.layer.borderColor = [UIColor colorWithHexString:@"#FFFFFF"].CGColor;
        cell.bgImageView.image = [UIImage imageNamed:@"hj_recharge_icon_no"];
        cell.goldlabel.textColor = [UIColor colorWithHexString:@"#333333"];
        cell.rmbLabel.textColor = [UIColor colorWithHexString:@"#81C9FF"];
    }
    return cell;
}

- (NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section {
    return self.productList.count;
}

- (CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath {

    return CGSizeMake((XC_SCREE_W-(12*4)-6)/3,XC_SCREE_W/375.0 *109);
}



- (void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath {
    [collectionView deselectItemAtIndexPath:indexPath animated:YES];
    [self onRmbSelected:indexPath.item];
}

- (UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    return UIEdgeInsetsMake(0, 7.5, 0, 7.5);

}


#pragma mark - PurseCoreClient
//wxPublic/checkBindAliPay
//是否绑定支付宝信息成功回调
- (void)onRequestcheckBindAliPaySuccess:(id )data{
   
    FYZfbBindInfoModel* model = [FYZfbBindInfoModel yy_modelWithJSON:data];
    self.isBindAlipay = model.isBindAlipay;
    if (self.isBindAlipay) {
        self.isBindBandCard = NO;
        _desView.hidden = YES;
        _wxView.hidden = NO;
        self.alipayAccount = model.alipayAccount;
        self.alipayAccountName = model.alipayAccountName;
        self.wxNameLabel.text = [NSString stringWithFormat:@"提现信息：%@  %@",self.alipayAccountName,self.alipayAccount];
                         
    }else{
        FYBindingWeixinVC* vc =   (FYBindingWeixinVC *)[[HJPurseViewControllerFactory sharedFactory] instantiateFYBindingWeixinVC];
              vc.selectZfbBlock = ^(NSString * _Nonnull alipayAccount, NSString * _Nonnull alipayAccountName) {
                
                  _desView.hidden = YES;
                _wxView.hidden = NO;
                self.wxNameLabel.text = [NSString stringWithFormat:@"提现信息：%@  %@",alipayAccountName,alipayAccount];
                  self.alipayAccount = alipayAccount;
                  self.alipayAccountName = alipayAccountName;
                  self.isBindAlipay = YES;//标识已经绑定支付宝
                  self.isBindBandCard = NO;
                  
              };
              [self.navigationController pushViewController:vc animated:YES];
    }
    
   
}



//MARK: -获取已绑定的银行卡信息，当未绑卡时会返回16003错误；
- (void)onRequestWithDrawMyBankCardSuccess:(NSArray *)list{
    if (list.count>0) {
         FYBankBindInfoModel* model = list[0];
        
               _desView.hidden = YES;
               _wxView.hidden = NO;
            self.wxNameLabel.text = [NSString stringWithFormat:@"提现信息：%@  %@",model.bankCardName,model.bankCard];
    }else{
            _desView.hidden = NO;
            _wxView.hidden = YES;
        
    }
    self.isBindBandCard = YES;
    self.isBindAlipay = NO;

    
}
//MARK: -获取已绑定的银行卡信息失败
- (void)onRequestWithDrawMyBankCardFailth:(NSNumber *)resCode{
    
    if ( [resCode integerValue] == 16003) {
         FYSetBankInfoVC* vc =   (FYSetBankInfoVC *)[[HJPurseViewControllerFactory sharedFactory] instantiateFYSetBankInfoVC];
        vc.takeBankCashWayBlock = ^(FYBankInfoListModel * _Nonnull model) {
            _desView.hidden = YES;
            _wxView.hidden = NO;
            self.wxNameLabel.text = [NSString stringWithFormat:@"提现信息：%@  %@",model.bankName,model.bankCard];
            self.isBindBandCard = YES;
             self.isBindAlipay = NO;
        };
        
        [self.navigationController pushViewController:vc animated:YES];
    }
   
}

-(void)showCachSuccessView:(NSString*)cashNum{
    [HJGetCachSuccessView show:^(HJCachSuccessType type) {
        
    } cashNum:cashNum];
 
}



//MARK: -提现成功
- (void)onDrawCashSuccess:(DrawExchangeModel*)balanceInfo{
    [self showCachSuccessView:[NSString stringWithFormat:@"%ld",self.selectModel.cashNum]];
//     [MBProgressHUD showMessage:@"提现申请已提交成功！"];
//     self.balanceInfo = balanceInfo;
     NSString *uid = GetCore(HJAuthCoreHelp).getUid;
     [GetCore(PurseCore) requestBalanceInfoWithDrawExchange:uid.userIDValue];
    
     self.amountLabel.text = [NSString stringWithFormat:@"%.1lf", [self.balanceInfo.diamondNum doubleValue]];
    
}
//提现失败
- (void)onDrawCashFail{
//    [MBProgressHUD showMessage:@"提现失败"];
}

-(void)drawCashNotificationAction:(NSNotification*)notif{
    NSString* content = (NSString*)notif.object;
    if ([content isEqualToString:@"success"]) {
        [self showCachSuccessView:[NSString stringWithFormat:@"%ld",self.selectModel.cashNum]];
          NSString *uid = GetCore(HJAuthCoreHelp).getUid;
              [GetCore(PurseCore) requestBalanceInfoWithDrawExchange:uid.userIDValue];
             
              self.amountLabel.text = [NSString stringWithFormat:@"%.1lf", [self.balanceInfo.diamondNum doubleValue]];
    }else{
        [MBProgressHUD showMessage:content];
    }
   
}


//MARK: - 个人信息更新
- (void)onDrawExchangeInfoUpdate:(DrawExchangeModel *)balanceInfo
{
    self.balanceInfo = balanceInfo;
    self.amountLabel.text = [NSString stringWithFormat:@"%.1lf", [self.balanceInfo.diamondNum doubleValue]];
      GetCore(PurseCore).balanceInfo.diamondNum = balanceInfo.diamondNum;
//    [GetCore(PurseCore) queryFirst];
//    if (self.balanceInfo.hasWx) {
//        _desView.hidden = YES;
//        _wxView.hidden = NO;
//        _wxNameLabel.text = @"微信名字";
//    }else{
//        _desView.hidden = NO;
//        _wxView.hidden = YES;
//    }
    
    
}
//MARK: -提现列表
- (void)onRequestGetCashListSuccess:(NSArray *)list
{
    self.productList = list;
    if (list.count>0) {
        if (self.selectModel.cashNum>0) {
            
        }else{
              self.selectModel = [self.productList objectAtIndex:0];
        }
       
    }
    
    [self.collectionView reloadData];
}

//MARK: - 判断实名认证
-(void)succUserRealNameStatus:(BOOL)code{
    self.failCode = 200;

}
-(void)failUserRealNameStatus:(NSNumber *)code message:(NSString*)message{
    self.failCode = [code integerValue];
    self.failMessage = message;

   
 
    
    
}

//MARK: - privately
- (void)onRmbSelected:(NSInteger)index
{
    _index = index;
     self.selectModel = [self.productList objectAtIndex:index];
    [self.collectionView reloadData];
    
}

//MARK: - Action
// 提现方式
- (IBAction)getCashWayAction:(id)sender {
//    [self showSelectPayway];
   HJZhiFuBaoAndBankInfoVC* vc = [[HJZhiFuBaoAndBankInfoVC alloc] init];
    vc.cashPayWayBlock = ^(NSInteger accountType, NSString * _Nonnull account, NSString * _Nonnull accountName) {
        self.accountType = accountType;
        self.account = account;
        self.accountName=accountName;
        if (accountType ==1) {
            [self.cashWayButton setImage:[UIImage imageNamed:@"hj_my_zhifu_icon"] forState:UIControlStateNormal];
            [self.cashWayButton setTitle:@"" forState:UIControlStateNormal];
        }else if (accountType ==2){
            [self.cashWayButton setImage:[UIImage imageNamed:@"hj_my_bank_icon"] forState:UIControlStateNormal];
             [self.cashWayButton setTitle:@"" forState:UIControlStateNormal];
        }
        
    };
    [self.navigationController pushViewController:vc animated:YES];
    
}

-(void)showSelectPayway{
    [FYCashBindingView show:^(FYCachType type) {
        switch (type) {
                   case FYCashType:
                   {
                       //支付宝
                       [self jumpBindingzhifubao];
                   }
                       break;
                case FYBankCashType:
                  {
                      //银行卡
                      [self jumpSetBankInfo];
                  }
                      break;
                //取消
                   case FYCancelType:
                   {
                      
                       
                   }
                       break;
                default:
                               break;
        }
        
        
    }];
}

//MARK: -获取已绑定的银行卡信息，当未绑卡时会返回16003错误；
-(void)jumpSetBankInfo{
    
    [GetCore(PurseCore) requestWithDrawMyBankCardWith:[GetCore(HJAuthCoreHelp).getUid userIDValue]];
    
}
//MARK: -选择支付宝支付
-(void)jumpBindingzhifubao{
    
    [GetCore(PurseCore) requestPostCheckBindAliPay:[GetCore(HJAuthCoreHelp).getUid userIDValue]];
    
   
}

-(void)showRealNameAuth{
    [FYRealNameAuthView show:^(FYRealNameAuthType type) {
               switch (type) {
                          case FYConfirmType:
                          {
                            //jump 认证页面H5
                              [self loadWebView:@"/front/real_name/index.html"];
                          }
                              break;
                          case FYRealNameCancelType:
                          {
    
    
                          }
                              break;
                       default:
                        break;
               }
    
    
           }];
}


//立即提现
- (IBAction)immediatelyCashAction:(id)sender {
    

       if (self.failCode ==2501) {
           [self showRealNameAuth];

       }else if (self.failCode ==2511){
           //提示未绑定手机
            [MBProgressHUD showMessage:self.failMessage];
   //         [self showRealNameAuth];
       }else if(self.failCode ==2508){
           [MBProgressHUD showMessage:self.failMessage];
       }else if (self.failCode == 200){
           //已认证--就可立即提现 发请求给g后台
           
           if (_account.length<1) {
                [MBProgressHUD showError:@"请选择提现方式"];
               return;
           }
           
           [HJAlerVerificationCodeView show:^(HJAlerVerificationCodeType type,NSString* code) {
                        switch (type) {
                                case HJAlerVerificationCodeFirmType:
                                //兑换请求
                              
                                [GetCore(PurseCore) requestWithDrawCash:[GetCore(HJAuthCoreHelp).getUid userIDValue] pid:self.selectModel.cashProdId type:self.accountType account:self.account accountName:self.accountName smsCode:code];
                                break;
                            case HJAlerGetVeriCodeType:
                                //获取验证码
                                [self getValidationCode];
                                break;
                                case HJAlerVerificationCodeCancelType:
                                
                                break;
                                
                            default:
                                break;
                        }
                        
                        
                    } content:@"" nick:@"" isAttribute:NO];
           
           
           
           
           
   //        [GetCore(PurseCore) requestWithDrawCash:[GetCore(HJAuthCoreHelp).getUid userIDValue] pid:self.selectModel.cashProdId type:_accountType account:_account accountName:_accountName smsCode:@"smscode"];

       }else{
            [MBProgressHUD showMessage:self.failMessage];
       }
       
    
}

//获取验证码
-(void)getValidationCode{
       
        [HJHttpRequestHelper getMsmWithType:0 Success:^(BOOL succeed) {
                         [GetCore(HJAuthCoreHelp) openCountdown];
    
                     } failure:^(NSNumber *code, NSString *msg) {
    
                     }];
}

- (void)loadWebView:(NSString *)url
{
    NSString *urlSting = [NSString stringWithFormat:@"%@%@",[HJHttpRequestHelper getHostUrl],url];

    HJWKWebViewController *webView = [[HJWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:webView animated:YES];
}




@end
