//
//  HJNotiFriendVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJNotiFriendVC.h"

#import "HJNotiFriendVC.h"
#import "HJWKWebViewController.h"
#import "HJNotiFriendToolView.h"
#import "HJNotiFriendTableViewCell.h"
#import "HJNotiFriendCore.h"
#import "HJImRoomCoreV2.h"
#import "HJNotiFriendCoreClient.h"
#import "HJImMessageCoreClient.h"
#import "HJAuthCoreHelp.h"
#import "HJUserCoreHelp.h"
#import "Attachment.h"
#import "HJMySpaceVC.h"
#import "HJUserViewControllerFactory.h"
#import "UIView+Toast.h"
#import "HJImMessageSendCoreClient.h"
#import "HJVersionCoreHelp.h"
#import "UIView+XCToast.h"

#import "HJIReachability.h"
#import "HJReachabilityCore.h"
#import "HJAuthCoreClient.h"
#import <IQKeyboardManager.h>
#import "UIView+XCToast.h"
#import "HJImMessageCore.h"
#import "HJMyTaskCore.h"
#import "HJImMessageSendCoreClient.h"

#import "UIImage+Utils.h"

#import "JXAuthorizationAlertHelper.h"

typedef NS_ENUM(NSUInteger, NotiFriendEnterRoom) {
    NotiFriendEnterRoomRequesting = 1,
    NotiFriendEnterRoomDidSuccess,
    NotiFriendEnterRoomFailed,
};


@interface HJNotiFriendVC ()<
UITableViewDataSource,
UITableViewDelegate,
UITextFieldDelegate,
HJNotiFriendCoreClient,
HJImMessageCoreClient,
HJImMessageSendCoreClient,
ReachabilityClient,
HJAuthCoreClient
>
@property (nonatomic, strong) HJNotiFriendToolView *notiFriendToolView;
@property (nonatomic, strong) UITableView *tableView;
@property (nonatomic, strong) NSMutableArray <HJIMMessage *> *chatMessage;
@property (nonatomic, strong) UserInfo *info;

@property (nonatomic, strong) UITapGestureRecognizer *tap;

@property (nonatomic, assign) NotiFriendEnterRoom enterRoomType;

@end

@implementation HJNotiFriendVC

#pragma mark - Life cycle
- (void)dealloc {
    [GetCore(HJNotiFriendCore) exitNotiFriendRoom];
    RemoveCoreClientAll(self);
}

- (void)viewDidLoad {
    [super viewDidLoad];
    
    AddCoreClient(ReachabilityClient, self);
    AddCoreClient(HJNotiFriendCoreClient, self);
    AddCoreClient(HJImMessageCoreClient, self);
    AddCoreClient(HJImMessageSendCoreClient, self);
    AddCoreClient(HJAuthCoreClient, self);

    [self initNav];
    [self initView];

    self.title = @"交友大厅";
    self.tableView.keyboardDismissMode = UIScrollViewKeyboardDismissModeOnDrag;
    
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];

    [IQKeyboardManager sharedManager].enableAutoToolbar = NO;
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;

}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    
    [IQKeyboardManager sharedManager].enableAutoToolbar = YES;
    [self.view endEditing:YES];
}

#pragma mark - Notification action
//当键盘出现
- (void)keyboardWillShow:(NSNotification *)notification {
    if (self.tabBarController.selectedIndex == 1) {
        //获取键盘的高度
        NSDictionary *userInfo = [notification userInfo];
        NSValue *value = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
        CGRect keyboardRect = [value CGRectValue];
        CGFloat height = keyboardRect.size.height;
        CGFloat duration = [notification.userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
        
        CGFloat changeHeight = iPhoneX ? -height+83 : -height+49;
        
        [self.notiFriendToolView mas_updateConstraints:^(MASConstraintMaker *make) {
            if (@available(iOS 11.0, *)) {
                make.bottom.equalTo(self.view.mas_safeAreaLayoutGuideBottom).offset(changeHeight);
            } else {
                // Fallback on earlier versions
                make.bottom.equalTo(self.view).offset(changeHeight);
            }
        }];

        [UIView animateWithDuration:duration animations:^{
            [self.view layoutIfNeeded];
        }];
        
        if (self.tap == nil) {
            __weak typeof(self) weakSelf = self;
            self.tap = [[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
                [weakSelf.view endEditing:YES];
            }];
            
            [[UIApplication sharedApplication].keyWindow addGestureRecognizer:self.tap];
        }
    }
}

//当键退出
- (void)keyboardWillHide:(NSNotification *)notification {
    if (self.tabBarController.selectedIndex == 1) {
        //获取键盘的高度
        NSDictionary *userInfo = [notification userInfo];
        NSValue *value = [userInfo objectForKey:UIKeyboardFrameEndUserInfoKey];
        CGFloat duration = [notification.userInfo[UIKeyboardAnimationDurationUserInfoKey] doubleValue];
        
        [self.notiFriendToolView mas_updateConstraints:^(MASConstraintMaker *make) {
            if (@available(iOS 11.0, *)) {
                make.bottom.equalTo(self.view.mas_safeAreaLayoutGuideBottom);
            } else {
                // Fallback on earlier versions
                make.bottom.equalTo(self.view);
            }
        }];
        
        [UIView animateWithDuration:duration animations:^{
            [self.view layoutIfNeeded];
        }];
        
        if (self.tap) {
            [[UIApplication sharedApplication].keyWindow removeGestureRecognizer:self.tap];
            self.tap = nil;
        }
    }
}

#pragma mark - <AuthCoreClient>
- (void)onKicked {
    if (self.chatMessage.count) {
        [self.chatMessage removeAllObjects];
        [self.tableView reloadData];
    }
    [GetCore(HJNotiFriendCore) exitNotiFriendRoom];
}

- (void)onLogout {
    if (self.chatMessage.count) {
        [self.chatMessage removeAllObjects];
        [self.tableView reloadData];
    }
    [GetCore(HJNotiFriendCore) exitNotiFriendRoom];
}

#pragma mark - <ImMessageSendCoreClient>
- (void)onSendPublicMessageDidSuccess:(NSString *)roomId {
    if (![roomId isEqualToString:GetCore(HJNotiFriendCore).getRoomid]) return;
    
    [GetCore(HJMyTaskCore) dutyFreshPublic];
}

#pragma mark - <NotiFriendCoreClient>
- (void)enterNotiFriendSuccess {
    self.enterRoomType = NotiFriendEnterRoomDidSuccess;
}

- (void)enterNotiFriendFail {
    if (self.chatMessage.count) {
        [self.chatMessage removeAllObjects];
        [self.tableView reloadData];
    }
    self.enterRoomType = NotiFriendEnterRoomFailed;
    NSString *msg = NSLocalizedString(XCDiscoverEnterRoomFailedTip, nil);
    self.tableView.hidden = YES;
    @weakify(self);
    [self.view showEmptyContentToastWithAttributeString:[[NSAttributedString alloc] initWithString:msg] tapBlock:^{
        @strongify(self);
        [self initCore];
    }];
}

- (void)onPublicMessagesDidUpdate:(NSArray<HJIMMessage *> *)messages {
    if (self.enterRoomType != NotiFriendEnterRoomDidSuccess) return;
    
    [self.chatMessage removeAllObjects];
    [self.chatMessage addObjectsFromArray:messages];
    [self.tableView reloadData];
    [self scroBottom];
}

#pragma mark - <ReachabilityClient>
- (void)reachabilityNetStateDidChange:(ReachabilityNetState)currentNetState {
    [self initCore];
}

#pragma mark - <UITableViewDataSource && UITableViewDelegate>
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    return self.chatMessage.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    HJNotiFriendTableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJNotiFriendTableViewCell" forIndexPath:indexPath];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    HJIMMessage *message = self.chatMessage[indexPath.row];
    cell.message = message;
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    [tableView deselectRowAtIndexPath:indexPath animated:false];
    HJIMMessage *message = self.chatMessage[indexPath.row];
    JXIMCustomObject *obj = message.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        if (attachment.first == Custom_Noti_Header_NotiFriendChat) {
            NSDictionary *dic = attachment.data;
            if ([dic containsObjectForKey:@"params"]) {
                NSDictionary *dicParams = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
                if ([dicParams containsObjectForKey:@"uid"]) {
                    
                    
                    HJMySpaceVC *vc = HJMeStoryBoard(@"HJMySpaceVC");
                    vc.userID = [dicParams[@"uid"] longLongValue];
                    
                    [self.navigationController pushViewController:vc animated:YES];
                    
                    
                    
                }
            }
        }
    }
    
    [self.view endEditing:YES];
}

- (CGFloat)tableView:(UITableView *)tableView estimatedHeightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return 108.5;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    return UITableViewAutomaticDimension;
}

#pragma mark - <UITextFieldDelegate>
- (BOOL)textFieldShouldReturn:(UITextField *)textField {
    if (textField.text.length > 0) {
//        if ([GetCore(HJImRoomCoreV2) isInRoom]) {
            if (textField.text.length > 45) {
                [MBProgressHUD showError:@"输入内容不能超过45个字" toView:[UIApplication sharedApplication].keyWindow];
            }
            else {
                @weakify(self);
                [self.notiFriendToolView startCountDownTime];
                [GetCore(HJImMessageCore) sensitiveWordRegexWithText:textField.text requestId:NSStringFromClass([self class]) finishBlock:^(BOOL isCanSend, NSString *msg) {
                    @strongify(self);
                    if (isCanSend) {
                        @weakify(self);
                        [GetCore(HJNotiFriendCore) sendChatMessage:self.notiFriendToolView.chatTextField.text withUserInfo:self.info success:^{
                            [GetCore(HJMyTaskCore) dutyFreshPublic];
                        } failure:^(NSInteger code, NSString *message) {
                            @strongify(self);
                            @weakify(self);
                            [JXAuthorizationAlertHelper showAuthorizationAlertWithViewController:self code:code message:message didTapActionHandler:^(UIViewController * _Nullable toViewController) {
                                @strongify(self);
                                if (toViewController) {
//                                    !self.didSelectedCellHandler ?: self.didSelectedCellHandler();
                                    // 动画同步处理
//                                    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                                        [self.navigationController pushViewController:toViewController animated:YES];
//                                    });
                                }
                            }];
                        }];
                        self.notiFriendToolView.chatTextField.text = @"";
                        [self.notiFriendToolView.chatTextField resignFirstResponder];
                    }
                    else {
//                        [MBProgressHUD showError:msg toView:[UIApplication sharedApplication].keyWindow];
                        
                        
                        [UIView showToastInKeyWindow:msg duration:1.0 position:(YYToastPosition)YYToastPositionAboveKeyboard];

                    }
                }];
            }
            
//        } else {
//            [MBProgressHUD showError:NSLocalizedString(XCDiscoverSendMsmFailedTip, nil) toView:[UIApplication sharedApplication].keyWindow];
//        }
    }
    return YES;
}

- (BOOL)textField:(UITextField *)textField shouldChangeCharactersInRange:(NSRange)range replacementString:(NSString *)string {
    if ([textField isEqual:self.notiFriendToolView.chatTextField]) {
        
        NSString *newString = [textField.text stringByReplacingCharactersInRange:range withString:string];
        if (newString.length > 45) {
            return NO;
        }
        return YES;
    }
    
    return YES;
}

#pragma mark - Event

- (void)jumpInviteShare {
    
}

- (void)showBanndMessage {
//    [UIView showToastInKeyWindow:@"亲，由于您的发言违反平台规定，现已被禁言，若有疑问可联系小熊语音客服。" duration:1 position:YYToastPositionCenter];
    
    [UIView showToastInKeyWindow:@"亲，由于您的发言违反平台规定，现已被禁言，若有疑问可联系小熊语音客服。" duration:1.0 position:(YYToastPosition)YYToastPositionAboveKeyboard];

}

- (void)checkQuestion {
    HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/friends/index.html",[HJHttpRequestHelper getHostUrl]];
    vc.url = [NSURL URLWithString:urlSting];
    [self.navigationController pushViewController:vc animated:YES];
}

- (void)scroBottom {
    if (self.chatMessage.count > 0) {
        [self.tableView scrollToRow:self.chatMessage.count - 1 inSection:0 atScrollPosition:UITableViewScrollPositionBottom animated:false];
    }
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)initCore {
    if (self.enterRoomType != NotiFriendEnterRoomRequesting && self.enterRoomType != NotiFriendEnterRoomDidSuccess) {
        self.enterRoomType = NotiFriendEnterRoomRequesting;
        [self.view hideToastView];
        self.tableView.hidden = NO;
        [GetCore(HJNotiFriendCore) enterNotiFriendRoom];
    }
}

- (void)updateData {
    NSString *userId = [GetCore(HJAuthCoreHelp) getUid];
    if (self.info.uid != userId.integerValue) {
        self.enterRoomType = 0;
    }
    self.info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[userId intValue]];
    [self initCore];
}

- (void)initNav {
//    UIBarButtonItem *rightBarButtonItem = [[UIBarButtonItem alloc]initWithImage:[[UIImage imageNamed:NSLocalizedString(XCDiscoverQuestionTitle, nil)] imageWithRenderingMode:
//                                                                                 UIImageRenderingModeAlwaysOriginal] style:UIBarButtonItemStylePlain target:self action:@selector(checkQuestion)];
//    self.navigationItem.rightBarButtonItem = rightBarButtonItem;
}

- (void)initView {
    
    //背景图
    UIImageView *background = [[UIImageView alloc] initWithImage:[UIImage imageWithColor:UIColorHex(7ABBFF)]];
    background.alpha = 0.1;
    [self.view addSubview:background];
    [background mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.view);
    }];
    
    [self.view addSubview:self.notiFriendToolView];
    
    [self.notiFriendToolView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.view);
        make.height.equalTo(@50);
        if (@available(iOS 11.0, *)) {
            make.bottom.equalTo(self.view.mas_safeAreaLayoutGuideBottom);
        } else {
            // Fallback on earlier versions
            make.bottom.equalTo(self.view);
        }
    }];
    
    self.tableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.tableView];
    [self.tableView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.equalTo(self.view);
        make.bottom.equalTo(self.notiFriendToolView.mas_top);
    }];
    
    self.tableView.tableHeaderView = [[UIView alloc] initWithFrame:CGRectMake(0, 0, kScreenWidth, 12)];

}

#pragma mark - Layout

#pragma mark - setters/getters
- (NSMutableArray<HJIMMessage *> *)chatMessage {
    if (!_chatMessage) {
        _chatMessage = [NSMutableArray array];
    }
    return _chatMessage;
}

- (UITableView *)tableView {
    if (!_tableView) {
        _tableView = [[UITableView alloc] init];
        _tableView.delegate = self;
        _tableView.dataSource = self;
        _tableView.separatorStyle = UITableViewCellSeparatorStyleNone;
        [_tableView registerNib:[UINib nibWithNibName:@"HJNotiFriendTableViewCell" bundle:nil] forCellReuseIdentifier:@"HJNotiFriendTableViewCell"];
    }
    return _tableView;
}

- (HJNotiFriendToolView *)notiFriendToolView {
    if (!_notiFriendToolView) {
        _notiFriendToolView = [[NSBundle mainBundle] loadNibNamed:@"HJNotiFriendToolView" owner:nil options:nil][0];
        _notiFriendToolView.chatTextField.delegate = self;
        __weak typeof(self) weakSlef = self;
        __weak typeof(_notiFriendToolView) weakToolView = _notiFriendToolView;
        [_notiFriendToolView setDidClickSendBtnActionBlock:^{
            
            [weakSlef textFieldShouldReturn:weakToolView.chatTextField];
        }];
    }
    return _notiFriendToolView;
}

@end
