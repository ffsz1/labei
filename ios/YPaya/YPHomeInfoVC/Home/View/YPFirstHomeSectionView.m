//
//  YPFirstHomeSectionView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFirstHomeSectionView.h"
#import "YPFYTool.h"
@implementation YPFirstHomeSectionView

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
    self.backgroundColor = [UIColor whiteColor];
    [self.logoImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self).mas_offset(15);
        make.size.mas_equalTo(CGSizeMake(20, 20));
        make.centerY.mas_equalTo(self);
    }];
    
    [self.tipLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.mas_equalTo(self.logoImageView.mas_right).mas_offset(10);
        make.centerY.mas_equalTo(self);
    }];
    
    [self.sexImageViewFlag mas_makeConstraints:^(MASConstraintMaker *make) {
             make.right.mas_equalTo(self).mas_offset(-10);
             make.centerY.mas_equalTo(self);
         }];
    
    [self.sexImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.right.mas_equalTo(self.sexImageViewFlag.mas_left).mas_offset(-5);
        make.centerY.mas_equalTo(self);
    }];
  [self.sexButton mas_makeConstraints:^(MASConstraintMaker *make) {
         make.right.mas_equalTo(self).mas_offset(-5);
         make.centerY.mas_equalTo(self);
       make.size.mas_equalTo(CGSizeMake(60,20));
     }];
    
    
    
}

- (void)detailBtnAction
{
    if (self.detailBlock) {
        self.detailBlock();
    }
}
- (void)leftBtnAction
{
    _sexImageView.hidden = YES;
    _sexImageViewFlag.hidden = YES;
    _sexButton.hidden = YES;
    if (self.leftBtnBlock) {
        self.leftBtnBlock();
    }
}
- (void)middleBtnAction
{
    _sexImageView.hidden = NO;
    _sexImageViewFlag.hidden = NO;
    _sexButton.hidden = NO;
    if (self.middleBtnBlock) {
        self.middleBtnBlock();
    }
}
- (void)rightBtnAction
{
    _sexImageView.hidden = YES;
    _sexImageViewFlag.hidden = YES;
    _sexButton.hidden = YES;
    if (self.rightBtnBlock) {
        self.rightBtnBlock();
    }
}

-(void)sexBtnAction:(UIButton*)sender{
    
    sender.selected = !sender.selected;
    if (sender.selected) {
          _sexImageViewFlag.image= [UIImage imageNamed:@"home_peipei_up"];
    }else{
          _sexImageViewFlag.image= [UIImage imageNamed:@"home_peipei_down"];
    }
    if (self.sexBtnBlock) {
           self.sexBtnBlock();
       }
}

#pragma mark - setter/getter
- (void)setDetailBlock:(HJHomeSectionDetailBlock)detailBlock
{
    _detailBlock = detailBlock;
    
    [self.arrowImageView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self);
        make.right.mas_equalTo(self).mas_offset(-15);
        make.size.mas_equalTo(CGSizeMake(8, 16));
    }];
    
    [self.detailBtn mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.mas_equalTo(self);
        make.right.mas_equalTo(self).mas_offset(-23);
        make.size.mas_equalTo(CGSizeMake(32, 30));
    }];
    
}

- (void)setLeftBtnBlock:(SectionLeftBtnBlock)leftBtnBlock{
    _leftBtnBlock = leftBtnBlock;
    
    [self.leftBtn mas_makeConstraints:^(MASConstraintMaker *make) {
           make.centerY.mas_equalTo(self);
        make.left.mas_equalTo(self).mas_offset(13);
           make.size.mas_equalTo(CGSizeMake(72, 30));
       }];
    [self.lineView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.rightBtn.mas_bottom).mas_offset(3);
        make.left.mas_equalTo(self.leftBtn.mas_left).mas_offset(8);
         
        make.size.mas_equalTo(CGSizeMake(56, 2));
  
          }];
    
}
- (void)setMiddleBtnBlock:(SectionMiddleBtnBlock)middleBtnBlock{
    _middleBtnBlock = middleBtnBlock;
    
    [self.middleBtn mas_makeConstraints:^(MASConstraintMaker *make) {
           make.centerY.mas_equalTo(self);
        make.left.mas_equalTo(self.leftBtn.mas_right).mas_offset(10);
            make.size.mas_equalTo(CGSizeMake(72, 30));
       }];
    [self.lineView2 mas_makeConstraints:^(MASConstraintMaker *make) {
          make.top.mas_equalTo(self.rightBtn.mas_bottom).mas_offset(3);
       make.left.mas_equalTo(self.middleBtn.mas_left).mas_offset(8);
        
                make.size.mas_equalTo(CGSizeMake(56, 2));
   
            }];
    
    
}
- (void)setRightBtnBlock:(SectionRightBtnBlock)rightBtnBlock{
    _rightBtnBlock = rightBtnBlock;
    
    [self.rightBtn mas_makeConstraints:^(MASConstraintMaker *make) {
           make.centerY.mas_equalTo(self);
        make.left.mas_equalTo(self.middleBtn.mas_right).mas_offset(10);
           make.size.mas_equalTo(CGSizeMake(74, 30));
       }];
    
    [self.lineView3 mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.mas_equalTo(self.rightBtn.mas_bottom).mas_offset(3);
        make.left.mas_equalTo(self.rightBtn.mas_left).mas_offset(8);
      
                make.size.mas_equalTo(CGSizeMake(56, 2));
        }];
   
}

-(void)setSexBtnBlock:(SectionSexBtnBlock)sexBtnBlock{
    _sexBtnBlock = sexBtnBlock;
    
    
}


- (UIImageView *)logoImageView
{
    if (!_logoImageView) {
        _logoImageView = [[UIImageView alloc] init];
        [self addSubview:_logoImageView];
    }
    return _logoImageView;
}

- (UILabel *)tipLabel
{
    if (!_tipLabel) {
        _tipLabel = [[UILabel alloc] init];
        _tipLabel.font = JXFontPingFangSCMedium(18);
        _tipLabel.textColor = UIColorHex(000000);
        [self addSubview:_tipLabel];
    }
    return _tipLabel;
}

- (UIImageView *)arrowImageView
{
    if (!_arrowImageView) {
        _arrowImageView = [[UIImageView alloc] initWithImage:[UIImage imageNamed:@"yp_home_attend_arrow"]];
        [self addSubview:_arrowImageView];
    }
    return _arrowImageView;
}

- (UIButton *)detailBtn
{
    if (!_detailBtn) {
        _detailBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_detailBtn setTitle:@"全部" forState:UIControlStateNormal];
        [_detailBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        _detailBtn.titleLabel.font = JXFontPingFangSCRegular(11);
        [_detailBtn addTarget:self action:@selector(detailBtnAction) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_detailBtn];
    }
    return _detailBtn;
}

- (UIButton *)leftBtn
{
    if (!_leftBtn) {
        _leftBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_leftBtn setTitle:@"热门房间" forState:UIControlStateNormal];
//        [_leftBtn setTitleColor:[UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_wenzhi_img_80"]] forState:UIControlStateNormal];
           [_leftBtn setTitleColor:[UIColor colorWithHexString:@"#333333"] forState:UIControlStateNormal];
        _leftBtn.titleLabel.font = JXFontPingFangSCRegular(18);
        [_leftBtn addTarget:self action:@selector(leftBtnAction) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_leftBtn];
    }
    return _leftBtn;
}
- (UIButton *)middleBtn
{
    if (!_middleBtn) {
        _middleBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_middleBtn setTitle:@"优质陪陪" forState:UIControlStateNormal];
        [_middleBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        _middleBtn.titleLabel.font = JXFontPingFangSCRegular(16);
        [_middleBtn addTarget:self action:@selector(middleBtnAction) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_middleBtn];
    }
    return _middleBtn;
}
- (UIButton *)rightBtn
{
    if (!_rightBtn) {
        _rightBtn = [UIButton buttonWithType:UIButtonTypeCustom];
        [_rightBtn setTitle:@"迎萌列表" forState:UIControlStateNormal];
        [_rightBtn setTitleColor:UIColorHex(999999) forState:UIControlStateNormal];
        _rightBtn.titleLabel.font = JXFontPingFangSCRegular(16);
        [_rightBtn addTarget:self action:@selector(rightBtnAction) forControlEvents:UIControlEventTouchUpInside];
        [self addSubview:_rightBtn];
    }
    return _rightBtn;
}

-(UIView*)lineView{
    if (!_lineView) {
        _lineView = [[UIView alloc] init];
//         _lineView.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_colorTitleBG"]];
//         [_lineView.layer addSublayer:[YPFYTool gradientLayerFromColorStrForPoint:@"#60A6FF" toColorStr:@"#E27CFF" height:2 width:64 endpoint:CGPointMake(1.0, 0)]];
        _lineView.backgroundColor = [UIColor colorWithHexString:@"#333333"];
         [self addSubview:_lineView];
    }
    return _lineView;
}

-(UIView*)lineView2{
    if (!_lineView2) {
        _lineView2 = [[UIView alloc] init];
//         _lineView2.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_colorTitleBG"]];
//         [_lineView2.layer addSublayer:[YPFYTool gradientLayerFromColorStrForPoint:@"#60A6FF" toColorStr:@"#E27CFF" height:2 width:64 endpoint:CGPointMake(1.0, 0)]];
         _lineView2.backgroundColor = [UIColor colorWithHexString:@"#333333"];
         [self addSubview:_lineView2];
        _lineView2.hidden = YES;
    }
    return _lineView2;
}
-(UIView*)lineView3{
    if (!_lineView3) {
        _lineView3 = [[UIView alloc] init];
         _lineView3.backgroundColor = [UIColor colorWithHexString:@"#333333"];
//        _lineView3.backgroundColor = [UIColor colorWithPatternImage:[UIImage imageNamed:@"yp_colorTitleBG"]];
//         [_lineView3.layer addSublayer:[YPFYTool gradientLayerFromColorStrForPoint:@"#60A6FF" toColorStr:@"#E27CFF" height:2 width:64 endpoint:CGPointMake(1.0, 0)]];
         [self addSubview:_lineView3];
        _lineView3.hidden = YES;
    }
    return _lineView3;
}


- (UIButton *)sexButton
{
    if (!_sexButton) {
        _sexButton = [UIButton buttonWithType:UIButtonTypeCustom];
        [_sexButton addTarget:self action:@selector(sexBtnAction:) forControlEvents:UIControlEventTouchUpInside];
        _sexButton.hidden = YES;
        [self addSubview:_sexButton];
    }
    return _sexButton;
}

- (UIImageView *)sexImageView
{
    if (!_sexImageView) {
        _sexImageView = [[UIImageView alloc] init];
        _sexImageView.image = [UIImage imageNamed:@"home_peipei_tegetder"];
         _sexImageView.hidden = YES;
        [self addSubview:_sexImageView];
    }
    return _sexImageView;
}
- (UIImageView *)sexImageViewFlag
{
    if (!_sexImageViewFlag) {
        _sexImageViewFlag = [[UIImageView alloc] init];
        _sexImageViewFlag.image= [UIImage imageNamed:@"home_peipei_down"];
        _sexImageViewFlag.hidden = YES;
        [self addSubview:_sexImageViewFlag];
    }
    return _sexImageViewFlag;
}
@end
