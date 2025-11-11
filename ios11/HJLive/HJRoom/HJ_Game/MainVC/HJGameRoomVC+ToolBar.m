//
//  HJGameRoomVC+ToolBar.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+ToolBar.h"
#import "HJGameRoomVC+Alert.h"

#import "UIImage+Gif.h"
#import "HJImMessageCore.h"
#import "HJImMessageCoreClient.h"


@implementation HJGameRoomVC (ToolBar)


- (void)updateToolBar {
//    [self.toolBar removeFromSuperview];
//    self.toolBar = nil;
    BOOL needAddSubview = YES;
    for (UIView *view in self.view.subviews) {
        if ([view isKindOfClass:[HJFinishLiveView class]]) {
            needAddSubview = NO;
        }
    }
    if (needAddSubview) {
//        self.toolBar = [[HJToolBar alloc]init];
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
    
    ChatRoomMember *mineMember = GetCore(HJRoomQueueCoreV2Help).myMember;
        NSMutableArray *items = [NSMutableArray array];
    if (!self.isFirstAddToolbar) {
       
           //------life->
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMessage]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroVolumeSwitch]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonMicroSwitch]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonChat]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonFace]];
             [items addObject:[self getHJToolBarButtonBy:HJToolBarButtonGift]];
             for (HJToolBarButton *btn in items) {
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
        if ([GetCore(HJRoomQueueCoreV2Help) isOnMicro:[GetCore(HJAuthCoreHelp)getUid].userIDValue]) { //自己是否在麦上
            self.musicBtn.hidden = NO;
            self.isIsOnMic = YES;
            for (HJToolBarButton *btn in self.toolBar.items) {
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
            self.isIsOnMic = NO;
            for (HJToolBarButton *btn in self.toolBar.items) {
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
        for (HJToolBarButton *btn in self.toolBar.items) {
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
    for (HJToolBarButton *btn in items) {//礼物一直都是显示
        if (btn.type ==HJToolBarButtonGift ) {
            btn.hidden = NO;
        }
    }
    //设置状态、图片
    for (HJToolBarButton *btn in items) {
        [self setBtnStyleWithtype:btn.type btn:btn];
    }
    self.musicBtn.hidden = YES;
    return items;
}

-(void)setBtnStyleWithtype:(HJToolBarButtonType)type btn:(HJToolBarButton*)btn{
    if (type == HJToolBarButtonMicroSwitch) {
            if (GetCore(HJMeetingCore).actor) {
                btn.enabled = YES;
                if (GetCore(HJMeetingCore).isCloseMicro) {
                    [self.recordingImg setImage:nil];
                    [btn setImage:[UIImage imageNamed:@"hj_room_icon_jinmai"] forState:UIControlStateNormal];
                } else {
                    self.recordingImg.image = [UIImage animatedGIFNamed:@"hj_recording"];
                    [btn setImage:[UIImage imageNamed:@"hj_room_icon_maikefen"] forState:UIControlStateNormal];
                }
            } else {
                [self.recordingImg setImage:nil];
                btn.enabled = NO;
                [btn setImage:[UIImage imageNamed:@"hj_room_icon_maikefen"] forState:UIControlStateNormal];
                [btn setImage:[UIImage imageNamed:@"hj_room_icon_jinmai"] forState:UIControlStateDisabled];
    //            [btn setImage:nil forState:UIControlStateNormal];
            }
            
        } else if (type == HJToolBarButtonMicroQue) {
            btn.disableIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
            btn.normalIcon = [UIImage imageNamed:@"xc_room_icon_zuowei"];
            btn.selectedIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
        } else if (type == HJToolBarButtonMicroVolumeSwitch) {
            if (GetCore(HJMeetingCore).isMute) {
                btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_shenyinmeishen"];
                btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_shenyinmeishen"];
                btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_shenyinmeishen"];
            }else {
                btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_shenyin"];
                btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_shenyin"];
                btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_shenyin"];
            }
        }else if (type == HJToolBarButtonChat) {
            if (GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch) {
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
            btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_biaoqin"];
            btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_biaoqin"];
            btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_biaoqin"];
        }else if (type == HJToolBarButtonShare) {
            btn.disableIcon = [UIImage imageNamed:@"xc_room_icon_zhuanfa"];
            btn.normalIcon = [UIImage imageNamed:@"xc_room_icon_zhuanfa"];
            btn.selectedIcon = [UIImage imageNamed:@"xc_room_icon_zhuanfa"];
        }
        else if (type == HJToolBarButtonMessage) {
            btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_tongzhi"];
            btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_tongzhi"];
            btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_tongzhi"];
        }else if (type == HJToolBarButtonGift) {//礼物
            btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_liwu"];
            btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_liwu"];
            btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_liwu"];
        }
}


- (HJToolBarButton *) getHJToolBarButtonBy:(HJToolBarButtonType)type {
    HJToolBarButton *btn = [[HJToolBarButton alloc]init];
    btn.type = type;
    if (type == HJToolBarButtonMicroSwitch) {
        if (GetCore(HJMeetingCore).actor) {
            btn.enabled = YES;
            if (GetCore(HJMeetingCore).isCloseMicro) {
                [self.recordingImg setImage:nil];
                [btn setImage:[UIImage imageNamed:@"hj_room_icon_jinmai"] forState:UIControlStateNormal];
            } else {
                self.recordingImg.image = [UIImage animatedGIFNamed:@"hj_recording"];
                [btn setImage:[UIImage imageNamed:@"hj_room_icon_maikefen"] forState:UIControlStateNormal];
            }
        } else {
            [self.recordingImg setImage:nil];
            btn.enabled = NO;
            [btn setImage:[UIImage imageNamed:@"hj_room_icon_maikefen"] forState:UIControlStateNormal];
            [btn setImage:[UIImage imageNamed:@"hj_room_icon_jinmai"] forState:UIControlStateDisabled];
//            [btn setImage:nil forState:UIControlStateNormal];
        }
        
    } else if (type == HJToolBarButtonMicroQue) {
        btn.disableIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
        btn.normalIcon = [UIImage imageNamed:@"xc_room_icon_zuowei"];
        btn.selectedIcon = [UIImage imageNamed:@"xc_room_icon_paimai"];
    } else if (type == HJToolBarButtonMicroVolumeSwitch) {
        if (GetCore(HJMeetingCore).isMute) {
            btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_shenyinmeishen"];
            btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_shenyinmeishen"];
            btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_shenyinmeishen"];
        }else {
            btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_shenyin"];
            btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_shenyin"];
            btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_shenyin"];
        }
    }else if (type == HJToolBarButtonChat) {
        if (GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch) {
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
        btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_biaoqin"];
        btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_biaoqin"];
        btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_biaoqin"];
    }else if (type == HJToolBarButtonShare) {
        btn.disableIcon = [UIImage imageNamed:@"xc_room_icon_zhuanfa"];
        btn.normalIcon = [UIImage imageNamed:@"xc_room_icon_zhuanfa"];
        btn.selectedIcon = [UIImage imageNamed:@"xc_room_icon_zhuanfa"];
    }
    else if (type == HJToolBarButtonMessage) {
        btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_tongzhi"];
        btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_tongzhi"];
        btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_tongzhi"];
    }else if (type == HJToolBarButtonGift) {//礼物
        btn.disableIcon = [UIImage imageNamed:@"hj_room_icon_liwu"];
        btn.normalIcon = [UIImage imageNamed:@"hj_room_icon_liwu"];
        btn.selectedIcon = [UIImage imageNamed:@"hj_room_icon_liwu"];
    }
    return btn;
}



- (void)toolBar:(HJToolBarButton *)toolBar didSelectItem:(NSInteger)index {
//    ChatRoomMember *mineMember = GetCore(HJRoomQueueCoreV2Help).myMember;
    
    if (toolBar.type == HJToolBarButtonMicroSwitch) {
        
        if ([GetCore(HJRoomQueueCoreV2Help) isOnMicro:[GetCore(HJAuthCoreHelp)getUid].userIDValue]) {
            //在麦上的话，是闭麦按钮
            if([GetCore(HJMeetingCore) setCloseMicro:!GetCore(HJMeetingCore).isCloseMicro]) {
                [self initMicroVoiceViewWithMicoBtn:toolBar];
            }
        } else {//不在麦上是排麦
            if (self.micInListOption) {
                [self showAlertMicroQueue];
            } else {
                if ([GetCore(HJMeetingCore) setMute:!GetCore(HJMeetingCore).isMute]) {
                    [self initVoiceViewWithVoiceBtn:toolBar];
                }
            }
        }
        return;

    } else if (toolBar.type == HJToolBarButtonMicroQue) {
        if (self.micInListOption) {
            [self showAlertMicroQueue];
        } else {
            if ([GetCore(HJMeetingCore) setMute:!GetCore(HJMeetingCore).isMute]) {
                [self initVoiceViewWithVoiceBtn:toolBar];
            }
        }
        return;
        
    } else if (toolBar.type == HJToolBarButtonMicroVolumeSwitch) {
        
        if ([GetCore(HJMeetingCore) setMute:!GetCore(HJMeetingCore).isMute]) {
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
    for (HJToolBarButton *sender in self.toolBar.items) {
        if (sender.type == HJToolBarButtonMicroVolumeSwitch) {
            [self initVoiceViewWithVoiceBtn:sender];
        }
        
        if (sender.type == HJToolBarButtonMicroSwitch) {
            [self initMicroVoiceViewWithMicoBtn:sender];
        }
    }
}


//设置声音 按钮
- (void)initVoiceViewWithVoiceBtn:(HJToolBarButton *)sender {
    if (GetCore(HJMeetingCore).isMute) {
        [sender setImage:[UIImage imageNamed:@"hj_room_icon_shenyinmeishen"] forState:UIControlStateNormal];
    }else {
        [sender setImage:[UIImage imageNamed:@"hj_room_icon_shenyin"] forState:UIControlStateNormal];
    }
    
}

- (void)initMicroVoiceViewWithMicoBtn:(HJToolBarButton *)sender {
    if (GetCore(HJMeetingCore).actor) {
        sender.enabled = YES;
        if (GetCore(HJMeetingCore).isCloseMicro) {
            [self.recordingImg setImage:nil];
            [sender setImage:[UIImage imageNamed:@"hj_room_icon_jinmai"] forState:UIControlStateNormal];
        } else {
            self.recordingImg.image = [UIImage animatedGIFNamed:@"hj_recording"];
            [sender setImage:[UIImage imageNamed:@"hj_room_icon_maikefen"] forState:UIControlStateNormal];
        }
    } else {
        [self.recordingImg setImage:nil];
        sender.enabled = NO;
        [sender setImage:[UIImage imageNamed:@"hj_room_icon_jinmai"] forState:UIControlStateNormal];
//        [sender setImage:nil forState:UIControlStateNormal];
    }
    
}

- (void)faceButtonClick {
    NSString *position = [GetCore(HJRoomQueueCoreV2Help)findThePositionByUid:[GetCore(HJAuthCoreHelp)getUid].userIDValue];
    if (position.length > 0 && position != nil) {
        if (self.faceView == nil) {
            self.faceView  = [HJGameRoomFaceView loadFromNib];
        }
        
        self.faceAlert = [TYAlertController alertControllerWithAlertView:self.faceView preferredStyle:(TYAlertControllerStyle)TYAlertControllerStyleActionSheet transitionAnimation:(TYAlertTransitionAnimation)TYAlertTransitionAnimationFade];
        self.faceAlert.backgoundTapDismissEnable = YES;
        [self presentViewController:self.faceAlert animated:YES completion:^{
            
        }];
    }else if (self.roomInfo.uid ==  [GetCore(HJAuthCoreHelp) getUid].userIDValue) {
        if (self.faceView == nil) {
            self.faceView  = [HJGameRoomFaceView loadFromNib];
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
    HJToolBarButton *btn = nil;
    for (HJToolBarButton *item in self.toolBar.items) {
        if (item.type == HJToolBarButtonMessage) {
            btn = item;
            break;
        }
    }
    if ([GetCore(HJImMessageCore) getUnreadCount] > 0) {
        [self.navigationController.tabBarItem setBadgeValue:[NSString stringWithFormat:@"%ld",[GetCore(HJImMessageCore)getUnreadCount]]];
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

