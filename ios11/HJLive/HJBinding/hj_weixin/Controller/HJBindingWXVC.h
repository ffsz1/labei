//
//  HJBindingWXVC.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJBindingWXVC : UIViewController

@property (assign,nonatomic) BOOL isQQ;
@property (nonatomic,copy) NSString *openId;
@property (nonatomic,copy) NSString *unionId;
@property (nonatomic,copy) NSString *accessToken;


@end

NS_ASSUME_NONNULL_END
