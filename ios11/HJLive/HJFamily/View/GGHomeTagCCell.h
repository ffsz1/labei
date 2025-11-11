//
//  GGHomeTagCCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface GGHomeTagCCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIImageView *logo;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;

@property (assign,nonatomic) int tagID;
@property (assign,nonatomic) BOOL isSel;

@end

NS_ASSUME_NONNULL_END
