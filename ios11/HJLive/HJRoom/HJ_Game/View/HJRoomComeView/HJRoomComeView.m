//
//  HJRoomComeView.m
//  HJLive
//
//  Created by feiyin on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomComeView.h"

@interface HJRoomComeView()

@property (strong,nonatomic) NSMutableArray *msgArr;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *width_name;
@property (assign,nonatomic) BOOL isShowing;


@end

@implementation HJRoomComeView

- (void)show:(ChatRoomMember *)member
{
    if (member == nil) {
        return;
    }
    
    //加入动画队列
    if (![self checkIsInArr:member.account]) {
        [self.msgArr addObject:member];
    }
    
    
    if (!self.isShowing) {
        [self setEnterAnimation];
    }
    
    
    
}

//开始进场动画
- (void)setEnterAnimation
{
    /*
     检测队列中是否有待执行目标
        有 - 执行动画
        没 - 跳过动画
     */
    if (self.msgArr.count>0) {
        self.isShowing = YES;
        
        
        ChatRoomMember *member = self.msgArr.firstObject;
        self.nameLabel.text = member.nick;
        
        //回调外部执行坐骑svga动画
        if (self.carBlock) {
            self.carBlock(member.car_url);
        }
        
        //适配最大宽度
        CGSize size = [self.nameLabel.text boundingRectWithSize:CGSizeMake(0, self.nameLabel.height) options:(NSStringDrawingUsesLineFragmentOrigin|NSStringDrawingUsesFontLeading) attributes:@{NSFontAttributeName:self.nameLabel.font} context:nil].size;
        self.width_name.constant = size.width>100?100:size.width;
        
        
        if (self.superview == nil) {
            return;
        }
        
        [self.superview layoutIfNeeded];
        [UIView animateWithDuration:0.5 animations:^{
            
            if (self.superview) {
                [self mas_updateConstraints:^(MASConstraintMaker *make) {
                    make.leading.mas_equalTo(self.superview.mas_trailing).mas_offset(-kScreenWidth+15);
                }];
                [self.superview layoutIfNeeded];
            }
            

        } completion:^(BOOL finished) {
            
            if (self.superview) {
                //停留1.5s
                dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1.5 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
                    [self setOutAnimation];
                });
            }
            
        }];
    }
    
}

//开始出场动画
- (void)setOutAnimation
{
    if (self.superview == nil) {
        return;
    }
    
    
    [self.superview layoutIfNeeded];

    if (self.superview) {
        [UIView animateWithDuration:0.3 animations:^{
            
            
            if (self.superview) {
                [self mas_updateConstraints:^(MASConstraintMaker *make) {
                    make.leading.mas_equalTo(self.superview.mas_trailing).mas_offset(-kScreenWidth-self.width);
                }];
                [self.superview layoutIfNeeded];
            }
            
            
        } completion:^(BOOL finished) {
            
            
            if (self.superview) {
                
                //移除消息队列
                [self.msgArr removeFirstObject];
                self.isShowing = NO;
                
                [self mas_updateConstraints:^(MASConstraintMaker *make) {
                    make.leading.mas_equalTo(self.superview.mas_trailing);
                }];
                //继续执行队列
                [self setEnterAnimation];
            }
            
            
            
            
        }];
    }
    
    
}

- (BOOL)checkIsInArr:(NSString *)uid {
    for (ChatRoomMember *member in self.msgArr) {
        if ([member.account isEqualToString:uid]) {
            return YES;
        }
    }
    return false;
}

- (NSMutableArray *)msgArr
{
    if (!_msgArr) {
        _msgArr = [[NSMutableArray alloc] init];
    }
    return _msgArr;
}




@end
