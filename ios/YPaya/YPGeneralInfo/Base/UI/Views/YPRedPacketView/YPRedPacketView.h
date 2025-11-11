//
//  YPRedPacketView.h
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol RedPacketViewDelegate <NSObject>
- (void)dismissRedPacketView;
@end

@interface YPRedPacketView : UIView

@property (weak, nonatomic) IBOutlet UILabel *redPacketNameLabel;
+ (instancetype)loadFromNib;
@property (weak, nonatomic) UINavigationController *navigationController;
@property (assign, nonatomic) double packetNum;
@property (weak, nonatomic) id<RedPacketViewDelegate> delagate;
@end
