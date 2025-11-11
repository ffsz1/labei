//
//  YPHomeFollowCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeFollowCell.h"

@implementation YPHomeFollowCell


- (IBAction)voiceBtnAction:(id)sender {
    if (_clickVoiceBtnBlock) {
        _clickVoiceBtnBlock(_model,self);
    }
   
}



- (IBAction)followBtnAction:(id)sender {
    
    if (self.followBlock) {
        self.followBlock(self.model.uid,self.model.isFan);
    }
    
}

- (void)setModel:(UserInfo *)model
{
    _model = model;
    
    if (model) {
        [self.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        
        self.nameLabel.text = model.nick;
        self.sexImageView.image = [UIImage imageNamed:model.gender==UserInfo_Male?@"yp_home_attend_man":@"yp_home_attend_woman"];
       
        if (model.userDesc!=nil && model.userDesc.length>0) {
                     self.idLabel.text = [NSString stringWithFormat:@"%@",model.userDesc];
               }else{
                   self.idLabel.text = @"这个人很懒，什么都没有留下~";
               }
        self.timeLabel.text = [NSString stringWithFormat:@"%ld″",model.voiceDura];
        self.followBtn.selected = model.isFan;
        
        //    192
        
        
        
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, 20) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
        
        if ((kScreenWidth-192)<size.width) {
            self.width_name.constant = kScreenWidth - 192;
        }else{
            self.width_name.constant = size.width;
        }
    }
    
    
    
}

@end
