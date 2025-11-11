//
//  YPMusicTableViewCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface YPMusicTableViewCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *titleLa;
@property (weak, nonatomic) IBOutlet UILabel *singerLabel;
@property (nonatomic, copy) void(^deleMusicBlock)();
@end
