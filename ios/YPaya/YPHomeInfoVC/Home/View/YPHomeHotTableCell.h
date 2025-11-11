//
//  YPHomeHotTableCell.h
//  HJLive
//
//  Created by feiyin on 2020/9/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#define HJHomeHotTableCellWidth (XC_SCREE_W-75)/2
#define HJHomeHotTableCellHeight 80+22
NS_ASSUME_NONNULL_BEGIN

@interface YPHomeHotTableCell : UITableViewCell
@property (nonatomic,strong) NSArray *roomArr;
@end

NS_ASSUME_NONNULL_END
