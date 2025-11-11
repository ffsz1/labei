//
//  YPDiscoveryFamilyTableViewCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPDiscoveryFamilyTableViewCell.h"

@interface YPDiscoveryFamilyTableViewCell ()
@property (nonatomic, strong) UIImageView *bgContenView;
@property (nonatomic, strong) UIImageView *avatar;
@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) UILabel *countLabel;
@property (nonatomic, strong) UIImageView *countLabelIcon;
@property (nonatomic, strong) UILabel *infoLabel;

@property (nonatomic, strong) UIImageView *peopleCountContentView;
@property (nonatomic, strong) UILabel *peopleCountLabel;
@property (nonatomic, strong) UIView *line;
@property (nonatomic, strong) UIView *point;
@property (nonatomic, strong) UILabel *indexLabel;

@end

@implementation YPDiscoveryFamilyTableViewCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        [self initView];
        [self setupLayout];
    }
    return self;
}

- (void)setupAvatar:(NSString *)avatar
              title:(NSString *)title
              count:(NSInteger)count
        memberCount:(NSInteger)memberCount
           infoText:(NSString *)info
              index:(NSInteger)index {
    self.titleLabel.text = title;
    [self.avatar qn_setImageImageWithUrl:avatar placeholderImage:default_bg type:ImageTypeHomePageItem];
    self.countLabel.text = [NSString stringWithFormat:@"%zd",count];
    self.peopleCountLabel.text = [NSString stringWithFormat:@"%zd个成员",memberCount];
    self.infoLabel.text = info;
    if (index == 0) {
        [self.bgContenView setImage:[[UIImage imageNamed:@"yp_discovery_icon_diyimin"] resizableImageWithCapInsets:UIEdgeInsetsMake(0,100,0,100) resizingMode:UIImageResizingModeStretch]];
    } else {
        [self.bgContenView setImage:[[UIImage imageNamed:@"yp_discovery_icon_qita"] resizableImageWithCapInsets:UIEdgeInsetsMake(0,100,0,100) resizingMode:UIImageResizingModeStretch]];
    }
    self.indexLabel.text = [NSString stringWithFormat:@"%d",index+1];
}

- (void)setupUserAvatars:(NSArray<NSString*> *)avatars {
    [self.peopleCountContentView removeAllSubviews];
    CGFloat x = 0;
    CGFloat w = 16;
    for (int i=0; i<avatars.count; i++) {
        UIImageView *userImageView = [[UIImageView alloc]initWithFrame:CGRectMake(x, 0, w, w)];
        [userImageView qn_setImageImageWithUrl:avatars[i] placeholderImage:default_bg type:ImageTypeHomePageItem];
        [self.peopleCountContentView addSubview:userImageView];
        x += (w+4);
    }
    [self.peopleCountContentView mas_updateConstraints:^(MASConstraintMaker *make) {
        make.width.mas_equalTo(x);
    }];
    [self layoutIfNeeded];
}

#pragma UI
- (void)initView {
    [self.contentView addSubview:self.bgContenView];
    [self.bgContenView addSubview:self.avatar];
    [self.bgContenView addSubview:self.titleLabel];
    [self.bgContenView addSubview:self.countLabel];
    [self.bgContenView addSubview:self.countLabelIcon];
    
    [self.bgContenView addSubview:self.peopleCountLabel];
    [self.bgContenView addSubview:self.peopleCountContentView];
    [self.bgContenView addSubview:self.line];
    [self.bgContenView addSubview:self.point];
    [self.bgContenView addSubview:self.infoLabel];
    [self.bgContenView addSubview:self.indexLabel];
}

- (void)setupLayout {
    [self.bgContenView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.contentView).with.insets(UIEdgeInsetsMake(0, 0, 0, 0));
    }];
    [self.avatar mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.equalTo(self.bgContenView).offset(22);
        make.width.height.equalTo(@70);
    }];
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.avatar);
        make.left.equalTo(self.avatar.mas_right).offset(9);;
        make.height.equalTo(@16);
        make.right.equalTo(self.bgContenView).offset(-15);;
    }];
    //？？威望
    [self.countLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.titleLabel);
        make.height.equalTo(@12);
        make.top.equalTo(self.titleLabel.mas_bottom).offset(8);
    }];
    [self.countLabelIcon mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self.countLabel);
        make.left.equalTo(self.countLabel.mas_right).offset(4);
    }];
    //人数 (饭头像)
    [self.peopleCountContentView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.height.equalTo(@16);
        make.left.equalTo(self.titleLabel);
        make.width.greaterThanOrEqualTo(@1);
        make.top.equalTo(self.countLabel.mas_bottom).offset(4);
    }];
    [self.peopleCountLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.peopleCountContentView.mas_right);
        make.height.equalTo(@16);
        make.centerY.equalTo(self.peopleCountContentView);
    }];
    //底部
    [self.line mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.avatar.mas_bottom).offset(13);
        make.left.equalTo(self.bgContenView).offset(13);
        make.right.equalTo(self.bgContenView).offset(-13);
        make.height.equalTo(@0.5);
    }];
    [self.point mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.equalTo(self.avatar);
        make.top.equalTo(self.line.mas_bottom).offset(9);
        //        make.bottom.equalTo(self.bgContenView).offset(20);
        make.height.width.equalTo(@6);
    }];
    [self.infoLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self.point);
        make.right.equalTo(self.bgContenView).offset(-15);
        make.left.equalTo(self.point.mas_right).offset(4);
    }];
    
    [self.indexLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.bgContenView).offset(10);
        make.right.equalTo(self.bgContenView).offset(-19);
        make.height.equalTo(@23);
        make.width.equalTo(@25);
    }];
}

#pragma mark - getter
- (UIImageView *)bgContenView {
    if (!_bgContenView) {
        _bgContenView = [[UIImageView alloc] initWithImage:[[UIImage imageNamed:@"yp_discovery_icon_qita"] resizableImageWithCapInsets:UIEdgeInsetsMake(0,100,0,100) resizingMode:UIImageResizingModeStretch]];
        _bgContenView.backgroundColor = [UIColor whiteColor];
        //        _bgContenView.layer.cornerRadius = 10.f;
        //        _bgContenView.layer.masksToBounds = YES;
    }
    return _bgContenView;
}

- (UILabel *)peopleCountLabel {
    if (!_peopleCountLabel) {
        _peopleCountLabel = [[UILabel alloc] init];
        _peopleCountLabel.font = [UIFont systemFontOfSize:12];
        _peopleCountLabel.textColor = UIColorHex(A19FA2);
    }
    return _peopleCountLabel;
}

- (UIImageView *)peopleCountContentView {
    if (!_peopleCountContentView) {
        _peopleCountContentView = [[UIImageView alloc] init];
    }
    return _peopleCountContentView;
}

- (UIImageView *)avatar {
    if (!_avatar) {
        _avatar = [[UIImageView alloc] initWithImage:[UIImage imageNamed:default_avatar]];
    }
    return _avatar;
}

- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [[UILabel alloc] init];
        _titleLabel.font = [UIFont boldSystemFontOfSize:16];
        _titleLabel.textColor = UIColorHex(3C363E);
    }
    return _titleLabel;
}

- (UILabel *)countLabel{
    if (!_countLabel) {
        _countLabel = [[UILabel alloc] init];
        _countLabel.font = [UIFont systemFontOfSize:13];
        _countLabel.textColor = UIColorHex(FECC32);
    }
    return _countLabel;
}

- (UIImageView *)countLabelIcon {
    if (!_countLabelIcon) {
        _countLabelIcon = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"yp_discovery_icon_jinbin"]];
    }
    return _countLabelIcon;
}

- (UIView *)line {
    if (!_line) {
        _line = [[UIView alloc] init];
        _line.backgroundColor = UIColorHex(EEEEEE);
    }
    return _line;
}

- (UIView *)point {
    if (!_point) {
        _point = [[UIView alloc] initWithFrame:CGRectMake(0, 0, 6, 6)];
        _point.backgroundColor = UIColorHex(DCDCDC);
        _point.layer.cornerRadius = 3.f;
        _point.layer.masksToBounds = YES;
    }
    return _point;
}

- (UILabel *)infoLabel {
    if (!_infoLabel) {
        _infoLabel = [[UILabel alloc] init];
        _infoLabel.font = [UIFont systemFontOfSize:12];
        _infoLabel.textColor = UIColorHex(C0BCC2);
    }
    return _infoLabel;
}

- (UILabel *)indexLabel {
    if (!_indexLabel) {
        _indexLabel = [[UILabel alloc] init];
        _indexLabel.textColor = [UIColor whiteColor];
        //        _indexLabel.font = [UIFont fontWithName:@"Vandana-Blod" size:17];//Thonburi
        _indexLabel.font = [UIFont fontWithName:@"Arial Rounded MT Bold" size:17];
        _indexLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _indexLabel;
}

@end
