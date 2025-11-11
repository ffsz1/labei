//
//  YPFamilyAnnouncementViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/2.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "YPFamilyAnnouncementViewController.h"
#import "YPFamilyCore.h"
#import "HJFamilyCoreClient.h"

#import "NSString+YPNIMKit.h"

@interface YPFamilyAnnouncementViewController ()<HJFamilyCoreClient>

@property (nonatomic, strong) UIImageView *iconView;
@property (nonatomic, strong) UILabel *nameLabel;
@property (nonatomic, strong) UITextView *textView;

@end

@implementation YPFamilyAnnouncementViewController

#pragma mark - Life cycle
- (void)dealloc {
    RemoveCoreClient(HJFamilyCoreClient, self);
}

- (instancetype)init {
    self = [super init];
    if (self) {
        [self commonInit];
    }
    return self;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view.
    
    [self addCores];
    [self setupNavigation];
    [self addControls];
    [self layoutControls];
    [self updateNavigation:NO];
    [self setupDatas];
}

#pragma mark - <FamilyCoreClient>
// 修改家族信息
- (void)familyEditFamilyTeamSuccess {
    [MBProgressHUD showSuccess:@"更新成功"];
    self.familyNotice = self.textView.text;
    [self updateControls];
    [self.navigationController popViewControllerAnimated:YES];
}
- (void)familyEditFamilyTeamFailedWithMessage:(NSString *)message {
    [MBProgressHUD hideHUD];
}

#pragma mark - Event
- (void)editAnnouncement {
    [self updateNavigation:YES];
    [self updateControls:YES];
}

- (void)saveAnnouncement {
    [self updateNavigation:NO];
    [self updateControls:NO];
    
    if (!self.textView.text.length) {
        [MBProgressHUD showError:@"请输入家族公告"];
        return;
    }
    
    if ([self.textView.text isEqualToString:self.familyNotice]) return;
    
    [MBProgressHUD showMessage:@""];
    [GetCore(YPFamilyCore) familyEditFamilyTeamWithFamilyId:self.familyId logo:self.familyLogo backgroundImage:self.familyBackground notice:self.textView.text];
}

#pragma mark - Public methods

#pragma mark - Private methods
- (void)updateControls {
    self.textView.text = self.familyNotice;
}

- (void)setupDatas {
    self.nameLabel.text = self.familyName;
    [self.iconView sd_setImageWithURL:[NSURL URLWithString:[self.familyLogo cutAvatarImageSize]] placeholderImage:[UIImage imageNamed:default_avatar]];
    self.textView.text = self.familyNotice;
}

- (void)addCores {
    AddCoreClient(HJFamilyCoreClient, self);
}

- (void)commonInit {
    self.enterType = HJFamilyAnnouncementEnterTypeNormal;
}

- (void)setupNavigation {
    self.navigationItem.title = @"家族公告";
}

- (void)updateNavigation:(BOOL)editable {
    if (self.enterType != HJFamilyAnnouncementEnterTypeEdit) return;
    
    UIButton *rightButton = ({
        UIButton *buffer = [UIButton new];
        buffer.size = CGSizeMake(44, 44);
        NSString *title = editable ? @"保存" : @"编辑";
        [buffer setTitle:title forState:UIControlStateNormal];
        [buffer setTitleColor:[UIColor colorWithHexString:@"#9574F5"] forState:UIControlStateNormal];
        buffer.titleLabel.font = [UIFont boldSystemFontOfSize:17];
        
        SEL selector = editable ? @selector(saveAnnouncement) : @selector(editAnnouncement);
        [buffer addTarget:self action:selector forControlEvents:UIControlEventTouchUpInside];
        buffer;
    });
    self.navigationItem.rightBarButtonItem = [[UIBarButtonItem alloc] initWithCustomView:rightButton];
}

- (void)updateControls:(BOOL)editable {
    if (self.enterType != HJFamilyAnnouncementEnterTypeEdit) return;
    
    self.textView.editable = editable;
}

#pragma mark - Layout
- (void)addControls {
    [self.view addSubview:self.iconView];
    [self.view addSubview:self.nameLabel];
    [self.view addSubview:self.textView];
}

- (void)layoutControls {
    CGSize iconSize = CGSizeMake(25, 25);
    [self.iconView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.size.equalTo(@(iconSize));
        if (@available(iOS 11.0, *)) {
            make.top.equalTo(self.view.mas_safeAreaLayoutGuideTop).offset(20);
        } else {
            make.top.equalTo(self.view.mas_top).offset(20);
        }
        make.left.equalTo(@(17));
    }];
    
    [self.nameLabel mas_makeConstraints:^(MASConstraintMaker *make) {
        make.centerY.equalTo(self.iconView);
        make.left.equalTo(self.iconView.mas_right).offset(9);
        make.right.equalTo(@(0));
    }];
    
    [self.textView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.equalTo(self.iconView.mas_bottom).offset(20);
        make.left.equalTo(@(15));
        make.right.equalTo(@(-15));
        make.bottom.equalTo(@(0));
    }];
}

#pragma mark - setters/getters
- (UIImageView *)iconView {
    if (!_iconView) {
        _iconView = [UIImageView new];
        _iconView.layer.cornerRadius = 25/2.f;
        _iconView.layer.masksToBounds = YES;
    }
    return _iconView;
}

- (UILabel *)nameLabel {
    if (!_nameLabel) {
        _nameLabel = [UILabel new];
        _nameLabel.textColor = [UIColor colorWithHexString:@"#4F4A4A"];
        _nameLabel.font = [UIFont systemFontOfSize:15];
    }
    return _nameLabel;
}

- (UITextView *)textView {
    if (!_textView) {
        _textView = [UITextView new];
        _textView.textColor = [UIColor colorWithHexString:@"#403D3D"];
        _textView.font = [UIFont boldSystemFontOfSize:16];
        _textView.editable = NO;
    }
    return _textView;
}

@end
