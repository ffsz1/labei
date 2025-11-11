//
//  GGMyFamilyCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "GGMyFamilyCell.h"

#import "HJRoomViewControllerCenter.h"

#import "NSString+NIMKit.h"

@implementation GGMyFamilyCell

- (IBAction)fingHimBtnAction:(UIButton *)sender {
    
    if (self.findBlock) {
        self.findBlock(self.model.uid);
    }
    
    
    
    
}
- (IBAction)headBtnAction:(UIButton *)sender {
    
    if (self.headTapBlock) {
        self.headTapBlock(self.model.uid,self.model.roleStatus);
    }
    
}






- (void)setModel:(HJFamilyInfoDetail *)model
{
    _model = model;
    
    [self.avatar sd_setImageWithURL:[NSURL URLWithString:[model.avatar cutAvatarImageSize]]];
    self.nameLabel.text = model.nike;
    self.tipLabel.text = model.userDesc;
    self.levelImageView.image = [UIImage imageNamed:[self setExperMent:model.level]];
    self.roleImageView.image = [UIImage imageNamed:[self getRoleImageName:model.roleStatus]];
    
    [self updateNameWidth];
}

- (void)updateNameWidth
{
//    175
    if (_model.nike != nil) {
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.size.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
        
        CGFloat width = ((kScreenWidth - size.width)<175)?175:(size.width+2);
        self.name_width.constant = width;
    }else self.name_width = 0;


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

- (NSString *)getRoleImageName:(NSInteger)role
{
    NSString *roleName = @"";
    if(role == 1){
        roleName = @"hj_family_owner";
    }else if(role == 2){
        roleName = @"hj_family_manager";
    }else if(role == 3){
        roleName = @"";
    }
    return roleName;
}



@end
