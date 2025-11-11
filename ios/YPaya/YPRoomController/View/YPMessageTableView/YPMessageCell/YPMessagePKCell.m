//
//  YPMessagePKCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPMessagePKCell.h"
#import "YPRoomPKView.h"

#import "YPHttpRequestHelper+PK.h"

@implementation YPMessagePKCell

- (IBAction)pkAction:(id)sender {
    
    NSDictionary *dict = _attachment.data;
    if (dict) {
        YPRoomPKMsgModel *model = [YPRoomPKMsgModel yy_modelWithJSON:dict[@"moraRecordMessage"]];
        
        NSString *recordID = [NSString stringWithFormat:@"%ld",(long)model.recordId];
        
        [MBProgressHUD showMessage:@"参与中..."];
        [YPHttpRequestHelper pk_joinPk:recordID success:^(id  _Nonnull data) {
            
            [MBProgressHUD hideHUD];
            if (data) {
                YPRoomPKJoinModel *model = [YPRoomPKJoinModel yy_modelWithJSON:data];
                [YPRoomPKView showSurePKView:model];
            }
            
            
        } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
            
            if ([resCode integerValue] == 60002) {
                [YPRoomPKView showFailView];
            }
            
//            [MBProgressHUD showError:message];
        }];
    }
}

- (void)setAttachment:(YPAttachment *)attachment
{
    _attachment = attachment;
    
    if (attachment) {
        
        
        
        NSDictionary *dict = attachment.data;
        if (dict) {
            YPRoomPKMsgModel *model = [YPRoomPKMsgModel yy_modelWithJSON:dict[@"moraRecordMessage"]];
            
            
            [self.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:placeholder_image_square type:ImageTypeUserIcon];
            self.nameLabel.text = model.nick;
            self.levelImageView.image = [UIImage imageNamed:[NSString getLevelImageName:model.experienceLevel]];
            self.resultLabel.text = model.subject;
            [self.giftImageView qn_setImageImageWithUrl:model.giftUrl placeholderImage:placeholder_image_square type:ImageTypeUserIcon];
            self.giftNumLabel.text = [NSString stringWithFormat:@"X%ld",(long)model.giftNum];
            
            self.nameLabel2.text = JX_STR_AVOID_nil(model.opponentNick);
            
            if (attachment.second == Custom_Noti_Sub_Mora_send) {
                self.pkBtn.hidden = NO;
            }else{
                self.pkBtn.hidden = YES;
            }
            
            if (attachment.second == Custom_Noti_Sub_Mora_ping) {
                self.giftImageView.hidden = YES;
                self.giftNumLabel.hidden = YES;
            }else{
                self.giftImageView.hidden = NO;
                self.giftNumLabel.hidden = NO;
            }
            
            
            if ([model.uid userIDValue] == [GetCore(YPAuthCoreHelp).getUid userIDValue]) {
                self.pkBtn.hidden = YES;
            }
            
        }
    }
}


@end
