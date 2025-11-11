//
//  HJFeedbackViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFeedbackViewController.h"
#import "SZTextView.h"
#import "UIView+XCToast.h"
#import "HJFeedbackCore.h"
#import "HJFeedbackCoreClient.h"
#import "UIColor+UIColor_Hex.h"
#import "NSString+JXCheck.h"
@interface HJFeedbackViewController ()<HJFeedbackCoreClient>
@property (weak, nonatomic) IBOutlet SZTextView *contentTextView;
@property (weak, nonatomic) IBOutlet UITextField *contactField;
- (IBAction)onSubmitBtnClicked:(id)sender;
@end

@implementation HJFeedbackViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    AddCoreClient(HJFeedbackCoreClient, self);
    // Do any additional setup after loading the view.
    self.contentTextView.placeholder = NSLocalizedString(XCMeFeedBackPlaceHolder, nil);
    self.title = NSLocalizedString(XCMeFeedBackTitle, nil);
    
    UIBarButtonItem *rightBarButtonItem = [[UIBarButtonItem alloc]initWithTitle:NSLocalizedString(XCMeFeedBackConfirm, nil) style:UIBarButtonItemStylePlain target:self action:@selector(onSubmitBtnClicked:)];
    self.navigationItem.rightBarButtonItem = rightBarButtonItem;
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
}

- (void)dealloc
{
    RemoveCoreClient(HJFeedbackCoreClient, self);
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

- (IBAction)onSubmitBtnClicked:(id)sender {
    if (self.contentTextView.text.length == 0) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCMeFeedBackNoDataTip, nil) duration:3 position:YYToastPositionCenter];
        return;
    }
    
    if (![self.contactField.text jx_isMobilePhone]) {
        [UIView showToastInKeyWindow:NSLocalizedString(XCPurseBindingBindingPhoneCorectTip, nil) duration:3 position:YYToastPositionCenter];
        return;
    }
    
    
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    [GetCore(HJFeedbackCore) requestFeedback:self.contentTextView.text contact:self.contactField.text];
}

#pragma mark - FeedbackCoreClient
- (void)onRequestFeedbackSuccess
{
    [MBProgressHUD showSuccess:@"提交成功"];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)onRequestFeedbackFailth
{
    [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
}
@end
