//
//  HJPlayingFaceMessageCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/15.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPlayingFaceMessageCell.h"
#import "Attachment.h"
#import "HJFaceSendInfo.h"
#import "NSObject+YYModel.h"
#import "HJFaceReceiveInfo.h"
#import "YYDefaultTheme.h"
#import <UIImageView+WebCache.h>
#import "HJFaceCore.h"

@interface HJPlayingFaceMessageCell()
@property (strong, nonatomic) NSMutableArray *labelArr;
@property (strong, nonatomic) NSMutableArray *imageViewArr;
@property (assign, nonatomic) CGFloat maxWidth;
@end

@implementation HJPlayingFaceMessageCell

- (void)awakeFromNib {
    [super awakeFromNib];
    // Initialization code
    
    
}

- (void)setMessage:(HJIMMessage *)message {
    _message = message;
    JXIMCustomObject *obj = message.messageObject;
    if (obj.attachment != nil && [obj.attachment isKindOfClass:[Attachment class]]) {
        Attachment *attachment = (Attachment *)obj.attachment;
        HJFaceSendInfo *faceattachement = [HJFaceSendInfo yy_modelWithJSON:attachment.data];
        NSMutableArray *arr = [faceattachement.data mutableCopy];
        if (arr.count == 0) {
            self.oneLabel.hidden = YES;
            self.oneImageView.hidden = YES;
            self.twoLabel.hidden = YES;
            self.twoImageView.hidden = YES;
            self.threeLabel.hidden = YES;
            self.threeImageView.hidden = YES;
            self.fourLabel.hidden = YES;
            self.fourImageView.hidden = YES;
            self.fiveLabel.hidden = YES;
            self.fiveImageView.hidden = YES;
            self.sixLabel.hidden = YES;
            self.sixImageView.hidden = YES;
            self.sevenLabel.hidden = YES;
            self.sevenImageView.hidden = YES;
            self.eightLabel.hidden = YES;
            self.eightImageView.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.containerHeightConstraint.constant = 0;
        } else if (arr.count == 1) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = YES;
            self.twoImageView.hidden = YES;
            self.threeLabel.hidden = YES;
            self.threeImageView.hidden = YES;
            self.fourLabel.hidden = YES;
            self.fourImageView.hidden = YES;
            self.fiveLabel.hidden = YES;
            self.fiveImageView.hidden = YES;
            self.sixLabel.hidden = YES;
            self.sixImageView.hidden = YES;
            self.sevenLabel.hidden = YES;
            self.sevenImageView.hidden = YES;
            self.eightLabel.hidden = YES;
            self.eightImageView.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.containerHeightConstraint.constant = 45;
        } else if (arr.count == 2) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = YES;
            self.threeImageView.hidden = YES;
            self.fourLabel.hidden = YES;
            self.fourImageView.hidden = YES;
            self.fiveLabel.hidden = YES;
            self.fiveImageView.hidden = YES;
            self.sixLabel.hidden = YES;
            self.sixImageView.hidden = YES;
            self.sevenLabel.hidden = YES;
            self.sevenImageView.hidden = YES;
            self.eightLabel.hidden = YES;
            self.eightImageView.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.containerHeightConstraint.constant = 81;
        } else if (arr.count == 3) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = NO;
            self.threeImageView.hidden = NO;
            self.fourLabel.hidden = YES;
            self.fourImageView.hidden = YES;
            self.fiveLabel.hidden = YES;
            self.fiveImageView.hidden = YES;
            self.sixLabel.hidden = YES;
            self.sixImageView.hidden = YES;
            self.sevenLabel.hidden = YES;
            self.sevenImageView.hidden = YES;
            self.eightLabel.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.eightImageView.hidden = YES;
            self.containerHeightConstraint.constant = 117;
        } else if (arr.count == 4) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = NO;
            self.threeImageView.hidden = NO;
            self.fourLabel.hidden = NO;
            self.fourImageView.hidden = NO;
            self.fiveLabel.hidden = YES;
            self.fiveImageView.hidden = YES;
            self.sixLabel.hidden = YES;
            self.sixImageView.hidden = YES;
            self.sevenLabel.hidden = YES;
            self.sevenImageView.hidden = YES;
            self.eightLabel.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.eightImageView.hidden = YES;
            self.containerHeightConstraint.constant = 153;
        } else if (arr.count == 5) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = NO;
            self.threeImageView.hidden = NO;
            self.fourLabel.hidden = NO;
            self.fourImageView.hidden = NO;
            self.fiveLabel.hidden = NO;
            self.fiveImageView.hidden = NO;
            self.sixLabel.hidden = YES;
            self.sixImageView.hidden = YES;
            self.sevenLabel.hidden = YES;
            self.sevenImageView.hidden = YES;
            self.eightLabel.hidden = YES;
            self.eightImageView.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.containerHeightConstraint.constant = 189;
        } else if (arr.count == 6) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = NO;
            self.threeImageView.hidden = NO;
            self.fourLabel.hidden = NO;
            self.fourImageView.hidden = NO;
            self.fiveLabel.hidden = NO;
            self.fiveImageView.hidden = NO;
            self.sixLabel.hidden = NO;
            self.sixImageView.hidden = NO;
            self.sevenLabel.hidden = YES;
            self.sevenImageView.hidden = YES;
            self.eightLabel.hidden = YES;
            self.eightImageView.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.containerHeightConstraint.constant = 225;
        } else if (arr.count == 7) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = NO;
            self.threeImageView.hidden = NO;
            self.fourLabel.hidden = NO;
            self.fourImageView.hidden = NO;
            self.fiveLabel.hidden = NO;
            self.fiveImageView.hidden = NO;
            self.sixLabel.hidden = NO;
            self.sixImageView.hidden = NO;
            self.sevenLabel.hidden = NO;
            self.sevenImageView.hidden = NO;
            self.eightLabel.hidden = YES;
            self.eightImageView.hidden = YES;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.containerHeightConstraint.constant = 261;
        } else if (arr.count == 8) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = NO;
            self.threeImageView.hidden = NO;
            self.fourLabel.hidden = NO;
            self.fourImageView.hidden = NO;
            self.fiveLabel.hidden = NO;
            self.fiveImageView.hidden = NO;
            self.sixLabel.hidden = NO;
            self.sixImageView.hidden = NO;
            self.sevenLabel.hidden = NO;
            self.sevenImageView.hidden = NO;
            self.eightLabel.hidden = NO;
            self.eightImageView.hidden = NO;
            self.nineLabel.hidden = YES;
            self.nineImageView.hidden = YES;
            self.containerHeightConstraint.constant = 300;
        } else if (arr.count == 9) {
            self.oneLabel.hidden = NO;
            self.oneImageView.hidden = NO;
            self.twoLabel.hidden = NO;
            self.twoImageView.hidden = NO;
            self.threeLabel.hidden = NO;
            self.threeImageView.hidden = NO;
            self.fourLabel.hidden = NO;
            self.fourImageView.hidden = NO;
            self.fiveLabel.hidden = NO;
            self.fiveImageView.hidden = NO;
            self.sixLabel.hidden = NO;
            self.sixImageView.hidden = NO;
            self.sevenLabel.hidden = NO;
            self.sevenImageView.hidden = NO;
            self.eightLabel.hidden = NO;
            self.eightImageView.hidden = NO;
            self.nineLabel.hidden = NO;
            self.nineImageView.hidden = NO;
            self.containerHeightConstraint.constant = 336;
        }

        [self layoutIfNeeded];
        NSMutableArray *tempArr = [NSMutableArray array];
        for (int i = 0; i< arr.count; i++) {
            HJFaceReceiveInfo *item = arr[i];
            UILabel *label = self.labelArr[i];
            NSString *content = [NSString stringWithFormat:@"%@ 出",item.nick];
            NSMutableAttributedString *str = [[NSMutableAttributedString alloc]initWithString:content];
            UIColor *redColor = [[YYDefaultTheme defaultTheme]colorWithHexString:@"#FF3A30" alpha:1.0];
            UIColor *whiteColor = [[YYDefaultTheme defaultTheme]colorWithHexString:@"#FFFFFF" alpha:1.0];
            [str addAttribute:NSForegroundColorAttributeName
                        value:redColor
                        range:NSMakeRange(0, item.nick.length)];
            [str addAttribute:NSForegroundColorAttributeName
                        value:whiteColor
                        range:NSMakeRange(item.nick.length + 1, 1)];
            [label setAttributedText:str];
            
            UIImageView *imageView = self.imageViewArr[i];
            FaceInfo *info = [GetCore(HJFaceCore)findFaceInfoById:item.faceId];
            [imageView sd_setImageWithURL:[NSURL URLWithString:info.facePicUrl]];
//            [tempArr addObject:@(label.frame.size.width)];
            CGFloat maxWidth = [self sizeWithText:label.text font:label.font maxWidth:MAXFLOAT].width;
            [tempArr addObject:@(maxWidth)];
            
//            UIFont *font = [UIFont fontWithName:@"HelveticaNeue" size:13.0f];
//            CGSize titleSize = [item.nick sizeWithFont:font constrainedToSize:CGSizeMake(MAXFLOAT, 30)];

        }
        CGFloat maxValue = [[tempArr valueForKeyPath:@"@max.floatValue"] floatValue];

        self.containerViewWidthConstraint.constant = 55 + maxValue;
        [self layoutIfNeeded];
        
    }
}


#pragma mark - Getter

- (NSMutableArray *)labelArr {
    if (_labelArr == nil) {
        _labelArr = [NSMutableArray array];
        [_labelArr addObject:self.oneLabel];
        [_labelArr addObject:self.twoLabel];
        [_labelArr addObject:self.threeLabel];
        [_labelArr addObject:self.fourLabel];
        [_labelArr addObject:self.fiveLabel];
        [_labelArr addObject:self.sixLabel];
        [_labelArr addObject:self.sevenLabel];
        [_labelArr addObject:self.eightLabel];
        [_labelArr addObject:self.nineLabel];
    }
    return _labelArr;
}

- (NSMutableArray *)imageViewArr {
    if (_imageViewArr == nil) {
        _imageViewArr = [NSMutableArray array];
        [_imageViewArr addObject:self.oneImageView];
        [_imageViewArr addObject:self.twoImageView];
        [_imageViewArr addObject:self.threeImageView];
        [_imageViewArr addObject:self.fourImageView];
        [_imageViewArr addObject:self.fiveImageView];
        [_imageViewArr addObject:self.sixImageView];
        [_imageViewArr addObject:self.sevenImageView];
        [_imageViewArr addObject:self.eightImageView];
        [_imageViewArr addObject:self.nineImageView];
    }
    return _imageViewArr;
}

- (CGSize)sizeWithText:(NSString *)text font:(UIFont *)font maxWidth:(CGFloat)width
{
    NSMutableDictionary *attrDict = [NSMutableDictionary dictionary];
    attrDict[NSFontAttributeName] = font;
    CGSize size = [text boundingRectWithSize:CGSizeMake(width, MAXFLOAT) options:NSStringDrawingUsesLineFragmentOrigin attributes:attrDict context:nil].size;
    return size;
}

@end
