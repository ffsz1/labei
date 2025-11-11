//
//  HJRechargeViewCell.h
//  HJLive
//
//  Created by feiyin on 2020/6/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
@protocol HJRechargeViewCellDelegate<NSObject>
- (void)onRmbSelected:(NSInteger) index;
@end

@interface HJRechargeViewCell : UITableViewCell
@property (nonatomic, assign) NSInteger index;
@property (weak, nonatomic) IBOutlet UILabel *subTitleLabel;
@property (nonatomic, strong) id<HJRechargeViewCellDelegate> delegate;
@property (weak, nonatomic) IBOutlet UILabel *cornLabel;
@property (weak, nonatomic) IBOutlet UIButton *rmbBtn;
- (IBAction)onRmbBtnClicked:(id)sender;
@end
