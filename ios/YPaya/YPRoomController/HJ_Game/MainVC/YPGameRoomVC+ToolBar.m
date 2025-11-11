//
//  YPGameRoomVC+ToolBar.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGameRoomVC+ToolBar.h"
#import "YPGameRoomVC+Alert.h"

#import "UIImage+Gif.h"
#import "YPImMessageCore.h"
#import "HJImMessageCoreClient.h"


@implementation YPGameRoomVC (ToolBar)


- (void)updateToolBar {
//    [self.toolBar removeFromSuperview];
//    self.toolBar = nil;
    BOOL needAddSubview = YES;
    for (UIView *view in self.view.subviews) {
        if ([view isKindOfClass:[YPFinishLiveView class]]) {
            needAddSubview = NO;
        }
    }
    if (needAddSubview) {
//        self.toolBar = [[YPToolBar alloc]init];
        self.toolBar.delegate = self;
        self.toolBar.itemWidth = 40;
        self.toolBar.itemHeight = 55;
        self.toolBar.widthSpacing = 0;
//        if (!self.isFirstAddToolbar) {
//             self.toolBar.items = [self getItemsArray];
//        }
         self.toolBar.items = [self getItemsArray];
//        [self.view addSubview:self.toolBar];
        CGFloat width = kScreenWidth - 20;
        dispatch_async(dispatch_get_main_queue(), ^{
            self.toolBar.frame = CGRectMake(10, self.view.frame.size.height - self.toolBar.itemHeight - 5-(iPhoneX?10:0), width, self.toolBar.itemHeight);
            [self.toolBar layoutTheViews];
        });
    }
    
    [self addBadge];
}

- (NSMutableArray *)getItemsArray {
    
    YPChatRoomMember *mineMember = GetCore(YPRoomQueueCoreV2Help).myMember;
        NSMutableArray *items = [NSMutableArray array];
    if (!self.isFirstAddToolbar) {
       
           //------life->
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMessage]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroVolumeSwitch]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroSwitch]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonChat]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonFace]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonGift]];
             for (YPToolBarButton *btn in items) {
                 if (btn.type ==HJToolBarButtonGift ) {
                      btn.hidden = NO;
                 }else{
                      btn.hidden = YES;
                 }
                
             }
        self.isFirstAddToolbar = YES;
           //------<
        
    }else{
        [items addObjectsFromArray:self.toolBar.items];
    }
    
    if (mineMember) {
        if ([GetCore(YPRoomQueueCoreV2Help) isOnMicro:[GetCore(YPAuthCoreHelp)getUid].userIDValue]) { //自己是否在麦上
            self.musicBtn.hidden = YES;
            for (YPToolBarButton *btn in self.toolBar.items) {
                btn.hidden = NO;
            }
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMessage]];
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroVolumeSwitch]];
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroSwitch]];
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonChat]];
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonFace]];
        } else {
            [self.recordingImg setImage:nil];
            self.musicBtn.hidden = YES;

            for (YPToolBarButton *btn in self.toolBar.items) {
                if (btn.type == HJToolBarButtonMessage || btn.type == HJToolBarButtonMicroVolumeSwitch ||btn.type == HJToolBarButtonChat) {
                     btn.hidden = NO;
                }else{
                     btn.hidden = YES;
                }
               
            }
            
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMessage]];
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroVolumeSwitch]];
//            [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonChat]];
            
        }
    }else {
        [self.recordingImg setImage:nil];
        self.musicBtn.hidden = YES;
        for (YPToolBarButton *btn in self.toolBar.items) {
                      if (btn.type == HJToolBarButtonMessage || btn.type == HJToolBarButtonMicroVolumeSwitch ||btn.type == HJToolBarButtonChat) {
                           btn.hidden = NO;
                      }else{
                           btn.hidden = YES;
                      }
                     
                  }
//        [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMessage]];
//        [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroVolumeSwitch]];
//        [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonChat]];
    }
    
//    [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonGift]];
    for (YPToolBarButton *btn in items) {//礼物一直都是显示
        if (btn.type ==HJToolBarButtonGift ) {
            btn.hidden = NO;
        }
    }
    //设置状态、图片
    for (YPToolBarButton *btn in items) {
        [self setBtnStyleWithtype:btn.type btn:btn];
    }
    
    return items;
}

-(void)setBtnStyleWithtype:(HJToolBarButtonType)type btn:(YPToolBarButton*)btn{
    if (type == HJToolBarButtonMicroSwitch) {
            if (GetCore(YPMeetingCore).actor) {
                btn.enabled = YES;
                if (GetCore(YPMeetingCore).isCloseMicro) {
                    [self.recordingImg setImage:nil];
                    [btn setImage:[UIImage imageNamed:@"yp_room_icon_jinmai"] forState:UIControlStateNormal];
                } else {
                    self.recordingImg.image = [UIImage animatedGIFNamed:@"yp_recording"];
                    [btn setImage:[UIImage imageNamed:@"yp_room_icon_maikefen"] forState:UIControlStateNormal];
                }
            } else {
                [self.recordingImg setImage:nil];
                btn.enabled = NO;
                [btn setImage:[UIImage imageNamed:@"yp_room_icon_maikefen"] forState:UIControlStateNormal];
                [btn setImage:[UIImage imageNamed:@"yp_room_icon_jinmai"] forState:UIControlStateDisabled];
    //            [btn setImage:nil forState:UIControlStateNormal];
            }
            
        } else if (type == HJToolBarButtonMicroQue) {
            btn.disableIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
            btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_zuoweii"];
            btn.selectedIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
        } else if (type == HJToolBarButtonMicroVolumeSwitch) {
            if (GetCore(YPMeetingCore).isMute) {
                btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_shenyinmeishen"];
                btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_shenyinmeishen"];
                btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_shenyinmeishen"];
            }else {
                btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_shenyin"];
                btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_shenyin"];
                btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_shenyin"];
            }
        }else if (type == HJToolBarButtonChat) {
            if (GetCore(YPImRoomCoreV2).currentRoomInfo.publicChatSwitch) {
                btn.enabled = NO;
                btn.disableIcon = [UIImage imageNamed:@"room_chat_icon_close"];
                btn.normalIcon = [UIImage imageNamed:@"room_chat_icon_close"];
                btn.selectedIcon = [UIImage imageNamed:@"room_chat_icon_close"];
            }
            else {
                btn.enabled = YES;
                btn.disableIcon = [UIImage imageNamed:@"room_chat_icon"];
                btn.normalIcon = [UIImage imageNamed:@"room_chat_icon"];
                btn.selectedIcon = [UIImage imageNamed:@"room_chat_icon"];
            }
        }else if (type == HJToolBarButtonFace) {
            btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_biaoqin"];
            btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_biaoqin"];
            btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_biaoqin"];
        }else if (type == HJToolBarButtonShare) {
            btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_zhuanfaa"];
            btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_zhuanfaa"];
            btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_zhuanfaa"];
        }
        else if (type == HJToolBarButtonMessage) {
            btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_tongzhi"];
            btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_tongzhi"];
            btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_tongzhi"];
        }else if (type == HJToolBarButtonGift) {//礼物
            btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_liwu"];
            btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_liwu"];
            btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_liwu"];
        }
}


- (YPToolBarButton *) getHJToolBarButtonBy:(HJToolBarButtonType)type {
    YPToolBarButton *btn = [[YPToolBarButton alloc]init];
    btn.type = type;
    if (type == HJToolBarButtonMicroSwitch) {
        if (GetCore(YPMeetingCore).actor) {
            btn.enabled = YES;
            if (GetCore(YPMeetingCore).isCloseMicro) {
                [self.recordingImg setImage:nil];
                [btn setImage:[UIImage imageNamed:@"yp_room_icon_jinmai"] forState:UIControlStateNormal];
            } else {
                self.recordingImg.image = [UIImage animatedGIFNamed:@"yp_recording"];
                [btn setImage:[UIImage imageNamed:@"yp_room_icon_maikefen"] forState:UIControlStateNormal];
            }
        } else {
            [self.recordingImg setImage:nil];
            btn.enabled = NO;
            [btn setImage:[UIImage imageNamed:@"yp_room_icon_maikefen"] forState:UIControlStateNormal];
            [btn setImage:[UIImage imageNamed:@"yp_room_icon_jinmai"] forState:UIControlStateDisabled];
//            [btn setImage:nil forState:UIControlStateNormal];
        }
        
    } else if (type == HJToolBarButtonMicroQue) {
        btn.disableIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
        btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_zuoweii"];
        btn.selectedIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
    } else if (type == HJToolBarButtonMicroVolumeSwitch) {
        if (GetCore(YPMeetingCore).isMute) {
            btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_shenyinmeishen"];
            btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_shenyinmeishen"];
            btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_shenyinmeishen"];
        }else {
            btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_shenyin"];
            btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_shenyin"];
            btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_shenyin"];
        }
    }else if (type == HJToolBarButtonChat) {
        if (GetCore(YPImRoomCoreV2).currentRoomInfo.publicChatSwitch) {
            btn.enabled = NO;
            btn.disableIcon = [UIImage imageNamed:@"room_chat_icon_close"];
            btn.normalIcon = [UIImage imageNamed:@"room_chat_icon_close"];
            btn.selectedIcon = [UIImage imageNamed:@"room_chat_icon_close"];
        }
        else {
            btn.enabled = YES;
            btn.disableIcon = [UIImage imageNamed:@"room_chat_icon"];
            btn.normalIcon = [UIImage imageNamed:@"room_chat_icon"];
            btn.selectedIcon = [UIImage imageNamed:@"room_chat_icon"];
        }
    }else if (type == HJToolBarButtonFace) {
        btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_biaoqin"];
        btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_biaoqin"];
        btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_biaoqin"];
    }else if (type == HJToolBarButtonShare) {
        btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_zhuanfaa"];
        btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_zhuanfaa"];
        btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_zhuanfaa"];
    }
    else if (type == HJToolBarButtonMessage) {
        btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_tongzhi"];
        btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_tongzhi"];
        btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_tongzhi"];
    }else if (type == HJToolBarButtonGift) {//礼物
        btn.disableIcon = [UIImage imageNamed:@"yp_room_icon_liwu"];
        btn.normalIcon = [UIImage imageNamed:@"yp_room_icon_liwu"];
        btn.selectedIcon = [UIImage imageNamed:@"yp_room_icon_liwu"];
    }
    return btn;
}



- (void)toolBar:(YPToolBarButton *)toolBar didSelectItem:(NSInteger)index {
    YPChatRoomMember *mineMember = GetCore(YPRoomQueueCoreV2Help).myMember;
    
    if (toolBar.type == HJToolBarButtonMicroSwitch) {
        
        if ([GetCore(YPRoomQueueCoreV2Help) isOnMicro:[GetCore(YPAuthCoreHelp)getUid].userIDValue]) {
            //在麦上的话，是闭麦按钮
            if([GetCore(YPMeetingCore) setCloseMicro:!GetCore(YPMeetingCore).isCloseMicro]) {
                [self initMicroVoiceViewWithMicoBtn:toolBar];
            }
        } else {//不在麦上是排麦
            if (self.micInListOption) {
                [self showAlertMicroQueue];
            } else {
                if ([GetCore(YPMeetingCore) setMute:!GetCore(YPMeetingCore).isMute]) {
                    [self initVoiceViewWithVoiceBtn:toolBar];
                }
            }
        }
        return;

    } else if (toolBar.type == HJToolBarButtonMicroQue) {
        if (self.micInListOption) {
            [self showAlertMicroQueue];
        } else {
            if ([GetCore(YPMeetingCore) setMute:!GetCore(YPMeetingCore).isMute]) {
                [self initVoiceViewWithVoiceBtn:toolBar];
            }
        }
        return;
        
    } else if (toolBar.type == HJToolBarButtonMicroVolumeSwitch) {
        
        if ([GetCore(YPMeetingCore) setMute:!GetCore(YPMeetingCore).isMute]) {
            [self initVoiceViewWithVoiceBtn:toolBar];
        }
        return;
        
    } else if (toolBar.type == HJToolBarButtonChat) {
        
        //第一个是聊天按钮 无论何种情况
        [self.editText becomeFirstResponder];
        [self.view bringSubviewToFront:self.editView];
        self.editView.hidden = NO;
        return;
        
    } else if (toolBar.type == HJToolBarButtonFace) {
        
        [self faceButtonClick];
        return;
        
    } else if (toolBar.type == HJToolBarButtonShare) {
        
        [self showSharePanelView];
        return;
        
    } else if (toolBar.type == HJToolBarButtonMessage) {
        
        [self showMessageView];
        return;
    }else if (toolBar.type == HJToolBarButtonGift) {
        [self.view bringSubviewToFront:self.giftContainer];
        [self showGiftContainerView];
        return;
    }
    
}


- (void)updateMicAndSpeakerStauts
{
    for (YPToolBarButton *sender in self.toolBar.items) {
        if (sender.type == HJToolBarButtonMicroVolumeSwitch) {
            [self initVoiceViewWithVoiceBtn:sender];
        }
        
        if (sender.type == HJToolBarButtonMicroSwitch) {
            [self initMicroVoiceViewWithMicoBtn:sender];
        }
    }
}


//设置声音 按钮
- (void)initVoiceViewWithVoiceBtn:(YPToolBarButton *)sender {
    if (GetCore(YPMeetingCore).isMute) {
        [sender setImage:[UIImage imageNamed:@"yp_room_icon_shenyinmeishen"] forState:UIControlStateNormal];
    }else {
        [sender setImage:[UIImage imageNamed:@"yp_room_icon_shenyin"] forState:UIControlStateNormal];
    }
    
}

- (void)initMicroVoiceViewWithMicoBtn:(YPToolBarButton *)sender {
    if (GetCore(YPMeetingCore).actor) {
        sender.enabled = YES;
        if (GetCore(YPMeetingCore).isCloseMicro) {
            [self.recordingImg setImage:nil];
            [sender setImage:[UIImage imageNamed:@"yp_room_icon_jinmai"] forState:UIControlStateNormal];
        } else {
            self.recordingImg.image = [UIImage animatedGIFNamed:@"yp_recording"];
            [sender setImage:[UIImage imageNamed:@"yp_room_icon_maikefen"] forState:UIControlStateNormal];
        }
    } else {
        [self.recordingImg setImage:nil];
        sender.enabled = NO;
        [sender setImage:[UIImage imageNamed:@"yp_room_icon_jinmai"] forState:UIControlStateNormal];
//        [sender setImage:nil forState:UIControlStateNormal];
    }
    
}

- (void)faceButtonClick {
    NSString *position = [GetCore(YPRoomQueueCoreV2Help)findThePositionByUid:[GetCore(YPAuthCoreHelp)getUid].userIDValue];
    if (position.length > 0 && position != nil) {
        if (self.faceView == nil) {
            self.faceView  = [YPGameRoomFaceView loadFromNib];
        }
        
        self.faceAlert = [TYAlertController alertControllerWithAlertView:self.faceView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleActionSheet transitionAnimation:(TYAlertTransitionAnimation)TYAlertTransitionAnimationFade];
        self.faceAlert.backgoundTapDismissEnable = YES;
        [self presentViewController:self.faceAlert animated:YES completion:^{
            
        }];
    }else if (self.roomInfo.uid ==  [GetCore(YPAuthCoreHelp) getUid].userIDValue) {
        if (self.faceView == nil) {
            self.faceView  = [YPGameRoomFaceView loadFromNib];
        }
        
        self.faceAlert = [TYAlertController alertControllerWithAlertView:self.faceView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleActionSheet transitionAnimation:(TYAlertTransitionAnimation)TYAlertTransitionAnimationFade];
        self.faceAlert.backgoundTapDismissEnable = YES;
        [self presentViewController:self.faceAlert animated:YES completion:^{
            
        }];
    }
    else {
        [UIView showToastInKeyWindow:NSLocalizedString(XCRoomMustOnMicCanSendFace, nil) duration:2 position:YYToastPositionCenter];
    }
}

#pragma mark - ImMessageCoreClient
- (void)onRecvAnMsg:(NIMMessage *)msg {
    [self addBadge];
}

- (void)addBadge {
    [self.navigationController.tabBarItem setBadgeValue:nil];
    YPToolBarButton *btn = nil;
    for (YPToolBarButton *item in self.toolBar.items) {
        if (item.type == HJToolBarButtonMessage) {
            btn = item;
            break;
        }
    }
    if ([GetCore(YPImMessageCore) getUnreadCount] > 0) {
        [self.navigationController.tabBarItem setBadgeValue:[NSString stringWithFormat:@"%ld",[GetCore(YPImMessageCore)getUnreadCount]]];
        if (btn) {
            btn.isShowRed = YES;
        }
    }else {
        [self.navigationController.tabBarItem setBadgeValue:nil];
        if (btn) {
            btn.isShowRed = NO;
        }
    }
}

@end

