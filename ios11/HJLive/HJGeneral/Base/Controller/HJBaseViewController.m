//
//  HJBaseViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJBaseViewController.h"

@interface HJBaseViewController ()

@end

@implementation HJBaseViewController


- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    //显示导航栏
    if (self.isHiddenNavBar==YES)
    {
        [self.navigationController setNavigationBarHidden:NO animated:YES];
    }
    
}


- (void)viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    
    //隐藏导航栏
    if (self.isHiddenNavBar==YES)
    {
        [self.navigationController setNavigationBarHidden:YES animated:YES];
    }
    
    @weakify(self);
    [self.navigationController.navigationBar.subviews enumerateObjectsUsingBlock:^(UIView *view, NSUInteger idx, BOOL *stop) {
        @strongify(self);
        if (iOS10) {
            //iOS10,改变了导航栏的私有接口为_UIBarBackground
            if ([view isKindOfClass:NSClassFromString(@"_UIBarBackground")]) {
                
                [view.subviews firstObject].hidden = self.isHiddenNavBar;
            }
        }else{
            //iOS10之前使用的是_UINavigationBarBackground
            if ([view isKindOfClass:NSClassFromString(@"_UINavigationBarBackground")]) {
                
                [view.subviews firstObject].hidden = self.isHiddenNavBar;
            }
        }
    }];
}


- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    self.view.backgroundColor = [UIColor whiteColor];
   
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

@end
