//
//  YPGGMyFamilySectionHeader.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGGMyFamilySectionHeader.h"

//#import "YPNotiFriendCore.h"
#import "UIView+getNavigationController.h"
#import "YPSessionViewController.h"

@implementation YPGGMyFamilySectionHeader


- (IBAction)enterBtnAction:(id)sender {
    
    if (self.roomID.length) {
        
        NIMSession *session = [NIMSession session:self.roomID type:NIMSessionTypeTeam];
//        YPNIMSessionViewController *vc = [[YPNIMSessionViewController alloc] initWithSession:session];
//        [[self getNavigationController] pushViewController:vc animated:YES];
        
        YPSessionViewController *vc = [[YPSessionViewController alloc] initWithSession:session];
        vc.familyID = self.familyID;
        [[self getNavigationController] pushViewController:vc animated:YES];
        
    }
}

@end
