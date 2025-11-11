//
//  HJLoginBindingPhoneView.h
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

typedef void(^BindingBlock)(void);


NS_ASSUME_NONNULL_BEGIN

@interface HJLoginBindingPhoneView : UIView
@property (weak, nonatomic) IBOutlet UITextField *phoneTextF;
@property (weak, nonatomic) IBOutlet UITextField *codeTextF;
@property (weak, nonatomic) IBOutlet UIButton *codeBtn;
@property (weak, nonatomic) IBOutlet UIButton *nextBtn;

@property (assign,nonatomic) BOOL isShow;

@property (copy,nonatomic) BindingBlock bindingBlock;


@end

NS_ASSUME_NONNULL_END
