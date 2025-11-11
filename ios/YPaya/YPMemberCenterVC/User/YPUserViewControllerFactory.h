//
//  YPUserViewControllerFactory.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPYYStoryboardViewControllerFactory.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPUserViewControllerFactory : YPYYStoryboardViewControllerFactory
+ (instancetype)sharedFactory;
- (UIViewController *)instantiatePersonalEditViewController;
- (UIViewController *)instantiateSingleLineTextModifyViewController;
- (UIViewController *)instantiateMultiLineTextModifyViewController;
- (UIViewController *)instantiatePersonalVoiceEditViewController;
- (UIViewController *)instantiateEditPersonalPhotosViewController;
@end


NS_ASSUME_NONNULL_END
