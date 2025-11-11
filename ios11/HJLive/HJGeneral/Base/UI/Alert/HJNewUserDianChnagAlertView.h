//
//  HJNewUserDianChnagAlertView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJNewUserDianChnagAlertView : UIView

@property (nonatomic, copy) void(^closeBtnActionBlock)();
@property (nonatomic, copy) void(^dianchangActionBlock)();

@property (nonatomic, copy) NSString *title;
@property (nonatomic, copy) NSString *avatar;


@end
