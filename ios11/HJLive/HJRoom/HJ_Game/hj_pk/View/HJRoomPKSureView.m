//
//  HJRoomPKSureView.m
//  HJLive
//
//  Created by apple on 2019/6/19.
//

#import "HJRoomPKSureView.h"

#import "HJHttpRequestHelper+PK.h"
#import "PurseCore.h"

@interface HJRoomPKSureView()
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView1;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView2;
@property (weak, nonatomic) IBOutlet UIImageView *giftImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel1;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel2;
@property (weak, nonatomic) IBOutlet UIButton *pkBtn1;
@property (weak, nonatomic) IBOutlet UIButton *pkBtn2;
@property (weak, nonatomic) IBOutlet UIButton *pkBtn3;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;

@end

@implementation HJRoomPKSureView
- (IBAction)closeAction:(id)sender {
    
    self.closeBlock();
    
}

- (IBAction)pkAction1:(id)sender {
    
    self.pkBtn1.selected = YES;
    self.pkBtn2.selected = NO;
    self.pkBtn3.selected = NO;
    
}

- (IBAction)pkAction2:(id)sender {
    self.pkBtn1.selected = NO;
    self.pkBtn2.selected = YES;
    self.pkBtn3.selected = NO;
}

- (IBAction)pkAction3:(id)sender {
    self.pkBtn1.selected = NO;
    self.pkBtn2.selected = NO;
    self.pkBtn3.selected = YES;
}

- (IBAction)sureAction:(id)sender {
    
    NSInteger choose = 2;
    if (self.pkBtn2.selected) choose = 1;
    if (self.pkBtn3.selected) choose = 3;
    
    NSString *recordID = [NSString stringWithFormat:@"%ld",self.joinModel.recordId];
    
    [MBProgressHUD showMessage:@"猜拳中..."];
    
    __weak typeof(self)weakSelf = self;
    [HJHttpRequestHelper pk_confirmJoinPk:recordID choose:choose success:^(id  _Nonnull data) {
        [MBProgressHUD hideHUD];
        [weakSelf closeAction:nil];
        
    } failure:^(NSNumber * _Nonnull resCode, NSString * _Nonnull message) {
        [MBProgressHUD hideHUD];
        [weakSelf closeAction:nil];
    }];
    
}

- (void)setJoinModel:(HJRoomPKJoinModel *)joinModel
{
    _joinModel = joinModel;
    
    if (_joinModel) {
//        self.nameLabel1.text = joinModel.
        
        [self.avatarImageView1 qn_setImageImageWithUrl:joinModel.avatar placeholderImage:default_avatar type:ImageTypeUserLibary];
        
        [self.avatarImageView2 qn_setImageImageWithUrl:joinModel.opponentAvatar placeholderImage:default_avatar type:ImageTypeUserLibary];
        
        [self.giftImageView qn_setImageImageWithUrl:joinModel.giftUrl placeholderImage:default_avatar type:ImageTypeUserLibary];

        self.numLabel.text = [NSString stringWithFormat:@"X %ld",joinModel.giftNum];

        self.nameLabel1.text = joinModel.nick;
        self.nameLabel2.text = joinModel.opponentNick;

        
    }
    
}

@end
