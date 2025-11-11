//
//  YPSexPopVC.m
//  HJLive
//
//  Created by feiyin on 2020/9/9.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPSexPopVC.h"

@interface YPSexPopVC ()
@property (weak, nonatomic) IBOutlet UIImageView *all_ImageView;
@property (weak, nonatomic) IBOutlet UIImageView *weman_ImageView;
@property (weak, nonatomic) IBOutlet UIImageView *man_ImageView;

@end

@implementation YPSexPopVC

- (void)viewDidLoad {
    [super viewDidLoad];
   self.preferredContentSize = CGSizeMake(86, 131);
    self.view.backgroundColor = [UIColor clearColor];
}

- (IBAction)allBtnAction:(id)sender {
    if (_allBtnBlock) {
           _allBtnBlock();
       }
}

- (IBAction)wemanBtnAction:(id)sender {
    if (_wemanBtnBlock) {
        _wemanBtnBlock();
    }
}
- (IBAction)manBtnAction:(id)sender {
    if (_manBtnBlock) {
           _manBtnBlock();
       }
}

@end
