//
//  YPXCDatePicker.m
//  HJLive
//
//  Created by feiyin on 2020/6/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPXCDatePicker.h"

@interface YPXCDatePicker()
@property (weak, nonatomic) IBOutlet UIDatePicker *datePicker;


@end



@implementation YPXCDatePicker

- (void)awakeFromNib {
    [super awakeFromNib];
}

- (void)setDateStr:(NSString *)dateStr {
    _dateStr = dateStr;
    NSDateFormatter *formatter = [[NSDateFormatter alloc]init];
    [formatter setDateFormat:@"yyyy年MM月dd日"];
    NSDate *resDate = [formatter dateFromString:dateStr];
    self.datePicker.maximumDate = resDate;
}

+ (instancetype)loadFromNib {
    return [[NSBundle mainBundle]loadNibNamed:@"YPXCDatePicker" owner:self options:nil].lastObject;
}

- (IBAction)dateValueChange:(UIDatePicker *)sender {
    self.datePicker.maximumDate = [NSDate date];
}

- (IBAction)cancelButton:(UIBarButtonItem *)sender {
    if ([_delegate respondsToSelector:@selector(dismissDatePicker)]) {
        [_delegate dismissDatePicker];
    }
}

- (IBAction)sureButtonClick:(UIBarButtonItem *)sender {
    if ([_delegate respondsToSelector:@selector(hadSelectedADate:)]) {
        [_delegate hadSelectedADate:self.datePicker.date];
    }
}

@end
