//
//  YPPersonPhotoCell.m
//  HJLive
//
//  Created by feiyin on 2020/7/3.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPPersonPhotoCell.h"

#define HJPersonPhotoCellLineItemCount (4.f)

@interface YPPersonPhotoCell ()

@property (nonatomic, strong) NSMutableArray *photoViewArr;
@property (nonatomic, strong) UIButton *addBtn;
@property (nonatomic, strong) UIView *midView;
@end

@implementation YPPersonPhotoCell

+ (YPPersonPhotoCell *)cellWithTableview:(UITableView *)tableView {
    
    YPPersonPhotoCell *cell = [tableView dequeueReusableCellWithIdentifier:@"YPPersonPhotoCell"];
    
    if (!cell) {
        cell = [[YPPersonPhotoCell alloc] initWithStyle:UITableViewCellStyleDefault reuseIdentifier:@"YPPersonPhotoCell"];
    }
    
    return cell;
}

- (instancetype)initWithStyle:(UITableViewCellStyle)style reuseIdentifier:(NSString *)reuseIdentifier {
    if (self = [super initWithStyle:style reuseIdentifier:reuseIdentifier]) {
        
        UIView *bgView = [UIView new];
        bgView.backgroundColor = [UIColor clearColor];
        [self.contentView addSubview:bgView];
        self.bgView = bgView;
        
        self.selectionStyle = UITableViewCellSelectionStyleNone;
        self.textLabel.textColor = UIColorHex(999999);
        self.textLabel.font = [UIFont boldSystemFontOfSize:13.f];
        
        [self.contentView addSubview:self.midView];
    }
    return self;
}

- (void)setIsOwner:(BOOL)isOwner {
    
    _isOwner = isOwner;
    
    if (isOwner) {
        self.textLabel.text = @"";
    }
    else {
        self.textLabel.text = @"Ta没上传任何照片~";
    }
}

- (void)setImageUrlArr:(NSArray *)imageUrlArr {
    
    _imageUrlArr = imageUrlArr;
    
    [self.addBtn removeFromSuperview];
    if (self.photoViewArr.count) {
        for (UIView *view in self.photoViewArr) {
            [view removeFromSuperview];
        }
    }
    self.photoViewArr = [NSMutableArray array];
    
    int index = 0;
    
    if (self.isOwner) {
        [self.contentView addSubview:self.addBtn];
        index = 1;
    }
    
    if (imageUrlArr.count) {
        self.textLabel.text = @"";
        
        CGFloat width = floor((XC_SCREE_W - 30.f - 14.f) / HJPersonPhotoCellLineItemCount);
        CGFloat height = width;
        for (int i = 0; i < imageUrlArr.count; i++) {
            
            if (i <= 8) {
                
                CGFloat x = 15.f + (width + 7.f) * ((i + index) % (int)HJPersonPhotoCellLineItemCount);
                CGFloat y = (height + 7.f) * ((i + index) / (int)HJPersonPhotoCellLineItemCount);
                
                UIImageView *imageView = [UIImageView new];
                imageView.userInteractionEnabled = YES;
                @weakify(self);
                [imageView addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
                    @strongify(self);
                    if (self.imageViewTapActionBlock) {
                        self.imageViewTapActionBlock(i);
                    }
                }]];;
                imageView.contentMode = UIViewContentModeScaleAspectFill;
                imageView.clipsToBounds = YES;
                [imageView sd_setImageWithURL:[NSURL URLWithString:imageUrlArr[i]] placeholderImage:[UIImage imageNamed:placeholder_image_square]];
                imageView.frame = CGRectMake(x, y, width, height);
                imageView.layer.cornerRadius = 10;
                imageView.layer.masksToBounds = YES;
                [self.bgView addSubview:imageView];
                [self.photoViewArr addObject:imageView];
            }
            
        }
    }

}

- (void)layoutSubviews {
    [super layoutSubviews];
    
    [self.bgView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.edges.equalTo(self.contentView);
    }];
    
    if (self.textLabel.text.length) {
        CGSize size = [self.textLabel.text boundingRectWithSize:CGSizeMake(XC_SCREE_W - 30.f, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin attributes:@{NSFontAttributeName : self.textLabel.font} context:nil].size;
        self.textLabel.frame = CGRectMake((XC_SCREE_W-size.width)/2, 0, size.width, size.height);
//        [self.textLabel mas_remakeConstraints:^(MASConstraintMaker *make) {
//            make.centerX.equalTo(self.contentView);
//            make.top.mas_equalTo(0);
//        }];
    }
    
    [self.midView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.left.right.equalTo(self.contentView);
        make.height.equalTo(@10);
        make.bottom.equalTo(self.contentView);
    }];
}

- (void)addBtnAction {
    
    if (self.addBtnActionBlock) {
        self.addBtnActionBlock();
    }
}

- (UIView *)midView {
    if (!_midView) {
        _midView = [UIView new];
        _midView.backgroundColor = UIColorHex(F5F5F5);
    }
    return _midView;
}

- (UIButton *)addBtn {
    if (!_addBtn) {
        _addBtn = [UIButton new];
        [_addBtn setBackgroundImage:[UIImage imageNamed:@"yp_me_person_addPhoto"] forState:UIControlStateNormal];
        [_addBtn setBackgroundImage:[UIImage imageNamed:@"yp_me_person_addPhoto"] forState:UIControlStateHighlighted];
        [_addBtn addTarget:self action:@selector(addBtnAction) forControlEvents:UIControlEventTouchUpInside];
        CGFloat width = floor((XC_SCREE_W - 30.f - 14.f) / HJPersonPhotoCellLineItemCount);
        _addBtn.frame = CGRectMake(15.f, 0, width, width);
    }
    return _addBtn;
}



+ (CGFloat)getHeightWithIsOwner:(BOOL)isOwner imageUrlArr:(NSArray *)imageUrlArr {
    
    CGFloat width = floor((XC_SCREE_W - 30.f - 14.f) / HJPersonPhotoCellLineItemCount);
    
    if (!imageUrlArr.count) {
        if (!isOwner) {
            return 47.f;
        }
        else {
            return width + 15.f;
        }
    }
    else {
        
        int count = imageUrlArr.count;
        if (isOwner) {
            count += 1;
        }
        
        if (count > 9) {
            count = 9;
        }
        
        int colon = ceil(count / HJPersonPhotoCellLineItemCount);
        
        return 15.f + colon * width +  7.f * (colon - 1);
    }
}

@end
