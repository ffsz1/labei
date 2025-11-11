//
//  YPHomeTagCCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "Healp.h"
#import "YPHomeTagCCell.h"

@implementation YPHomeTagCCell

- (void)setIsSel:(BOOL)isSel
{
    _isSel = isSel;
    
    
    
    if (isSel) {
        
        self.barImageView.hidden = NO;
        self.nameLabel.font = self.stytle.selFont;
        //life-hj
        self.nameLabel.textAlignment=NSTextAlignmentCenter;
        if (self.stytle.isPictureTitleColor) {
//             self.nameLabel.textColor=[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_home_peopleBg"]];
             self.nameLabel.textColor=[UIColor colorWithHexString:@"#333333"];
             
        }else{
            self.nameLabel.textColor = self.stytle.selColor;
        }
       
        
    }else{
        
        self.barImageView.hidden = YES;
        self.nameLabel.font = self.stytle.normalFont;
        self.nameLabel.textColor = self.stytle.normalColor;
    }
    
}

- (void)setIsHomeSectionStyle:(BOOL)isHomeSectionStyle
{
    _isHomeSectionStyle = isHomeSectionStyle;
}

- (void)setStytle:(YPFirstHomeTagStytleModel *)stytle
{
    _stytle = stytle;
    if (stytle) {
        
//        self.nameLabel.verticalAlignment = self.stytle.verticalAlignment;

        
        self.lineWidth.constant = stytle.lineWidth;
        self.lineHeight.constant = stytle.lineHeight;
        self.barImageView.backgroundColor = stytle.lineColor;
        
        self.isSel = self.isSel;
        
    }
}
@end
