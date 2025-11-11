//
//  HJSignerCell.h
//  HJLive
//
//  Created by apple on 2019/5/22.
//

#import <UIKit/UIKit.h>

#import "HJMMHomeItemModel.h"

typedef void(^UpdateBlock)(void);

NS_ASSUME_NONNULL_BEGIN

@interface HJSignerCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *logoImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *coinLabel;
@property (weak, nonatomic) IBOutlet UIButton *btn;

@property (strong,nonatomic) HJMMHomeItemModel *model;

@property (copy,nonatomic) UpdateBlock updateBlock;


@end

NS_ASSUME_NONNULL_END
