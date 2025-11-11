//
//  YPPersonalLevelTipVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/18.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPersonalLevelTipVC.h"

@interface YPPersonalLevelTipVC ()
@property (weak, nonatomic) IBOutlet UIImageView *image;
@property (weak, nonatomic) IBOutlet NSLayoutConstraint *imageHeight;

@end

@implementation YPPersonalLevelTipVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = @"等级说明";
    
    NSString * imageStr = self.type == 0 ? @"yp_me_dengjiguize" : @"yp_me_meiliguize";
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
