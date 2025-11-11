//
//  YPXCFamilyMessageCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPXCFamilyMessageCell.h"

@interface YPXCFamilyMessageCell ()

@property (weak, nonatomic) IBOutlet UIImageView *photoView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *detailLabel;

@property (weak, nonatomic) IBOutlet UIImageView *lvImageView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *lvW;


@property (weak, nonatomic) IBOutlet UIImageView *mlImageView;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mlW;


@property (weak, nonatomic) IBOutlet UILabel *handleLabel;
@property (weak, nonatomic) IBOutlet UIButton *agreeBtn;
@property (weak, nonatomic) IBOutlet UIButton *refuseBtn;


@end

@implementation YPXCFamilyMessageCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(YPFamilyMessage *)model {
    _model = model;
    
    [self.photoView sd_setImageWithURL:[NSURL URLWithString:model.avatar] placeholderImage:[UIImage imageNamed:default_avatar]];
    self.nameLabel.text = model.nick.length ? model.nick : @"";
    self.detailLabel.text = model.type == 1 ? @"申请加入家族" : @"申请退出家族";
    
    if (model.level > 0) {
        self.lvImageView.image = [UIImage imageNamed:[self setExperMent:model.level]];
        self.lvW.constant = 40.f;
    }
    else {
        self.lvImageView.image = nil;
        self.lvW.constant = 0;
    }
    
    if (model.charm > 0) {
//        self.mlImageView.image = [UIImage imageNamed:[NSString stringWithFormat:@"ml%zd",model.charm]];
        self.mlImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:model.charm]];
        self.mlW.constant = 40.f;
    }
    else {
        self.mlImageView.image = nil;
        self.mlW.constant = 0;
    }
    
//    if (model.isHandle) {
//        self.agreeBtn.hidden = YES;
//        self.refuseBtn.hidden = YES;
//        self.handleLabel.hidden = NO;
//    }
//    else {
//        self.agreeBtn.hidden = NO;
//        self.refuseBtn.hidden = NO;
//        self.handleLabel.hidden = YES;;
//    }
}

-(NSString *)setExperMent:(NSInteger)level
{
    return [NSString getMoneyLevelImageName:level];
//    NSInteger num = level /5;
//    NSInteger nuber = level % 5;
//    if (level %5 == 0) {
//        num = num -1;
//        nuber = nuber + 5;
//    }
//    NSString *dengji;
//    if (num == 0) {
//        dengji = @"qingtong";
//    }else if(num == 1){
//        dengji = @"baiyin";
//    }else if(num == 2){
//        dengji = @"huangjin";
//    }else if(num == 3){
//        dengji = @"bojin";
//    }else if(num == 4){
//        dengji = @"zuanshi";
//    }else if(num == 5){
//        dengji = @"xingyao";
//    }else if(num == 6){
//        dengji = @"wangzhe";
//    }
//    NSString *name = [NSString stringWithFormat:@"%@%ld",dengji,nuber];
//    return name;
}

- (IBAction)agreeBtnAction:(id)sender {
    
    if (self.agreeBtnActionBlock) {
        self.agreeBtnActionBlock();
    }
}

- (IBAction)refuseBtnAction:(id)sender {
    
    if (self.refuseBtnActionBlock) {
        self.refuseBtnActionBlock();
    }
}

@end
