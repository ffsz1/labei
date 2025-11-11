//
//  YPAdMusicView.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPAdMusicView : UIView
@property (weak, nonatomic) IBOutlet UILabel *wifiTipLabel;
@property (weak, nonatomic) IBOutlet UILabel *ipLabel;
@property (weak, nonatomic) IBOutlet UILabel *savedLabel;

@property (nonatomic, strong) void(^senderBlock)();
@end
