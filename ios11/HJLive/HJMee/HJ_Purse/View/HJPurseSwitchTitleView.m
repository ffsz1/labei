//
//  HJPurseSwitchTitleView.m
//  HJLive
//
//  Created by feiyin on 2020/7/1.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJPurseSwitchTitleView.h"

const CGFloat buttonMinMagin = 20.f;
const NSInteger buttonIndex = 18977;

@interface HJPurseSwitchTitleView ()

@property (nonatomic, strong) NSArray<XCPurseSwitchModel *> *styleArr;
@property (nonatomic, strong) NSArray<NSString *> *titleArr;
@property (nonatomic, strong) NSMutableArray *buttonWidthArr;
@property (nonatomic, strong) NSMutableArray *bttonArr;
@property (nonatomic, strong) NSMutableArray *lineWidthArr;

@property (nonatomic, strong) XCPurseSwitchModel *normalStyle;

@property (nonatomic, weak) UIScrollView *scrollView;
@property (nonatomic, weak) UIView *lineView;

@property (nonatomic, copy) void(^didClickButton)(NSInteger index);


@end


@implementation HJPurseSwitchTitleView

- (instancetype)initWithFrame:(CGRect)frame titleArr:(NSArray<NSString *> *)titleArr styleArr:(NSArray<XCPurseSwitchModel *> *)styleArr currentButton:(NSInteger)currentButton didClickButtonBlock:(void(^)(NSInteger index))didClickButtonBlock {
    
    if (self = [super initWithFrame:frame]) {
        
        _titleArr = titleArr;
        _styleArr = styleArr;
        _currentButton = currentButton;
        _didClickButton = didClickButtonBlock;
        [self setupScrollViewWithFrame:frame];
        [self setupButtonsWithFrame:frame];
    }
    
    return self;
}


- (void)setupScrollViewWithFrame:(CGRect)frame {
    
    CGFloat contenSizeW = 0.f;
    
    self.buttonWidthArr = [NSMutableArray array];
    self.lineWidthArr = [NSMutableArray array];
    for (int i = 0; i < self.titleArr.count; i++) {
        XCPurseSwitchModel *style = nil;
        CGFloat buttonW = 0.f;
        if (i < self.styleArr.count) {
            style = self.styleArr[i];
            buttonW = style.buttonWidth;
        }
        else {
            style = self.normalStyle;
            
        }
        
        CGFloat textW = [self getTextWidthWithFont:style.selTextFont text:self.titleArr[i]];
        
        if (buttonW <= 0.f) {
            
            buttonW = textW + buttonMinMagin;
        }
        
        CGFloat lineW = style.lineWidth;
        if (lineW <= 0) {
            lineW = 0.5 * textW;
        }
        
        [self.lineWidthArr addObject:@(lineW)];
        [self.buttonWidthArr addObject:@(buttonW)];
        contenSizeW += buttonW;
    }

    if (contenSizeW > frame.size.width) {
        UIScrollView *scrollView = [[UIScrollView alloc] initWithFrame:CGRectMake(0, 0, frame.size.width, frame.size.height)];
        scrollView.showsVerticalScrollIndicator = NO;
        scrollView.showsHorizontalScrollIndicator = NO;
        scrollView.contentSize = CGSizeMake(contenSizeW, 0);
        [self addSubview:scrollView];
        self.scrollView = scrollView;
    }

}

- (void)setupButtonsWithFrame:(CGRect)frame {
    
    CGFloat y = 0.f;
    CGFloat h = frame.size.height;
    CGFloat x = 0.f;
    
    self.bttonArr = [NSMutableArray array];
    
    CGFloat selectIndex = -1;
    for (int i = 0; i < self.titleArr.count; i++) {
        XCPurseSwitchModel *style = nil;
        if (i < self.styleArr.count) {
            style = self.styleArr[i];
        }
        else {
            style = self.normalStyle;
            
        }

        UIButton *btn = [UIButton buttonWithType:UIButtonTypeCustom];
        btn.frame = CGRectMake(x, y, [self.buttonWidthArr[i] floatValue], h);
        [btn setTitle:self.titleArr[i] forState:UIControlStateNormal];
        [btn setTitleColor:style.norTextColor forState:UIControlStateNormal];
        [btn setTitleColor:style.selTextColor forState:UIControlStateSelected];
        btn.titleLabel.font = style.norTextFont;
        [btn addTarget:self action:@selector(btnAction:) forControlEvents:UIControlEventTouchUpInside];
        if (style.textAlignment) {
            btn.contentHorizontalAlignment = style.textAlignment;
        }
        if (style.norTextBgColor) {
            btn.backgroundColor = style.norTextBgColor;
        }
        btn.tag = buttonIndex + i;
        if (self.scrollView) {
            [self.scrollView addSubview:btn];
        }
        else {
            [self addSubview:btn];
        }
        
        [self.bttonArr addObject:btn];
        
        if (self.currentButton == i) {
            selectIndex = i;
            UIView *lineView = [[UIView alloc] initWithFrame:CGRectMake(x + 0.5 * ([self.buttonWidthArr[i] floatValue] - [self.lineWidthArr[i] floatValue]), frame.size.height - 2 + style.textLineOffsetY, [self.lineWidthArr[i] floatValue], 2)];
            lineView.backgroundColor = style.lineColor;
            if (self.scrollView) {
                [self.scrollView addSubview:lineView];
            }
            else {
                [self addSubview:lineView];
            }
            self.lineView = lineView;
        }
        
        x += [self.buttonWidthArr[i] floatValue];
    }
    
    if (self.scrollView) {
        [self.scrollView bringSubviewToFront:self.lineView];
    }
    else {
        [self bringSubviewToFront:self.lineView];
    }
    
    [self setupButtonStyleWithIndex:selectIndex];
}

- (void)btnAction:(UIButton *)btn {
    
    if (btn.selected) {
        return;
    }
    
    NSInteger index = btn.tag - buttonIndex;
    
    [self setupButtonStyleWithIndex:index];
    
    if (self.didClickButton) {
        self.didClickButton(index);
    }

}

- (void)setupButtonStyleWithIndex:(NSInteger)index {
    
    XCPurseSwitchModel *style = nil;
    if (index < self.styleArr.count) {
        style = self.styleArr[index];
    }
    else {
        style = self.normalStyle;
        
    }
    
    for (UIButton *button in self.bttonArr) {
        button.selected = NO;
        
        button.titleLabel.font = style.norTextFont;
        if (style.norTextBgColor) {
            button.backgroundColor = style.norTextBgColor;
        }
    }
    
    UIButton *btn = self.bttonArr[index];
    btn.selected = YES;
    
    btn.titleLabel.font = style.selTextFont;
    if (style.selTextBgColor) {
        btn.backgroundColor = style.selTextBgColor;
    }
    
    self.lineView.backgroundColor = style.lineColor;
    
    
    // 移动线
    CGRect frame = self.lineView.frame;
    CGFloat lineW = [self.lineWidthArr[index] floatValue];
    frame.size.width = lineW;
    frame.origin.x = btn.frame.origin.x + 0.5 * (btn.frame.size.width - lineW);
    frame.origin.y = btn.frame.size.height - style.lineHeight + style.textLineOffsetY;
    frame.size.height = style.lineHeight;
    __weak typeof(self) weakSelf = self;
    [UIView animateWithDuration:0.5 animations:^{
        weakSelf.lineView.frame = frame;
    }];
}

- (CGFloat)getTextWidthWithFont:(UIFont *)font text:(NSString *)text {
    
    return [text boundingRectWithSize:CGSizeMake(CGFLOAT_MAX, CGFLOAT_MAX) options:NSStringDrawingUsesFontLeading | NSStringDrawingUsesLineFragmentOrigin  attributes:@{NSFontAttributeName : font} context:nil].size.width;
}

- (void)setCurrentButton:(NSInteger)currentButton {
    
    _currentButton = currentButton;

    [self setupButtonStyleWithIndex:_currentButton];
    
    if (self.scrollView) {
        
        UIButton *btn = self.bttonArr[_currentButton];
        CGFloat contentOffsetX = 0.f;
        CGFloat btnX = btn.frame.origin.x;
        CGFloat btnCenterX = btnX + 0.5 * (btn.frame.size.width);

        CGFloat scrollContentOffsetX = self.scrollView.contentOffset.x;
        CGFloat scrollContentOffsetCenterX = scrollContentOffsetX + 0.5 * self.scrollView.frame.size.width;
        
        CGFloat scrollMagin = 0.f;
        CGFloat scrollMaxMagin = 0.f;
        CGFloat scrollX = 0.f;
        if (btnCenterX > scrollContentOffsetCenterX) {
            // 向左滑
            scrollMagin = btnCenterX - scrollContentOffsetCenterX;
            scrollMaxMagin = scrollContentOffsetX + self.scrollView.frame.size.width + scrollMagin;
            if (scrollMaxMagin >= self.scrollView.contentSize.width) {
                scrollX = self.scrollView.contentSize.width - self.scrollView.frame.size.width;
            }
            else {
                scrollX = scrollContentOffsetX + scrollMagin;
            }
        }
        else {
            // 向右滑
            scrollMagin = scrollContentOffsetCenterX - btnCenterX;
            scrollMaxMagin = scrollContentOffsetX - scrollMagin;
            if (scrollMaxMagin >= 0) {
                scrollX = scrollMaxMagin;
            }
            else {
                scrollX = 0;
            }
        }
        
        [self.scrollView setContentOffset:CGPointMake(scrollX, 0) animated:YES];
        
    }
}

- (XCPurseSwitchModel *)normalStyle {
    
    if (!_normalStyle) {
        
        _normalStyle = [XCPurseSwitchModel new];
        _normalStyle.norTextColor = UIColorHex(999999);
        _normalStyle.selTextColor = UIColorHex(1A1A1A);
        _normalStyle.norTextFont = [UIFont boldSystemFontOfSize:15];
        _normalStyle.selTextFont = [UIFont boldSystemFontOfSize:15];
        _normalStyle.lineColor = UIColorHex(00C2FF);
        _normalStyle.lineHeight = 2;
        _normalStyle.textAlignment = UIControlContentHorizontalAlignmentCenter;
        
    }
    return _normalStyle;
}

@end

@implementation XCPurseSwitchModel



@end

