//
//  YPCarCardCell.h
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPCarCardCell : UICollectionViewCell

@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
@property (weak, nonatomic) IBOutlet UIImageView *imgView;
@property (weak, nonatomic) IBOutlet UIButton *isUseButton;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UIButton *buyButton;
@property (assign, nonatomic) BOOL isCarSys;
@property (nonatomic, strong) NSDictionary *dic;
@property (weak, nonatomic) IBOutlet UIImageView *jinbi;
@property (weak, nonatomic) IBOutlet UIImageView *time;
@property (weak, nonatomic) IBOutlet UIImageView *playImageView;
@property (weak, nonatomic) IBOutlet UIImageView *bgImageView;
@property (assign, nonatomic) BOOL isSel;
@property (copy, nonatomic) void(^isUseBlock)(void);
@property (copy, nonatomic) void(^imgBlock)(void);
@property (copy, nonatomic) void(^buyBlock)(void);
@property (copy, nonatomic) void(^playBlock)(void);

@property (weak, nonatomic) IBOutlet UILabel *shijiaLabel;

//@property (weak, nonatomic) IBOutlet UILabel *shijiaLine;




@end
