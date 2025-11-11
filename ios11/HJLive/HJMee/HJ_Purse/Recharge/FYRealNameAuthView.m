//
//  FYRealNameAuthView.m
//  XBD
//
//  Created by feiyin on 2019/11/21.
//

#import "FYRealNameAuthView.h"
@interface FYRealNameAuthView()

@property (copy, nonatomic) RealNameAuthBlock menuBlock;
@property (weak, nonatomic) IBOutlet UIView *effectView;
@property (weak, nonatomic) IBOutlet UIView *bgview;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *mobileCenterYLayout;
@property (weak, nonatomic) IBOutlet UILabel *desLabel;



@end
@implementation FYRealNameAuthView


+ (void)show:(RealNameAuthBlock)menuBlock
{
    FYRealNameAuthView *shareView = [[NSBundle mainBundle]loadNibNamed:@"FYRealNameAuthView" owner:self options:nil][0];
    shareView.frame = CGRectMake(0, 0, XC_SCREE_W, XC_SCREE_H);
    
    [[UIApplication sharedApplication].keyWindow addSubview:shareView];

    shareView.menuBlock = menuBlock;
    shareView.mobileCenterYLayout.constant = kScreenHeight;
    [shareView layoutIfNeeded];
    
    [UIView animateWithDuration:0.5 delay:0 usingSpringWithDamping:0.7 initialSpringVelocity:0 options:UIViewAnimationOptionCurveEaseIn animations:^{
        
        
    
         shareView.mobileCenterYLayout.constant = 0;
        
        [shareView layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        
        
    }];
}

- (void)awakeFromNib
{
    [super awakeFromNib];
    self.bgview.layer.cornerRadius = 20;
    self.bgview.layer.masksToBounds = YES;

    
    
    NSMutableAttributedString *contentStr = [[NSMutableAttributedString alloc] initWithString:@"未实名认证-无法兑换收益"];
    //找出特定字符在整个字符串中的位置
    NSRange redRange = NSMakeRange([[contentStr string] rangeOfString:@"无法兑换收益"].location, [[contentStr string] rangeOfString:@"无法兑换收益"].length);
    //修改特定字符的颜色
    [contentStr addAttribute:NSForegroundColorAttributeName value:[UIColor colorWithRGB:0xEE47B7 alpha:1] range:redRange];
    //修改特定字符的字体大小
    [contentStr addAttribute:NSFontAttributeName value:[UIFont boldSystemFontOfSize:20] range:redRange];
   
    self.desLabel.attributedText = contentStr;
    
   UITapGestureRecognizer* tapGest = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(tapAction)];
    [self.effectView addGestureRecognizer:tapGest];
    
}

- (void)close
{
    [self layoutIfNeeded];
    
    [UIView animateWithDuration:0.4 animations:^{
        
        self.mobileCenterYLayout.constant = kScreenHeight;
   
        [self layoutIfNeeded];
        
    } completion:^(BOOL finished) {
        [self removeFromSuperview];
    }];
    
}




- (IBAction)confirmAction:(id)sender {
    self.menuBlock(FYConfirmType);
    [self close];
}


- (IBAction)cancelBtnAction:(id)sender {
    self.menuBlock(FYRealNameCancelType);
    [self close];
}



-(void)tapAction{
     [self close];
    
}

@end
