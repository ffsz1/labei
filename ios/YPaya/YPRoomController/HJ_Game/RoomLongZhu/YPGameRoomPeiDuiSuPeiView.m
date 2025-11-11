//
//  YPGameRoomPeiDuiSuPeiView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomPeiDuiSuPeiView.h"
#import "HJLongZhuCoreClient.h"

@interface YPGameRoomPeiDuiSuPeiView ()<HJLongZhuCoreClient>

@property (weak, nonatomic) IBOutlet UIImageView *ballImageView1;
@property (weak, nonatomic) IBOutlet UIImageView *ballImageView2;
@property (weak, nonatomic) IBOutlet UIImageView *ballImageView3;

@property (weak, nonatomic) IBOutlet UIButton *supeiBtn;
@property (weak, nonatomic) IBOutlet UIButton *showBtn;


@end

@implementation YPGameRoomPeiDuiSuPeiView

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    self.supeiBtn.enabled = NO;
    self.showBtn.enabled = NO;
    self.isAnimation = NO;
    AddCoreClient(HJLongZhuCoreClient, self);
}
- (IBAction)cancelBtnAction:(id)sender {
    
    self.supeiBtn.enabled = NO;
    self.showBtn.enabled = NO;
    if (self.cancelBtnActionBlock) {
        self.cancelBtnActionBlock();
    }
}

- (IBAction)showBtnAction:(id)sender {
    self.supeiBtn.enabled = NO;
    self.showBtn.enabled = NO;
    if (self.showBtnActionBlock) {
        self.showBtnActionBlock();
    }
}

- (IBAction)supeiBtnAction:(id)sender {
    self.supeiBtn.enabled = NO;
    self.showBtn.enabled = NO;
    if (self.supeiBtnActionBlock) {
        self.supeiBtnActionBlock();
    }
}

- (IBAction)ruleBtnAction:(id)sender {
    
    if (self.ruleBtnActionBlock) {
        self.ruleBtnActionBlock();
    }
}

- (void)setSelectedNum:(NSInteger)selectedNum {
    
    _selectedNum = selectedNum;
    
    if (selectedNum <= 0) {
        self.supeiBtn.enabled = YES;
        self.showBtn.enabled = NO;
        self.cancelBtn.hidden = YES;

    }
    else {
        self.supeiBtn.enabled = NO;
        self.showBtn.enabled = YES;
        self.cancelBtn.hidden = NO;

        NSInteger num1 = selectedNum / 100;
        NSInteger num2 = (selectedNum - num1 * 100) / 10;
        NSInteger num3 = selectedNum - num1 * 100 - num2 * 10;
        
        self.ballImageView1.image = [UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num1]];
        self.ballImageView2.image = [UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num2]];
        self.ballImageView3.image = [UIImage imageNamed:[NSString stringWithFormat:@"room_game_ball_%zd",num3]];
    }
}


#pragma mark - LongZhuCoreClient
// 获取速配随机数/保存自己选择的数
- (void)getChooseResultSuccessWithResult:(NSInteger)result type:(NSInteger)type {
    // 动画
    if (type == 1) {
        
        NSArray * imgsArr = @[
                              [UIImage imageNamed:@"room_game_ball_0_1"],
                              [UIImage imageNamed:@"room_game_ball_0_2"],
                              [UIImage imageNamed:@"room_game_ball_0_3"],
                              ];
        // 设置动画图片数组
        [self.ballImageView1 setAnimationImages:imgsArr];
        [self.ballImageView1 setAnimationDuration:0.2];
        self.ballImageView1.animationRepeatCount = 4;
        
        [self.ballImageView2 setAnimationImages:imgsArr];
        [self.ballImageView2 setAnimationDuration:0.2];
        self.ballImageView2.animationRepeatCount = 4;
        
        [self.ballImageView3 setAnimationImages:imgsArr];
        [self.ballImageView3 setAnimationDuration:0.2];
        self.ballImageView3.animationRepeatCount = 4;
        
        [self.ballImageView1 startAnimating];
        [self.ballImageView2 startAnimating];
        [self.ballImageView3 startAnimating];
        
        self.supeiBtn.enabled = NO;
        self.isAnimation = YES;
        
        @weakify(self);
        dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(0.2 * 4 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
            @strongify(self);
            self.selectedNum = result;
            self.isAnimation = NO;
        });
        
    }
}
- (void)getChooseResultFailedWithMessage:(NSString *)message type:(NSInteger)type {
    self.supeiBtn.enabled = YES;
    self.showBtn.enabled = NO;
}

// 展示结果
- (void)confirmResultFailedWithMessage:(NSString *)message type:(NSInteger)type {
    self.supeiBtn.enabled = NO;
    self.showBtn.enabled = YES;
}

@end
