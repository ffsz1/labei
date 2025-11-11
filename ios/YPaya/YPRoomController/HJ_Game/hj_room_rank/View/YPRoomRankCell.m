//
//  YPRoomRankCell.m
//  HJLive
//
//  Created by apple on 2019/7/5.
//

#import "YPRoomRankCell.h"

#import "NSString+GGImage.h"

@interface YPRoomRankCell ()
@property (weak, nonatomic) IBOutlet UILabel *numLabel;
@property (weak, nonatomic) IBOutlet GGImageView *avatarImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
//@property (weak, nonatomic) IBOutlet UIImageView *richLevelImageView;
//@property (weak, nonatomic) IBOutlet UIImageView *levelImageView;
@property (weak, nonatomic) IBOutlet UIImageView *genderImg;


@property (weak, nonatomic) IBOutlet UIImageView *coinImageView;
@property (weak, nonatomic) IBOutlet UILabel *coinLabel;

@end

@implementation YPRoomRankCell

- (void)setModel:(YPRoomBounsListInfo *)model
{
    _model = model;
    if (model) {
        self.nameLabel.text = model.nick;
        
        self.coinLabel.text = model.sumGold;
        [self.avatarImageView qn_setImageImageWithUrl:model.avatar placeholderImage:default_avatar type:ImageTypeUserIcon];
        [self.genderImg setImage:[UIImage imageNamed:model.gender == 1 ? @"yp_room_rank_maleicon" : @"yp_room_rank_femaleicon"]];
//        if (self.isCharm) {
//            self.levelImageView.image = [UIImage imageNamed:[NSString getCharmLevelImageName:model.charmLevel]];
//            self.levelImageView.hidden = NO;
//            self.richLevelImageView.hidden = YES;

//        }else{
//            self.levelImageView.hidden = YES;
//            self.richLevelImageView.hidden = NO;
            
//            self.richLevelImageView.image = [UIImage imageNamed:[NSString getMoneyLevelImageName:model.experLevel]];
//        }
        
        
    }
}

- (void)setIndexPath:(NSIndexPath *)indexPath
{
    _indexPath = indexPath;
    
    self.numLabel.text = [NSString stringWithFormat:@"%ld",indexPath.row+4];
    
}

- (void)setIsCharm:(BOOL)isCharm
{
    _isCharm = isCharm;
    
    self.coinImageView.image = [UIImage imageNamed:!isCharm?@"yp_room_rank_coin":@"yp_room_rank_charm_coin"];
    
}



@end
