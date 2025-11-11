//
//  GGHomeTagCCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "GGHomeTagCCell.h"

@implementation GGHomeTagCCell

- (void)setIsSel:(BOOL)isSel
{
    _isSel = isSel;
    
    if (isSel) {
        self.nameLabel.font = [UIFont fontWithName:@"PingFangSC-Medium" size:12];
        self.nameLabel.textColor = UIColorHex(9F62FB);
        self.logo.image = [UIImage imageNamed:[self getSelLogoImg]];
    }else{
        self.nameLabel.font = [UIFont fontWithName:@"PingFangSC-Medium" size:12];
        self.nameLabel.textColor = UIColorHex(ABABAB);
        self.logo.image = [UIImage imageNamed:[self getLogoImg]];
    }
    
}


- (NSString *)getLogoImg
{
    NSString *title = self.nameLabel.text;
    if (title) {
        NSString *name = @"hj_home_iocn_moren_shibie";
        if ([title isEqualToString:@"关注"]) {
            name = @"hj_home_icon_guangzhumoren";
        }
        
        if ([title isEqualToString:@"热门"]) {
            name = @"hj_home_iocn_remen_normal";
        }
        
        if ([title isEqualToString:@"新秀"]) {
            name = @"hj_home_icon_xinxiumoren";
        }
        
        if ([title isEqualToString:@"交友"]) {
            name = @"hj_home_icon_jiaoyoumoren";
        }
        
        if ([title isEqualToString:@"陪玩"]) {
            name = @"hj_home_icon_peiwanmoren";
        }
        
        if ([title isEqualToString:@"男神"]) {
            name = @"hj_home_icon_nanshenmoren";
        }
        
        if ([title isEqualToString:@"女神"]) {
            name = @"hj_home_icon_nvshenmoren";
        }
        
        //MARK:补充.根据ID获取图标（#9 新秀  8 交友 5 陪玩 6 电台 7 游戏）
        if (self.tagID == 9) {
            name = @"hj_home_icon_xinxiumoren";
        } else if (self.tagID == 8) {
            name = @"hj_home_icon_jiaoyoumoren";
        } else if (self.tagID == 5) {
            name = @"hj_home_icon_peiwanmoren";
        } else if (self.tagID == 6) {
            name = @"hj_home_icon_diantaimoren";
        } else if (self.tagID == 7) {
            name = @"hj_home_icon_yulemoren";
        }
        
        return name;
    }
    return @"";
    
    
    
}

- (NSString *)getSelLogoImg
{
    NSString *title = self.nameLabel.text;
    if (title) {
        NSString *name = @"hj_home_iocn_moren";
        if ([title isEqualToString:@"关注"]) {
            name = @"hj_home_icon_guangzhu";
        }
        
        if ([title isEqualToString:@"热门"]) {
            name = @"hj_home_icon_remen";
        }
        
        if ([title isEqualToString:@"新秀"]) {
            name = @"hj_home_icon_xinxiu";
        }
        
        if ([title isEqualToString:@"交友"]) {
            name = @"hj_home_icon_jiaoyou";
        }
        
        if ([title isEqualToString:@"陪玩"]) {
            name = @"hj_home_icon_pewna";
        }
        
        if ([title isEqualToString:@"男神"]) {
            name = @"hj_home_icon_nanshen";
        }
        
        if ([title isEqualToString:@"女神"]) {
            name = @"hj_home_icon_nvshen";
        }
        
        //MARK:补充.根据ID获取图标（#9 新秀  8 交友 5 陪玩 6 电台 7 游戏）
        if (self.tagID == 9) {
            name = @"hj_home_icon_xinxiu";
        } else if (self.tagID == 8) {
            name = @"hj_home_icon_jiaoyou";
        } else if (self.tagID == 5) {
            name = @"hj_home_icon_pewna";
        } else if (self.tagID == 6) {
            name = @"hj_home_icon_diantai";
        } else if (self.tagID == 7) {
            name = @"hj_home_icon_yule";
        }
        
        return name;
    }
    return @"";
    
    
    
}


@end
