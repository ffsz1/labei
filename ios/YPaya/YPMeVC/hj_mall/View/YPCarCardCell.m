//
//  YPCarCardCell.m
//  HJLive
//
//  Created by feiyin on 2020/4/12.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPCarCardCell.h"

@implementation YPCarCardCell
/*
 @property (weak, nonatomic) IBOutlet UILabel *nameLabel;
 @property (weak, nonatomic) IBOutlet UILabel *moneyLabel;
 @property (weak, nonatomic) IBOutlet UIImageView *imgView;
 @property (weak, nonatomic) IBOutlet UIButton *isUseButton;
 @property (weak, nonatomic) IBOutlet UILabel *timeLabel;
 
- (void)setDic:(NSDictionary *)dic {
    _dic = dic;
    self.nameLabel.text = self.isCarSys ? dic[@"carName"] : dic[@"headwearName"];
    self.moneyLabel.text = [NSString stringWithFormat:@"%@", dic[@"goldPrice"]];
    if (!self.isCarSys) {
        self.time.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.timeLabel.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.jinbi.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.moneyLabel.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.buyButton.hidden = [self.moneyLabel.text isEqualToString:@"0"];
    }
    
    [self.imgView sd_setImageWithURL:dic[@"picUrl"] placeholderImage: [UIImage imageNamed:placeholder_image_rectangle]];
    NSInteger isPurse = [dic[@"isPurse"] integerValue];
    if (isPurse == 0) {
      //未购买
        self.timeLabel.text = [NSString stringWithFormat:@"%@%@",dic[@"effectiveTime"],NSLocalizedString(XCCarSysDay, nil)];
        [self.isUseButton setTitle:self.isCarSys ? NSLocalizedString(XCCarSysTryToDrive, nil) : @"" forState:UIControlStateNormal];
        [self.isUseButton setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        [self.isUseButton setImage:[UIImage imageNamed:@""] forState:UIControlStateNormal];
        self.buyButton.backgroundColor = UIColorHex(00C2FF);
        [self.buyButton setTitle:NSLocalizedString(XCCarSysBuy, nil) forState:UIControlStateNormal];
    } else if (isPurse == 1 ) {
        //已购买
        self.timeLabel.text = [NSString stringWithFormat:@"%@%@",dic[@"daysRemaining"],NSLocalizedString(XCCarSysDay, nil)];
        [self.isUseButton setTitle:NSLocalizedString(XCCarSysUse, nil) forState:UIControlStateNormal];
        [self.isUseButton setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        [self.isUseButton setImage:[UIImage imageNamed:@"yp_zuojia_gx_moren"] forState:UIControlStateNormal];
        self.buyButton.backgroundColor = UIColorHex(00C2FF);
        [self.buyButton setTitle:NSLocalizedString(XCCarSysBuyAgain, nil) forState:UIControlStateNormal];
    } else if (isPurse == 2) {
        //正在使用
        self.timeLabel.text = [NSString stringWithFormat:@"%@%@",dic[@"daysRemaining"],NSLocalizedString(XCCarSysDay, nil)];
        [self.isUseButton setTitle:self.isCarSys ? NSLocalizedString(XCCarSysDriving, nil) : NSLocalizedString(XCCarSysUsing, nil) forState:UIControlStateNormal];
        [self.isUseButton setTitleColor:UIColorHex(EA3131) forState:UIControlStateNormal];
        [self.isUseButton setImage:[UIImage imageNamed:@"yp_zuojia_gx_dianji"] forState:UIControlStateNormal];
        self.buyButton.backgroundColor = UIColorHex(00C2FF);
        [self.buyButton setTitle:NSLocalizedString(XCCarSysBuyAgain, nil) forState:UIControlStateNormal];
    }
}

- (IBAction)buyOrlConsClick:(UIButton *)sender {
    if (self.buyBlock) {
        self.buyBlock();
    }
}

- (IBAction)isUseClick:(UIButton *)sender {
    if (self.isUseBlock) {
        self.isUseBlock();
    }
}

- (void)imgClick:(UITapGestureRecognizer *)sender {
    if (self.imgBlock) {
        self.imgBlock();
    }
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(imgClick:)];
    [self.imgView addGestureRecognizer:tap];
}
*/
-(void)setIsSel:(BOOL)isSel{
    if (isSel) {
         self.timeLabel.textColor = [UIColor whiteColor];
          self.nameLabel.textColor = [UIColor whiteColor];
          self.moneyLabel.textColor = [UIColor whiteColor];
        self.time.image = [UIImage imageNamed:@"yp_prop_icon_time_white"];
        self.shijiaLabel.textColor = [UIColor whiteColor];
     }else{
         self.shijiaLabel.textColor = [UIColor colorWithHexString:@"#8A58FF"];
         self.time.image = [UIImage imageNamed:@"yp_prop_icon_time"];
          self.timeLabel.textColor = [UIColor colorWithHexString:@"#999999"];
          self.nameLabel.textColor = [UIColor colorWithHexString:@"#999999"];
          self.moneyLabel.textColor = [UIColor colorWithHexString:@"#999999"];
     }
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
        self.buyButton.hidden = [self.moneyLabel.text isEqualToString:@"0"];
        self.playImageView.hidden = YES;
    }
    
    [self.imgView sd_setImageWithURL:dic[@"picUrl"] placeholderImage: [UIImage imageNamed:@"default_avatar"]];
    NSInteger isPurse = [dic[@"isPurse"] integerValue];
    if (isPurse == 0) {
        //未购买
        self.timeLabel.text = [NSString stringWithFormat:@"/%@%@",dic[@"effectiveTime"],NSLocalizedString(XCCarSysDay, nil)];
        [self.isUseButton setImage:[UIImage imageNamed:@" "] forState:UIControlStateNormal];
//        self.buyButton.backgroundColor = UIColorHex(ff5fab);
//        [self.buyButton setTitleColor:UIColorHex(ffffff) forState:UIControlStateNormal];
//        [self.buyButton setTitle:NSLocalizedString(XCCarSysBuy, nil) forState:UIControlStateNormal];
    } else if (isPurse == 1 ) {
        //已购买
        self.timeLabel.text = [NSString stringWithFormat:@"/%@%@",dic[@"daysRemaining"],NSLocalizedString(XCCarSysDay, nil)];
        [self.isUseButton setImage:[UIImage imageNamed:@"yp_prop_icon_weigouxuan"] forState:UIControlStateNormal];
//        self.buyButton.backgroundColor = UIColorHex(ffffff);
//        [self.buyButton setTitleColor:UIColorHex(ff5fab) forState:UIControlStateNormal];
//        [self.buyButton setTitle:NSLocalizedString(XCCarSysBuyAgain, nil) forState:UIControlStateNormal];
    } else if (isPurse == 2) {
        //正在使用
        self.timeLabel.text = [NSString stringWithFormat:@"/%@%@",dic[@"daysRemaining"],NSLocalizedString(XCCarSysDay, nil)];
        [self.isUseButton setImage:[UIImage imageNamed:@"yp_prop_icon_yixuan"] forState:UIControlStateNormal];
//        self.buyButton.backgroundColor = UIColorHex(ffffff);
//        [self.buyButton setTitleColor:UIColorHex(ff5fab) forState:UIControlStateNormal];
//        [self.buyButton setTitle:NSLocalizedString(XCCarSysBuyAgain, nil) forState:UIControlStateNormal];
    }
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

- (void)playImageClickAction{
    if (self.playBlock) {
        self.playBlock();
    }
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    //    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(imgClick:)];
    //    [self.imgView addGestureRecognizer:tap];
    
    self.buyButton.layer.borderWidth = 0.5;
    self.buyButton.layer.borderColor = UIColorHex(ff5fab).CGColor;
    
    UITapGestureRecognizer *playTap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(playImageClickAction)];
    self.playImageView.userInteractionEnabled = YES;
    [self.playImageView addGestureRecognizer:playTap];
    
    
    NSDictionary *underlineAttribute = @{NSUnderlineStyleAttributeName: @(NSUnderlineStyleSingle)};
    self.shijiaLabel.attributedText = [[NSAttributedString alloc] initWithString:@"点击试驾"
                                                                 attributes:underlineAttribute];
    
    
}

@end
