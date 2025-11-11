//
//  HJPersonerSignCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface HJPersonerSignCell : UITableViewCell
+ (HJPersonerSignCell *)cellWithTableView:(UITableView *)tableView;

+ (CGFloat)getHeightWithSign:(NSString *)sign;

@end

NS_ASSUME_NONNULL_END
