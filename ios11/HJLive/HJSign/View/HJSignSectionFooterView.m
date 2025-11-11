
//
//  HJSignSectionFooterView.m
//  HJLive
//
//  Created by apple on 2019/5/22.
//

#import "HJSignSectionFooterView.h"

#import "UIImage+JXImageEffect.h"
#import "UIColor+JXBase.h"

@implementation HJSignSectionFooterView


- (instancetype)initWithReuseIdentifier:(NSString *)reuseIdentifier
{
    self = [super initWithReuseIdentifier:reuseIdentifier];
    if (self) {
        [self addControls];
        [self layoutControls];
    }
    return self;
}


#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.cardView];
}

- (void)layoutControls {
    [self.cardView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self);
        make.leading.equalTo(self).offset(15);
        make.trailing.equalTo(self).offset(-15);
        make.height.equalTo(@(20));
    }];
}

#pragma mark - setters/getters
- (UIImageView *)cardView {
    if (!_cardView) {
        _cardView = [UIImageView new];
        
        CGFloat width = XC_SCREE_W - 15 * 2;
        UIImage *image = [UIImage jx_imageWithColor:[UIColor jx_colorWithHexString:@"#FFFFFF"] size:CGSizeMake(width, 20)];
        image = [image jx_imageByRoundCornerRadius:10.f corners:UIRectCornerBottomLeft|UIRectCornerBottomRight borderWidth:0 borderColor:nil borderLineJoin:kCGLineJoinMiter];
        _cardView.image = image;
    }
    return _cardView;
}
@end
