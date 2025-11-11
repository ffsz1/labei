//
//  HJRechargeViewCollectionViewCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol HJRechargeViewCellDelegate<NSObject>
- (void)onRmbSelected:(NSInteger) index;
@end

@interface HJRechargeViewCollectionViewCell : UICollectionViewCell
@property (weak, nonatomic) IBOutlet UIView *bgView;
@property (weak, nonatomic) IBOutlet UIImageView *bgImageView;
@property (weak, nonatomic) IBOutlet UILabel *titleLable;
@property (weak, nonatomic) IBOutlet UILabel *goldlabel;
@property (weak, nonatomic) IBOutlet UILabel *rmbLabel;
@property (nonatomic, assign) NSInteger index;
@property (nonatomic, strong) id<HJRechargeViewCellDelegate> delegate;

@end
