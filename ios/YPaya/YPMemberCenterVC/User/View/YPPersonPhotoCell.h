//
//  YPPersonPhotoCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

@interface YPPersonPhotoCell : UITableViewCell
+ (YPPersonPhotoCell *)cellWithTableview:(UITableView *)tableView;

@property (nonatomic, weak) UIView *bgView;

@property (nonatomic, assign) BOOL isOwner;
@property (nonatomic, strong) NSArray *imageUrlArr;

@property (nonatomic, copy) void(^addBtnActionBlock)(void);
@property (nonatomic, copy) void(^imageViewTapActionBlock)(NSInteger index);

+ (CGFloat)getHeightWithIsOwner:(BOOL)isOwner imageUrlArr:(NSArray *)imageUrlArr;

@end

NS_ASSUME_NONNULL_END
