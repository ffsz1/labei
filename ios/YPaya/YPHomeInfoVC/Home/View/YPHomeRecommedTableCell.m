//
//  YPHomeRecommedTableCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeRecommedTableCell.h"
#import "YPHomeRecommedRoomModel.h"
#import "YPRoomViewControllerCenter.h"
#import "YPWKWebViewController.h"
#import "UIView+getTopVC.h"
#import "UIImage+Gif.h"
#import "YPHomePageInfo.h"
@interface YPHomeRecommedTableCell()

@property (nonatomic,strong) UIImageView *roomImageView;
@property (nonatomic,strong) UIImageView *shadowImageView;
@property (nonatomic,strong) UILabel *nameLabel;
@property (nonatomic,strong) UIView *peopleView;
@property (nonatomic,strong) UIImageView *peopleImageView;
@property (nonatomic,strong) UILabel *peopleLabel;

@property (nonatomic,strong) UIImageView *roomImageView2;
@property (nonatomic,strong) UIImageView *shadowImageView2;
@property (nonatomic,strong) UILabel *nameLabel2;
@property (nonatomic,strong) UIView *peopleView2;
@property (nonatomic,strong) UIImageView *peopleImageView2;
@property (nonatomic,strong) UILabel *peopleLabel2;

@property (nonatomic,strong) UIImageView *roomImageView3;
@property (nonatomic,strong) UIImageView *shadowImageView3;
@property (nonatomic,strong) UILabel *nameLabel3;
@property (nonatomic,strong) UIView *peopleView3;
@property (nonatomic,strong) UIImageView *peopleImageView3;
@property (nonatomic,strong) UILabel *peopleLabel3;

@property (nonatomic,strong) UIImageView *flagImgView;
@property (nonatomic,strong) UIImageView *flagImgView2;
@property (nonatomic,strong) UIImageView *flagImgView3;

@end
@implementation YPHomeRecommedTableCell


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
    self.backgroundColor = [UIColor clearColor];
    self.selectionStyle = UITableViewCellSelectionStyleNone;
    
    [self.shadowImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
           make.top.mas_equalTo(self.contentView).offset(0);
            make.left.mas_equalTo(self.contentView).offset(12);
           make.height.mas_equalTo(JXHomeRecommedCellWidth*(147/116.0));
             make.width.mas_equalTo(JXHomeRecommedCellWidth);
       }];

       [self.shadowImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
          make.top.mas_equalTo(self.contentView).offset(0);
            make.left.mas_equalTo(self.shadowImageView.mas_right).offset(12);
                   make.height.mas_equalTo(JXHomeRecommedCellWidth*(147/116.0));
             make.width.mas_equalTo(JXHomeRecommedCellWidth);
       }];

       [self.shadowImageView3 mas_remakeConstraints:^(MASConstraintMaker *make) {
          make.top.mas_equalTo(self.contentView).offset(0);
             make.right.mas_equalTo(self.contentView).offset(-12);
                   make.height.mas_equalTo(JXHomeRecommedCellWidth*(147/116.0));
             make.width.mas_equalTo(JXHomeRecommedCellWidth);
       }];
    
    [self.roomImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(JXHomeRecommedCellWidth-12*(kScreenWidth/375.0), JXHomeRecommedCellWidth-12*(kScreenWidth/375.0)));
        make.left.mas_equalTo(self.shadowImageView).offset(6*(kScreenWidth/375.0));
        make.top.mas_equalTo(self.contentView).offset(6);
    }];
    
    [self.roomImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(JXHomeRecommedCellWidth-12*(kScreenWidth/375.0), JXHomeRecommedCellWidth-12*(kScreenWidth/375.0)));
        make.top.mas_equalTo(self.contentView).offset(6);
        make.left.mas_equalTo(self.shadowImageView2.mas_left).offset(6*(kScreenWidth/375.0));
    }];
    
    [self.roomImageView3 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(JXHomeRecommedCellWidth-12*(kScreenWidth/375.0), JXHomeRecommedCellWidth-12*(kScreenWidth/375.0)));
        make.top.mas_equalTo(self.contentView).offset(6);
        make.left.mas_equalTo(self.shadowImageView3.mas_left).offset(6*(kScreenWidth/375.0));
    }];
    
    
    
   
    
    [self.nameLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.roomImageView).offset(-5);
        make.left.mas_equalTo(self.roomImageView).offset(5);
        make.top.mas_equalTo(self.roomImageView.mas_bottom).offset(7);
    }];
    
    [self.nameLabel2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.roomImageView2).offset(-5);
        make.left.mas_equalTo(self.roomImageView2).offset(5);
        make.top.mas_equalTo(self.roomImageView2.mas_bottom).offset(7);
    }];
    
    [self.nameLabel3 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.roomImageView3).offset(-5);
        make.left.mas_equalTo(self.roomImageView3).offset(5);
        make.top.mas_equalTo(self.roomImageView3.mas_bottom).offset(7);
    }];
    
    [self.peopleView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(50, 16));
        make.right.mas_equalTo(self.roomImageView).offset(-8);
        make.bottom.mas_equalTo(self.roomImageView.mas_bottom).offset(-7);
    }];
    
    [self.peopleView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(50, 16));
        make.right.mas_equalTo(self.roomImageView2).offset(-8);
         make.bottom.mas_equalTo(self.roomImageView2.mas_bottom).offset(-7);
    }];
    
    [self.peopleView3 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(50, 16));
        make.right.mas_equalTo(self.roomImageView3).offset(-8);
         make.bottom.mas_equalTo(self.roomImageView3.mas_bottom).offset(-7);
    }];
    
    [self.peopleImageView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(10, 10));
        make.centerY.mas_equalTo(self.peopleView);
        make.left.mas_equalTo(self.peopleView).offset(8);
    }];
    
    [self.peopleImageView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(10, 10));
        make.centerY.mas_equalTo(self.peopleView2);
        make.left.mas_equalTo(self.peopleView2).offset(8);
    }];
    
    [self.peopleImageView3 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(10, 10));
        make.centerY.mas_equalTo(self.peopleView3);
        make.left.mas_equalTo(self.peopleView3).offset(8);
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
    
    [self.peopleLabel3 mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self.peopleView3);
       
        make.right.mas_equalTo(self.peopleView3).offset(-3);
         make.left.mas_equalTo(self.peopleImageView3.mas_right).offset(1);

    }];
    
    [self.flagImgView mas_remakeConstraints:^(MASConstraintMaker *make) {
        make.size.mas_equalTo(CGSizeMake(30, 16));
        make.bottom.mas_equalTo(self.roomImageView.mas_bottom).offset(-7);
        make.left.mas_equalTo(self.roomImageView).offset(8);
    }];
    [self.flagImgView2 mas_remakeConstraints:^(MASConstraintMaker *make) {
           make.size.mas_equalTo(CGSizeMake(30, 16));
           make.bottom.mas_equalTo(self.roomImageView2.mas_bottom).offset(-7);
           make.left.mas_equalTo(self.roomImageView2).offset(8);
       }];
    [self.flagImgView3 mas_remakeConstraints:^(MASConstraintMaker *make) {
           make.size.mas_equalTo(CGSizeMake(30, 16));
           make.bottom.mas_equalTo(self.roomImageView3.mas_bottom).offset(-7);
           make.left.mas_equalTo(self.roomImageView3).offset(8);
       }];
   
    
    
    
    //    [self initData];
}

- (void)initData
{
    if (self.roomArr.count>0) {
//        YPHomeRecommedRoomModel *room = self.roomArr[0];
          YPHomePageInfo  *room = self.roomArr[0];
        [self.roomImageView qn_setImageImageWithUrl:room.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
        self.nameLabel.text = room.title;
        self.peopleLabel.text = [NSString stringWithFormat:@"%ld",room.onlineNum];
        
//        CGSize size = [self.peopleLabel.text boundingRectWithSize:CGSizeMake(0, self.peopleLabel.size.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:[UIFont fontWithName:@"PingFang SC" size:11]} context:nil].size;
//        [self.peopleView mas_updateConstraints:^(MASConstraintMaker *make) {
//            make.width.mas_equalTo(size.width + 24);
//        }];
        
        self.peopleImageView.image = [UIImage animatedGIFNamed:@"yp_home_people"];
        [self.flagImgView sd_setImageWithURL:[NSURL URLWithString:room.tagPict]];
    }
    
    if (self.roomArr.count>1) {
        YPHomePageInfo *room = self.roomArr[1];
        [self.roomImageView2 qn_setImageImageWithUrl:room.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
        self.nameLabel2.text = room.title;
        self.peopleLabel2.text = [NSString stringWithFormat:@"%ld",room.onlineNum];
        
        self.nameLabel2.hidden = NO;
        self.peopleView2.hidden = NO;
        self.shadowImageView2.hidden = NO;
        self.flagImgView.hidden = NO;
//        CGSize size = [self.peopleLabel2.text boundingRectWithSize:CGSizeMake(0, self.peopleLabel2.size.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:[UIFont fontWithName:@"PingFang SC" size:11]} context:nil].size;
//        [self.peopleView2 mas_updateConstraints:^(MASConstraintMaker *make) {
//            make.width.mas_equalTo(size.width + 24);
//        }];
        
        self.peopleImageView2.image = [UIImage animatedGIFNamed:@"yp_home_people"];
         [self.flagImgView2 sd_setImageWithURL:[NSURL URLWithString:room.tagPict]];
        
    }else{
        self.roomImageView2.image = [UIImage imageNamed:@"yp_home_greenDelegate"];//绿色公约
        self.nameLabel2.hidden = YES;
        self.peopleView2.hidden = YES;
        self.shadowImageView2.hidden = NO;
        self.flagImgView.hidden = YES;
    }
    
    if (self.roomArr.count>2) {
        YPHomePageInfo *room = self.roomArr[2];
        [self.roomImageView3 qn_setImageImageWithUrl:room.avatar placeholderImage:placeholder_image_square type:ImageTypeHomePageItem];
        self.nameLabel3.text = room.title;
        self.peopleLabel3.text = [NSString stringWithFormat:@"%ld",room.onlineNum];
        
        self.nameLabel3.hidden = NO;
        self.peopleView3.hidden = NO;
        self.shadowImageView3.hidden = NO;
        self.flagImgView.hidden = NO;
//        CGSize size = [self.peopleLabel3.text boundingRectWithSize:CGSizeMake(0, self.peopleLabel3.size.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:[UIFont fontWithName:@"PingFang SC" size:11]} context:nil].size;
//        [self.peopleView3 mas_updateConstraints:^(MASConstraintMaker *make) {
//            make.width.mas_equalTo(size.width + 24);
//        }];
        
        self.peopleImageView3.image = [UIImage animatedGIFNamed:@"yp_home_people"];
        [self.flagImgView3 sd_setImageWithURL:[NSURL URLWithString:room.tagPict]];
        
    }else{
        self.roomImageView3.image = [UIImage imageNamed:@"yp_home_xuweiyidai"];
        self.nameLabel3.hidden = YES;
        self.peopleView3.hidden = YES;
        self.shadowImageView3.hidden = NO;
        self.flagImgView.hidden = YES;
    }
    
    
    
}

#pragma mark - private method
- (void)tapAction
{
    YPHomeRecommedRoomModel *room = [self.roomArr safeObjectAtIndex:0];
    [self pushRoom:room.uid];
}

- (void)tapAction2
{
    if (self.roomArr.count<2) {
        [self pushToGreenH5];
        return;
    }
    YPHomeRecommedRoomModel *room = [self.roomArr safeObjectAtIndex:1];
    [self pushRoom:room.uid];
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
    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
     NSString *urlSting = [NSString stringWithFormat:@"%@/front/convention/index.html",[YPHttpRequestHelper getHostUrl]];
    webView.url = [NSURL URLWithString:urlSting];
    [[self topViewController].navigationController pushViewController:webView animated:YES];
}

- (void)pushToH5
{
    YPWKWebViewController *webView = [[YPWKWebViewController alloc]init];
    NSString *urlSting = [NSString stringWithFormat:@"%@/front/hotroom/index.html",[YPHttpRequestHelper getHostUrl]];
    webView.url = [NSURL URLWithString:urlSting];
    [[self topViewController].navigationController pushViewController:webView animated:YES];
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
        _shadowImageView.image = [UIImage imageNamed:@"yp_home_tuijian_bg"];
        [self.contentView addSubview:_shadowImageView];
    }
    return _shadowImageView;
}

- (UIImageView *)shadowImageView2
{
    if (!_shadowImageView2) {
        _shadowImageView2 = [[UIImageView alloc] init];
        _shadowImageView2.image = [UIImage imageNamed:@"yp_home_tuijian_bg"];
        [self.contentView addSubview:_shadowImageView2];
    }
    return _shadowImageView2;
}

- (UIImageView *)shadowImageView3
{
    if (!_shadowImageView3) {
        _shadowImageView3 = [[UIImageView alloc] init];
        _shadowImageView3.image = [UIImage imageNamed:@"yp_home_tuijian_bg"];
        [self.contentView addSubview:_shadowImageView3];
    }
    return _shadowImageView3;
}
- (UIImageView *)roomImageView
{
    if (!_roomImageView) {
        _roomImageView = [[UIImageView alloc] init];
        _roomImageView.layer.cornerRadius = 10;
        _roomImageView.layer.masksToBounds = YES;
        _roomImageView.userInteractionEnabled = YES;
        
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
        _roomImageView2.layer.cornerRadius = 10;
        _roomImageView2.layer.masksToBounds = YES;
        _roomImageView2.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction2)];
        [_roomImageView2 addGestureRecognizer:tap];
        _roomImageView2.contentMode = UIViewContentModeScaleAspectFill;

        [self.contentView addSubview:_roomImageView2];
    }
    return _roomImageView2;
}

- (UIImageView *)roomImageView3
{
    if (!_roomImageView3) {
        _roomImageView3 = [[UIImageView alloc] init];
        _roomImageView3.layer.cornerRadius = 10;
        _roomImageView3.layer.masksToBounds = YES;
        _roomImageView3.userInteractionEnabled = YES;
        UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction3)];
        [_roomImageView3 addGestureRecognizer:tap];
        _roomImageView3.contentMode = UIViewContentModeScaleAspectFill;

        [self.contentView addSubview:_roomImageView3];
    }
    return _roomImageView3;
}



- (UILabel *)nameLabel
{
    if (!_nameLabel) {
        _nameLabel = [UILabel new];
        _nameLabel.font = JXFontPingFangSCMedium(12);
        _nameLabel.textColor = UIColorHex(333333);
        _nameLabel.textAlignment = NSTextAlignmentCenter;
        [self.contentView addSubview:_nameLabel];
    }
    return _nameLabel;
}

- (UILabel *)nameLabel2
{
    if (!_nameLabel2) {
        _nameLabel2 = [UILabel new];
        _nameLabel2.font = JXFontPingFangSCMedium(12);
        _nameLabel2.textColor = UIColorHex(333333);
        _nameLabel2.textAlignment = NSTextAlignmentCenter;

        [self.contentView addSubview:_nameLabel2];
    }
    return _nameLabel2;
}

- (UILabel *)nameLabel3
{
    if (!_nameLabel3) {
        _nameLabel3 = [UILabel new];
        _nameLabel3.font = JXFontPingFangSCMedium(12);
        _nameLabel3.textColor = UIColorHex(333333);
        _nameLabel3.textAlignment = NSTextAlignmentCenter;
        [self.contentView addSubview:_nameLabel3];
    }
    return _nameLabel3;
}

- (UIView *)peopleView
{
    if (!_peopleView) {
        _peopleView = [UIView new];
        _peopleView.backgroundColor =  [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3];
        _peopleView.layer.cornerRadius = 8;
        _peopleView.layer.masksToBounds = YES;
        [self.contentView addSubview:_peopleView];
    }
    return _peopleView;
}

- (UIView *)peopleView2
{
    if (!_peopleView2) {
        _peopleView2 = [UIView new];
        _peopleView2.backgroundColor =  [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3];
        _peopleView2.layer.cornerRadius = 8;
        _peopleView2.layer.masksToBounds = YES;
        [self.contentView addSubview:_peopleView2];
    }
    return _peopleView2;
}

- (UIView *)peopleView3
{
    if (!_peopleView3) {
        _peopleView3 = [UIView new];
        _peopleView3.backgroundColor = [UIColor colorWithRed:0 green:0 blue:0 alpha:0.3];
        _peopleView3.layer.cornerRadius = 8;
        _peopleView3.layer.masksToBounds = YES;
        [self.contentView addSubview:_peopleView3];
    }
    return _peopleView3;
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

- (UIImageView *)peopleImageView3
{
    if (!_peopleImageView3) {
        _peopleImageView3 = [UIImageView new];
        _peopleImageView3.image = [UIImage imageNamed:@"yp_home_peopleLogo"];
        [self.peopleView3 addSubview:_peopleImageView3];
    }
    return _peopleImageView3;
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
- (UIImageView *)flagImgView3
{
    if (!_flagImgView3) {
        _flagImgView3 = [UIImageView new];
       
        [self.contentView addSubview:_flagImgView3];
    }
    return _flagImgView3;
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

- (UILabel *)peopleLabel3
{
    if (!_peopleLabel3) {
        _peopleLabel3 = [UILabel new];
        _peopleLabel3.font = JXFontPingFangSCRegular(11);
        _peopleLabel3.textColor = [UIColor whiteColor];
        
        _peopleLabel3.textAlignment = NSTextAlignmentCenter;
        [self.peopleView3 addSubview:_peopleLabel3];
    }
    return _peopleLabel3;
}

- (void)setRoomArr:(NSArray *)roomArr
{
    _roomArr = roomArr;
    [self initData];
}


@end
