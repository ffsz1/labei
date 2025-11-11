//
//  FYRegularVC.m
//  XBD
//
//  Created by feiyin on 2019/11/1.
//

#import "FYRegularVC.h"

@interface FYRegularVC ()
@property (weak, nonatomic) IBOutlet UILabel *biliLabel;
@property (weak, nonatomic) IBOutlet UILabel *addWxLabel;

@end

@implementation FYRegularVC

- (void)viewDidLoad {
    [super viewDidLoad];
    //隐藏导航栏下划线
     UIImageView *tmp=[FYTool findNavBarBottomLine: self.navigationController.navigationBar];
       tmp.hidden=YES;
    //设置字符串部分颜色
    NSString* strStaus = @"10钻石= 1元（人民币)";
    NSString *str = [NSString stringWithFormat:@"2.提现比例：%@  ",strStaus];
    _biliLabel.attributedText = [FYTool setAttributedString:strStaus origStr:str color:@"#EE47B7"];
    
//    NSString* selectString = @"lieyuyin";
//    _addWxLabel.attributedText =[FYTool setAttributedString:selectString origStr:_addWxLabel.text color:@"#EE47B7"];

}







@end
