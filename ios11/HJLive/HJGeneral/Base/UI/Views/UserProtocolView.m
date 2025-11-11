//
//  UserProtocolView.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UserProtocolView.h"
#import "UIView+XCToast.h"
#import "HJWKWebViewController.h"

#import "HJVersionCoreHelp.h"

@interface UserProtocolView()
@property (weak, nonatomic) IBOutlet UIImageView *checkLogo;
@property (weak, nonatomic) IBOutlet UILabel *protocolLabel;
@property (nonatomic, assign) BOOL select;
@end

@implementation UserProtocolView
+ (instancetype)loadFromNIB {
    return [[NSBundle mainBundle] loadNibNamed:@"UserProtocolView" owner:nil options:nil].firstObject;
}

- (BOOL) isSelect
{
    return _select;
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    _select = YES;
    self.checkLogo.userInteractionEnabled = YES;
    UITapGestureRecognizer *rec = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onCheckLogoClicked:)];
    [self.checkLogo addGestureRecognizer:rec];
    
    self.protocolLabel.userInteractionEnabled = YES;
    UITapGestureRecognizer *rec1 = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(onUserProtocolLabelClicked:)];
    [self.protocolLabel addGestureRecognizer:rec1];
    
}

- (void) onCheckLogoClicked:(UITapGestureRecognizer *)rec
{
    if (_select) {
        _select = NO;
        self.checkLogo.image = [UIImage imageNamed:@"hj_protocol_logo_un_select"];
    } else {
        _select = YES;
        self.checkLogo.image = [UIImage imageNamed:@"hj_protocol_logo"];
    }
    if (self.delegate != nil) {
        [self.delegate onSelectBtnClicked:_select];
    }
}

- (void) onUserProtocolLabelClicked:(UITapGestureRecognizer *)rec
{
    if (self.nav != nil) {
        HJWKWebViewController *vc = [[HJWKWebViewController alloc]init];
//        vc.type = WebViewTypeProtocal;
        NSString *urlSting = [NSString stringWithFormat:@"%@/front/agreement/agreement.html",[HJHttpRequestHelper getHostUrl]];
        vc.url = [NSURL URLWithString:urlSting];
        [self.nav pushViewController:vc animated:YES];
    }
}



@end
