//
//  HJRoomSettingTopicVC.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJRoomSettingTopicVC.h"
#import <IQTextView.h>
#import "HJImRoomCoreV2.h"
#import "HJRoomCoreV2Help.h"
#import "HJRoomCoreClient.h"
#import "HJImMessageCore.h"
#import "HJImRoomCoreV2.h"
@interface HJRoomSettingTopicVC ()<
    HJRoomCoreClient
>
@property (weak, nonatomic) IBOutlet IQTextView *topicTextField;
@property (weak, nonatomic) IBOutlet IQTextView *contentTextView;
@end

@implementation HJRoomSettingTopicVC

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = NSLocalizedString(XCRoomSetTopic, nil);
    UIBarButtonItem *bar = [[UIBarButtonItem alloc] initWithTitle:NSLocalizedString(XCAlertDone, nil) style:UIBarButtonItemStylePlain target:self action:@selector(doneClick)];
    self.navigationItem.rightBarButtonItem = bar;

    if (GetCore(HJImRoomCoreV2).currentRoomInfo.roomDesc.length > 0) {
        self.topicTextField.text = GetCore(HJImRoomCoreV2).currentRoomInfo.roomDesc;
    }
    
    if (GetCore(HJImRoomCoreV2).currentRoomInfo.roomNotice.length > 0) {
        self.contentTextView.text = GetCore(HJImRoomCoreV2).currentRoomInfo.roomNotice;
    }


    self.topicTextField.placeholder = NSLocalizedString(XCRoomFillTopic, nil);
    self.contentTextView.placeholder = NSLocalizedString(XCRoomFillContent, nil);
    
    AddCoreClient(HJRoomCoreClient, self);
    
    @weakify(self);
    [self.topicTextField.rac_textSignal subscribeNext:^(NSString *x) {
        @strongify(self);
        if (x.length > 12) {
            self.topicTextField.text = [self.topicTextField.text substringToIndex:12];
        }
    }];
    
    [self.contentTextView.rac_textSignal subscribeNext:^(NSString *x) {
        @strongify(self);
        if (x.length > 300) {
            self.contentTextView.text = [self.contentTextView.text substringToIndex:300];
        }
    }];
}

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [self.navigationController setNavigationBarHidden:NO animated:YES];
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)doneClick {
        
    if (GetCore(HJImRoomCoreV2).myMember.is_creator) {
        [GetCore(HJRoomCoreV2Help) updateGameRoomInfo:GetCore(HJImRoomCoreV2).currentRoomInfo.uid backPic:nil title:nil roomTopic:self.topicTextField.text roomNotice:self.contentTextView.text roomPassword:nil tag:GetCore(HJImRoomCoreV2).currentRoomInfo.tagId playInfo:GetCore(HJImRoomCoreV2).currentRoomInfo.playInfo giftEffectSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch giftCardSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch publicChatSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch];
    } else if (GetCore(HJImRoomCoreV2).myMember.is_manager) {
        [GetCore(HJRoomCoreV2Help) managerUpdateGameRoomInfo:[GetCore(HJAuthCoreHelp)getUid].userIDValue backPic:nil title:nil roomTopic:self.topicTextField.text roomNotice:self.contentTextView.text roomPassword:nil tag:GetCore(HJImRoomCoreV2).currentRoomInfo.tagId playInfo:GetCore(HJImRoomCoreV2).currentRoomInfo.playInfo giftEffectSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch giftCardSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.giftEffectSwitch publicChatSwitch:GetCore(HJImRoomCoreV2).currentRoomInfo.publicChatSwitch];
    }
    
    
}

- (void)onGameRoomInfoUpdateSuccessV2:(ChatRoomInfo *)info {
    //玩法消息发送
    [self sendWanfaMessageForChangeInfo:self.topicTextField.text content:self.contentTextView.text];
    [self.navigationController popViewControllerAnimated:YES];
}

- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}


- (void)sendWanfaMessageForChangeInfo:(NSString *)title content:(NSString *)content{
    
    
    Attachment *attachement = [[Attachment alloc]init];
    attachement.first = Custom_Noti_Header_Wanfa;
    attachement.second = Custom_Noti_Header_Wanfa;

   
    NSMutableDictionary *buffer = [NSMutableDictionary dictionary];
    [buffer setObject:title forKey:@"roomDesc"];
    [buffer setObject:content forKey:@"roomNotice"];
     [buffer setObject:[NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] forKey:@"roomId"];
    NSDictionary *attMessageDic = @{@"params": buffer};
    attachement.data = attMessageDic;
    
    [GetCore(HJImMessageCore) sendCustomMessageAttachement:attachement sessionId:[NSString stringWithFormat:@"%lld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId] type:(JXIMSessionType) NIMSessionTypeChatroom];
}

@end
