//
//  HJRoomRankTopView.m
//  HJLive
//
//  Created by apple on 2019/7/5.
//

#import "HJRoomRankTopView.h"
#import "NSString+Utils.h"
#import "HJRoomBounsListInfo.h"

@interface HJRoomRankTopView ()
@property (weak, nonatomic) IBOutlet UIView *secondBGView;
@property (weak, nonatomic) IBOutlet UIView *thirdBGView;
@property (weak, nonatomic) IBOutlet GGImageView *firstAvatar;

@property (weak, nonatomic) IBOutlet GGImageView *secondAvatar;
@property (weak, nonatomic) IBOutlet GGImageView *thirdAvatar;
@property (weak, nonatomic) IBOutlet UILabel *firstNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *secondNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *thirdNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *firstCoinLabel;
@property (weak, nonatomic) IBOutlet UILabel *secondCoinLabel;
@property (weak, nonatomic) IBOutlet UILabel *thirdCoinLabel;
@property (weak, nonatomic) IBOutlet UIImageView *coin3ImageView;
@property (weak, nonatomic) IBOutlet UIImageView *coinImageView2;
@property (weak, nonatomic) IBOutlet UIImageView *coinImageView;
@property (weak, nonatomic) IBOutlet UIImageView *bgImageView;
@property (weak, nonatomic) IBOutlet UIImageView *seatImageView;

@property (weak, nonatomic) IBOutlet UIImageView *crownImg;
@property (weak, nonatomic) IBOutlet UIImageView *crownImg1;
@property (weak, nonatomic) IBOutlet UIImageView *crownImg2;

@property (weak, nonatomic) IBOutlet UIImageView *secondGender;
@property (weak, nonatomic) IBOutlet UIImageView *thirdGender;
@property (weak, nonatomic) IBOutlet UIImageView *firstGender;

@end

@implementation HJRoomRankTopView

- (IBAction)firstAction:(id)sender {
    
    if (self.dataArr.count>0) {
        HJRoomBounsListInfo *model = self.dataArr[0];
        
        self.cardBlock(model.ctrbUid);
    }
    
    
}

- (IBAction)secondAction:(id)sender {
    
    if (self.dataArr.count>1) {
        HJRoomBounsListInfo *model = self.dataArr[1];
        
        self.cardBlock(model.ctrbUid);
    }
    
}

- (IBAction)thirdAction:(id)sender {
    
    if (self.dataArr.count>2) {
        HJRoomBounsListInfo *model = self.dataArr[2];
        
        self.cardBlock(model.ctrbUid);
    }
    
    
}

- (void)setIsCharm:(BOOL)isCharm
{
    _isCharm = isCharm;
    
    self.coinImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charm_coin":@"hj_room_rank_coin"];
    self.coinImageView2.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charm_coin":@"hj_room_rank_coin"];
    self.coin3ImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charm_coin":@"hj_room_rank_coin"];
    
    self.crownImg1.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charmest":@"hj_room_rank_richest"];
//    self.bgImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charm_top2":@"hj_room_rank_top2"];
//    self.seatImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charm_seat":@"hj_room_rank_seat"];
    
    self.bgImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charmbkg":@"hj_room_rank_wealthbkg"];
    self.seatImageView.image = [UIImage imageNamed:isCharm?@"hj_room_rank_charmseat":@"hj_room_rank_wealthseat"];

    
}

- (void)setDataArr:(NSMutableArray *)dataArr
{
    self.crownImg.transform = CGAffineTransformMakeRotation(-M_1_PI/2);
//    self.crownImg1.transform = CGAffineTransformMakeRotation(-M_1_PI/2);
    self.crownImg2.transform = CGAffineTransformMakeRotation(-M_1_PI/2);
    
    _dataArr = dataArr;
    self.firstAvatar.image = [UIImage imageNamed:_isCharm?@"hj_room_rank_empty_rich":@"hj_room_rank_emptywealth"];
    self.secondAvatar.image = [UIImage imageNamed:_isCharm?@"hj_room_rank_empty_rich":@"hj_room_rank_emptywealth"];
    self.thirdAvatar.image = [UIImage imageNamed:_isCharm?@"hj_room_rank_empty_rich":@"hj_room_rank_emptywealth"];
    
    self.firstNameLabel.text = @"???";
    self.secondNameLabel.text = @"???";
    self.thirdNameLabel.text = @"???";
    
    self.firstCoinLabel.text = @"0";
    self.secondCoinLabel.text = @"0";
    self.thirdCoinLabel.text = @"0";

    
    if (dataArr) {
        if (self.dataArr.count>0) {
            HJRoomBounsListInfo *model = self.dataArr[0];
            [self.secondGender setImage:[UIImage imageNamed:model.gender == 1 ? @"hj_room_rank_maleicon" : @"hj_room_rank_femaleicon"]];
            [self.firstAvatar qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
            self.firstNameLabel.text = model.nick;
//            self.firstCoinLabel.text = model.sumGold;//model.sumGold.integerValue
            self.firstCoinLabel.text = [NSString numberStringFromNumber:[NSNumber numberWithInteger:model.sumGold.integerValue] withPrefixString:@""];
            
        }
        
        if (self.dataArr.count>1) {
//            self.secondBGView.hidden = NO;

            HJRoomBounsListInfo *model = self.dataArr[1];
            [self.secondGender setImage:[UIImage imageNamed:model.gender == 1 ? @"hj_room_rank_maleicon" : @"hj_room_rank_femaleicon"]];
            [self.secondAvatar qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
            self.secondNameLabel.text = model.nick;
//            self.secondCoinLabel.text = model.sumGold;
            self.secondCoinLabel.text = [NSString numberStringFromNumber:[NSNumber numberWithInteger:model.sumGold.integerValue] withPrefixString:@""];
        }else{
//            self.secondBGView.hidden = YES;
        }
        
        if (self.dataArr.count>2) {
//            self.thirdBGView.hidden = NO;

            HJRoomBounsListInfo *model = self.dataArr[2];
            [self.secondGender setImage:[UIImage imageNamed:model.gender == 1 ? @"hj_room_rank_maleicon" : @"hj_room_rank_femaleicon"]];
            [self.thirdAvatar qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
            self.thirdNameLabel.text = model.nick;
//            self.thirdCoinLabel.text = model.sumGold;
            self.thirdCoinLabel.text = [NSString numberStringFromNumber:[NSNumber numberWithInteger:model.sumGold.integerValue] withPrefixString:@""];
        }else{
//            self.thirdBGView.hidden = YES;
        }

    }
}


@end
