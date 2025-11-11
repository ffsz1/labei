//
//  HJPersonLabelCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPersonLabelCell.h"

@interface HJPersonLabelCell ()

@property (nonatomic, strong) NSMutableArray *labelArr;

@end

@implementation HJPersonLabelCell

+ (HJPersonLabelCell *)cellWithTableView:(UITableView *)tableView {
    
    HJPersonLabelCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJPersonLabelCell"];
    
    if (!cell) {
        cell = [[HJPersonLabelCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"HJPersonLabelCell"];
    }
    
    return cell;
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        self.selectionStyle = UITableViewCellSelectionStyleNone;
    }
    return self;
}

- (void)setLabelStrArr:(NSArray *)labelStrArr {
    
    _labelStrArr = labelStrArr;
    
    if (self.labelArr.count) {
        for (UIView *view in self.labelArr) {
            [view removeFromSuperview];
        }
    }
    
    self.labelArr = [NSMutableArray array];
    
    CGFloat maxtWidth = XC_SCREE_W - 30.f;
    CGFloat padding = 10.f;
    CGFloat textPadding = 9.f;
    CGFloat h = 20.f;
    CGFloat x = 15.f;
    CGFloat y = 0.f;

    
    NSInteger index = 0;
    for (NSString *str in labelStrArr) {
        UILabel *label = [self getLabel];
        label.text = str;
        CGFloat w = [HJPersonLabelCell getWidthWithStr:str font:label.font] + textPadding * 2;
        if (x + w > maxtWidth) {
            x = 15.f;
            y += (10.f + h);
        }
        label.frame = CGRectMake(x, y, w, h);
        if (index == 0) {
            
            if (self.sex == 0) {
                
                label.backgroundColor = UIColorHex(FF6186);
            }
            else {
                label.backgroundColor = UIColorHex(61A8FF);
            }
        }
        else if (index == 1) {
            label.backgroundColor = UIColorHex(A967FF);
        }
        
        [self.contentView addSubview:label];
        [self.labelArr addObject:label];
        
        x += (w + padding);
        index++;
    }
}

- (UILabel *)getLabel {
    UILabel *label = [UILabel new];
    label.textColor = [UIColor whiteColor];
    label.font = [UIFont boldSystemFontOfSize:13.f];
    label.layer.masksToBounds = YES;
    label.layer.cornerRadius = 10.f;
    label.textAlignment = NSTextAlignmentCenter;
    return label;
}

+ (CGFloat)getWidthWithStr:(NSString *)str font:(UIFont *)font {
    return [str boundingRectWithSize:CGSizeMake(CGFLOAT_MAX, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : font} context:nil].size.width;
}

+ (CGFloat)getHeightWithStrArr:(NSArray *)labelStrArr {
    
    CGFloat maxtWidth = XC_SCREE_W - 30.f;
    CGFloat padding = 10.f;
    CGFloat textPadding = 9.f;
    CGFloat h = 20.f;
    CGFloat x = 15.f;
    CGFloat y = 0.f;
    
    
    for (NSString *str in labelStrArr) {
        CGFloat w = [HJPersonLabelCell getWidthWithStr:str font:[UIFont boldSystemFontOfSize:13.f]] + textPadding * 2;
        if (x + w > maxtWidth) {
            x = 15.f;
            y += (10.f + h);
        }
        
        x += (w + padding);
    }
    
    return y + h + 15.f;
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
