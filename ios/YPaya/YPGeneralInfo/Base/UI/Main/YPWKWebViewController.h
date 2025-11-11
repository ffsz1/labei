//
//  YPWKWebViewController.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <WebKit/WebKit.h>
NS_ASSUME_NONNULL_BEGIN

@interface YPWKWebViewController : UIViewController
@property (strong, nonatomic) NSURL *url;
@property (nonatomic, copy) NSString *callBackJS;
@end

NS_ASSUME_NONNULL_END
