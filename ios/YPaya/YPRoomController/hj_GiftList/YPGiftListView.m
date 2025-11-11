//
//  YPGiftListView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPGiftListView.h"

#import "YPGiftCore.h"
#import "HJGiftCoreClient.h"
#import "YPImRoomCoreV2.h"
#import "YPAttachment.h"
#import "YPNIMKitUtil.h"

#import "YPGiftListAlertCell.h"
#import "YPGiftReceiveInfo.h"
#import "NSObject+YYModel.h"


@interface YPGiftListView ()<
UITableViewDelegate,
UITableViewDataSource,
HJGiftCoreClient
>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, strong) NSMutableArray *messages;//消息

@end

@implementation YPGiftListView

- (void)dealloc {
    [self removeCore];
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    AddCoreClient(HJGiftCoreClient, self);
    
    [self.tableView registerNib:[UINib nibWithNibName:@"YPGiftListAlertCell" bundle:nil] forCellReuseIdentifier:@"YPGiftListAlertCell"];
    
//    self.layer.cornerRadius = 8;
//    self.layer.masksToBounds = YES;
//    self.backgroundColor = [UIColor whiteColor];
//    self.tableView.backgroundColor = [UIColor whiteColor];
    
    self.tableView.delegate = self;
    self.tableView.dataSource = self;
    self.tableView.separatorStyle = UITableViewCellSelectionStyleNone;
    self.tableView.separatorColor = UIColorHex(F7F7F7);
    self.tableView.separatorInset = UIEdgeInsetsMake(0, 15, 0, 15);
    
    [self updateMessage];
}

#pragma mark - RoomCoreClient
//房间信息改变
- (void)onReceiveGift:(YPGiftAllMicroSendInfo *)giftReceiveInfo isALLChannelSend:(BOOL)isALLChannelSend {
    [self updateMessage];
}

//刷新
- (void)updateMessage {
    NSMutableArray *messages = [NSMutableArray arrayWithArray:GetCore(YPGiftCore).currentGiftMsgArr];
    self.messages = [NSMutableArray array];
    for (int i = (int)messages.count - 1; i >= 0 ; i--) {
        YPIMMessage *msg = [messages safeObjectAtIndex:i];
        NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(YPImRoomCoreV2).currentRoomInfo.roomId];
        if (msg.messageType == NIMMessageTypeCustom && [msg.session.sessionId isEqualToString:sessionID]) {
            JXIMCustomObject *obj = msg.messageObject;
            YPAttachment *attachment = (YPAttachment *)obj.attachment;
            if (attachment.first == Custom_Noti_Header_Gift || attachment.first == Custom_Noti_Header_ALLMicroSend) {
                // 礼物
                [self.messages addObject:msg];
            }
        }
    }
    [self.tableView reloadData];
    
}

#pragma mark - UITableViewDelegate && UITableViewDataSource
- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section {
    
    return self.messages.count;
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath {
    
//    return 77.f;
    return 100.f;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath {
    
    YPGiftListAlertCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPGiftListAlertCell"];
    
    YPIMMessage *msg = [self.messages safeObjectAtIndex:indexPath.row];
    JXIMCustomObject *obj = msg.messageObject;
    YPAttachment *attachment = (YPAttachment *)obj.attachment;
    YPGiftReceiveInfo *info = [YPGiftReceiveInfo yy_modelWithDictionary:attachment.data];
    YPGiftInfo *giftInfo = [GetCore(YPGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeNormal];
    if (giftInfo == nil) {
        giftInfo = [GetCore(YPGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeMystic];
    }
    
    NSString *timeSting = [YPNIMKitUtil showTime:msg.timestamp showDetail:NO];
    
    cell.isAll = (attachment.first == Custom_Noti_Header_ALLMicroSend);
    cell.model = info;
    cell.giftInfo = giftInfo;
    cell.timeString = timeSting;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    YPIMMessage *msg = [self.messages safeObjectAtIndex:indexPath.row];
    JXIMCustomObject *obj = msg.messageObject;
    YPAttachment *attachment = (YPAttachment *)obj.attachment;
    YPGiftReceiveInfo *info = [YPGiftReceiveInfo yy_modelWithDictionary:attachment.data];
    if (self.alertUserInformation) {
        self.alertUserInformation(info.uid);
    }
}

- (void)removeCore {
    RemoveCoreClientAll(self);
}

- (IBAction)closeAction:(id)sender {
    
    if (self.didClickCloseBtnBlock) {
        self.didClickCloseBtnBlock();
    }
}


@end
