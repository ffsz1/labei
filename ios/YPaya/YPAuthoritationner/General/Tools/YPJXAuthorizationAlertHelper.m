//
//  YPJXAuthorizationAlertHelper.m
//  XChat
//
//  Created by Colin on 2019/2/19.
//

#import "YPJXAuthorizationAlertHelper.h"
#import "YPBindingPhoneVC.h"
#import "YPWKWebViewController.h"

#import "YPAlertControllerCenter.h"
//#import <JXCategories/JXCategories.h>
#import "JXCategories.h"
@implementation YPJXAuthorizationAlertHelper

const NSInteger JXAuthorizationAlertCodeNoValid = 2507;  ///< 未实名认证
const NSInteger JXAuthorizationAlertCodeCheckIn = 2508; ///<
const NSInteger JXAuthorizationAlertCodeNoPhone = 2511;  ///< 未认证手机

+ (BOOL)isErrorCodeForAuthorization:(NSInteger)errorCode {
    NSArray<NSNumber *> *codes = @[
                                   @(JXAuthorizationAlertCodeNoValid),
                                   @(JXAuthorizationAlertCodeCheckIn),
                                   @(JXAuthorizationAlertCodeNoPhone),
                                   ];
    return [codes containsObject:@(errorCode)];;
}

+ (void)showAuthorizationAlertWithViewController:(UIViewController *)viewController  code:(NSInteger)code message:(NSString *)message didTapActionHandler:(JXAuthorizationAlertHelperDidTapActionHandler)didTapActionHandler {
    if (!viewController) return;
    
    BOOL shouldShow = NO;
    NSMutableArray *actions = @[].mutableCopy;
    YPAlertControllerCenter *alertCenter = [YPAlertControllerCenter defaultCenter];
    @weakify(self);
    switch (code) {
        case JXAuthorizationAlertCodeNoValid: // 未实名认证
        {
            shouldShow = YES;
            JXAuthorizationAlertAction *action = [[JXAuthorizationAlertAction alloc] initWithTitle:@"去认证" style:JXAuthorizationAlertActionStyleNormal handler:^{
                @strongify(self);
                [alertCenter dismissAlertNeedBlock:NO];
                
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    
                    !didTapActionHandler ?: didTapActionHandler([self getAuthorizationViewController]);
                    
                });
                
                
                
                
                
            }];
            [actions addObject:action];
            
            JXAuthorizationAlertAction *cancelAction = [[JXAuthorizationAlertAction alloc] initWithTitle:@"暂时不用" style:JXAuthorizationAlertActionStyleCancel handler:^{
                @strongify(self);
                [alertCenter dismissAlertNeedBlock:NO];
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    
                    !didTapActionHandler ?: didTapActionHandler(nil);
                    
                });
                
                
                
            }];
            [actions addObject:cancelAction];
        }
            break;
        case JXAuthorizationAlertCodeNoPhone: // 未认证手机
        {
            shouldShow = YES;
            JXAuthorizationAlertAction *action = [[JXAuthorizationAlertAction alloc] initWithTitle:@"去绑定" style:JXAuthorizationAlertActionStyleNormal handler:^{
                @strongify(self);
                [alertCenter dismissAlertNeedBlock:NO];
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    
                    !didTapActionHandler ?: didTapActionHandler([self getBindingPhoneViewController]);

                });
                
            }];
            [actions addObject:action];
            
            JXAuthorizationAlertAction *cancelAction = [[JXAuthorizationAlertAction alloc] initWithTitle:@"暂时不用" style:JXAuthorizationAlertActionStyleCancel handler:^{
                @strongify(self);
                [alertCenter dismissAlertNeedBlock:NO];
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    
                    !didTapActionHandler ?: didTapActionHandler(nil);

                });
                
            }];
            [actions addObject:cancelAction];
        }
            break;
        case JXAuthorizationAlertCodeCheckIn: // 实名认证中
        {
            shouldShow = YES;
            JXAuthorizationAlertAction *cancelAction = [[JXAuthorizationAlertAction alloc] initWithTitle:@"关闭" style:JXAuthorizationAlertActionStyleNormal handler:^{
                @strongify(self);
                [alertCenter dismissAlertNeedBlock:NO];
                
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.3 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    
                    !didTapActionHandler ?: didTapActionHandler(nil);

                });
                
            }];
            [actions addObject:cancelAction];
        }
            break;
            
        default:
            break;
    }
    if (!shouldShow) return;
    
    YPJXAuthorizationAlertView *alertView = [[YPJXAuthorizationAlertView alloc] initWithFrame:CGRectMake(0, 0, 240, 140) title:message actions:actions.copy];
    
    [alertCenter presentAlertWith:viewController view:alertView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleAlert dismissBlock:nil completionBlock:nil];
}

+ (UIViewController *)getAuthorizationViewController {
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/real_name/index.html",[YPHttpRequestHelper getHostUrl]];
    
    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
    webView.url = [NSURL URLWithString:urlSting];
    return webView;
}

+ (UIViewController *)getBindingPhoneViewController {
//    YPBindingPhoneVC *viewController = (YPBindingPhoneVC *)[[XCBindingZFBControllerFactory sharedFactory] instantiateBindingPhoneController];
//    viewController.isPush = YES;
//    viewController.isBindPhone = NO;
    
    YPBindingPhoneVC *vc = (YPBindingPhoneVC *)YPWalletStoryBoard(@"YPBindingPhoneVC");
    vc.isPush = YES;
    vc.isBindPhone = NO;
    
    return vc;
}

@end
