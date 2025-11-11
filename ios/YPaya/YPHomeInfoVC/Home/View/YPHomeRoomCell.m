//
//  YPHomeRoomCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeRoomCell.h"

#import "NSString+YPNIMKit.h"
#import "UIImage+Gif.h"

@implementation YPHomeRoomCell

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    self.voiceButton.layer.cornerRadius = 10;
    self.voiceButton.layer.masksToBounds=YES;
//    self.mengxinBG.layer.cornerRadius = 10;
//       self.mengxinBG.layer.masksToBounds=YES;
    self.avatarImageView.userInteractionEnabled = YES;
    self.nameLabel.userInteractionEnabled = YES;
       UITapGestureRecognizer *tapGestureRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAvatarAction:)];
       [self.avatarImageView addGestureRecognizer:tapGestureRecognizer];
    
    UITapGestureRecognizer *tapGestureRecognizer2 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAvatarAction:)];
    [self.nameLabel addGestureRecognizer:tapGestureRecognizer2];
    self.sexImageView = [[UIImageView alloc] init];
    [self.contentView addSubview:self.sexImageView];
    self.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
    
}
-(void)setPeipeiModel:(YPHomePeipeiModel *)peipeiModel{
    _peipeiModel = peipeiModel;
    //语音
    self.type = 2;
    self.voiceButton.hidden = NO;
    self.gifImageview.hidden = NO;
    self.voiceTime.hidden = NO;
    self.tagLabel.hidden = YES;
    self.idLabel.hidden = YES;
    
   
    self.mengxinBG.hidden = YES;
    self.mengxinStart.hidden = YES;
    self.mengxinLabel.hidden = YES;
    //
    self.nameLabel.text = peipeiModel.nick;
     self.desLabel.text = @"这个人很懒，什么都没有留下~";
       if (peipeiModel.userDescription!=nil) {
             if (![peipeiModel.userDescription isEqualToString:@""]) {
                     self.desLabel.text = peipeiModel.userDescription;
                 }
       }
    self.voiceTime.text = [NSString stringWithFormat:@"%ld″",peipeiModel.voiceDuration];
    self.animationImageView.image = [UIImage animatedGIFNamed:@"yp_home_people333"];
    self.flagLabel.text = @"";
     self.peopleBgImageView.image = [UIImage imageNamed:@"yp_first_findTa_icon"];
    self.numLabel.text = @"";
    [self.avatarImageView qn_setImageImageWithUrl:peipeiModel.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
    
    CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
          
           CGFloat width = size.width + 15;
    
    self.sexImageView.frame = CGRectMake( CGRectGetMaxX(self.avatarImageView.frame)+  width, CGRectGetMinY(self.nameLabel.frame)+3, 12, 12);
       if (peipeiModel.gender ==1 ) {
            self.sexImageView.image= [UIImage imageNamed:@"yp_home_attend_man"];
       }else{
              self.sexImageView.image= [UIImage imageNamed:@"yp_home_attend_woman"];
       }
    
    
}
-(void)setMengxinModel:(YPHomeMengxinModel *)mengxinModel{
    _mengxinModel = mengxinModel;
    
//       CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
    
     self.type = 3;
    self.voiceButton.hidden = YES;
       self.gifImageview.hidden = YES;
    self.voiceTime.hidden = YES;
    self.tagLabel.hidden = YES;
    self.idLabel.hidden = YES;
    
    self.mengxinBG.hidden = NO;
    self.mengxinStart.hidden = NO;
    self.mengxinLabel.hidden = NO;
    
    if (mengxinModel.isFirstCharge) {
          self.mengxinBG.image = [UIImage imageNamed:@"yp_mengxin_icon_new"];
    }else{
         self.mengxinBG.image = [UIImage imageNamed:@"yp_xinjing_icon_new"];
    }
    //添加财富等级标签
    if (mengxinModel.experLevel>1) {
        self.caifuLevImageView.hidden = NO;
         self.caifuLevImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:mengxinModel.experLevel]];
    }else{
        self.caifuLevImageView.hidden = YES;
    }
   
     self.desLabel.text = @"这个人很懒，什么都没有留下~";
       if (mengxinModel.userDescription!=nil) {
           
           if (![mengxinModel.userDescription isEqualToString:@""]) {
               self.desLabel.text = mengxinModel.userDescription;
           }
       }
    self.nameLabel.text = mengxinModel.nick;
    self.animationImageView.image = [UIImage animatedGIFNamed:@"yp_home_people333"];
    self.flagLabel.text = @"";
    self.peopleBgImageView.image = [UIImage imageNamed:@"yp_first_chat_icon"];
    self.numLabel.text = @"";
    
     [self.avatarImageView qn_setImageImageWithUrl:mengxinModel.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
   CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
          
           CGFloat width = size.width + 15;
    
    self.sexImageView.frame = CGRectMake( CGRectGetMaxX(self.avatarImageView.frame)+  width, CGRectGetMinY(self.nameLabel.frame)+3, 12, 12);
    if (mengxinModel.gender ==1 ) {
             self.sexImageView.image= [UIImage imageNamed:@"yp_home_attend_man"];
       }else{
            self.sexImageView.image= [UIImage imageNamed:@"yp_home_attend_woman"];
           
       }
    
}
- (void)setModel:(YPHomePageInfo *)model
{
    _model = model;
     self.type = 1;
     self.flagLabel.text = @"";
    self.voiceButton.hidden = YES;
       self.gifImageview.hidden = YES;
    self.voiceTime.hidden = YES;
    self.tagLabel.hidden = NO;
    self.idLabel.hidden = NO;
    
    self.mengxinBG.hidden = YES;
    self.mengxinStart.hidden = YES;
    self.mengxinLabel.hidden = YES;
      self.desLabel.text = @"这个人很懒，什么都没有留下~";
      if (model.userDescription!=nil) {
          if (![model.userDescription isEqualToString:@""]) {
              self.desLabel.text = model.userDescription;
          }
            
        }
//    NSString *avatarStr = [model[@"avatar"] cutAvatarImageSize];
    
    [self.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
//    self.idLabel.text = model[];
    
    [self.tagLabel qn_setImageImageWithUrl:model.tagPict placeholderImage:NSLocalizedString(XCHomeOther, nil) type:ImageTypeRoomGift];
    
    NSString *roomName = model.title;
    if (roomName.length>13) {
        roomName = [NSString stringWithFormat:@"%@...",[roomName substringToIndex:13]];
    }
    self.nameLabel.text = roomName;

    self.numLabel.text = [NSString stringWithFormat:@"%ld",(long)model.onlineNum];

    NSString *idText = [NSString stringWithFormat:@"ID:%@",[model.erbanNo description]];
    self.idLabel.text = idText;
    
    if ([model.badge length] > 0) {
        self.channelLabel.hidden = NO;
        self.left_id_width.constant = 8;
        
        [self.channelLabel qn_setImageImageWithUrl:[NSString stringWithFormat:@"%@",model.badge] placeholderImage:nil type:ImageTypeRoomGift success:nil];
        
    }else{
        self.channelLabel.hidden = YES;
        self.left_id_width.constant = -53;

    }
    
    self.animationImageView.image = [UIImage animatedGIFNamed:@"yp_home_people"];
    
}


- (IBAction)voiceButtonAction:(id)sender {
    
    if (_clickVoiceBtnBlock) {
        _clickVoiceBtnBlock(_peipeiModel,self);
    }
    
//    if (!self.peipeiModel.userVoice) {
//
//        [MBProgressHUD showError:@"暂无声音"];
//        BOOL isOwner = [GetCore(YPAuthCoreHelp) getUid].userIDValue == self.userID;
//
//        if (isOwner) {
//            YPMICRecordVC *MICRecordViewController = [YPMICRecordVC new];
//            [self.navigationController pushViewController:MICRecordViewController animated:YES];
//        }
//
//        return;
//    }
   
    
}
- (IBAction)gotoAction:(id)sender {
    if (_type ==2){
        if (_tapGotoTaBlock) {
            _tapGotoTaBlock(_peipeiModel,self);
        }
    }else if (_type ==3){
        if (_tapChatBlock) {
            _tapChatBlock(_mengxinModel,self);
        }
    }
    
}
- (void)tapAvatarAction:(UITapGestureRecognizer*)tap {
    if (_tapAvatarBlock) {
        if (_type ==1 ) {
            _tapAvatarBlock(_model.uid,self,1);
        }else if (_type ==2){
               _tapAvatarBlock(_peipeiModel.uid,self,2);
        }else if (_type ==3){
               _tapAvatarBlock(_mengxinModel.uid,self,3);
        }
    }
    
}

@end
