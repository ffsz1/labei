//
//  YPRoomMessageTableViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPRoomMessageTableViewController.h"

#import "YPRoomMessageview.h"

@interface YPRoomMessageTableViewController ()

@property (nonatomic, strong) UIView *bV;

@end

@implementation YPRoomMessageTableViewController

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.self.bV = [UIView new];
    
    
    UIView *bbV = [UIView new];
    bbV.backgroundColor = [UIColor blackColor];
    bbV.alpha = 0.5;
    bbV.frame = self.view.bounds;
    [self.view addSubview:bbV];
    
    self.view.backgroundColor = [UIColor clearColor];
    
    self.bV = [UIView new];
    self.bV.backgroundColor = [UIColor clearColor];
//    self.bV.frame = self.view.bounds;
    self.bV.frame = CGRectMake(0, 180, XC_SCREE_W, XC_SCREE_H-180);//self.view.bounds;
    [self.view addSubview:self.bV];
    
    
    
    YPRoomMessageview *messageview = [[NSBundle mainBundle] loadNibNamed:@"YPRoomMessageview" owner:nil options:nil][0];
    messageview.titleLabel.text = self.topTitle;;
    
    [self.bV addSubview:messageview];
    [messageview mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.mas_equalTo(0);
    }];
    
    
    @weakify(self)
//    [messageview mas_makeConstraints:^(MASConstraintMaker *make) {
//        @strongify(self);
//        make.left.equalTo(@2);
//        make.right.equalTo(@-2);
//        if (iPhoneX) {
//            if (@available(iOS 11.0, *)) {
//                make.top.equalTo(self.bV.mas_safeAreaLayoutGuideTop).offset(180);
//                make.bottom.equalTo(self.bV.mas_safeAreaLayoutGuideBottom);
//            }
//        } else {
//            make.top.equalTo(self.bV.mas_top).offset(180);
//            make.bottom.equalTo(self.bV.mas_bottom);
//        }
//    }];
    
    YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:self.recentSession.session];
    ;
    [vc setRoomMessageListDidSelectCell:^UINavigationController *{
        
        @strongify(self);
        if (self.addBadgeBlock) {
            self.addBadgeBlock();
        }
        self.messageWindow.hidden = YES;
        [self.messageWindow resignKeyWindow];
        [self.messageWindow removeFromSuperview];
        self.messageWindow = nil;
        self.messageListWindow.hidden = YES;
        [self.messageListWindow resignKeyWindow];
        [self.messageListWindow removeFromSuperview];
        self.messageListWindow = nil;
        
        if (self.didCloseAllBlock) {
            self.didCloseAllBlock();
        }
        
        if (self.roomMessageListDidSelectCell) {
            return self.roomMessageListDidSelectCell();
        }
        else {
            return nil;
        }
    }];
    
    [self addChildViewController:vc];
    [messageview.contentView addSubview:vc.view];
    
//    @weakify(messageview);
    [vc.view mas_makeConstraints:^(MASConstraintMaker *make) {
//        @strongify(messageview);
        make.top.leading.trailing.bottom.mas_equalTo(0);
    }];
    
    
    [messageview setCloseBtnActionBlock:^{
        @strongify(self);
        if (self.addBadgeBlock) {
            self.addBadgeBlock();
        }
        self.messageWindow.hidden = YES;
        [self.messageWindow resignKeyWindow];
        [self.messageWindow removeFromSuperview];
        self.messageWindow = nil;
        
        if (self.didCloseBlock) {
            self.didCloseBlock();
        }
    }];
    
    [bbV addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        @strongify(self);
        if (self.addBadgeBlock) {
            self.addBadgeBlock();
        }
        self.messageWindow.hidden = YES;
        [self.messageWindow resignKeyWindow];
        [self.messageWindow removeFromSuperview];
        self.messageWindow = nil;
        if (self.didCloseBlock) {
            self.didCloseBlock();
        }
    }]];
    
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/*
#pragma mark - Navigation

// In a storyboard-based application, you will often want to do a little preparation before navigation
- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender {
    // Get the new view controller using [segue destinationViewController].
    // Pass the selected object to the new view controller.
}
*/

@end
