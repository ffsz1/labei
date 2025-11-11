//
//  HJFamilyMembersView.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyMembersView.h"
#import "UserInfo.h"
#import "NSString+NIMKit.h"

@interface HJFamilyMembersView ()

@property (nonatomic, assign) NSInteger limitedItems;
@property (nonatomic, assign) CGFloat itemSpacing;
@property (nonatomic, assign) CGSize itemSize;

@property (nonatomic, strong) NSMutableArray<UIView *> *itemViews;

@end

@implementation HJFamilyMembersView

#pragma mark - Life cycle
- (instancetype)initWithFrame:(CGRect)frame {
    self = [super initWithFrame:frame];
    if (self) {
        [self commonInit];
        [self addControls];
        [self layoutControls];
    }
    return self;
}

#pragma mark - Event

#pragma mark - Public methods

#pragma mark - Private methods
- (UIImageView *)getItemViewWithInfo:(UserInfo *)info {
    UIImageView *itemView = [UIImageView new];
    
    [itemView sd_setImageWithURL:[NSURL URLWithString:[info.avatar cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
    itemView.layer.cornerRadius = 50.f/2;
    itemView.layer.masksToBounds = YES;
    itemView.layer.borderColor = [UIColor whiteColor].CGColor;
    itemView.layer.borderWidth = 2.f;
    itemView.userInteractionEnabled = YES;
    return itemView;
}

- (void)commonInit {
    self.limitedItems = 6;
    self.itemSize = CGSizeMake(50, 50);
    self.itemSpacing = -10;
    @weakify(self);
    [self addGestureRecognizer:[[UITapGestureRecognizer alloc] initWithActionBlock:^(id  _Nonnull sender) {
        @strongify(self);
        !self.didTapHandler ?: self.didTapHandler();
    }]];
    self.exclusiveTouch = YES;
}

- (void)resetControls {
    [self.itemViews makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [self.itemViews removeAllObjects];
}

#pragma mark - Layout
- (void)addItemViews {
    @weakify(self);
    [self.items enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        @strongify(self);
        if (idx < self.limitedItems) {
            UIImageView *itemView = [self getItemViewWithInfo:obj];
            [self addSubview:itemView];
            [self.itemViews addObject:itemView];
        }
        
        if (idx >= self.limitedItems) {
            *stop = YES;
        }
    }];
}

- (void)layoutItemViews {
    UIView *lastItem = nil;
    for (NSInteger i = 0; i < self.itemViews.count; i++) {
        UIView *item = [self.itemViews objectOrNilAtIndex:i];
        [item mas_makeConstraints:^(MASConstraintMaker *make) {
            make.size.equalTo(@(self.itemSize));
        }];
        
        if (i == 0) {
            [item mas_makeConstraints:^(MASConstraintMaker *make) {
                make.left.equalTo(self);
                make.top.equalTo(self);
                make.bottom.equalTo(self).priority(750);
            }];
        }
        
        if (i == self.itemViews.count - 1) {
            [item mas_makeConstraints:^(MASConstraintMaker *make) {
                make.right.equalTo(self).priority(750);
            }];
        }
        
        if (lastItem) {
            [item mas_makeConstraints:^(MASConstraintMaker *make) {
                make.centerY.equalTo(lastItem);
                make.left.equalTo(lastItem.mas_right).offset(self.itemSpacing);
            }];
        }
        lastItem = item;
    }
}

- (void)addControls {
    
}

- (void)layoutControls {
    
}

#pragma mark - setters/getters
- (void)setItems:(NSArray<UserInfo *> *)items {
    _items = items;
    
    [self resetControls];
    [self addItemViews];
    [self layoutItemViews];
}

- (NSMutableArray<UIView *> *)itemViews {
    if (!_itemViews) {
        _itemViews = @[].mutableCopy;
    }
    return _itemViews;
}

@end
