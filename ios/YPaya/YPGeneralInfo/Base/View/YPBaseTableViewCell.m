//
//  YPBaseTableViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPBaseTableViewCell.h"

@implementation YPBaseTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
   
}
- (id)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style reuseIdentifier:reuseIdentifier];
    
    if (self)
    {
        [self drawCellUI];
    }
    
    return self;
}

- (void)drawCellUI
{
    
}

- (void)configCellWithModel:(NSObject *)model
{
    
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

}

@end
