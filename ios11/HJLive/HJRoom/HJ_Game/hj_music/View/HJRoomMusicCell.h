//
//  HJRoomMusicCell.h
//  HJLive
//
//  Created by feiyin on 2020/7/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "HJMusicInfoModel.h"

NS_ASSUME_NONNULL_BEGIN

@interface HJRoomMusicCell : UITableViewCell
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *singerLabel;

@property (strong,nonatomic) HJMusicInfoModel *model;
@property (copy,nonatomic) NSString *url;



@end

NS_ASSUME_NONNULL_END
