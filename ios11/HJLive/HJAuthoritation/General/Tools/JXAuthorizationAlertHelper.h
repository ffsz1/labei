//
//  JXAuthorizationAlertHelper.h
//  XChat
//
//  Created by Colin on 2019/2/19.
//

#import <Foundation/Foundation.h>
#import "JXAuthorizationAlertView.h"

NS_ASSUME_NONNULL_BEGIN

typedef void(^JXAuthorizationAlertHelperDidTapActionHandler)(UIViewController * _Nullable toViewController);

@interface JXAuthorizationAlertHelper : NSObject

+ (void)showAuthorizationAlertWithViewController:(UIViewController *)viewController
                                            code:(NSInteger)code
                                         message:(NSString *)message
                             didTapActionHandler:(JXAuthorizationAlertHelperDidTapActionHandler)didTapActionHandler;

+ (BOOL)isErrorCodeForAuthorization:(NSInteger)errorCode;

@end

NS_ASSUME_NONNULL_END
