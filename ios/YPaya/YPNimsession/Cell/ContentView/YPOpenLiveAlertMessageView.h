//
//  YPOpenLiveAlertMessageView.h
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPOpenLiveAlertMessageView : UIView
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UILabel *subTitle;
+ (instancetype)loadFromNib;
@end
