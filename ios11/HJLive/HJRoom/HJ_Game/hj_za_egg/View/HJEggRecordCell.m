//
//  HJEggRecordCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/11.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJEggRecordCell.h"

#import "NIMKitUtil.h"

@implementation HJEggRecordCell

- (void)awakeFromNib {
    [super awakeFromNib];
    
    
    
}

- (void)setModel:(HJEggRecordModel *)model
{
    _model = model;
    
    if (model) {
        [self.logo qn_setImageImageWithUrl:model.picUrl placeholderImage:placeholder_image_square type:ImageTypeHomeBanner];
        self.priceLabel.text = [NSString stringWithFormat:@"%zd的",model.goldPrice];
        self.nameLabel.text = [NSString stringWithFormat:@"%@ X%ld",model.giftName,(long)model.giftNum];
        
        
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        
//        [formatter setDateStyle:NSDateFormatterMediumStyle];
//
//        [formatter setTimeStyle:NSDateFormatterShortStyle];
        
//        [formatter setDateFormat:@"YYYY-MM-dd hh:mm:ss"];
        [formatter setDateFormat:@"MM-dd hh:mm:ss"];
        
        NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Beijing"];
        
        [formatter setTimeZone:timeZone];
        
        NSString *time = model.createTime;
        if (model.createTime.length>10) {
            time = [model.createTime substringToIndex:10];
        }
        
        
        
        NSInteger timesp = [time integerValue];
        
        NSDate *confromTimesp = [NSDate dateWithTimeIntervalSince1970:timesp];
        
        
        NSString *confromTimespStr = [formatter stringFromDate:confromTimesp];
        
        
//        NSString *timeSting = [NIMKitUtil showTime:model.createTime showDetail:NO];
        self.timeLabel.text = confromTimespStr;
    }
    
}

@end
