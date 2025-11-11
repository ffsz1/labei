//
//  YPSetPasswordViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPSetPasswordViewController : UIViewController
@property (weak, nonatomic) IBOutlet UITextField *pswTextField;
@property (weak, nonatomic) IBOutlet UITextField *sureTextField;
@property (weak, nonatomic) IBOutlet UIButton *pswSecurityBtn;
@property (weak, nonatomic) IBOutlet UIButton *sureSecurityBtn;

@property (nonatomic,copy) NSString *codeStr;
@property (nonatomic,copy) NSString *phoneStr;

@end

NS_ASSUME_NONNULL_END
