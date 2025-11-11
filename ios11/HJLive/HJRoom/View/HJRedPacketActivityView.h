//
//  HJRedPacketActivityView.h
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJRedPacketActivityView : UIView
@property (weak, nonatomic) IBOutlet UIImageView *activityImageView;
+ (instancetype)loadFromNib;
@end
