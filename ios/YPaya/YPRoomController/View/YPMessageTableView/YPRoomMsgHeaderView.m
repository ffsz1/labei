//
//  YPRoomMsgHeaderView.m
//  HJLive
//
//  Created by feiyin on 2020/7/16.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomMsgHeaderView.h"

#import "YPGiftCore.h"

@implementation YPRoomMsgHeaderView

- (void)awakeFromNib
{
    [super awakeFromNib];
    
    self.tipLabel.adjustsFontSizeToFitWidth = YES;
}

- (instancetype)initWithFrame:(CGRect)frame
{
    self = [super initWithFrame:frame];
    if (self) {
        [self setUI];
    }
    return self;
}

- (void)setUI
{
    __weak typeof(self) weakSelf = self;
    [self.tipLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(weakSelf).offset(8);
        make.right.mas_equalTo(weakSelf).offset(-8);
        make.top.mas_equalTo(weakSelf);
        make.height.mas_equalTo(39);
    }];
    
    [self.giftView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(weakSelf).offset(8);
        make.width.mas_equalTo(170);
        make.bottom.mas_equalTo(weakSelf).offset(-4);
        make.height.mas_equalTo(22);
    }];
    
    [self.giftLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(weakSelf.giftView.left).offset(12);
        make.centerY.mas_equalTo(weakSelf.giftView);
        make.height.mas_equalTo(12);
    }];
    
    [self.giftImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(weakSelf.giftView.right).offset(-42);
        make.centerY.mas_equalTo(weakSelf.giftView);
        make.height.mas_equalTo(17);
        make.width.mas_equalTo(17);
    }];
    
    [self.numLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(weakSelf.giftView);
        make.height.mas_equalTo(12);
//        make.left.mas_equalTo(weakSelf.giftImageView.right).offset(2);
        make.right.mas_equalTo(weakSelf.giftView.right).offset(-2);
        make.width.mas_equalTo(40);
    }];
    
    
    
    
    
    
}

#pragma mark - Setter

- (void)setModel:(YPGiftReceiveInfo *)model {
    _model = model;
    NSString *nickSting = model.nick.length > 6 ? [NSString stringWithFormat:@"%@...",[model.nick substringWithRange:NSMakeRange(0, 6)]] : model.nick;
    
    NSString *targetNickString = model.targetNick.length > 6 ? [NSString stringWithFormat:@"%@...",[model.targetNick substringWithRange:NSMakeRange(0, 6)]] : model.targetNick;
    NSString *sendString = @"给";
    if (self.isAll) {
        targetNickString = @"";
        sendString = NSLocalizedString(XCRoomSendAllTheMicPeople, nil);
    }
    
    NSString *tipStr = [NSString stringWithFormat:@"%@%@%@送了",nickSting,sendString,targetNickString];
    self.giftLabel.text = tipStr;
    YPGiftInfo *giftInfo = [GetCore(YPGiftCore) findGiftInfoByGiftId:model.giftId giftyType:GiftTypeNormal];
    [self.giftImageView qn_setImageImageWithUrl:giftInfo.giftUrl placeholderImage:nil type:ImageTypeRoomBg];
    self.numLabel.text = [NSString stringWithFormat:@"x%ld",model.giftNum];

    
    
    CGSize giftSendSize = [self.giftLabel.text boundingRectWithSize:CGSizeMake(0, self.giftLabel.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:self.giftLabel.font} context:nil].size;
    CGFloat width = giftSendSize.width;
    [self.giftLabel mas_updateConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(width + 4);
    }];
    
    [self.giftView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(width + 64.5 + 4 + 10);
    }];
    
}


- (UILabel *)tipLabel
{
    if (!_tipLabel) {
        _tipLabel = [[UILabel alloc] init];
        _tipLabel.font = [UIFont fontWithName:@"PingFangSC Medium" size:15];
        _tipLabel.adjustsFontSizeToFitWidth = YES;
        _tipLabel.numberOfLines = 0;
        _tipLabel.textColor = UIColorHex(41FE88);
        _tipLabel.text = @"系统通知：官方倡导绿色健康的互动 体验，严禁传送色情、赌博、政治等 不良信息，一经发现，封停账号。";
        [self addSubview:_tipLabel];
    }
    return _tipLabel;
}

- (UIView *)giftView
{
    if (!_giftView) {
        _giftView = [[UIView alloc] init];
        _giftView.backgroundColor = UIColorHex(35257E);
        _giftView.layer.cornerRadius = 11;
        [self addSubview:_giftView];
    }
    return _giftView;
}

- (UILabel *)giftLabel
{
    if (!_giftLabel) {
        _giftLabel = [[UILabel alloc] init];
        _giftLabel.font = [UIFont systemFontOfSize:11];
        _giftLabel.textColor = UIColorHex(D9D1FE);
        _giftLabel.adjustsFontSizeToFitWidth = YES;
        [self.giftView addSubview:_giftLabel];
    }
    return _giftLabel;
}

- (UIImageView *)giftImageView
{
    if (!_giftImageView) {
        _giftImageView = [[UIImageView alloc] init];
        _giftImageView.layer.cornerRadius = 17/2;
        _giftImageView.layer.masksToBounds = YES;
        [self.giftView addSubview:_giftImageView];
    }
    return _giftImageView;
}

- (UILabel *)numLabel
{
    if (!_numLabel) {
        _numLabel = [[UILabel alloc] init];
        _numLabel.font = [UIFont systemFontOfSize:13];
        _numLabel.adjustsFontSizeToFitWidth = YES;
        _numLabel.textColor = UIColorHex(FFEC00);
        [self.giftView addSubview:_numLabel];
    }
    return _numLabel;
}

@end
