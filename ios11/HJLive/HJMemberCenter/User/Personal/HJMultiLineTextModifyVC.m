//
//  HJMultiLineTextModifyVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJMultiLineTextModifyVC.h"
#import "HJUserCoreHelp.h"
#import "UIView+Toast.h"

@interface HJMultiLineTextModifyVC ()<UITextViewDelegate>
@property (weak, nonatomic) IBOutlet UITextView *multiLineField;
@property (weak, nonatomic) IBOutlet UILabel *textNumberLabel;
@end

@implementation HJMultiLineTextModifyVC

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.title = self.pageTitle;
    UIBarButtonItem *buttonItem = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(XCAlertDone, nil) style:UIBarButtonItemStyleDone target:self action:@selector(onCompleteBtnClicked)];
    self.navigationItem.rightBarButtonItem = buttonItem;
    self.multiLineField.text = self.defaultText;
    self.textNumberLabel.text = [NSString stringWithFormat:@"%ld", (long)self.maxLength - self.multiLineField.text.length];
    UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(keyboardHide:)];
    //设置成NO表示当前控件响应后会传播到其他控件上，默认为YES。
    tapGestureRecognizer.cancelsTouchesInView = NO;
    //将触摸事件添加到当前view
    [self.view addGestureRecognizer:tapGestureRecognizer];
    
    [self.multiLineField setDelegate:self];
//    [self.multiLineField becomeFirstResponder];
}

-(void)keyboardHide:(UITapGestureRecognizer*)tap
{
    [self.multiLineField resignFirstResponder];
}

- (void)textViewDidChange:(UITextView *)textView
{
    if (textView.text.length > self.maxLength) {
        textView.text = [textView.text substringToIndex:self.maxLength];
    }
    self.textNumberLabel.text = [NSString stringWithFormat:@"%lu", self.maxLength - self.multiLineField.text.length];
}

- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    if (textView.text.length == self.maxLength - 1 && range.location == self.maxLength - 1)
    {
        if (text.length == 2 || range.length == 2) {
            return NO;
        }else {
            return YES;
        }
    }else {
        return YES;
    }
}

- (void) onCompleteBtnClicked
{
    if (self.key.length == 0) {
        return;
    }
    
    NSString *text = self.multiLineField.text;
    
    if ([text isEqualToString:self.defaultText]) {
        return;
    }
    
    NSMutableDictionary *userinfos = [NSMutableDictionary dictionary];
    [userinfos setObject:text  forKey:self.key];
    
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    [[GetCore(HJUserCoreHelp) saveUserInfoWithUserID:self.userID userInfos:userinfos] subscribeNext:^(id x) {
        [MBProgressHUD hideHUD];
        [self.navigationController popViewControllerAnimated:YES];
    } error:^(NSError *error) {
        [MBProgressHUD hideHUD];
//        [self.view makeToast:@"请求失败，请检查网络" duration:3 position:CSToastPositionCenter];
    }];
    [self.multiLineField resignFirstResponder];
}

@end
