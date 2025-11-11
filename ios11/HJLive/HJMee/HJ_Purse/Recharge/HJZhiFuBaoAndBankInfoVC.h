//
//  HJZhiFuBaoAndBankInfoVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/29.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJZhiFuBaoAndBankInfoVC : UIViewController

@property (weak, nonatomic) IBOutlet UIButton *zhifubaoBtn;
@property (weak, nonatomic) IBOutlet UIButton *bankBtn;

@property (copy, nonatomic) void(^cashPayWayBlock)(NSInteger accountType,NSString* account,NSString*accountName);

@end

NS_ASSUME_NONNULL_END
