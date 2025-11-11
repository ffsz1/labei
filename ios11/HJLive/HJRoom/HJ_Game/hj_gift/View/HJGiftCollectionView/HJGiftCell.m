//
//  HJGiftCell.m
//  HJLive
//
//  Created by apple on 2019/7/10.
//

#import "HJGiftCell.h"

@interface HJGiftCell ()
@property (weak, nonatomic) IBOutlet UIImageView *selImageView;

@property (weak, nonatomic) IBOutlet UIImageView *logoImageView;
@property (weak, nonatomic) IBOutlet UILabel *nameLabel;
@property (weak, nonatomic) IBOutlet UILabel *priceLabel;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;


@property (weak, nonatomic) IBOutlet UIImageView *firstImgView;

@property (weak, nonatomic) IBOutlet UIImageView *secondImgView;

@property (weak, nonatomic) IBOutlet UIImageView *thirdImgView;




@end

@implementation HJGiftCell

- (void)setGiftModel:(GiftInfo *)giftModel
{
    _giftModel = giftModel;
    
    if (giftModel) {
        self.nameLabel.text = giftModel.giftName;
        [self.logoImageView qn_setImageImageWithUrl:giftModel.giftUrl placeholderImage:placeholder_image_rectangle type:ImageTypeRoomGift];
        self.priceLabel.text = [NSString stringWithFormat:@"%.0f",giftModel.goldPrice];
        
        if (giftModel.userGiftPurseNum >0) {
            self.numLabel.text = [NSString stringWithFormat:@"%ld",(long)giftModel.userGiftPurseNum];
        }else{
            self.numLabel.text = @"";
        }
        
        self.firstImgView.hidden = YES;
        self.secondImgView.hidden = YES;
        self.thirdImgView.hidden = YES;
        //设置限时、最新、特效标识
        if (giftModel.hasTimeLimit) {
            self.firstImgView.hidden = NO;
            self.firstImgView.image = [UIImage imageNamed:@"hj_gift_xianshi_icon"];
            if (giftModel.hasLatest) {
                self.secondImgView.hidden = NO;
                self.secondImgView.image = [UIImage imageNamed:@"hj_gift_zuixin_icon"];
                if (giftModel.hasEffect) {
                    self.thirdImgView.hidden = NO;
                     self.thirdImgView.image = [UIImage imageNamed:@"hj_gift_texiao_icon"];
                }
            }
        }else if (giftModel.hasLatest){
            self.firstImgView.hidden = NO;
             self.firstImgView.image = [UIImage imageNamed:@"hj_gift_zuixin_icon"];
            if (giftModel.hasEffect) {
                 self.secondImgView.hidden = NO;
                  self.secondImgView.image = [UIImage imageNamed:@"hj_gift_texiao_icon"];
            }
        }else if (giftModel.hasEffect){
             self.firstImgView.hidden = NO;
             self.firstImgView.image = [UIImage imageNamed:@"hj_gift_texiao_icon"];
        }
        
        
        
        
    }
}

- (void)setSelStytle:(BOOL)isSel
{
    self.selImageView.image = [UIImage imageNamed:isSel?@"hj_gift_cell_sel":@"hj_gift_cell_normal"];
}

@end
