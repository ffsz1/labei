//
//  GGMyFamilySectionHeader.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "GGMyFamilySectionHeader.h"

//#import "HJNotiFriendCore.h"
#import "UIView+getNavigationController.h"
#import "HJSessionViewController.h"

@implementation GGMyFamilySectionHeader


- (IBAction)enterBtnAction:(id)sender {
    
    if (self.roomID.length) {
        
        NIMSession *session = [NIMSession session:self.roomID type:NIMSessionTypeTeam];
//        NIMSessionViewController *vc = [[NIMSessionViewController alloc] initWithSession:session];
//        [[self getNavigationController] pushViewController:vc animated:YES];
        
        HJSessionViewController *vc = [[HJSessionViewController alloc] initWithSession:session];
        vc.familyID = self.familyID;
        [[self getNavigationController] pushViewController:vc animated:YES];
        
    }
}

@end
