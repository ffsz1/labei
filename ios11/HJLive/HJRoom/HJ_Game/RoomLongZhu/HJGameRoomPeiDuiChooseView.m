//
//  HJGameRoomPeiDuiChooseView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomPeiDuiChooseView.h"
#import "HJGameRoomPeiDuiRuleView.h"

#import "HJLongZhuCoreClient.h"

typedef NS_ENUM(NSUInteger, XCGameRoomPeiDuiChooseViewType) {
    XCGameRoomPeiDuiChooseViewTypeDidNotChoose,
    XCGameRoomPeiDuiChooseViewTypeChoosing,
    XCGameRoomPeiDuiChooseViewTypeDidSure,
};

@interface HJGameRoomPeiDuiChooseView ()<HJLongZhuCoreClient>

@property (weak, nonatomic) IBOutlet UIButton *oneBtn;
@property (weak, nonatomic) IBOutlet UIButton *twoBtn;
@property (weak, nonatomic) IBOutlet UIButton *threeBtn;
@property (weak, nonatomic) IBOutlet UIButton *fourBtn;
@property (weak, nonatomic) IBOutlet UIButton *fiveBtn;
@property (weak, nonatomic) IBOutlet UIButton *sixBtn;
@property (weak, nonatomic) IBOutlet UIButton *sevenBtn;
@property (weak, nonatomic) IBOutlet UIButton *eightBtn;
@property (weak, nonatomic) IBOutlet UIButton *nighBtn;
@property (weak, nonatomic) IBOutlet UIButton *randomBtn;

@property (weak, nonatomic) IBOutlet UIButton *chooseBtn;
@property (weak, nonatomic) IBOutlet UIButton *showBtn;

@property (nonatomic, assign) XCGameRoomPeiDuiChooseViewType tyoe;
@property (nonatomic, assign) NSInteger temSelctedNum;

@end

@implementation HJGameRoomPeiDuiChooseView

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    AddCoreClient(HJLongZhuCoreClient, self);
    self.chooseBtn.enabled = NO;
    self.showBtn.enabled = NO;
    self.temSelctedNum = 0;
}

- (void)setSelectedNum:(NSInteger)selectedNum {
    
    _selectedNum = selectedNum;
    [self setupDefaltBall];
    if (selectedNum >= 1 && selectedNum <= 9) {
        self.temSelctedNum = selectedNum;
        self.tyoe = XCGameRoomPeiDuiChooseViewTypeDidSure;
        switch (selectedNum) {
            case 1:
                self.oneBtn.selected = YES;
                break;
            case 2:
                self.twoBtn.selected = YES;
                break;
            case 3:
                self.threeBtn.selected = YES;
                break;
            case 4:
                self.fourBtn.selected = YES;
                break;
            case 5:
                self.fiveBtn.selected = YES;
                break;
            case 6:
                self.sixBtn.selected = YES;
                break;
            case 7:
                self.sevenBtn.selected = YES;
                break;
            case 8:
                self.eightBtn.selected = YES;
                break;
            case 9:
                self.nighBtn.selected = YES;
                break;
                
            default:
                break;
        }
    }
    else {
        self.tyoe = XCGameRoomPeiDuiChooseViewTypeDidNotChoose;
        self.temSelctedNum = 0;
    }
    
    [self handleType];
}

- (void)handleType {
    
    switch (self.tyoe) {
        case XCGameRoomPeiDuiChooseViewTypeDidNotChoose: // 未选择
        {
            [self setupNumBallEnable:YES];
            self.randomBtn.userInteractionEnabled = YES;
            self.chooseBtn.enabled = NO;
            self.showBtn.enabled = NO;
            break;
        }
        case XCGameRoomPeiDuiChooseViewTypeChoosing: // 正在选择
        {
            [self setupNumBallEnable:YES];
            self.randomBtn.userInteractionEnabled = YES;
            self.chooseBtn.enabled = YES;
            self.showBtn.enabled = NO;
            break;
        }
        case XCGameRoomPeiDuiChooseViewTypeDidSure: // 已经选择
        {
            [self setupNumBallEnable:NO];
            self.randomBtn.userInteractionEnabled = NO;
            self.chooseBtn.enabled = NO;
            self.showBtn.enabled = YES;
            break;
        }
        default:
            break;
    }
}

- (void)setupNumBallEnable:(BOOL)enable {
    self.oneBtn.userInteractionEnabled = enable;
    self.twoBtn.userInteractionEnabled = enable;
    self.threeBtn.userInteractionEnabled = enable;
    self.fourBtn.userInteractionEnabled = enable;
    self.fiveBtn.userInteractionEnabled = enable;
    self.sixBtn.userInteractionEnabled = enable;
    self.sevenBtn.userInteractionEnabled = enable;
    self.eightBtn.userInteractionEnabled = enable;
    self.nighBtn.userInteractionEnabled = enable;
}

- (void)setupDefaltBall {
    self.oneBtn.selected = NO;
    self.twoBtn.selected = NO;
    self.threeBtn.selected = NO;
    self.fourBtn.selected = NO;
    self.fiveBtn.selected = NO;
    self.sixBtn.selected = NO;
    self.sevenBtn.selected = NO;
    self.eightBtn.selected = NO;
    self.nighBtn.selected = NO;
}

- (IBAction)numBtnAction:(id)sender {
    
    [self setupDefaltBall];

    UIButton *btn = (UIButton *)sender;
    self.temSelctedNum = btn.tag;
    btn.selected = YES;

    self.tyoe = XCGameRoomPeiDuiChooseViewTypeChoosing;
    [self handleType];
}


- (IBAction)randomBtnAction:(id)sender {
    
    int num = [self getRandomNumber:1 to:9];
    NSLog(@"%d",num);
    UIButton *btn = [self viewWithTag:num];
    [self numBtnAction:btn];
    
    if (self.randomBtnActionBlock) {
        self.randomBtnActionBlock();
    }
}

- (int)getRandomNumber:(int)from to:(int)to
{
    return (int)(from + (arc4random() % (to - from + 1)));
}

- (IBAction)chooseBtnAction:(id)sender {
    
    self.selectedNum = self.temSelctedNum;
    
    if (self.chooseBtnActionBlock) {
        self.chooseBtnActionBlock();
    }
}
- (IBAction)showBtnAction:(id)sender {
    
    self.showBtn.userInteractionEnabled = NO;
    
    if (self.showBtnActionBlock) {
        self.showBtnActionBlock();
    }
}
- (IBAction)ruleBtnAction:(id)sender {
    
    if (self.ruleBtnActionBlock) {
        self.ruleBtnActionBlock();
    }
    
}

#pragma mark - LongZhuCoreClient
// 获取速配随机数/保存自己选择的数
- (void)getChooseResultFailedWithMessage:(NSString *)message type:(NSInteger)type {
    
    if (type == 2) {
        
        NSInteger temNum = self.temSelctedNum;
        self.chooseBtn.userInteractionEnabled = YES;
        UIButton *btn = [self viewWithTag:self.temSelctedNum];
        self.selectedNum = 0;
        [self numBtnAction:btn];
        self.temSelctedNum = temNum;
    }
}

// 展示结果
- (void)confirmResultFailedWithMessage:(NSString *)message type:(NSInteger)type {
    if (type == 2) {
        self.showBtn.userInteractionEnabled = YES;
    }
}

@end
