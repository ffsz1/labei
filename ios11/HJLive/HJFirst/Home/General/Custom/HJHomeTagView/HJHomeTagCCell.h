//
//  HJHomeTagCCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "HJFirstHomeTagStytleModel.h"

//#import "HJVerticalLabel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJHomeTagCCell : UICollectionViewCell

@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *barImageView;

@property (assign,nonatomic) BOOL isSel;

@property (assign,nonatomic) BOOL isHomeSectionStyle;

@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineHeight;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lineWidth;

@property (nonatomic,strong) HJFirstHomeTagStytleModel *stytle;


@end

NS_ASSUME_NONNULL_END
