//
//  SingleTextModifyViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSingleLineTextModifyVC.h"
#import "HJUserCoreHelp.h"
#import "UIView+Toast.h"


@interface HJSingleLineTextModifyVC ()
@property (weak, nonatomic) IBOutlet UITextField *editText;
@end

@implementation HJSingleLineTextModifyVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = self.pageTitle;
    UIBarButtonItem *buttonItem = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(XCAlertDone, nil) style:UIBarButtonItemStyleDone target:self action:@selector(onCompleteBtnClicked)];
    self.navigationItem.rightBarButtonItem = buttonItem;
    self.editText.text = self.defaultText;
    [self.editText addTarget:self action:@selector(textFieldDidChange:) forControlEvents:UIControlEventEditingChanged];
//    [self.editText becomeFirstResponder];
}

- (void)touchesBegan:(NSSet<UITouch *> *)touches withEvent:(UIEvent *)event {
    [self.editText resignFirstResponder];
}

- (void)dealloc {
}

-(void)textFieldDidChange :(UITextField *)theTextField {
    if (theTextField.text.length > self.maxLength) {
        theTextField.text = [theTextField.text substringToIndex:self.maxLength];
    }
}

- (void)onCompleteBtnClicked {
    NSLog(@"%@",self.editText.text);
    if (self.key.length == 0) {
        return;
    }
    
    NSString *text = [self.editText.text stringByTrimmingCharactersInSet:[NSCharacterSet whitespaceAndNewlineCharacterSet]];
    
    if ([text isEqualToString:self.defaultText]) {
        return;
    }
    
    if (text.length > 0) {
        NSMutableDictionary *userinfos = [NSMutableDictionary dictionary];
        [userinfos setObject:text  forKey:self.key];
        
        [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
        @weakify(self)
        [[GetCore(HJUserCoreHelp) saveUserInfoWithUserID:self.userID userInfos:userinfos] subscribeNext:^(id x) {
            [MBProgressHUD hideHUD];
            @strongify(self);
//            [GetCore(UserCore)getUserInfo:self.userID refresh:YES];
            [GetCore(HJUserCoreHelp)getUserInfo:self.userID refresh:YES success:^(UserInfo *info) {
                
            }];
            [self.navigationController popViewControllerAnimated:YES];
        } error:^(NSError *error) {
            [MBProgressHUD hideHUD];
            @strongify(self);
            [self.view makeToast:NSLocalizedString(XCHudNetError, nil) duration:3 position:CSToastPositionCenter];
        }];
    } else {
        self.editText.text = text;
        [self.view makeToast:NSLocalizedString(XCEditNickNoData, nil) duration:3 position:CSToastPositionCenter];
    }
    [self.editText resignFirstResponder];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}



/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
