//
//  YPHomePeipeiTableCell.m
//  HJLive
//
//  Created by feiyin on 2020/9/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomePeipeiTableCell.h"
#import "YPHomeRecommedRoomModel.h"
#import "YPRoomViewControllerCenter.h"
#import "YPWKWebViewController.h"
#import "UIView+getTopVC.h"
#import "UIImage+Gif.h"
#import "YPHomePageInfo.h"


@interface YPHomePeipeiTableCell()

@property (nonatomic,strong) UIImageView *roomImageView;
@property (nonatomic,strong) UILabel *nameLabel;
@property (nonatomic,strong) UIView *peopleView;

@property (nonatomic,strong) UILabel *peopleLabel;

@property (nonatomic,strong) UIImageView *roomImageView2;
@property (nonatomic,strong) UILabel *nameLabel2;
@property (nonatomic,strong) UIView *peopleView2;

@property (nonatomic,strong) UILabel *peopleLabel2;

@property (nonatomic,strong) UIImageView *flagImgView;
@property (nonatomic,strong) UIImageView *flagImgView2;
@property  (nonatomic, strong) UIImageView* sexImageView;
@property  (nonatomic, strong) UIImageView* sexImageView2;
@property (nonatomic,strong) UIImageView *shadowImageView;
@property (nonatomic,strong) UIImageView *shadowImageView2;

@property (nonatomic,strong) UIImageView *animationImageView;
@property (nonatomic,strong) UIImageView *animationImageView2;

@end
@implementation YPHomePeipeiTableCell

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithStyle:style
                reuseIdentifier:reuseIdentifier];
    if (self) {
        [self setUI];
    }
    return self;
}

- (void)setUI
{
    self.backgroundColor = [UIColor colorWithHexString:@"FAFAFA"];
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    
    
    [self.shadowImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.bottom.mas_equalTo(self.contentView.mas_bottom).offset(0);
          make.left.mas_equalTo(self.contentView).offset(8);
           make.height.mas_equalTo(HJHomePeipeiTableCellWidth*(110.0/169)+8);
          make.width.mas_equalTo(HJHomePeipeiTableCellWidth+8);
    }];

    [self.shadowImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
       make.bottom.mas_equalTo(self.contentView.mas_bottom).offset(0);
         make.right.mas_equalTo(self.contentView).offset(-8);
        make.height.mas_equalTo(HJHomePeipeiTableCellWidth*(110.0/169)+8);
          make.width.mas_equalTo(HJHomePeipeiTableCellWidth+8);
    }];
    
    [self.animationImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(HJHomePeipeiTableCellWidth-20, HJHomePeipeiTableCellWidth-20));
        
        make.centerX.equalTo(self.shadowImageView);
        make.centerY.equalTo(self.shadowImageView.mas_top);
    }];
    
    [self.animationImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(HJHomePeipeiTableCellWidth-20, HJHomePeipeiTableCellWidth-20));
        
        make.centerX.equalTo(self.shadowImageView2);
        make.centerY.equalTo(self.shadowImageView2.mas_top);
    }];
    
    [self.roomImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(HJHomePeipeiTableCellWidth-50, HJHomePeipeiTableCellWidth-50));

          make.centerX.equalTo(self.shadowImageView);
        make.centerY.equalTo(self.shadowImageView.mas_top);

    }];
    
    [self.roomImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
         make.size.mas_equalTo(CGSizeMake(HJHomePeipeiTableCellWidth-50, HJHomePeipeiTableCellWidth-50));

        make.centerX.equalTo(self.shadowImageView2);
         make.centerY.equalTo(self.shadowImageView.mas_top);
    }];
    
    
    [self.nameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
          make.centerX.equalTo(self.shadowImageView).offset(-8);
        make.top.mas_equalTo(self.roomImageView.mas_bottom).offset(10);
    }];
    
    [self.nameLabel2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.centerX.equalTo(self.shadowImageView2).offset(-8);
        make.top.mas_equalTo(self.roomImageView2.mas_bottom).offset(10);
    }];
    
    [self.sexImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
                
           make.left.mas_equalTo(self.nameLabel.mas_right).offset(5);
              make.centerY.mas_equalTo(self.nameLabel);
               make.size.mas_equalTo(CGSizeMake(12, 12));
             }];
          [self.sexImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
                  make.left.mas_equalTo(self.nameLabel2.mas_right).offset(5);
                        make.centerY.mas_equalTo(self.nameLabel2);
                         make.size.mas_equalTo(CGSizeMake(12, 12));
             }];
    
   
    
    [self.peopleView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(73, 23));

         make.centerX.equalTo(self.roomImageView);
        make.bottom.mas_equalTo(self.roomImageView.mas_bottom).offset(-4);
    }];
    
    [self.peopleView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(73, 23));
         make.centerX.equalTo(self.roomImageView2);
         make.bottom.mas_equalTo(self.roomImageView2.mas_bottom).offset(-4);
    }];
    
    [self.voiceButton mas_remakeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(20, 20));
          make.centerY.mas_equalTo(self.peopleView);
          make.left.mas_equalTo(self.peopleView).offset(0);
      }];
    [self.voiceButton2 mas_remakeConstraints:^(MASConstraintMaker *make) {
          make.size.mas_equalTo(CGSizeMake(20, 20));
          make.centerY.mas_equalTo(self.peopleView2);
          make.left.mas_equalTo(self.peopleView2).offset(0);
      }];
    
    
    
    [self.peopleImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(15.6, 12));
        make.centerY.mas_equalTo(self.peopleView);
        make.left.mas_equalTo(self.voiceButton.mas_right).offset(8);
    }];
    
    [self.peopleImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(15.6, 12));
        make.centerY.mas_equalTo(self.peopleView2);
        make.left.mas_equalTo(self.voiceButton2.mas_right).offset(8);
    }];
    
    [self.peopleLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
       make.centerY.mas_equalTo(self.peopleView);
        make.right.mas_equalTo(self.peopleView).offset(-3);
        make.left.mas_equalTo(self.peopleImageView.mas_right).offset(1);
    }];
    
    [self.peopleLabel2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.peopleView2);
        make.right.mas_equalTo(self.peopleView2).offset(-3);
        make.left.mas_equalTo(self.peopleImageView2.mas_right).offset(1);

    }];

    [self.flagImgView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(30, 16));
        make.top.mas_equalTo(self.roomImageView.mas_top).offset(0);
        make.left.mas_equalTo(self.roomImageView).offset(0);
    }];
    [self.flagImgView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
           make.size.mas_equalTo(CGSizeMake(30, 16));
           make.top.mas_equalTo(self.roomImageView2.mas_top).offset(0);
           make.left.mas_equalTo(self.roomImageView2).offset(0);
       }];

}

- (void)initData
{
    if (self.roomArr.count>0) {
          YPHomePeipeiModel  *room = self.roomArr[0];
        self.peipeiModel1 = room;
        self.peipeiModel1.isLeft = YES;
        [self.roomImageView qn_setImageImageWithUrl:room.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
        self.nameLabel.text = room.nick;
        if (room.voiceDuration <1) {
                    self.peopleView.hidden = YES;
               }else{
                    self.peopleView.hidden = NO;
                    self.peopleLabel.text = [NSString stringWithFormat:@"%ld″",room.voiceDuration];
               }
        self.peopleImageView.image = [UIImage imageNamed:@"home_first_peipei_bolang1"];
        if (room.gender == 1) {
            _sexImageView.image = [UIImage imageNamed:@"yp_home_attend_man"];
             _roomImageView.layer.borderColor = [UIColor colorWithHexString:@"#7ECDFF"].CGColor;
        }else{
             _roomImageView.layer.borderColor = [UIColor colorWithHexString:@"#FFB4F9"].CGColor;
             _sexImageView.image = [UIImage imageNamed:@"yp_home_attend_woman"];
        }
        
    }
    
    if (self.roomArr.count>1) {
        YPHomePeipeiModel *room = self.roomArr[1];
        self.peipeiModel2 = room;
         self.peipeiModel2.isLeft = NO;
        [self.roomImageView2 qn_setImageImageWithUrl:room.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
        self.nameLabel2.text = room.nick;
        if (room.voiceDuration <1) {
             self.peopleView2.hidden = YES;
        }else{
             self.peopleView2.hidden = NO;
             self.peopleLabel2.text = [NSString stringWithFormat:@"%ld″",room.voiceDuration];
        }
       
        self.nameLabel2.hidden = NO;
        self.flagImgView.hidden = NO;
        self.roomImageView2.hidden = NO;
         self.shadowImageView2.hidden = NO;
        self.sexImageView2.hidden = NO;
        self.peopleImageView2.image = [UIImage imageNamed:@"home_first_peipei_bolang1"];
        if (room.gender == 1) {
             _sexImageView2.image = [UIImage imageNamed:@"yp_home_attend_man"];
             _roomImageView2.layer.borderColor = [UIColor colorWithHexString:@"#7ECDFF"].CGColor;
        }else{
             _sexImageView2.image = [UIImage imageNamed:@"yp_home_attend_woman"];
             _roomImageView2.layer.borderColor = [UIColor colorWithHexString:@"#FFB4F9"].CGColor;
        }
        
    }else{
        self.nameLabel2.hidden = YES;
        self.peopleView2.hidden = YES;
        self.roomImageView2.hidden = YES;
        self.flagImgView.hidden = YES;
        self.shadowImageView2.hidden = YES;
        self.sexImageView2.hidden = YES;
    }
    
    [self.sexImageView mas_updateConstraints:^(MASConstraintMaker *make) {
          make.left.mas_equalTo(self.nameLabel.mas_right).offset(5);
    }];
    [self.sexImageView2 mas_updateConstraints:^(MASConstraintMaker *make) {
         make.left.mas_equalTo(self.nameLabel2.mas_right).offset(5);
    }];
 
//    [self setAnimation];
    
}

- (void)setAnimation{
    if (self.peipeiModel1.roomState ==1) {
        NSMutableArray  *arrayM=[NSMutableArray array];
           for (int i=0; i<20; i++) {
               [arrayM addObject:[UIImage imageNamed:[NSString stringWithFormat:@"xc_peipei_speak_1_0000%d",i]]];
           }
           [self.animationImageView setAnimationImages:arrayM];
           [self.animationImageView setAnimationRepeatCount:0];
        [self.animationImageView setAnimationDuration:1.8f];
           [self.animationImageView startAnimating];
    }
   
    if (self.peipeiModel2.roomState ==1) {
         NSMutableArray  *arrayM=[NSMutableArray array];
                  for (int i=0; i<20; i++) {
                      [arrayM addObject:[UIImage imageNamed:[NSString stringWithFormat:@"xc_peipei_speak_1_0000%d",i]]];
                  }
        [self.animationImageView2 setAnimationImages:arrayM];
        [self.animationImageView2 setAnimationRepeatCount:0];
        [self.animationImageView2 setAnimationDuration:1.8f];
        [self.animationImageView2 startAnimating];
    }
    
}

#pragma mark - private method

-(void)voiceButtonAction{
    if (_tapVoiceBtnForPeiPeiBlock) {
      _tapVoiceBtnForPeiPeiBlock(self.peipeiModel1,self);
    }
}
-(void)voiceButton2Action{
    if (_tapVoiceBtnForPeiPeiBlock) {
        _tapVoiceBtnForPeiPeiBlock(self.peipeiModel2,self);
      }
}

-(void)tapTitleAction{
    if (_tapTilteLeftBlock) {
           _tapTilteLeftBlock(self.peipeiModel1,self);
       }
}
-(void)tapTitle2Action{
    if (_tapTilteRightBlock) {
           _tapTilteRightBlock(self.peipeiModel2,self);
       }
}
- (void)tapAction
{
//    YPHomeRecommedRoomModel *room = [self.roomArr safeObjectAtIndex:0];
//    [self pushRoom:room.uid];
    if (_tapGotoTaLeftBlock) {
        _tapGotoTaLeftBlock(self.peipeiModel1,self);
    }
}

- (void)tapAction2
{
    if (self.roomArr.count<2) {
        [self pushToGreenH5];
        return;
    }
//    YPHomeRecommedRoomModel *room = [self.roomArr safeObjectAtIndex:1];
//    [self pushRoom:room.uid];
    if (_tapGotoTaRightBlock) {
           _tapGotoTaRightBlock(self.peipeiModel2,self);
       }
}

- (void)tapAction3
{
    if (self.roomArr.count<3) {
//        [self pushToH5];
        return;
    }
    YPHomeRecommedRoomModel *room = [self.roomArr safeObjectAtIndex:2];
    [self pushRoom:room.uid];
}

- (void)pushToGreenH5
{
//    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
//     NSString *urlSting = [NSString stringWithFormat:@"%@/front/convention/index.html",[YPHttpRequestHelper getHostUrl]];
//    webView.url = [NSURL URLWithString:urlSting];
//    [[self topViewController].navigationController pushViewController:webView animated:YES];
}

- (void)pushToH5
{
//    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
//    NSString *urlSting = [NSString stringWithFormat:@"%@/front/hotroom/index.html",[YPHttpRequestHelper getHostUrl]];
//    webView.url = [NSURL URLWithString:urlSting];
//    [[self topViewController].navigationController pushViewController:webView animated:YES];
}

- (void)pushRoom:(UserID)uid
{
    //根据房间所有者id。获取房间信息
    [MBProgressHUD showMessage:NSLocalizedString(XCHudLoadingTip1, nil)];
    [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomOwnerUid:uid succ:^(YPChatRoomInfo *roomInfo) {
        if (roomInfo != nil && roomInfo.title.length > 0) {
            //            [MBProgressHUD hideHUD];
            //根据房间信息开房
            [[YPRoomViewControllerCenter defaultCenter] presentRoomViewWithRoomInfo:roomInfo];
        }else {
            [MBProgressHUD showError:NSLocalizedString(XCHudNetError, nil)];
        }
    } fail:^(NSString *errorMsg) {
        [MBProgressHUD showError:errorMsg];
    }];
}

#pragma mark - setter/getter
- (UIImageView *)shadowImageView
{
    if (!_shadowImageView) {
        _shadowImageView = [[UIImageView alloc] init];
        _shadowImageView.backgroundColor = [UIColor clearColor];
        _shadowImageView.image = [UIImage imageNamed:@"home_peipei_bg"];
//        _shadowImageView.layer.cornerRadius = 10;
//        _shadowImageView.layer.masksToBounds = YES;
         _shadowImageView.contentMode = UIViewContentModeScaleToFill;
        [self.contentView addSubview:_shadowImageView];
    }
    return _shadowImageView;
}

- (UIImageView *)shadowImageView2
{
    if (!_shadowImageView2) {
        _shadowImageView2 = [[UIImageView alloc] init];
        _shadowImageView2.backgroundColor = [UIColor clearColor];
        _shadowImageView2.image = [UIImage imageNamed:@"home_peipei_bg"];
//        _shadowImageView2.layer.cornerRadius = 10;
//        _shadowImageView2.layer.masksToBounds = YES;
        _shadowImageView2.contentMode = UIViewContentModeScaleToFill;
        [self.contentView addSubview:_shadowImageView2];
    }
    return _shadowImageView2;
}

- (UIImageView *)animationImageView{
    if (!_animationImageView) {
        _animationImageView = [[UIImageView alloc]init];
        _animationImageView.backgroundColor = [UIColor clearColor];
        _animationImageView.contentMode = UIViewContentModeScaleToFill;
        [self.contentView addSubview:_animationImageView];
    }
    return _animationImageView;
}

- (UIImageView *)animationImageView2{
    if (!_animationImageView2) {
        _animationImageView2 = [[UIImageView alloc]init];
        _animationImageView.backgroundColor = [UIColor clearColor];
        _animationImageView2.contentMode = UIViewContentModeScaleToFill;
        [self.contentView addSubview:_animationImageView2];
    }
    return _animationImageView2;
}

- (UIImageView *)roomImageView
{
    if (!_roomImageView) {
        _roomImageView = [[UIImageView alloc] init];
        _roomImageView.layer.cornerRadius = (HJHomePeipeiTableCellWidth-50)/2.0;
        _roomImageView.layer.masksToBounds = YES;
        _roomImageView.userInteractionEnabled = YES;
        
        _roomImageView.layer.borderWidth = 2;
       
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction)];
        [_roomImageView addGestureRecognizer:tap];
        
        _roomImageView.contentMode = UIViewContentModeScaleAspectFill;
        
        [self.contentView addSubview:_roomImageView];
    }
    return _roomImageView;
}

- (UIImageView *)roomImageView2
{
    if (!_roomImageView2) {
        _roomImageView2 = [[UIImageView alloc] init];
        _roomImageView2.layer.cornerRadius = (HJHomePeipeiTableCellWidth-50)/2.0;
        _roomImageView2.layer.masksToBounds = YES;
        _roomImageView2.userInteractionEnabled = YES;
        _roomImageView2.layer.borderWidth = 2;
      
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction2)];
        [_roomImageView2 addGestureRecognizer:tap];
        _roomImageView2.contentMode = UIViewContentModeScaleAspectFill;

        [self.contentView addSubview:_roomImageView2];
    }
    return _roomImageView2;
}





- (UILabel *)nameLabel
{
    if (!_nameLabel) {
        _nameLabel = [UILabel new];
        _nameLabel.font = JXFontPingFangSCRegular(14);
        _nameLabel.textColor = UIColorHex(333333);
        _nameLabel.textAlignment = NSTextAlignmentCenter;
        _nameLabel.userInteractionEnabled = YES;
        UITapGestureRecognizer *taptitle = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapTitleAction)];
               [_nameLabel addGestureRecognizer:taptitle];
        [self.contentView addSubview:_nameLabel];
    }
    return _nameLabel;
}

- (UILabel *)nameLabel2
{
    if (!_nameLabel2) {
        _nameLabel2 = [UILabel new];
        _nameLabel2.font = JXFontPingFangSCRegular(14);
        _nameLabel2.textColor = UIColorHex(333333);
        _nameLabel2.textAlignment = NSTextAlignmentCenter;
         _nameLabel2.userInteractionEnabled = YES;
        UITapGestureRecognizer *taptitle2 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapTitle2Action)];
                      [_nameLabel2 addGestureRecognizer:taptitle2];
        [self.contentView addSubview:_nameLabel2];
    }
    return _nameLabel2;
}


- (UIView *)peopleView
{
    if (!_peopleView) {
        _peopleView = [UIView new];
        _peopleView.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.38];
        _peopleView.layer.cornerRadius = 12;
        _peopleView.layer.masksToBounds = YES;
        [self.contentView addSubview:_peopleView];
    }
    return _peopleView;
}

- (UIView *)peopleView2
{
    if (!_peopleView2) {
        _peopleView2 = [UIView new];
        _peopleView2.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.38];
        _peopleView2.layer.cornerRadius = 12;
        _peopleView2.layer.masksToBounds = YES;
        [self.contentView addSubview:_peopleView2];
    }
    return _peopleView2;
}


- (UIImageView *)peopleImageView
{
    if (!_peopleImageView) {
        _peopleImageView = [UIImageView new];
        _peopleImageView.image = [UIImage imageNamed:@"yp_home_peopleLogo"];
        [self.peopleView addSubview:_peopleImageView];
    }
    return _peopleImageView;
}

- (UIImageView *)peopleImageView2
{
    if (!_peopleImageView2) {
        _peopleImageView2 = [UIImageView new];
        _peopleImageView2.image = [UIImage imageNamed:@"yp_home_peopleLogo"];
        [self.peopleView2 addSubview:_peopleImageView2];
    }
    return _peopleImageView2;
}



- (UIImageView *)flagImgView
{
    if (!_flagImgView) {
        _flagImgView = [UIImageView new];
       
        [self.contentView addSubview:_flagImgView];
    }
    return _flagImgView;
}
- (UIImageView *)flagImgView2
{
    if (!_flagImgView2) {
        _flagImgView2 = [UIImageView new];
      
        [self.contentView addSubview:_flagImgView2];
    }
    return _flagImgView2;
}
- (UIImageView *)sexImageView
{
    if (!_sexImageView) {
        _sexImageView = [UIImageView new];
      
        [self.contentView addSubview:_sexImageView];
    }
    return _sexImageView;
}
- (UIImageView *)sexImageView2
{
    if (!_sexImageView2) {
        _sexImageView2 = [UIImageView new];
      
        [self.contentView addSubview:_sexImageView2];
    }
    return _sexImageView2;
}


- (UILabel *)peopleLabel
{
    if (!_peopleLabel) {
        _peopleLabel = [UILabel new];
        _peopleLabel.font = JXFontPingFangSCRegular(11);
        _peopleLabel.textColor = [UIColor whiteColor];
        _peopleLabel.textAlignment = NSTextAlignmentCenter;

        [self.peopleView addSubview:_peopleLabel];
    }
    return _peopleLabel;
}

- (UILabel *)peopleLabel2
{
    if (!_peopleLabel2) {
        _peopleLabel2 = [UILabel new];
        _peopleLabel2.font = JXFontPingFangSCRegular(11);
        _peopleLabel2.textColor = [UIColor whiteColor];
        _peopleLabel2.textAlignment = NSTextAlignmentCenter;

        [self.peopleView2 addSubview:_peopleLabel2];
    }
    return _peopleLabel2;
}



- (void)setRoomArr:(NSArray *)roomArr
{
    _roomArr = roomArr;
    [self initData];
}

- (UIButton *)voiceButton
{
    if (!_voiceButton) {
        _voiceButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [_voiceButton setImage:[UIImage imageNamed:@"home_first_peipei_play"] forState:UIControlStateNormal];
         [_voiceButton addTarget:self action:@selector(voiceButtonAction) forControlEvents:UIControlEventTouchUpInside];
        [self.peopleView addSubview:_voiceButton];
    }
    return _voiceButton;
}

- (UIButton *)voiceButton2
{
    if (!_voiceButton2) {
        _voiceButton2 = [UIButton buttonWithType:UIButtonTypeCustom];
         [_voiceButton2 setImage:[UIImage imageNamed:@"home_first_peipei_play"] forState:UIControlStateNormal];
        [_voiceButton2 addTarget:self action:@selector(voiceButton2Action) forControlEvents:UIControlEventTouchUpInside];
        [self.peopleView2 addSubview:_voiceButton2];
    }
    return _voiceButton2;
}

@end
