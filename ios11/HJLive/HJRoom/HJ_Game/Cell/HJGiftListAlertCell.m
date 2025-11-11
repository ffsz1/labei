//
//  HJGiftListAlertCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/8.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJGiftListAlertCell.h"
#import "UIImageView+QiNiu.h"

@interface HJGiftListAlertCell ()

@property (weak, nonatomic) IBOutlet UIImageView *photoView;
@property (weak, nonatomic) IBOutlet UILabel *senderNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *timeLabel;
@property (weak, nonatomic) IBOutlet UILabel *giftNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *receiverNameLabel;
@property (weak, nonatomic) IBOutlet UILabel *numLabel;

@end

@implementation HJGiftListAlertCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    self.photoView.contentMode = UIViewContentModeScaleAspectFill;
    self.photoView.clipsToBounds = YES;
    self.selectionStyle = UITableViewCellSelectionStyleNone;
}

- (void)setSelected:(BOOL)selected animated:(BOOL)animated {
    [super setSelected:selected animated:animated];

    // Configure the view for the selected state
}

- (void)setModel:(GiftReceiveInfo *)model {
    
    _model = model;
    
    self.senderNameLabel.text = model.nick;
    if (self.isAll) {
        self.receiverNameLabel.text = NSLocalizedString(XCRoomAllMic, nil);
    }
    else {
        
        self.receiverNameLabel.text = model.targetNick;
    }
    self.numLabel.text = [NSString stringWithFormat:@"x%ld",model.giftNum];
}

- (void)setGiftInfo:(GiftInfo *)giftInfo {
    
    _giftInfo = giftInfo;
    
    self.giftNameLabel.text = giftInfo.giftName;
    [self.photoView qn_setImageImageWithUrl:giftInfo.giftUrl placeholderImage:nil type:(ImageType)ImageTypeRoomGift];
}

- (void)setTimeString:(NSString *)timeString {
    
    _timeString = timeString;
    self.timeLabel.text = timeString;
}


@end
