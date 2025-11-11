//
//  YPHomeMyFollowCCell.m
//  HJLive
//
//  Created by feiyin on 2020/6/22.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPHomeMyFollowCCell.h"
#import "UIView+getTopVC.h"
#import "YPRoomPusher.h"

@implementation YPHomeMyFollowCCell
- (IBAction)findHimAction:(id)sender {
    
    NSString *isFirst = [[NSUserDefaults standardUserDefaults]objectForKey:@"isFirstEnterRoom"];
    if (![isFirst isEqualToString:@"1"]) {
        
        @weakify(self);
        UIAlertController *alert = [UIAlertController alertControllerWithTitle:@"房间使用协议" message:NSLocalizedString(@"      房间是你与别人互动的地方，官方倡导绿色健康的房间体验，请务必文明用语。严禁涉及色情，政治等不良信息，若封面、背景含低俗、引导、暴露等不良内容都将会被永久封号，对于引起不适的内容请用户及时举报，我们会迅速响应处理！\n同意即可开始使用房间功能！", nil) preferredStyle:UIAlertControllerStyleAlert];
        UIAlertAction *enter = [UIAlertAction actionWithTitle:@"同意" style:UIAlertActionStyleDestructive handler:^(UIAlertAction * _Nonnull action) {
            @strongify(self);
            
            
            [YPRoomPusher pushUserInRoomByID:self.model.uid];
            
            [[NSUserDefaults standardUserDefaults]setObject:@"1" forKey:@"isFirstEnterRoom"];
            [[NSUserDefaults standardUserDefaults]synchronize];
        }];
        UIAlertAction *cancel = [UIAlertAction actionWithTitle:@"取消" style:UIAlertActionStyleCancel handler:^(UIAlertAction * _Nonnull action) {
            
        }];
        [alert addAction:cancel];
        [alert addAction:enter];
        [[self topViewController] presentViewController:alert animated:YES completion:nil];
    }else{
        [YPRoomPusher pushUserInRoomByID:self.model.uid];
    }
    
}

- (void)setModel:(YPAttention *)model
{
    _model = model;
    
    [self.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
    
    self.sexImageView.image = [UIImage imageNamed:model.gender==UserInfo_Male?@"yp_home_attend_man":@"yp_home_attend_woman"];
    
    self.nameLabel.text = model.nick;
}

@end
