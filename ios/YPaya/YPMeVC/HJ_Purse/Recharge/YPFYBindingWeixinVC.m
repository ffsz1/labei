//
//  YPFYBindingWeixinVC.m
//  XBD
//
//  Created by feiyin on 2019/11/1.
//
#import "YPPurseCore.h"
#import "YPFYBindingWeixinVC.h"
#import "YPAuthCoreHelp.h"
#import "YPUserCoreHelp.h"
#import "HJPurseCoreClient.h"
#import "YPFYTool.h"

@interface YPFYBindingWeixinVC ()

@property (weak, nonatomic) IBOutlet UITextField *photoTF;//支付宝账户

@property (weak, nonatomic) IBOutlet UITextField *verificationTF;//真实名字

@property (weak, nonatomic) IBOutlet UITextField *nameTF;//备用

@property (weak, nonatomic) IBOutlet UIButton *confirmBtn;


@end

@implementation YPFYBindingWeixinVC

- (void)viewDidLoad {
    [super viewDidLoad];
    AddCoreClient(HJPurseCoreClient, self);
    //隐藏导航栏下划线
    UIImageView *tmp=[YPFYTool findNavBarBottomLine: self.navigationController.navigationBar];
    tmp.hidden=YES;
    [self setUI];
}

-(void)setUI{
    self.photoTF.layer.cornerRadius = 20;
    self.photoTF.layer.masksToBounds = YES;
    self.photoTF.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
    self.photoTF.layer.borderWidth = 1.0;
    self.photoTF.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
    self.photoTF.leftViewMode = UITextFieldViewModeAlways;
    
    self.verificationTF.layer.cornerRadius = 20;
    self.verificationTF.layer.masksToBounds = YES;
    self.verificationTF.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
    self.verificationTF.layer.borderWidth = 1.0;
    self.verificationTF.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
    self.verificationTF.leftViewMode = UITextFieldViewModeAlways;
    
    self.nameTF.layer.cornerRadius = 20;
    self.nameTF.layer.masksToBounds = YES;
    self.nameTF.layer.borderColor = [UIColor colorWithHexString:@"CCCCCC"].CGColor;
    self.nameTF.layer.borderWidth = 1.0;
    self.nameTF.leftView = [[UIView alloc]initWithFrame:CGRectMake(0, 0, 16, 0)];
    self.nameTF.leftViewMode = UITextFieldViewModeAlways;
    

}

//提交支付宝信息接口
-(void)succUserWithDrawBound:(id)data{
    [MBProgressHUD showMessage:@"支付宝信息提交成功~"];

    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(2.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        if (_selectZfbBlock) {
            NSDictionary* dict = (NSDictionary*)data;
            _selectZfbBlock(dict[@"alipayAccount"],dict[@"alipayAccountName"]);
        }
        [self.navigationController popViewControllerAnimated:YES];
    });
    
}
-(void)failUserWithDrawBound:(NSNumber *)code message:(NSString*)message{
//     [MBProgressHUD showMessage:message];
}



//确认按钮
- (IBAction)confirmBtnAction:(id)sender {
//     [GetCore(YPPurseCore) requestGetUserRealNameStatus:GetCore(XBDAuthCore).getUid.userIDValue type:@"cash"];
    [ GetCore(YPPurseCore) requestPostWithDrawBound:[GetCore(YPAuthCoreHelp).getUid userIDValue] code:@"" aliPayAccount:_photoTF.text aliPayAccountName:_verificationTF.text];
}






@end
