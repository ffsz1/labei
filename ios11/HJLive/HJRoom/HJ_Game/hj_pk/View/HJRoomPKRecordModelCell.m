//
//  HJRoomPKRecordModelCell.m
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import "HJRoomPKRecordModelCell.h"

@implementation HJRoomPKRecordModelCell


- (void)setModel:(HJRoomPKRecordModel *)model
{
    _model = model;
    if (model) {
        [self.avatarImageVIew qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserLibary];
        self.nameLabel.text = model.nick;
        self.resultLabel.text = model.subject;
        self.giftLabel.text = [NSString stringWithFormat:@"%@X%ld",model.giftName,(long)model.num];
        [self.giftImageView qn_setImageImageWithUrl:model.giftUrl placeholderImage:default_avatar type:ImageTypeUserLibary];
        
        
        NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
        
        
        [formatter setDateFormat:@"YYYY-MM-dd hh:mm:ss"];
        
        NSTimeZone *timeZone = [NSTimeZone timeZoneWithName:@"Asia/Beijing"];
        
        [formatter setTimeZone:timeZone];
        
        NSString *time = [NSString stringWithFormat:@"%ld",(long)model.createTime];
        if (time.length>10) {
            time = [time substringToIndex:10];
        }
        
        
        
        NSInteger timesp = [time integerValue];
        
        NSDate *confromTimesp = [NSDate dateWithTimeIntervalSince1970:timesp];
        
        
        NSString *confromTimespStr = [formatter stringFromDate:confromTimesp];
        
        self.timeLabel.text = confromTimespStr;
    }
}

@end
