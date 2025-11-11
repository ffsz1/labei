//
//  HJPersonerSignCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPersonerSignCell.h"

@implementation HJPersonerSignCell

+ (HJPersonerSignCell *)cellWithTableView:(UITableView *)tableView {
    
    HJPersonerSignCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJPersonerSignCell"];
    
    if (!cell) {
        cell = [[HJPersonerSignCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"HJPersonerSignCell"];
    }
    
    return cell;
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        self.textLabel.textColor = UIColorHex(999999);
        self.textLabel.font = [UIFont boldSystemFontOfSize:13.f];
        self.textLabel.numberOfLines = 0;
        self.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    return self;
}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    if (self.textLabel.text.length) {
        CGSize size = [self.textLabel.text boundingRectWithSize:CGSizeMake(XC_SCREE_W - 30.f, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : self.textLabel.font} context:nil].size;
        self.textLabel.frame = CGRectMake(15.f, 0, size.width, size.height);
    }
}

+ (CGFloat)getHeightWithSign:(NSString *)sign {
    
     CGSize size = [sign boundingRectWithSize:CGSizeMake(XC_SCREE_W - 30.f, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : [UIFont boldSystemFontOfSize:13.f]} context:nil].size;
    
    CGFloat height = size.height + 25.f;
    
    if (height < 47.f) {
        return 47.f;
    }
    
    return height;
}

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

@end
