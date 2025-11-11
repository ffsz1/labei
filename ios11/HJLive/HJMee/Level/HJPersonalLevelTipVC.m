//
//  HJPersonalLevelTipVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPersonalLevelTipVC.h"

@interface HJPersonalLevelTipVC ()
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *imageHeight;

@end

@implementation HJPersonalLevelTipVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"等级说明";
    
    NSString * imageStr = self.type == 0 ? @"hj_me_dengjiguize" : @"hj_me_meiliguize";
    if (self.type == 0) {
        self.imageHeight.constant = 439;
    }else{
        self.imageHeight.constant = 505;
    }

    [self.image setImage:[UIImage imageNamed:imageStr]];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];

}

@end
