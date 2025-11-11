//
//  HJCarCardCell.m
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJCarCardCell.h"
#import "UIImage+JXGIFImage.h"
@implementation HJCarCardCell

-(void)setIsSel:(BOOL)isSel{
    if (isSel) {
         self.timeLabel.textColor = [UIColor whiteColor];
          self.nameLabel.textColor = [UIColor whiteColor];
          self.moneyLabel.textColor = [UIColor whiteColor];
        self.time.image = [UIImage imageNamed:@"hj_prop_icon_time_white"];
     }else{
         self.time.image = [UIImage imageNamed:@"hj_prop_icon_time"];
          self.timeLabel.textColor = [UIColor colorWithHexString:@"#999999"];
          self.nameLabel.textColor = [UIColor colorWithHexString:@"#999999"];
          self.moneyLabel.textColor = [UIColor colorWithHexString:@"#999999"];
     }
    _isUseLabel.textColor = isSel?[UIColor whiteColor]:UIColorHex(#999999);
}
- (void)setDic:(NSDictionary *)dic {
    _dic = dic;
    self.nameLabel.text = self.isCarSys ? dic[@"carName"] : dic[@"headwearName"];
    self.moneyLabel.text = [NSString stringWithFormat:@"%@", dic[@"goldPrice"]];
    if (!self.isCarSys) {
        self.time.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.timeLabel.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.jinbi.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.moneyLabel.hidden = [self.moneyLabel.text isEqualToString:@"0"];
    }
    if (self.isBackpack) {
        self.remindTimeLabel.hidden = NO;
        self.remindTimeLabel.text = [NSString stringWithFormat:@"剩余%@天",dic[@"daysRemaining"]];
//        self.tryBtn.hidden = YES;
    }else{
        self.remindTimeLabel.hidden = YES;
    }
    
     [self.imgView sd_setImageWithURL:dic[@"picUrl"] placeholderImage: [UIImage imageNamed:@"default_avatar"]];
    
//    if(self.isCarSys){
//        [self.imgView sd_setImageWithURL:dic[@"picUrl"] placeholderImage: [UIImage imageNamed:@"default_avatar"]];
//    }else{
//        //兼容动态头饰
//
//        BOOL isgif  = [dic boolValueForKey:@"hasVggPic" default:NO];;
//        if (isgif) {
//            NSData *imagedata = [NSData dataWithContentsOfURL:[NSURL URLWithString:dic[@"picUrl"]]];//hasVggPic
//            self.imgView.image = [UIImage jx_imageWithSmallGIFData:imagedata scale:1.0];
//        }else {
//             [self.imgView sd_setImageWithURL:dic[@"picUrl"] placeholderImage: [UIImage imageNamed:@"default_avatar"]];
//        }
//
//    }
    
    NSInteger isPurse = [dic[@"isPurse"] integerValue];
    
    self.timeLabel.text = [NSString stringWithFormat:@"%@%@",dic[@"effectiveTime"],NSLocalizedString(XCCarSysDay, nil)];
    if (isPurse == 0) {
        //未购买
//        [self.isUseButton setImage:[UIImage imageNamed:@" "] forState:UIControlStateNormal];
        _isUseLabel.text = @"";
    } else if (isPurse == 1 ) {
        //已购买
//        [self.isUseButton setImage:[UIImage imageNamed:@"hj_prop_icon_weigouxuan"] forState:UIControlStateNormal];
        _isUseLabel.text = @"已拥有";
    } else if (isPurse == 2) {
        //正在使用
//        self.timeLabel.text = [NSString stringWithFormat:@"%@%@",dic[@"daysRemaining"],NSLocalizedString(XCCarSysDay, nil)];
//        [self.isUseButton setImage:[UIImage imageNamed:@"hj_prop_icon_yixuan"] forState:UIControlStateNormal];
        _isUseLabel.text = @"已装备";
    }
    
//    // 新增显示 new标签： isNew  1：新上架 0：旧上架
//    if([dic[@"isNew"] intValue] == 1){
//        self.isNewImgView.hidden = NO;
//    }else{
//        self.isNewImgView.hidden = YES;
//    }
}


- (IBAction)isUseClick:(UIButton *)sender {
    if (self.isUseBlock) {
        self.isUseBlock();
    }
}

- (void)imgClick:(UITapGestureRecognizer *)sender {
    //    if (self.imgBlock) {
    //        self.imgBlock();
    //    }
}
- (IBAction)tryAction:(id)sender {
    if (self.isCarSys) {
        if (self.playBlock) {
            self.playBlock();
        }
    }
}

- (void)playImageClickAction{
    if (self.playBlock) {
        self.playBlock();
    }
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    //    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(imgClick:)];
    //    [self.imgView addGestureRecognizer:tap];
    
//    self.buyButton.layer.borderWidth = 0.5;
//    self.buyButton.layer.borderColor = UIColorHex(ff5fab).CGColor;
    
//    UITapGestureRecognizer *playTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(playImageClickAction)];
//    self.playImageView.userInteractionEnabled = YES;
//    [self.playImageView addGestureRecognizer:playTap];
    self.playImageView.hidden = YES;
}

@end
