//
//  YPFamilyEditTableController.h
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPFamilyEditTableController : UITableViewController

@property (nonatomic, assign) long creatTime;
@property (nonatomic, assign) long openTime;
@property (nonatomic, copy) NSString *imageUrl;
@property (nonatomic, copy) NSString *text;
@property (nonatomic, copy) NSString *familyId;
@property (nonatomic, copy) void(^update)(void);

@end

NS_ASSUME_NONNULL_END
