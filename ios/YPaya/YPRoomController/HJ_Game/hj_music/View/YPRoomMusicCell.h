//
//  YPRoomMusicCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "YPMusicInfoModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface YPRoomMusicCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *singerLabel;

@property (strong,nonatomic) YPMusicInfoModel *model;
@property (copy,nonatomic) NSString *url;



@end

NS_ASSUME_NONNULL_END
