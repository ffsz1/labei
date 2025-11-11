//
//  HJGiftMicCell.m
//  HJLive
//
//  Created by apple on 2019/7/9.
//

#import "HJGiftMicCell.h"

@interface HJGiftMicCell ()

@property (weak, nonatomic) IBOutlet UIImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UIImageView *selImageView;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;



@end

@implementation HJGiftMicCell


- (void)setSelStytle:(BOOL)isSel
{
    if ([self.numLabel.text isEqualToString:@"全麦"]) {
        self.numLabel.text = @"";
         self.imgView2.image =[UIImage imageNamed:@""];
        self.imgView1.image = isSel?[UIImage imageNamed:@"hj_quanmai_flag_sel"]:[UIImage imageNamed:@"hj_quanmai_flag"];
    }else{
        self.imgView1.image = isSel?[UIImage imageNamed:@"hj_mai_icon_bg_yes"]:[UIImage imageNamed:@"hj_mai_icon_bg"];
        self.imgView2.image =[UIImage imageNamed:@"hj_mai_icon"];
    }
    
    if ([self.numLabel.text isEqualToString:@"房主"]) {
        self.numLabel.font = [UIFont systemFontOfSize:8];
        self.numLabel_bottom_layout.constant = 4;
         self.numLabel.textColor = isSel?UIColorHex(60A6FF):UIColorHex(9A9A9A);
          self.imgView2.image =[UIImage imageNamed:@""];
    }else{
        self.numLabel.font = [UIFont systemFontOfSize:9];
        self.numLabel_bottom_layout.constant = 5;
         self.numLabel.textColor = UIColorHex(ffffff);
    }
    if (isSel) {
        _avatarImageView.layer.borderColor = [UIColor colorWithHexString:@"#60A6FF"].CGColor;
        _avatarImageView.layer.borderWidth = 2;
    }else{
        _avatarImageView.layer.borderColor = [UIColor colorWithHexString:@"#ffffff"].CGColor;
        _avatarImageView.layer.borderWidth = 2;
    }
    
}


- (void)setItem:(NSInteger)item
{
    _item = item;
    if (item==0) {
        self.numLabel.text = @"全麦";
        self.avatarImageView.image = [UIImage imageNamed:@"hj_gift_all_mic"];
        
    }else{
        
        
    }
    
}

- (void)setInfo:(HJIMQueueItem *)info
{
    _info = info;
    
    if (info) {
        [self.avatarImageView qn_setImageImageWithUrl:info.queueInfo.chatRoomMember.avatar placeholderImage:nil type:ImageTypeUserIcon];
        
        if (info.position == -1) {
            self.numLabel.text = @"房主";
           
            
        }else{
            self.numLabel.text = [NSString stringWithFormat:@"%ld",info.position+1];
        }
        
    }
}


@end
