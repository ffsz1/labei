//
//  HJGameRoomVC+PK.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGameRoomVC+PK.h"

#import "HJHttpRequestHelper+PK.h"

#import "HJRoomPKView.h"


@implementation HJGameRoomVC (PK)

- (void)checkPKState:(BOOL)isFromMin
{
    
    if (self.hasCheckPK) {
        return;
    }
    
    
    self.hasCheckPK = YES;
    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper pk_getState:^(BOOL open) {
        
        if (open) {
            [weakSelf setPKView];
            
            if (!isFromMin) {
                [weakSelf getLastPKData];
            }
        }
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        
    }];
}

- (void)getLastPKData
{
    [HJHttpRequestHelper pk_unSureRecord:^(NSArray * _Nonnull arr) {
        
        NSMutableArray *imMsgArr = [[NSMutableArray alloc] init];
        for (NSDictionary *dict in arr) {
            
            HJIMMessage *message = [HJIMMessage new];
            message.messageType = HJIMMessageTypeCustom;
            message.timestamp = [NSDate date].timeIntervalSince1970;
            
            
            JXIMSession *session = [JXIMSession new];
//            session.sessionId = roomId;
            session.sessionType = JXIMSessionTypeChatroom;
            message.session = session;
            
            Attachment *attachment = [Attachment yy_modelWithJSON:dict];
            attachment.first = Custom_Noti_Header_Mora;
            attachment.second = Custom_Noti_Sub_Mora_send;
            JXIMCustomObject *messageObject = [JXIMCustomObject new];
            messageObject.attachment = attachment;
            message.messageObject = messageObject;
            [imMsgArr addObject:message];
        }
        
        
        [GetCore(HJRoomCoreV2Help).messages addObjectsFromArray:imMsgArr];
        
        [self.messageTableView.messages addObjectsFromArray:imMsgArr];
        [self.messageTableView reloadData];
        
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        
    }];
}

- (void)setPKView
{
    [self.view addSubview:self.pkBtn];
    
    [self.pkBtn addTarget:self action:@selector(showPKView) forControlEvents:UIControlEventTouchUpInside];
    
    [self.pkBtn mas_remakeConstraints:^(MASConstraintMaker *make) {
        
        make.size.mas_equalTo(CGSizeMake(47, 50));
        make.right.mas_equalTo(self.view).mas_equalTo(-15);
        make.bottom.mas_equalTo(self.view).mas_equalTo(-200);
    }];
}

- (void)showPKView
{
    self.pkBtn.userInteractionEnabled = NO;
    
    [HJRoomPKView show];
    
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        self.pkBtn.userInteractionEnabled = YES;
    });
}

@end
