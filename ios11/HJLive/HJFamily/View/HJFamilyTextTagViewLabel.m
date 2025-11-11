//
//  HJFamilyTextTagViewLabel.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJFamilyTextTagViewLabel.h"

@implementation HJFamilyTextTagViewLabel
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

- (void)layoutSubviews {
    [super layoutSubviews];
    
    self.layer.cornerRadius = self.size.height/2.f;
    self.layer.masksToBounds = YES;
}

#pragma mark - Event

#pragma mark - Public methods
- (CGSize)intrinsicContentSize {
    CGSize size = [super intrinsicContentSize];
    if (size.height < 22) {
        size.height = 22;
    }
    
    if (size.width < 54) {
        size.width = 54;
    }
    return size;
}

- (void)configureWithRoleStatus:(XCFamilyRoleStatus)status level:(NSInteger)level {
    NSString *tagText = nil;
    NSString *colorString = nil;
    switch (status) {
        case XCFamilyRoleStatusLeader:
        {
            tagText = @"族长";
            colorString = @"#E6A3F4";
        }
            break;
        case XCFamilyRoleStatusManager:
        {
            tagText = @"副族长";
            colorString = @"#91D9EE";
        }
            break;
        case XCFamilyRoleStatusMember:
        {
            if (level > 0) {
                tagText = @"";
                colorString = @"#FFFFFF";
            } else {
                tagText = @"";
                colorString = @"#FFFFFF";
            }
        }
            break;
    }
    self.text = tagText;
    self.backgroundColor = [UIColor colorWithHexString:colorString];
}

#pragma mark - Private methods
- (void)commonInit {
    self.textColor = [UIColor colorWithHexString:@"#FFFFFF"];
    self.font = [UIFont boldSystemFontOfSize:14];
    self.backgroundColor = [UIColor colorWithHexString:@"#9AE2A2"];
    self.textAlignment = NSTextAlignmentCenter;
}

#pragma mark - Layout
- (void)addControls {
    
}

- (void)layoutControls {
    
}

#pragma mark - setters/getters

@end
