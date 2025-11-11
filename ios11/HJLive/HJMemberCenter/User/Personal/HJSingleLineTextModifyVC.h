//
//  HJSingleLineTextModifyVC.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YYViewController.h"

@interface HJSingleLineTextModifyVC : YYViewController
@property (nonatomic, strong) NSString *pageTitle;
@property (nonatomic, strong) NSString *defaultText;
@property (nonatomic, assign) UserID userID;
@property (nonatomic, assign) NSInteger maxLength;
@property (nonatomic, strong) NSString *key;
@end
