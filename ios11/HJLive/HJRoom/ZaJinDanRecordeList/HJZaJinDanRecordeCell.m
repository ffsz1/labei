//
//  HJZaJinDanRecordeCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJZaJinDanRecordeCell.h"

@interface HJZaJinDanRecordeCell ()

@property (weak, nonatomic) IBOutlet UIImageView *photoView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;


@end

@implementation HJZaJinDanRecordeCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.photoView.clipsToBounds = YES;
    self.photoView.contentMode = UIViewContentModeScaleAspectFill;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(HJGiftPurseRecord *)model {
    
    _model = model;
    
    [self.photoView sd_setImageWithURL:[NSURL URLWithString:model.picUrl] placeholderImage:[UIImage imageNamed:placeholder_image_square]];
    self.nameLabel.text = [NSString stringWithFormat:@"%@X%zd",model.giftName.length ? model.giftName : @"",model.giftNum];
    
    NSTimeInterval interval    = model.createTime / 1000.0;
    NSDate *date               = [NSDate dateWithTimeIntervalSince1970:interval];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
    NSString *dateString       = [formatter stringFromDate: date];
    self.timeLabel.text = dateString;
}

@end
