//
//  YPManagerSettingCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/13.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@protocol XCManagerSettingCellDelegate
- (void)removeBy:(NSIndexPath *)indexPath;
@end

@interface YPManagerSettingCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UIImageView *avatar;
@property (weak, nonatomic) IBOutlet UIImageView *roleTagImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UIImageView *sexImageView;
@property (weak, nonatomic) IBOutlet UILabel *idLabel;
@property (weak, nonatomic) IBOutlet UIButton *removeBtn;
@property (strong, nonatomic)NSIndexPath *indexPath;

@property (weak, nonatomic) id<XCManagerSettingCellDelegate> delegate;
@end
