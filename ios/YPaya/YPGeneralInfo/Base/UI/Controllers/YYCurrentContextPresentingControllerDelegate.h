//
//  YYCurrentContextPresentingControllerDelegate.h
//  YYMobile
//
//  Created by jianglinjie on 14-7-30.
//  Copyright (c) 2014年 YY.inc. All rights reserved.
//

#import <Foundation/Foundation.h>
@protocol YYCurrentContextPresentingControllerDelegate <NSObject>
@required
- (void)currentContextPresentingNeedDismissing:(BOOL)animated;
@end


