//
//  HJRoomSquareMsgVC.m
//  HJLive
//
//  Created by apple on 2019/4/17.
//

#import "HJRoomSquareMsgVC.h"

#import "HJRoomSquareCell.h"

#import "HJNotiFriendCore.h"
#import "HJNotiFriendCoreClient.h"
#import "UIView+XCToast.h"
#import "HJUserCoreHelp.h"

#import "NIMKitUtil.h"

#import "HJNotiFriendVC.h"

#import "HJRoomViewControllerCenter.h"

#import "NSString+GGImage.h"

@interface HJRoomSquareMsgVC ()<UITableViewDelegate,UITableViewDataSource>

@property (weak, nonatomic) IBOutlet UITableView *tableView;

@property (nonatomic, assign) NSInteger enterRoomType;

@property (nonatomic, strong) UserInfo *info;

@property (nonatomic, strong) NSMutableArray <HJIMMessage *> *chatMessage;


@end

@implementation HJRoomSquareMsgVC


- (void)viewDidLoad {
    [super viewDidLoad];
    
    self.chatMessage = [[NSMutableArray alloc] init];
    
    AddCoreClient(HJNotiFriendCoreClient, self);
    
    [self updateData];
    
}

- (void)dealloc {
    [GetCore(HJNotiFriendCore) exitNotiFriendRoom];
    RemoveCoreClientAll(self);
}

#pragma mark - Private methods
- (void)initCore {
    if (self.enterRoomType != 1 && self.enterRoomType != 2) {
        self.enterRoomType = 1;
        [self.tableView hideToastView];
        self.tableView.hidden = NO;
        [GetCore(HJNotiFriendCore) enterNotiFriendRoom];
    }
}

- (void)updateData {
    NSString *userId = [GetCore(HJAuthCoreHelp) getUid];
    if (self.info.uid != userId.integerValue) {
        self.enterRoomType = 0;
    }
    self.info = [GetCore(HJUserCoreHelp) getUserInfoInDB:[userId intValue]];
    [self initCore];
}


#pragma mark - <NotiFriendCoreClient>
- (void)enterNotiFriendSuccess {
    self.enterRoomType = 2;
}

- (void)enterNotiFriendFail {
    if (self.chatMessage.count) {
        [self.chatMessage removeAllObjects];
        [self.tableView reloadData];
    }
    self.enterRoomType = 3;
    NSString *msg = NSLocalizedString(XCDiscoverEnterRoomFailedTip, nil);
    self.tableView.hidden = YES;
    @weakify(self);
    [self.view showEmptyContentToastWithAttributeString:[[NSAttributedString alloc] initWithString:msg] tapBlock:^{
        @strongify(self);
        [self initCore];
    }];
}

- (void)onPublicMessagesDidUpdate:(NSArray<HJIMMessage *> *)messages {
    if (self.enterRoomType != 2) return;
    
    [self.chatMessage removeAllObjects];
    [self.chatMessage addObjectsFromArray:messages];
    [self.tableView reloadData];
}

- (CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath
{
    return 88;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section
{
    return self.chatMessage.count;
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath
{
    HJRoomSquareCell *cell = [tableView dequeueReusableCellWithIdentifier:@"HJRoomSquareCell" forIndexPath:indexPath];
    cell.selectionStyle = UITableViewCellSelectionStyleNone;
    
    cell.level_width = 0;
    cell.level.image = nil;
    cell.meiliLevel.image = nil;
    
    if (indexPath.row<self.chatMessage.count) {
        
        HJIMMessage *message = self.chatMessage[indexPath.row];

        if (message.messageType == NIMMessageTypeCustom) {
            JXIMCustomObject *obj = message.messageObject;
            if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
                Attachment *attachment = (Attachment *)obj.attachment;
                NSDictionary *dic = attachment.data;
                if (dic) {
                    if ([dic containsObjectForKey:@"msg"]) {
                        cell.msgLabel.text = dic[@"msg"];
                    }
                    if ([dic containsObjectForKey:@"params"]) {
                        NSDictionary *dicParams = [JSONTools ll_dictionaryWithJSON:dic[@"params"]];
                        
                        if ([dicParams containsObjectForKey:@"avatar"]) {
                            
                            [cell.avatar qn_setImageImageWithUrl:dicParams[@"avatar"] placeholderImage:default_avatar type:ImageTypeHomePageItem];
                        }
                        if ([dicParams containsObjectForKey:@"nick"]) {
                            cell.nameLabel.text = dicParams[@"nick"];
                            CGSize size = [cell.nameLabel.text boundingRectWithSize:CGSizeMake(0, cell.nameLabel.height) options:NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName:cell.nameLabel.font} context:nil].size;
                            cell.width_name.constant = (size.width+255)>kScreenWidth?kScreenWidth-255:size.width;
                        }
                        
                        if ([dicParams containsObjectForKey:@"experLevel"]) {
                            cell.level_width.constant = 37;
                            cell.level.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:[dicParams[@"experLevel"] integerValue]]];
                        }
                        
                        if ([dicParams containsObjectForKey:@"charmLevel"]) {
                            cell.meiliLevel.image = [UIImage imageNamed:[NSString getCharmLevelImageName:[dicParams[@"charmLevel"] integerValue]]];

                        }
                        
                    }
            }}
        }
        cell.timeLabel.text = [self timestampDescriptionForRecentSession:message.timestamp];
    }
    
    return cell;
}

- (void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath
{
    if (self.squareBlock) {
        self.squareBlock();
    }
    
//    HJNotiFriendVC *vc = [[HJNotiFriendVC alloc] init];
//    [vc updateData];
//    [[UIApplication sharedApplication].keyWindow.rootViewController.navigationController pushViewController:vc animated:YES];
}

- (NSString *)timestampDescriptionForRecentSession:(NSTimeInterval )timestamp{
    return [NIMKitUtil showTime:timestamp showDetail:NO];
}





@end
