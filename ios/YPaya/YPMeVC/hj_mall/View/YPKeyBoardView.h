//
//  YPKeyBoardView.h
//  HJLive
//
//  Created by MacBook on 2020/8/19.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN
typedef void(^KeyBoardViewSureActionHandler)(NSInteger  count);
@interface YPKeyBoardView : UIView

+ (instancetype)showKeyboardWithSureBtnImgStr:(NSString *)sureBtnImgStr sureBtnTitle:(NSString *)sureBtnTitle sureActionHandler:(KeyBoardViewSureActionHandler)didTapActionHandler;

@end

NS_ASSUME_NONNULL_END
