//
//  HJSearchHistoryTableCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJSearchHistoryTableCell.h"
@interface HJSearchHistoryTableCell ()

@property (nonatomic, strong) UIView *lineView;
@end
@implementation HJSearchHistoryTableCell

+ (HJSearchHistoryTableCell *)cellWithTableView:(UITableView *)tableView {
    
    HJSearchHistoryTableCell *cell = [tableView dequeueReusableCellWithIdentifier:NSStringFromClass([HJSearchHistoryTableCell class])];
    
    if (!cell) {
        cell = [[HJSearchHistoryTableCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:NSStringFromClass([HJSearchHistoryTableCell class])];
    }
    
    return cell;
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        
        self.textLabel.font = [UIFont boldSystemFontOfSize:15];
        self.textLabel.textColor = UIColorHex(A1A1A1);
        [self.contentView addSubview:self.lineView];
    }
    
    return self;
}

- (void)layoutSubviews {
    
    [super layoutSubviews];
    
    self.textLabel.frame = CGRectMake(15, 0, kScreenWidth - 30, 44);
    
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.leading.mas_equalTo(15);
        make.trailing.mas_equalTo(-15);
        make.height.mas_equalTo(1);
        make.bottom.mas_equalTo(0);
    }];
}


- (void)awakeFromNib {
    [super awakeFromNib];
   
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    
}

- (UIView *)lineView {
    
    if (!_lineView) {
        _lineView = [UIView new];
        _lineView.backgroundColor = UIColorHex(F5F5F5);
    }
    
    return _lineView;
}

@end
