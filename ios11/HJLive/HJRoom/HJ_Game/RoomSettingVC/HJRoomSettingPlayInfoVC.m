//
//  HJRoomSettingPlayInfoVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomSettingPlayInfoVC.h"
#import <IQTextView.h>
#import "HJImRoomCoreV2.h"
#import "HJRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"

#import <IQKeyboardManager.h>

@interface HJRoomSettingPlayInfoVC ()<
HJRoomCoreClient,
UITextViewDelegate
>
@property (weak, nonatomic) IBOutlet IQTextView *textView;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_TextView;

@end

@implementation HJRoomSettingPlayInfoVC

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [IQKeyboardManager sharedManager].enable = NO;
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
}

- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [IQKeyboardManager sharedManager].enable = YES;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.title = @"设置进入房间提示";
    self.textView.delegate = self;
    self.textView.placeholder = @"请输入内容";
    self.textView.text = GetCore(HJImRoomCoreV2).currentRoomInfo.playInfo;
    
    UIBarButtonItem *bar = [[UIBarButtonItem alloc] initWithTitle:@"保存" style:UIBarButtonItemStylePlain target:self action:@selector(doneClick)];
    self.navigationItem.rightBarButtonItem = bar;
    
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardWillChangeFrame:) name:UIKeyboardWillShowNotification object:nil];
    [[NSNotificationCenter defaultCenter] addObserver:self selector:@selector(keyboardDidHide:) name:UIKeyboardWillHideNotification object:nil];
    
    
    AddCoreClient(HJRoomCoreClient, self);

}

- (void)dealloc {
    RemoveCoreClientAll(self);
    [[NSNotificationCenter defaultCenter]removeObserver:self];
}

#pragma mark --键盘弹出
- (void)keyboardWillChangeFrame:(NSNotification *)notification{
    CGFloat duration = [notification.userInfo[UIKeyboardAnimationDurationUserInfoKey] floatValue];
    
    CGRect keyboardFrame = [notification.userInfo[UIKeyboardFrameEndUserInfoKey] CGRectValue];
    
    //计算控制器的view需要平移的距离
    CGFloat transformY = keyboardFrame.origin.y - self.view.frame.size.height;
    
    //执行动画
    [UIView animateWithDuration:duration animations:^{
        
        self.bottom_TextView.constant = - transformY;
        
    }];
}
#pragma mark --键盘收回
- (void)keyboardDidHide:(NSNotification *)notification{
    CGFloat duration = [notification.userInfo[UIKeyboardAnimationDurationUserInfoKey] floatValue];
    [UIView animateWithDuration:duration animations:^{
        self.bottom_TextView.constant = 0;
    }];
}




- (void)doneClick {
    
    if (!self.textView.text.length) {
        [MBProgressHUD showError:@"请输入内容"];
        return;
    }
    
    if (GetCore(HJImRoomCoreV2).myMember.is_creator) {
        [GetCore(HJRoomCoreV2Help) updateGameRoomInfo:GetCore(HJImRoomCoreV2).currentRoomInfo.uid backPic:nil title:nil roomTopic:GetCore(HJImRoomCoreV2).currentRoomInfo.roomDesc roomNotice:GetCore(HJImRoomCoreV2).currentRoomInfo.roomNotice roomPassword:nil tag:GetCore(HJImRoomCoreV2).currentRoomInfo.tagId playInfo:self.textView.text giftEffectSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch giftCardSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch publicChatSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch];
    } else if (GetCore(HJImRoomCoreV2).myMember.is_manager) {
        [GetCore(HJRoomCoreV2Help) managerUpdateGameRoomInfo:[GetCore(HJAuthCoreHelp)getUid].userIDValue backPic:nil title:nil roomTopic:GetCore(HJImRoomCoreV2).currentRoomInfo.roomDesc roomNotice:GetCore(HJImRoomCoreV2).currentRoomInfo.roomNotice roomPassword:nil tag:GetCore(HJImRoomCoreV2).currentRoomInfo.tagId playInfo:self.textView.text giftEffectSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch giftCardSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch publicChatSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch];
    }
}

- (void)onGameRoomInfoUpdateSuccessV2:(ChatRoomInfo *)info {
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)onGameRoomInfoUpdateSuccess:(ChatRoomInfo *)info isFromMessage:(BOOL)isFromMessage
{
    [self.navigationController popViewControllerAnimated:YES];
}



- (BOOL)textView:(UITextView *)textView shouldChangeTextInRange:(NSRange)range replacementText:(NSString *)text {
    
//    NSInteger maxLineNum = 15;
//    NSString *textString = @"Text";
//    CGSize fontSize = [textString sizeWithAttributes:@{NSFontAttributeName:textView.font}];
    
    NSString* newText = [textView.text stringByReplacingCharactersInRange:range withString:text];
//    CGSize tallerSize = CGSizeMake(textView.frame.size.width-15,textView.frame.size.height*2);
//
//    CGSize newSize = [newText boundingRectWithSize:tallerSize
//                                           options:NSStringDrawingUsesLineFragmentOrigin
//                                        attributes:@{NSFontAttributeName: textView.font}
//                                           context:nil].size;
//    NSInteger newLineNum = newSize.height / fontSize.height;
//    if ([text isEqualToString:@"\n"]) {
//        newLineNum += 1;
//    }
//
//    if ((newLineNum <= maxLineNum)
//        && newSize.width < textView.frame.size.width-15)
//    {
//        if (newText.length > 300) {
//            return NO;
//        }
//        return YES;
//    }else{
//        return NO;
//    }
    
    if (newText.length > 300) {
        return NO;
    }
    return YES;
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
