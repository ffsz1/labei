//
//  HJPersonLabelCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJPersonLabelCell : UITableViewCell
+ (HJPersonLabelCell *)cellWithTableView:(UITableView *)tableView;

@property (nonatomic, assign) NSInteger sex;

@property (nonatomic, strong) NSArray *labelStrArr;

+ (CGFloat)getHeightWithStrArr:(NSArray *)labelStrArr;

@end

NS_ASSUME_NONNULL_END
