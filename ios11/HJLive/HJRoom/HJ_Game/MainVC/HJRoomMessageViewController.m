//
//  HJRoomMessageViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomMessageViewController.h"
#import "HJMessageListView.h"

#import "HJRoomSquareMsgVC.h"

@interface HJRoomMessageViewController ()

@property (nonatomic, strong) UIView *bV;
@property (nonatomic, strong) UIView *cover;
@end

@implementation HJRoomMessageViewController

- (void)viewWillLayoutSubviews
{
    [UIView animateWithDuration:0.3 animations:^{
        self.bV.transform = CGAffineTransformMakeTranslation(0, -(XC_SCREE_H-180));
        self.cover .alpha = 0.5;
    } completion:^(BOOL finished) {
        
    }];
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    self.cover = [[UIView alloc] init];
    self.cover .backgroundColor = [UIColor blackColor];
    self.cover .alpha = 0.0;
    self.cover .frame = self.view.bounds;;//CGRectMake(0, 0, XC_SCREE_W, 180+XC_Height_NavBar);//self.view.bounds;
    [self.view addSubview:self.cover ];
    
    self.bV = [[UIView alloc] init];
    self.bV.backgroundColor = [UIColor clearColor];
    self.bV.frame = CGRectMake(0, XC_SCREE_H, XC_SCREE_W, XC_SCREE_H-180);//self.view.bounds;
    [self.view addSubview:self.bV];
    
    HJMessageListView *listView = [[NSBundle mainBundle] loadNibNamed:@"HJMessageListView" owner:nil options:nil][0];
    
    [self.bV addSubview:listView];
    
    @weakify(self);
    [listView mas_makeConstraints:^(MASConstraintMaker *make) {
//        @strongify(self);
        make.edges.mas_equalTo(0);
    }];
    
    HJSessionListViewController *vc = [[HJSessionListViewController alloc] init];
    [self addChildViewController:vc];
    vc.isFromRoom = YES;
    [listView.contentView addSubview:vc.view];
    
//    @weakify(listView);
    [vc.view mas_makeConstraints:^(MASConstraintMaker *make) {
//        @strongify(listView);
        make.top.leading.trailing.bottom.mas_equalTo(0);
    }];
    @weakify(vc);
    [vc setRoomMessageListDidSelectCell:^(NSInteger row) {
        @strongify(self);
        @strongify(vc);
        NIMRecentSession *recent = vc.recentSessions[row];
        if (self.roomMessageListDidSelectCell) {
            self.roomMessageListDidSelectCell(recent, [vc nameForRecentSession:recent]);
        }
    }];
    
    
    [listView setCloseBtnActionBlock:^{
        @strongify(self);
        if (self.addBadgeBlock) {
            self.addBadgeBlock();
        }
//        self.messageListWindow.hidden = YES;
//        self.messageListWindow.hidden = YES;
//        [self.messageListWindow resignKeyWindow];
//        [self.messageListWindow removeFromSuperview];
//        self.messageListWindow = nil;
//
//        if (self.didCloseBlock) {
//            self.didCloseBlock();
//        }
        
        if (self.didCloseBlock) {
            self.didCloseBlock();
        }
        [UIView animateWithDuration:0.3 animations:^{
            self.bV.transform = CGAffineTransformIdentity;
            self.cover .alpha = 0.0;
        } completion:^(BOOL finished) {
            
            [self.bV removeFromSuperview];
            
            self.bV = nil;
            
            self.messageListWindow.hidden = YES;
            self.messageListWindow.hidden = YES;
            [self.messageListWindow resignKeyWindow];
            [self.messageListWindow removeFromSuperview];
            self.messageListWindow = nil;
        }];
        
    }];
    
    [self.cover  addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        @strongify(self);
        if (self.addBadgeBlock) {
            self.addBadgeBlock();
        }
//        self.messageListWindow.hidden = YES;
//        self.messageListWindow.hidden = YES;
//        [self.messageListWindow resignKeyWindow];
//        [self.messageListWindow removeFromSuperview];
//        self.messageListWindow = nil;
//        if (self.didCloseBlock) {
//            self.didCloseBlock();
//        }
        if (self.didCloseBlock) {
            self.didCloseBlock();
        }
        [UIView animateWithDuration:0.3 animations:^{
            
            self.bV.transform = CGAffineTransformIdentity;
            self.cover .alpha = 0.0;
        } completion:^(BOOL finished) {
            
            [self.bV removeFromSuperview];
            
            self.bV = nil;
            
            self.messageListWindow.hidden = YES;
            self.messageListWindow.hidden = YES;
            [self.messageListWindow resignKeyWindow];
            [self.messageListWindow removeFromSuperview];
            self.messageListWindow = nil;
        }];
    }]];
    
    HJRoomSquareMsgVC *squareVC = HJRoomStoryBoard(@"HJRoomSquareMsgVC");
    [self addChildViewController:squareVC];
    [listView.squareView addSubview:squareVC.view];
    
    squareVC.squareBlock = ^{
        @strongify(self);
        self.messageListWindow.hidden = YES;
        self.messageListWindow.hidden = YES;
        [self.messageListWindow resignKeyWindow];
        [self.messageListWindow removeFromSuperview];
        self.messageListWindow = nil;
        
        if (self.didCloseBlock) {
            self.didCloseBlock();
        }
        
        if (self.squareBlock) {
            self.squareBlock();
        }
    };
    
    [squareVC.view mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.leading.trailing.bottom.mas_equalTo(0);
    }];
    
}

@end
