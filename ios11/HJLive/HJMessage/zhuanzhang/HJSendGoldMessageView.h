//
//  HJSendGoldMessageView.h
//  HJLive
//
//  Created by feiyin on 2020/7/28.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJSendGoldMessageView : UIView
@property (weak, nonatomic) IBOutlet UIImageView *sendGoldImageView;
@property (weak, nonatomic) IBOutlet UILabel *goldLabel;
@property (weak, nonatomic) IBOutlet UILabel *desLabel;
+ (instancetype)loadFromNib;
@end

NS_ASSUME_NONNULL_END
