//
//  YPEggRecordListView.h
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

#import "YPEggRecordCell.h"

#import "GGMaskView.h"


NS_ASSUME_NONNULL_BEGIN

@interface YPEggRecordListView : GGMaskView<UITableViewDelegate,UITableViewDataSource>
@property (weak, nonatomic) IBOutlet UITableView *tableView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *bottom_bgView;

@property (strong,nonatomic) NSMutableArray *dataArr;

+ (void)showRecord;

@end

NS_ASSUME_NONNULL_END
