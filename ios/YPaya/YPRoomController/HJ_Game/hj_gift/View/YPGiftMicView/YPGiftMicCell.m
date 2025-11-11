//
//  YPGiftMicCell.m
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import "YPGiftMicCell.h"

@interface YPGiftMicCell ()

@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *selImageView;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;



@end

@implementation YPGiftMicCell


- (void)setSelStytle:(BOOL)isSel
{
    if ([self.numLabel.text isEqualToString:@"全麦"]) {
        self.numLabel.text = @"";
        self.avatarImageView.image = isSel?[UIImage imageNamed:@"yp_quanmai_yes"]:[UIImage imageNamed:@"yp_quanmai_no"];
         self.imgView2.image =[UIImage imageNamed:@""];
        self.imgView1.image =[UIImage imageNamed:@""];
//        self.imgView1.image = isSel?[UIImage imageNamed:@"yp_quanmai_flag_sel"]:[UIImage imageNamed:@"yp_quanmai_flag"];
    }else{
        self.imgView1.image = isSel?[UIImage imageNamed:@"yp_mai_icon_bg_yes"]:[UIImage imageNamed:@"yp_mai_icon_bg"];
//        self.imgView2.image =[UIImage imageNamed:@"yp_mai_icon"];
        self.imgView2.image =[UIImage imageNamed:@""];
//        self.avatarImageView.image =[UIImage imageNamed:@""];
    }
    
    if ([self.numLabel.text isEqualToString:@"房主"]) {
        self.numLabel.font = [UIFont systemFontOfSize:8];
        self.numLabel_bottom_layout.constant = 4;
         self.numLabel.textColor = isSel?UIColorHex(FFFFFF):UIColorHex(9A9A9A);
          self.imgView2.image =[UIImage imageNamed:@""];
    }else{
        self.numLabel.font = [UIFont systemFontOfSize:9];
        self.numLabel_bottom_layout.constant = 5;
         self.numLabel.textColor = UIColorHex(ffffff);
    }
    if (isSel) {
        _avatarImageView.layer.borderColor = [UIColor colorWithHexString:@"#8A58FF"].CGColor;
        _avatarImageView.layer.borderWidth = 2;
    }else{
        _avatarImageView.layer.borderColor = [UIColor clearColor].CGColor;
        _avatarImageView.layer.borderWidth = 2;
    }
    
}


- (void)setItem:(NSInteger)item
{
    _item = item;
    if (item==0) {
        self.numLabel.text = @"全麦";
//        self.avatarImageView.image = [UIImage imageNamed:@"yp_gift_all_mic"];
        
    }else{
        
        
    }
    
}

- (void)setInfo:(YPIMQueueItem *)info
{
    _info = info;
    
    if (info) {
        [self.avatarImageView qn_setImageImageWithUrl:info.queueInfo.chatRoomMember.avatar placeholderImage:nil type:ImageTypeUserIcon];
        
        if (info.position == -1) {
            self.numLabel.text = @"房主";
           
            
        }else{
            self.numLabel.text = [NSString stringWithFormat:@"%ld麦",info.position+1];
        }
        
    }
}


@end
