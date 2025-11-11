//
//  HJGiftListView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGiftListView.h"

#import "HJGiftCore.h"
#import "HJGiftCoreClient.h"
#import "HJImRoomCoreV2.h"
#import "Attachment.h"
#import "NIMKitUtil.h"

#import "HJGiftListAlertCell.h"
#import "GiftReceiveInfo.h"
#import "NSObject+YYModel.h"


@interface HJGiftListView ()<
UITableViewDelegate,
UITableViewDataSource,
HJGiftCoreClient
>
@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, strong) NSMutableArray *messages;//消息

@end

@implementation HJGiftListView

- (void)dealloc {
    [self removeCore];
}

- (void)awakeFromNib {
    [super awakeFromNib];
    
    AddCoreClient(HJGiftCoreClient, self);
    
    [self.tableView registerNib:[UINib nibWithNibName:@"HJGiftListAlertCell" bundle:nil] forCellReuseIdentifier:@"HJGiftListAlertCell"];
    
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
- (void)onReceiveGift:(HJGiftAllMicroSendInfo *)giftReceiveInfo isALLChannelSend:(BOOL)isALLChannelSend {
    [self updateMessage];
}

//刷新
- (void)updateMessage {
    NSMutableArray *messages = [NSMutableArray arrayWithArray:GetCore(HJGiftCore).currentGiftMsgArr];
    self.messages = [NSMutableArray array];
    for (int i = (int)messages.count - 1; i >= 0 ; i--) {
        HJIMMessage *msg = [messages safeObjectAtIndex:i];
        NSString *sessionID = [NSString stringWithFormat:@"%ld",GetCore(HJImRoomCoreV2).currentRoomInfo.roomId];
        if (msg.messageType == NIMMessageTypeCustom && [msg.session.sessionId isEqualToString:sessionID]) {
            JXIMCustomObject *obj = msg.messageObject;
            Attachment *attachment = (Attachment *)obj.attachment;
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
    
    HJGiftListAlertCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJGiftListAlertCell"];
    
    HJIMMessage *msg = [self.messages safeObjectAtIndex:indexPath.row];
    JXIMCustomObject *obj = msg.messageObject;
    Attachment *attachment = (Attachment *)obj.attachment;
    GiftReceiveInfo *info = [GiftReceiveInfo yy_modelWithDictionary:attachment.data];
    GiftInfo *giftInfo = [GetCore(HJGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeNormal];
    if (giftInfo == nil) {
        giftInfo = [GetCore(HJGiftCore) findGiftInfoByGiftId:info.giftId giftyType:GiftTypeMystic];
    }
    
    NSString *timeSting = [NIMKitUtil showTime:msg.timestamp showDetail:NO];
    
    cell.isAll = (attachment.first == Custom_Noti_Header_ALLMicroSend);
    cell.model = info;
    cell.giftInfo = giftInfo;
    cell.timeString = timeSting;
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath {
    
    HJIMMessage *msg = [self.messages safeObjectAtIndex:indexPath.row];
    JXIMCustomObject *obj = msg.messageObject;
    Attachment *attachment = (Attachment *)obj.attachment;
    GiftReceiveInfo *info = [GiftReceiveInfo yy_modelWithDictionary:attachment.data];
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
