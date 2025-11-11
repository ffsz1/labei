//
//  HJRankConsTopView.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJRankConsTopView : UIView
@property (weak, nonatomic) IBOutlet UIButton *caifuBtn;
@property (weak, nonatomic) IBOutlet UIButton *meiliBtn;
@property (weak, nonatomic) IBOutlet UIView *line;
@property (weak, nonatomic) IBOutlet UIButton *backBtn;

@property (nonatomic, copy) void(^sendBlock)(NSInteger tag);
@property (nonatomic, copy) void(^goBackBlock)();
- (IBAction)tabClick:(UIButton *)sender;
@end

NS_ASSUME_NONNULL_END
