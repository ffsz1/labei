//
//  JXAuthorizationAlertView.m
//  XChat
//
//  Created by Colin on 2019/2/18.
//

#import "JXAuthorizationAlertView.h"

//#import <JXCategories/JXCategories.h>
#import "JXCategories.h"
@implementation JXAuthorizationAlertAction

- (instancetype)initWithTitle:(NSString *)title style:(JXAuthorizationAlertActionStyle)style handler:(JXAuthorizationAlertViewActionDidTapHandler)handler {
    self = [super init];
    if (self) {
        self.title = title;
        self.style = style;
        if (handler) {
            self.didTapHandler = handler;
        }
    }
    return self;
}

@end


@interface JXAuthorizationAlertView ()

@property (nonatomic, strong) UILabel *titleLabel;
@property (nonatomic, strong) NSMutableArray<UIButton *> *items;
@property (nonatomic, strong) NSMutableArray<UIView *> *lines;

@property (nonatomic, strong) NSMutableArray<JXAuthorizationAlertAction *> *actions;
@property (nonatomic, copy) NSString *title;

@property (nonatomic, strong) MASConstraint *titleLabelButtomConstraint;

@end

@implementation JXAuthorizationAlertView

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

- (instancetype)initWithFrame:(CGRect)frame title:(NSString *)title actions:(NSArray<JXAuthorizationAlertAction *> *)actions {
    self = [self initWithFrame:frame];
    if (self) {
        _title = title;
        if (actions) {
            [self.actions addObjectsFromArray:actions];
        }
        [self addItems];
        [self layoutItems];
        [self updateControls];
    }
    return self;
}

#pragma mark - Event

#pragma mark - Public methods

#pragma mark - Private methods
- (void)commonInit {
    self.backgroundColor = [UIColor whiteColor];
    self.layer.cornerRadius = 10.f;
    self.layer.masksToBounds = YES;
}

- (UIButton *)getItemViewWithAction:(JXAuthorizationAlertAction *)action {
    if (!action) return nil;
    
    NSString *titleColor = @"";
    switch (action.style) {
        case JXAuthorizationAlertActionStyleNormal:
        {
            titleColor = @"#9F62FB";
        }
            break;
        case JXAuthorizationAlertActionStyleCancel:
        {
            titleColor = @"#BABABA";
        }
            break;
    }
    UIButton *button = [UIButton new];
    [button setTitle:action.title forState:UIControlStateNormal];
    [button setTitleColor:[UIColor jx_colorWithHexString:titleColor] forState:UIControlStateNormal];
    button.titleLabel.font = [UIFont boldSystemFontOfSize:15];
    [button jx_addBlockForTouchUpInsideControlEvent:^(id sender) {
        !action.didTapHandler ?: action.didTapHandler();
    }];
    return button;
}

- (UIView *)getLine {
    UIView *lineView = [UIView new];
    lineView.backgroundColor = [UIColor jx_colorWithHexString:@"#F6F6F6"];
    return lineView;
}

- (void)updateControls {
    self.titleLabel.text = self.title;
    
    [self.titleLabelButtomConstraint uninstall];
    if (self.items.count) {
        UIView *item = [self.items jx_objectOrNilAtIndex:0];
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            self.titleLabelButtomConstraint = make.bottom.equalTo(item.mas_top).offset(-20).priority(750);
        }];
    } else {
        [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
            self.titleLabelButtomConstraint = make.bottom.equalTo(self).offset(-20).priority(750);
        }];
    }
}

#pragma mark - Layout
- (void)addControls {
    [self addSubview:self.titleLabel];
//    [self addSubview:self.lineView];
}

- (void)layoutControls {
    [self.titleLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.leading.equalTo(self).offset(20);
        make.trailing.equalTo(self).offset(-20);
        make.top.equalTo(self).offset(20);
        self.titleLabelButtomConstraint = make.bottom.equalTo(self).offset(-20).priority(750);
    }];
}

- (void)clearLines {
    [self.lines makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [self.lines removeAllObjects];
}

- (void)clearItems {
    [self.items makeObjectsPerformSelector:@selector(removeFromSuperview)];
    [self.items removeAllObjects];
}

- (void)addItems {
    [self clearItems];
    [self clearLines];
    
    for (JXAuthorizationAlertAction *action in self.actions) {
        UIButton *item = [self getItemViewWithAction:action];
        [self.items addObject:item];
        [self addSubview:item];
    }
}

- (void)layoutItems {
    NSInteger count = self.items.count;
    if (!count) return;
    
    CGFloat itemHeight = 44.f;
    if (count == 2) {
        UIView *itemLeft = [self.items jx_objectOrNilAtIndex:0];
        UIView *itemRight = [self.items jx_objectOrNilAtIndex:1];
        [itemLeft mas_makeConstraints:^(MASConstraintMaker *make) {
            make.leading.equalTo(self);
            make.bottom.equalTo(self);
            make.height.equalTo(@(itemHeight));
            make.width.equalTo(itemRight);
        }];
        
        [itemRight mas_makeConstraints:^(MASConstraintMaker *make) {
            make.leading.equalTo(itemLeft.mas_trailing);
            make.trailing.equalTo(self);
            make.bottom.equalTo(itemLeft);
            make.height.equalTo(itemLeft);
            make.width.equalTo(itemLeft);
        }];
        
        UIView *lineView = [self getLine];
        [self addSubview:lineView];
        [self.lines addObject:lineView];
        [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
            make.width.equalTo(@0.5);
            make.top.bottom.equalTo(itemLeft);
            make.centerX.equalTo(self);
        }];
    } else {
        __block UIView *lastItem = nil;
        @weakify(self);
        [self.items enumerateObjectsWithOptions:NSEnumerationReverse usingBlock:^(UIButton * _Nonnull item, NSUInteger idx, BOOL * _Nonnull stop) {
            @strongify(self);
            UIView *lineView = [self getLine];
            [self addSubview:lineView];
            [self.lines addObject:lineView];
            [lineView mas_makeConstraints:^(MASConstraintMaker *make) {
                make.height.equalTo(@0.5);
                make.leading.trailing.equalTo(self);
                make.top.equalTo(item);
            }];
            
            [item mas_makeConstraints:^(MASConstraintMaker *make) {
                make.leading.trailing.equalTo(self);
                make.height.equalTo(@(itemHeight));
            }];
            
            if (lastItem) {
                [item mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.equalTo(lastItem.mas_top);
                }];
            } else {
                [item mas_makeConstraints:^(MASConstraintMaker *make) {
                    make.bottom.equalTo(self).priority(750);
                }];
            }
            lastItem = item;
        }];
    }
}

#pragma mark - setters/getters
- (UILabel *)titleLabel {
    if (!_titleLabel) {
        _titleLabel = [UILabel new];
        _titleLabel.font = [UIFont boldSystemFontOfSize:16];
        _titleLabel.textColor = [UIColor jx_colorWithHexString:@"#444444"];
        _titleLabel.numberOfLines = 0;
        _titleLabel.textAlignment = NSTextAlignmentCenter;
    }
    return _titleLabel;
}

- (NSMutableArray<JXAuthorizationAlertAction *> *)actions {
    if (!_actions) {
        _actions = @[].mutableCopy;
    }
    return _actions;
}

- (NSMutableArray<UIButton *> *)items {
    if (!_items) {
        _items = @[].mutableCopy;
    }
    return _items;
}

@end
