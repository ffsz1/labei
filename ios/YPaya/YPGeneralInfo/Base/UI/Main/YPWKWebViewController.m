//
//  YPWKWebViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPWKWebViewController.h"
#import <JavaScriptCore/JavaScriptCore.h>

#import "TYAlertController.h"
#import "HJShareCoreClient.h"
#import "NSObject+YYModel.h"
#import "YPRedPacketView.h"
#import "YPAuthCoreHelp.h"
#import "YYUtility.h"
#import "YPUserViewControllerFactory.h"
#import "YPMySpaceVC.h"
#import "YPPurseViewControllerFactory.h"
#import "YPRoomViewControllerCenter.h"
#import "YPVersionCoreHelp.h"
#import "YYHttpClient.h"
#import "NSData+JXEncryptAndDecrypt.h"
#import "YPEnvironmentDefine.h"
#import "YPUserCoreHelp.h"
#import "YPYouthAlterView.h"

#import "YPYYActionSheetViewController.h"
#import "YPImagePickerUtils.h"
#import "YPFileCore.h"
#import "HJFileCoreClient.h"
#import "YPWebResponseInfo.h"
#import <UIButton+WebCache.h>
#import "UIBarButtonItem+JXBase.h"
#import "YPShareView.h"

#define JX_WEB_AES_ENCRYPT_KEY @"E#R$T%Y^13579000" ///< AES加密Key
#define JX_WEB_AES_ENCRYPT_IV @"Q!W@E#R$T%Y^U&I*"  ///< AES加密Iv

#define JX_WEB_AES_DECRYPT_KEY @"E#R$T%Y^13579000" ///< AES解密Key
#define JX_WEB_AES_DECRYPT_IV @"Q!W@E#R$T%Y^U&I*"  ///< AES加密Iv

@interface YPWKWebViewController ()<WKNavigationDelegate,HJShareCoreClient, RedPacketViewDelegate,WKScriptMessageHandler,WKUIDelegate,UIImagePickerControllerDelegate,HJFileCoreClient,UINavigationControllerDelegate>

@property (strong, nonatomic) WKWebView *webview;
@property (strong, nonatomic) UIProgressView *progressView;
@property (strong, nonatomic) NSString *shareJsonData;

@property (strong, nonatomic) TYAlertController *shareAlertPanel;
@property (strong, nonatomic) YPRedPacketView *redPacketView;//界面
@property (strong, nonatomic) TYAlertController *redPacketAlertView;
@property (nonatomic, strong) WKUserContentController *userContentController;
@property (nonatomic, assign) BOOL webViewHadReload;

@property (nonatomic, assign) BOOL isSetupNavigationBarRightItem;

@end

@implementation YPWKWebViewController
#pragma mark - life cycle
- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    YPBlackStatusBar
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    [self.view addSubview:self.progressView];
//    if (@available(iOS 11.0, *)) {
//        self.navigationController.navigationBar.translucent = false;
//    }else{
//        [[[self.navigationController.navigationBar subviews] objectAtIndex:0] setAlpha:1];
//        self.navigationController.navigationBar.translucent = NO;
//    }
    
    if ([[self.url absoluteString] containsString:@"activity"]) {
        NSString *js = [NSString stringWithFormat:@"refreshWeb()"];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
        }];
    }
}

- (void)viewDidLoad {
    [super viewDidLoad];
    [self initWebView];
    AddCoreClient(HJShareCoreClient, self);
    AddCoreClient(HJFileCoreClient, self);
}

- (void)initNav {
    UIBarButtonItem *rightBarButtonItem = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"yp_inviteshare_share"] style:UIBarButtonItemStylePlain target:self action:@selector(showSharePanel)];
    self.navigationItem.rightBarButtonItem = rightBarButtonItem;
}

- (void)initWebView {
    UIBarButtonItem *leftBarButtonItem = [[UIBarButtonItem alloc]initWithImage:[UIImage imageNamed:@"yp_arrow_little_left"] style:UIBarButtonItemStylePlain target:self action:@selector(backButtonClick)];
    self.navigationItem.leftBarButtonItem = leftBarButtonItem;
    self.automaticallyAdjustsScrollViewInsets = NO;
    [self.view addSubview:self.webview];
    [self.webview mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.top.bottom.equalTo(self.view);
    }];
}

- (void)setUrl:(NSURL *)url{
    _url = url;
    if (self.url == nil) {
        NSString *urlSting = [NSString stringWithFormat:@"%@/front/links/links.html",[YPHttpRequestHelper getHostUrl]];
        self.url = [NSURL URLWithString:urlSting];
    }
    NSURLRequest *request = [NSURLRequest requestWithURL:self.url];
    [self.webview loadRequest:request];
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    NSSet *websiteDataTypes = [WKWebsiteDataStore allWebsiteDataTypes];
    NSDate *dateFrom = [NSDate dateWithTimeIntervalSince1970:0];
    [[WKWebsiteDataStore defaultDataStore] removeDataOfTypes:websiteDataTypes modifiedSince:dateFrom completionHandler:^{}];
    
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


#pragma mark - WKWebViewDelegate
//开始加载
- (void)webView:(WKWebView *)webView didStartProvisionalNavigation:(WKNavigation *)navigation {
    NSLog(@"开始加载网页");
    self.progressView.hidden = NO;
    self.progressView.transform = CGAffineTransformMakeScale(1.0f, 1.5f);
    [self.view bringSubviewToFront:self.progressView];
}

//加载完成
- (void)webView:(WKWebView *)webView didFinishNavigation:(WKNavigation *)navigation {
    //加载完成后隐藏progressView
    self.progressView.hidden = YES;
    [webView evaluateJavaScript:@"shareInfo()" completionHandler:^(id _Nullable response, NSError * _Nullable error) {
        if (error == nil) {
            if (response !=  nil) {
                self.shareJsonData = response;
                    [self initNav];
            }
        }
        NSLog(@"%@",response);
    }];
}

//捕抓打电话事件
- (void)webView:(WKWebView *)webView decidePolicyForNavigationAction:(WKNavigationAction *)navigationAction decisionHandler:(void (^)(WKNavigationActionPolicy))decisionHandler {
    NSURL *URL = navigationAction.request.URL;
    NSString *scheme = [URL scheme];
    if ([scheme isEqualToString:@"tel"]) {
        NSString *resourceSpecifier = [URL resourceSpecifier];
        NSString *callPhone = [NSString stringWithFormat:@"telprompt://%@", resourceSpecifier];
        /// 防止iOS 10及其之后，拨打电话系统弹出框延迟出现
        dispatch_async(dispatch_get_global_queue(0, 0), ^{
            [[UIApplication sharedApplication] openURL:[NSURL URLWithString:callPhone]];
        });
    }

    decisionHandler(WKNavigationActionPolicyAllow);
}



//加载失败
- (void)webView:(WKWebView *)webView didFailProvisionalNavigation:(WKNavigation *)navigation withError:(NSError *)error {
    //加载失败同样需要隐藏progressView
    self.progressView.hidden = YES;
}

#pragma mark - WKScriptMessageHandler
//h5交换
- (void)userContentController:(WKUserContentController *)userContentController didReceiveScriptMessage:(WKScriptMessage *)message {
    if ([message.name isEqualToString:@"openPurse"]) {//跳钱包页面
        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [self.navigationController pushViewController:vc animated:YES];
        
    }else if ([message.name isEqualToString:@"openChargePage"]) {//跳充值页
        UIViewController *vc = [[YPPurseViewControllerFactory sharedFactory] instantiateHJMyWalletVC];
        [self.navigationController pushViewController:vc animated:YES];
    }else if ([message.name isEqualToString:@"openPersonPage"]){//排行榜去个人页
        NSString *uid = [NSString stringWithFormat:@"%@",message.body];
        if (uid.length > 0) {
            
            
            YPMySpaceVC *vc = YPMeStoryBoard(@"YPMySpaceVC");
            vc.userID = uid.userIDValue;
            [self.navigationController pushViewController:vc animated:YES];
            
            
        }
        
    }else if ([message.name isEqualToString:@"openSharePage"]){//弹出分享界面
        [self showSharePanel];
        
    }else if ([message.name isEqualToString:@"openRoom"]){ //排行榜跳房间
        NSString *uid = [NSString stringWithFormat:@"%@",message.body];
        if (uid.length > 0) {
            [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomOwnerUid:uid.userIDValue succ:^(YPChatRoomInfo *roomInfo) {
                if (roomInfo !=nil) {
                    [[YPRoomViewControllerCenter defaultCenter]presentRoomViewWithRoomInfo:roomInfo];
                }
            } fail:^(NSString *errorMsg) {
                [MBProgressHUD showError:errorMsg];
            }];
        }
    }else if ([message.name isEqualToString:@"getDeviceId"]){//获取设备id
        NSString *js = [NSString stringWithFormat:@"getMessage(\"deviceId\",%@)",[YYUtility deviceUniqueIdentification]];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    }else if ([message.name isEqualToString:@"getUid"]){//获取uid
        NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
        NSString *js = [NSString stringWithFormat:@"getMessage(\"uid\",%@)",uid];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
        
    }else if ([message.name isEqualToString:@"getPosition"]){//ranking
        NSString *js = self.callBackJS;
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    } else if ([message.name isEqualToString:@"getTicket"]) { //获取ticket
        NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
        NSString *js = [NSString stringWithFormat:@"getMessage(\"ticket\",\"%@\")",ticket];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    } else if ([message.name isEqualToString:@"getCommonParams"]) {
        NSString *commonParams = [YYHttpClient sharedClient].basicParameterConstructor().yy_modelToJSONString;
        NSLog(@"%@",commonParams);
        NSString *js = [NSString stringWithFormat:@"getMessage(\"commonParams\",%@)",commonParams];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    }else if ([message.name isEqualToString:@"buildRequest"]) { /// Encrypt
        NSString *jsonString = [NSString stringWithFormat:@"%@",message.body];
        NSData *data = [jsonString dataUsingEncoding:NSUTF8StringEncoding];
        data = [data jx_AES256EncryptWithKey:[JX_WEB_AES_ENCRYPT_KEY dataUsingEncoding:NSUTF8StringEncoding] iv:[JX_WEB_AES_ENCRYPT_IV dataUsingEncoding:NSUTF8StringEncoding]];
        NSString *encryptString = [data base64EncodedString];
        NSString *js = [NSString stringWithFormat:@"getMessage(\"encryptString\",\"%@\")",encryptString];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    } else if ([message.name isEqualToString:@"extractResponse"]) { /// Decrypt
        NSString *encryptString = [NSString stringWithFormat:@"%@",message.body];
        NSData *data = [NSData dataWithBase64EncodedString:encryptString];
        data = [data jx_AES256DecryptWithKey:[JX_WEB_AES_DECRYPT_KEY dataUsingEncoding:NSUTF8StringEncoding] iv:[JX_WEB_AES_DECRYPT_IV dataUsingEncoding:NSUTF8StringEncoding]];
        NSString *decryptString = data.utf8String;
        NSString *js = [NSString stringWithFormat:@"getMessage(\"decryptString\",\"%@\")",decryptString];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    }else if ([message.name isEqualToString:@"getUserPhoneNumber"]) {
        NSString *userId = [GetCore(YPAuthCoreHelp) getUid];
        UserInfo *info = [GetCore(YPUserCoreHelp) getUserInfoInDB:[userId intValue]];
        
        NSString *phone = info.phone;
        if (phone.length<11) {
            phone = @"";
        }
        NSString *js = [NSString stringWithFormat:@"getMessage(\"phone\",%@)",phone];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    }else if ([message.name isEqualToString:@"requestImageChooser"]) {
        [self showAvatarController];
    } else if ([message.name isEqualToString:@"httpRequest"]) {
        @try {
            NSString *json = [NSString stringWithFormat:@"%@",message.body];
            NSDictionary *roomExtD = [JSONTools ll_dictionaryWithJSON:json];
            NSInteger requestMethod = [roomExtD[@"requestMethod"] integerValue];
            NSString *urlCon = roomExtD[@"urlController"];
            NSString *paramMapString = roomExtD[@"paramMapString"];
            NSMutableDictionary *paramDic = [NSMutableDictionary dictionaryWithDictionary:[JSONTools ll_dictionaryWithJSON:paramMapString]];
            NSString *uid = [GetCore(YPAuthCoreHelp)getUid];
            NSString *ticket = [GetCore(YPAuthCoreHelp)getTicket];
            [paramDic setObject:uid forKey:@"uid"];
            [paramDic setObject:ticket forKey:@"ticket"];
            
            if (requestMethod == 1) {
               [YPHttpRequestHelper POST:urlCon params:paramDic success:^(id data) {
                                [self callBackJSON:urlCon isRequestError:false bodyString:[(NSDictionary *)data yy_modelToJSONString]];
                            } failure:^(NSNumber *resCode, NSString *message) {
                                [self callBackJSON:urlCon isRequestError:YES bodyString:@""];
                            }];
            } else {
                [YPHttpRequestHelper GET:urlCon params:paramDic success:^(id data) {
                                   [self callBackJSON:urlCon isRequestError:false bodyString:[(NSDictionary *)data yy_modelToJSONString]];
                               } failure:^(NSNumber *resCode, NSString *message) {
                                   [self callBackJSON:urlCon isRequestError:YES bodyString:@""];
                               }];
            }
        } @catch (NSException *exception) {
            
        } @finally {
            
        }
        
    } else if ([message.name isEqualToString:@"setupNavigationBarRightItem"]) {
        NSString *jsonString = [NSString stringWithFormat:@"%@",message.body];
        YPWebResponseInfo *info = [YPWebResponseInfo yy_modelWithJSON:jsonString];
        [self setupNavigationBarRightItem:info];
    } else if ([message.name isEqualToString:@"closeWin"]) {
        [self.navigationController popViewControllerAnimated:YES];
    }
//    else if([message.name isEqualToString:@"openTeenagerModelCallback"]){
//        NSString *isOnYouthSet = [NSString stringWithFormat:@"isOnYouthSet%@",GetCore(YPAuthCoreHelp).getUid];
//        [[NSUserDefaults standardUserDefaults] setObject:@(YES) forKey:isOnYouthSet];
//        [[NSUserDefaults standardUserDefaults]synchronize];
//
//        [YPYouthAlterView updateYouthData];
//
//    }else if([message.name isEqualToString:@"closeTeengerModelCallback"]){
//        NSString *isOnYouthSet = [NSString stringWithFormat:@"isOnYouthSet%@",GetCore(YPAuthCoreHelp).getUid];
//        [[NSUserDefaults standardUserDefaults] setObject:@(NO) forKey:isOnYouthSet];
//        [[NSUserDefaults standardUserDefaults]synchronize];
//
//        [YPYouthAlterView updateYouthData];
//    }
}

- (void)setupNavigationBarRightItem:(YPWebResponseInfo *)info {
    self.isSetupNavigationBarRightItem = YES;
    NSString *imageURLString = info.data[@"imageUrl"];
    UIButton *button = [UIButton new];
    [button sd_setImageWithURL:[NSURL URLWithString:JX_STR_AVOID_nil(imageURLString)] forState:UIControlStateNormal];
    button.size = CGSizeMake(30, 30);
    button.imageView.contentMode = UIViewContentModeScaleAspectFit;
    @weakify(self);
    self.navigationItem.rightBarButtonItem = [UIBarButtonItem jx_barButtonItemWithCustomView:button actionBlock:^(id sender) {
        @strongify(self);
        [self notifyNavigationBarRightItemDidClicked];
    }];
}
- (void)notifyNavigationBarRightItemDidClicked {
    [self.webview evaluateJavaScript:@"onNavigationBarRightItemDidClicked()" completionHandler:^(id _Nullable response, NSError * _Nullable error) {
    }];
}

- (void)callBackJSON:(NSString *)urlCon isRequestError:(BOOL)isRequestError bodyString:(NSString *)bodyString {
    @try {
        NSMutableDictionary *dic = [NSMutableDictionary dictionary];
        dic[@"urlController"] = urlCon;
        dic[@"isRequestError"] = @(isRequestError);
        if (bodyString) {
            dic[@"bodyString"] = bodyString;
        } else {
            dic[@"bodyString"] = @"";
        }
        NSString *json = [dic yy_modelToJSONString];
        NSString *js = [NSString stringWithFormat:@"onHttpResponse(\"responseStr\",%@)",json];
        [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
            NSLog(@"%@",error);
        }];
    } @catch (NSException *exception) {
        
    } @finally {
    }
    
}

- (void)showAvatarController {
    YPYYActionSheetViewController *actionSheet = [YPImagePickerUtils showImagePickerSystemLibSheet:^{
    } title:nil view:self.view tailor:YES delegate:self];
    [actionSheet show];
}


#pragma mark - UIImagePickerControllerDelegate
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info
{
    __weak __typeof__(self) wself = self;
    [picker dismissViewControllerAnimated:YES completion:^{
        UIImage *selectedPhoto = [info objectForKey:UIImagePickerControllerEditedImage];
        if (selectedPhoto) {
            __strong __typeof (wself) sSelf = wself;
            if (picker.sourceType == UIImagePickerControllerSourceTypeCamera) {
                UIImageWriteToSavedPhotosAlbum(selectedPhoto, nil, nil, nil);
            }
            [GetCore(YPFileCore) qiNiuUploadImage:selectedPhoto uploadType:UploadImageTypeIDCard];
        }
    }];
}

- (void)didUploadIDCardImageSuccessUseQiNiu:(NSString *)key {
    
    NSString *url = [NSString stringWithFormat:@"%@/%@?imageslim",JX_IMAGE_HOST_URL,key];
    
    NSString *js = [NSString stringWithFormat:@"onImageChooserResult(\"imageUrl\",\"%@\")",url];
    
    [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
    }];
}

- (void)didUploadIDCardImageFailUseQiNiu:(NSString *)message {
    NSString *url = @"";
    NSString *js = [NSString stringWithFormat:@"onImageChooserResult(\"imageUrl\",%@)",url];
    [self.webview evaluateJavaScript:js completionHandler:^(id _Nullable other, NSError * _Nullable error) {
    }];
}

- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker
{
    [picker dismissViewControllerAnimated:YES completion:^{
    }];
}



#pragma mark - RedPacketViewDelegate
- (void)dismissRedPacketView {
    [self.redPacketAlertView dismissViewControllerAnimated:NO];
}

#pragma mark - ShareCoreClient
- (void)onShareH5Success {
    [self.shareAlertPanel dismissViewControllerAnimated:YES];
}

- (void)onShareH5Failth:(NSString *)message {
    [MBProgressHUD hideHUD];
}


#pragma mark - private method
//显示分享页
- (void)showSharePanel {
    if (self.shareJsonData != nil || self.shareJsonData.length > 0) {
        
        YPShareInfo *shareInfo = [YPShareInfo yy_modelWithJSON:self.shareJsonData];
        shareInfo.type = HJShareTypeNormol;
        [YPShareView show:shareInfo];
        
    }
}

//返回按钮
- (void)backButtonClick {
    if ([self.webview canGoBack]) {
        [self.webview goBack];
    }else {
        [self.navigationController popViewControllerAnimated:YES];
        [self.userContentController removeScriptMessageHandlerForName:@"openPurse"];
        [self.userContentController removeScriptMessageHandlerForName:@"openChargePage"];
        [self.userContentController removeScriptMessageHandlerForName:@"openPersonPage"];
        [self.userContentController removeScriptMessageHandlerForName:@"openSharePage"];
        [self.userContentController removeScriptMessageHandlerForName:@"openRoom"];
        [self.userContentController removeScriptMessageHandlerForName:@"getPosition"];
        [self.userContentController removeScriptMessageHandlerForName:@"getUid"];
        [self.userContentController removeScriptMessageHandlerForName:@"getDeviceId"];
        [self.userContentController removeScriptMessageHandlerForName:@"getTicket"];
        [self.userContentController removeScriptMessageHandlerForName:@"refreshWeb"];
        [self.userContentController removeScriptMessageHandlerForName:@"getCommonParams"];
        [self.userContentController removeScriptMessageHandlerForName:@"buildRequest"];
        [self.userContentController removeScriptMessageHandlerForName:@"extractResponse"];
        [self.userContentController removeScriptMessageHandlerForName:@"getUserPhoneNumber"];
        [self.userContentController removeScriptMessageHandlerForName:@"requestImageChooser"];
        [self.userContentController removeScriptMessageHandlerForName:@"httpRequest"];
        [self.userContentController removeScriptMessageHandlerForName:@"setupNavigationBarRightItem"];
        [self.userContentController removeScriptMessageHandlerForName:@"closeWin"];
//        [self.userContentController removeScriptMessageHandlerForName:@"openTeenagerModelCallbcak"];
//        [self.userContentController removeScriptMessageHandlerForName:@"closeTeenagerModelCallback"];


    }
}

- (void)webView:(WKWebView *)webView runJavaScriptAlertPanelWithMessage:(NSString *)message initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(void))completionHandler{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertTip, nil) message:message?:@"" preferredStyle:UIAlertControllerStyleAlert];
    [alertController addAction:([UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        completionHandler();
    }])];
    [self presentViewController:alertController animated:YES completion:nil];
    
}

- (void)webView:(WKWebView *)webView runJavaScriptConfirmPanelWithMessage:(NSString *)message initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(BOOL))completionHandler{
    //    DLOG(@"msg = %@ frmae = %@",message,frame);
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:NSLocalizedString(XCAlertTip, nil) message:message?:@"" preferredStyle:UIAlertControllerStyleAlert];
    [alertController addAction:([UIAlertAction actionWithTitle:NSLocalizedString(XCRoomCancel, nil) style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
        completionHandler(NO);
    }])];
    [alertController addAction:([UIAlertAction actionWithTitle:NSLocalizedString(XCAlertConfirm, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        completionHandler(YES);
    }])];
    [self presentViewController:alertController animated:YES completion:nil];
}

- (void)webView:(WKWebView *)webView runJavaScriptTextInputPanelWithPrompt:(NSString *)prompt defaultText:(NSString *)defaultText initiatedByFrame:(WKFrameInfo *)frame completionHandler:(void (^)(NSString * _Nullable))completionHandler{
    UIAlertController *alertController = [UIAlertController alertControllerWithTitle:prompt message:@"" preferredStyle:UIAlertControllerStyleAlert];
    [alertController addTextFieldWithConfigurationHandler:^(UITextField * _Nonnull textField) {
        textField.text = defaultText;
    }];
    [alertController addAction:([UIAlertAction actionWithTitle:NSLocalizedString(XCAlertDone, nil) style:UIAlertActionStyleDefault handler:^(UIAlertAction * _Nonnull action) {
        completionHandler(alertController.textFields[0].text?:@"");
    }])];
    
    [self presentViewController:alertController animated:YES completion:nil];
}

#pragma mark - KVO
- (void)observeValueForKeyPath:(NSString *)keyPath ofObject:(id)object change:(NSDictionary<NSString *,id> *)change context:(void *)context {
    if ([keyPath isEqualToString:@"estimatedProgress"]) {
        self.progressView.progress = self.webview.estimatedProgress;
        if (self.progressView.progress == 1) {
            __weak typeof (self)weakSelf = self;
            [UIView animateWithDuration:0.25f delay:0.3f options:UIViewAnimationOptionCurveEaseOut animations:^{
                weakSelf.progressView.transform = CGAffineTransformMakeScale(1.0f, 1.4f);
            } completion:^(BOOL finished) {
                weakSelf.progressView.hidden = YES;
            }];
        }
    }else if ([keyPath isEqualToString:@"title"]) {
        if (object == self.webview) {
            self.title = self.webview.title;
        }else{
            [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
        }
    }else{
        [super observeValueForKeyPath:keyPath ofObject:object change:change context:context];
    }
}



#pragma mark - Getter
- (WKWebView *)webview {
    if (_webview == nil) {
        WKWebViewConfiguration *configuration = [[WKWebViewConfiguration alloc]init];
        configuration.preferences.javaScriptEnabled = YES;
        configuration.preferences.javaScriptCanOpenWindowsAutomatically = YES;
        configuration.preferences.minimumFontSize = 10;
        configuration.selectionGranularity = WKSelectionGranularityCharacter;
        configuration.userContentController = self.userContentController;
        
//        CGSize size = [UIScreen mainScreen].bounds.size;
        _webview = [[WKWebView alloc]initWithFrame:CGRectZero configuration:configuration];
//        _webview.scrollView.bounces = false;
        _webview.navigationDelegate = self;
        _webview.UIDelegate = self;

        //添加KVO，WKWebView有一个属性estimatedProgress，就是当前网页加载的进度，所以监听这个属性
        [_webview addObserver:self forKeyPath:@"estimatedProgress" options:NSKeyValueObservingOptionNew context:nil];
        //添加KVO，监听title属性
        [_webview addObserver:self forKeyPath:@"title" options:NSKeyValueObservingOptionNew context:NULL];
        
        UISwipeGestureRecognizer *swiftGesture = [[UISwipeGestureRecognizer alloc]initWithTarget:self action:@selector(backButtonClick)];
        [_webview addGestureRecognizer:swiftGesture];
        
        //set useragent
        [_webview evaluateJavaScript:@"navigator.userAgent" completionHandler:^(id result, NSError *error) {
            NSString *userAgent = result;
            if (![userAgent containsString:JX_WEB_USER_AGENT_NAME]){
                NSString *newUserAgent = [[userAgent stringByAppendingString:@" "] stringByAppendingString:JX_WEB_USER_AGENT_NAME];
                NSDictionary *dictionary = [NSDictionary dictionaryWithObjectsAndKeys:newUserAgent, @"UserAgent", nil];
                [[NSUserDefaults standardUserDefaults] registerDefaults:dictionary];
                [[NSUserDefaults standardUserDefaults] synchronize];
                [_webview setCustomUserAgent:newUserAgent];
            }
        }];
    }
    return _webview;
}

- (UIProgressView *)progressView{
    if (!_progressView) {
        _progressView = [[UIProgressView alloc] initWithFrame:CGRectMake(0, 0, [[UIScreen mainScreen] bounds].size.width, 2)];
        _progressView.progressTintColor = RGBACOLOR(254, 215, 0, 1);
        _progressView.trackTintColor = [UIColor clearColor];
        //设置进度条的高度，下面这句代码表示进度条的宽度变为原来的1倍，高度变为原来的1.5倍.
        _progressView.transform = CGAffineTransformMakeScale(1.0f, 1.5f);
    }
    return _progressView;
}

//h5交互方法
- (WKUserContentController *)userContentController{
    if (!_userContentController) {
        _userContentController = [[WKUserContentController alloc] init];
        [_userContentController addScriptMessageHandler:self name:@"openPurse"];
        [_userContentController addScriptMessageHandler:self name:@"openChargePage"];
        [_userContentController addScriptMessageHandler:self name:@"openPersonPage"];
        [_userContentController addScriptMessageHandler:self name:@"openSharePage"];
        [_userContentController addScriptMessageHandler:self name:@"openRoom"];
        [_userContentController addScriptMessageHandler:self name:@"getPosition"];
        [_userContentController addScriptMessageHandler:self name:@"getUid"];
        [_userContentController addScriptMessageHandler:self name:@"getDeviceId"];
        [_userContentController addScriptMessageHandler:self name:@"getTicket"];
        [_userContentController addScriptMessageHandler:self name:@"refreshWeb"];
        [_userContentController addScriptMessageHandler:self name:@"getCommonParams"];
        [_userContentController addScriptMessageHandler:self name:@"buildRequest"];
        [_userContentController addScriptMessageHandler:self name:@"extractResponse"];
        [_userContentController addScriptMessageHandler:self name:@"getUserPhoneNumber"];
        [_userContentController addScriptMessageHandler:self name:@"requestImageChooser"];
        [_userContentController addScriptMessageHandler:self name:@"httpRequest"];
        [_userContentController addScriptMessageHandler:self name:@"setupNavigationBarRightItem"];
        [_userContentController addScriptMessageHandler:self name:@"closeWin"];
//        [_userContentController addScriptMessageHandler:self name:@"openTeenagerModelCallback"];
//        [_userContentController addScriptMessageHandler:self name:@"closeTeenagerModelCallback"];



    }
    return _userContentController;
}



- (void)dealloc {
    RemoveCoreClientAll(self);
    [self.webview removeObserver:self forKeyPath:@"estimatedProgress"];
    [self.webview removeObserver:self forKeyPath:@"title"];
    self.webview = nil;
}
@end
