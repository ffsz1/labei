//
//  HJRedPackeMessagetView.h
//  HJLive
//
//  Created by feiyin on 2020/6/26.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface HJRedPackeMessagetView : UIView
@property (weak, nonatomic) IBOutlet UIImageView *redIcon;
@property (weak, nonatomic) IBOutlet UILabel *redLabel;
+ (instancetype)loadFromNib;
@end
