//
//  FYSetBankInfoVC.m
//  XBD
//
//  Created by feiyin on 2019/11/20.
//

#import "FYSetBankInfoVC.h"

#import "FYBankInfoCell.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "PurseCore.h"
#import "HJPurseCoreClient.h"
#import "FYBankInfoListModel.h"


@interface FYSetBankInfoVC ()<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITextField *bankCodeTF;

@property (weak, nonatomic) IBOutlet UITextField *bankTF;

@property (weak, nonatomic) IBOutlet UITextField *realNameTF;

@property (weak, nonatomic) IBOutlet UITextField *subBankTF;


@property (weak, nonatomic) IBOutlet UIButton *confirmBtn;


@property (weak, nonatomic) IBOutlet UITableView *tableview;

@property (weak, nonatomic) IBOutlet UIImageView *bankLogo;
@property (weak, nonatomic) IBOutlet UILabel *bankTitleLabel;
@property (strong, nonatomic) NSArray* bankInfoArray;
@property (weak, nonatomic) IBOutlet UIImageView *arrowImage;
@property (strong, nonatomic) NSString* currentbankCode;
@property (strong, nonatomic) FYBankInfoListModel* banklistModel;

@end

@implementation FYSetBankInfoVC
- (void)viewDidLoad {
    [super viewDidLoad];
    self.tableview.delegate = self;
    self.tableview.dataSource = self;
     [self.tableview registerNib:[UINib nibWithNibName:@"FYBankInfoCell" bundle:nil] forCellReuseIdentifier:@"FYBankInfoCell"];
    self.tableview.separatorStyle = UITableViewCellSeparatorStyleNone;
    self.arrowImage.userInteractionEnabled = YES;
    self.tableview.layer.cornerRadius = 8;
    self.tableview.layer.masksToBounds = YES;
    self.tableview.bounces = NO;
    //隐藏导航栏下划线
    UIImageView *tmp=[FYTool findNavBarBottomLine: self.navigationController.navigationBar];
    tmp.hidden=YES;
    [self setUI];
    //数据请求
    
     AddCoreClient(HJPurseCoreClient, self);
    //获取银行卡信息列表
      [GetCore(PurseCore) requestBankList];
    
    
}

-(void)setUI{
    self.bankCodeTF.layer.cornerRadius = 20;
    self.bankCodeTF.layer.masksToBounds = YES;
    self.bankCodeTF.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
    self.bankCodeTF.layer.borderWidth = 1.0;
    self.bankCodeTF.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
    self.bankCodeTF.leftViewMode = UITextFieldViewModeAlways;
    
    self.bankTF.layer.cornerRadius = 20;
    self.bankTF.layer.masksToBounds = YES;
    self.bankTF.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
    self.bankTF.layer.borderWidth = 1.0;
    self.bankTF.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
    self.bankTF.leftViewMode = UITextFieldViewModeAlways;
    
    self.realNameTF.layer.cornerRadius = 20;
    self.realNameTF.layer.masksToBounds = YES;
    self.realNameTF.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
    self.realNameTF.layer.borderWidth = 1.0;
    self.realNameTF.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
    self.realNameTF.leftViewMode = UITextFieldViewModeAlways;
    
    self.subBankTF.layer.cornerRadius = 20;
    self.subBankTF.layer.masksToBounds = YES;
    self.subBankTF.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
    self.subBankTF.layer.borderWidth = 1.0;
    self.subBankTF.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
    self.subBankTF.leftViewMode = UITextFieldViewModeAlways;
    
    

}
//MARK: - Delegate
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.bankInfoArray.count;
}

-(UITableViewCell*)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
   
    
//    static NSString *ID = @"FYBankInfoCell";
//       UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:ID];
//       if (cell == nil) {
//           cell = [[UITableViewCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:ID];
//       }
     FYBankInfoCell *cell = [tableView dequeueReusableCellWithIdentifier:@"FYBankInfoCell" forIndexPath:indexPath];
    
       cell.selectionStyle = UITableViewCellSelectionStyleNone;
      FYBankInfoListModel* model = self.bankInfoArray[indexPath.row];
      
//       cell.imgView.image = [UIImage imageNamed:model.imageStr];
    
     cell.titleLabel.text = model.bankName;
       return cell;
}

-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 50;
}

-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
     FYBankInfoListModel* model = self.bankInfoArray[indexPath.row];
//    _bankLogo.image = [UIImage imageNamed:model.imageStr];
    _bankTitleLabel.text = model.bankName;
    _currentbankCode = model.openBankCode;
    model.bankCard = self.bankCodeTF.text;
    self.banklistModel = model;
    _tableview.hidden = YES;
    _bankTF.placeholder = @"";
    
}



//MARK: - 请求回调
//银行卡信息列表
- (void)onRequestBankInfoListSuccess:(NSArray *)list{
   
    self.bankInfoArray = list;
    [self.tableview reloadData];
}
- (void)onRequestBankInfoListFailth{
    
}
//绑定银行卡
- (void)onRequestWithDrawBankCardBindSuccess:(NSArray *)list{
  
    if (self.takeBankCashWayBlock) {
        self.takeBankCashWayBlock(self.banklistModel);
    }
    
    [self.navigationController popViewControllerAnimated:YES];
}
- (void)onRequestWithDrawBankCardBindFailth{
    
}


//确认按钮
- (IBAction)confirmBtnAction:(id)sender {
    //绑定银行卡
    NSMutableDictionary* params = [NSMutableDictionary dictionary];
     [params setObject:@([GetCore(HJAuthCoreHelp).getUid userIDValue]) forKey:@"uid"];
     [params setObject:@(3) forKey:@"type"];
     [params setObject:_currentbankCode forKey:@"openBankCode"];//银行代号
     [params setObject:self.bankCodeTF.text forKey:@"bankCard"];
     [params setObject:self.realNameTF.text forKey:@"bankCardName"];
     
      [GetCore(PurseCore) requestWithDrawBankCardBindWith:params];
    
    
}
//选择银行按钮
- (IBAction)selectBankAction:(id)sender {
    _tableview.hidden = !_tableview.hidden;
    [_bankCodeTF resignFirstResponder] ;
    [_realNameTF resignFirstResponder] ;
    [_subBankTF resignFirstResponder] ;
}

//MAKR: - set/get
-(NSArray*)bankInfoArray{
    if (!_bankInfoArray) {
        _bankInfoArray = [NSArray array];
        
//        NSArray* imageArr = @[@"fy_banklogo_gongshang",@"fy_banklogo_jianshe",@"fy_banklogo_nongye",@"fy_banklogo_zhongyin" ,@"fy_banklogo_youzheng",@"fy_banklogo_mensheng",@"fy_banklogo_pufa",@"fy_banklogo_zhaoshang"];
//        NSArray* nameArr = @[@"中国工商银行",@"中国建设银行",@"中国农业银行",@"中国银行",@"中国邮政储蓄银行",@"中国民生银行",@"中国浦发银行",@"中国招商银行"];
//
//
//        for (int i = 0; i< imageArr.count; i++) {
//            FYBankInfoModel* model = [[FYBankInfoModel alloc] init];
//            model.imageStr = imageArr[i];
//             model.nameStr = nameArr[i];
//             model.code = i;
//            [_bankInfoArray addObject:model];
//        }

    }
    return _bankInfoArray;
}


@end
