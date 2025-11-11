//
//  HJAddMusicViewController.m
//  HJLive
//
//  Created by feiyin on 2020/7/14.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "HJAddMusicViewController.h"
#import "GCDWebUploader.h"
#import "SJXCSMIPHelper.h"
#import "UIViewController+Cloudox.h"
#import "HJHomeCoreClient.h"
#import "HJAdMusicView.h"
#import "YYUtility.h"
#import "HJMusicCore.h"
#import "HJMusicCoreClient.h"

@interface HJAddMusicViewController ()<
GCDWebUploaderDelegate,
HJHomeCoreClient,
HJMusicCoreClient
>
{
    GCDWebUploader * _webServer;
}
@property (nonatomic, strong) HJAdMusicView *adMusicView;
@property (nonatomic, assign) NSInteger oldSongCount;
@end

@implementation HJAddMusicViewController

- (HJAdMusicView *)adMusicView {
    if (!_adMusicView) {
        _adMusicView = [[NSBundle mainBundle] loadNibNamed:@"HJAdMusicView" owner:nil options:nil][0];
     }
    return _adMusicView;
}

- (void)viewDidLoad {
    [super viewDidLoad];
    self.title = NSLocalizedString(XCRoomMyVideoPlayerTranslateMusic, nil);
    AddCoreClient(HJHomeCoreClient, self);
    AddCoreClient(HJMusicCoreClient, self);
    [self.view addSubview:self.adMusicView];
    [self.adMusicView mas_makeConstraints:^(MASConstraintMaker *make) {
        make.top.left.right.bottom.equalTo(self.view);
    }];
    @weakify(self);
    [self.adMusicView setSenderBlock:^{
        @strongify(self);
        [self.navigationController popViewControllerAnimated:YES];
    }];
    
    [self networkReconnect:[YYUtility networkStatus]];
    
    self.oldSongCount = GetCore(HJMusicCore).items.count;
}

- (void)updateList {
    self.adMusicView.savedLabel.text = [NSString stringWithFormat:@"已导入%lu首歌",GetCore(HJMusicCore).items.count - self.oldSongCount];
}

- (void)networkReconnect:(NSInteger)tag {
    if (tag == 1) {
        //2\3\4g
        self.adMusicView.wifiTipLabel.text = @"请连接wifi";
    } else if (tag == 2) {
        //wifi
        self.adMusicView.wifiTipLabel.text = @"已连接wifi";
    } else {
        self.adMusicView.wifiTipLabel.text = @"请连接wifi";
    }
}

- (void)dealloc {
    RemoveCoreClientAll(self);
}

- (void)viewWillDisappear:(BOOL)animated {
    [super viewWillDisappear:animated];
    self.navBarBgAlpha = @"0.0";
}

- (void)viewWillAppear:(BOOL)animated {
    [super viewWillAppear:animated];
    
    self.navBarBgAlpha = @"1.0";
    [UIApplication sharedApplication].statusBarStyle = UIStatusBarStyleDefault;
    
    [self.navigationController setNavigationBarHidden:NO animated:YES];
    
    // 文件存储位置
    NSString* documentsPath = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) firstObject];
    NSLog(@"文件存储位置 : %@", documentsPath);
    
    // 创建webServer，设置根目录
    _webServer = [[GCDWebUploader alloc] initWithUploadDirectory:documentsPath];
    // 设置代理
    _webServer.delegate = self;
    _webServer.allowHiddenItems = YES;
    
    // 限制文件上传类型
    _webServer.allowedFileExtensions = @[@"mp3"];
    
    if ([_webServer start]) {
        NSString *str = [NSString stringWithFormat:@"http://%@:%zd/", [SJXCSMIPHelper deviceIPAdress], _webServer.port];
        
        if ([str containsString:@"error"]) {
             self.adMusicView.ipLabel.text = @"请先连接wifi网络~";
        }else{
            self.adMusicView.ipLabel.text = str;
        }
        
    } else {
//        self.showIpLabel.text = NSLocalizedString(@"GCDWebServer not running!", nil);
        self.adMusicView.ipLabel.text = @"暂时只支持wifi，请连接同一个wifi传输";
    }
}

- (void)viewDidDisappear:(BOOL)animated {
    [super viewDidDisappear:animated];
    
    [_webServer stop];
    _webServer = nil;
}
    
#pragma mark - <GCDWebUploaderDelegate>
- (void)webUploader:(GCDWebUploader*)uploader didUploadFileAtPath:(NSString*)path {
    [GetCore(HJMusicCore) playBackgroundMusicList];
//    self.showIpLabel.hidden = YES;
//
//    NSString *string = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
//    NSFileManager *fileManager = [NSFileManager defaultManager];
//    self.fileArray = [NSMutableArray arrayWithArray:[fileManager contentsOfDirectoryAtPath:string error:nil]];
//
//    [self.fileTableView reloadData];
}
    
- (void)webUploader:(GCDWebUploader*)uploader didMoveItemFromPath:(NSString*)fromPath toPath:(NSString*)toPath {
    NSLog(@"[MOVE] %@ -> %@", fromPath, toPath);
}
    
- (void)webUploader:(GCDWebUploader*)uploader didDeleteItemAtPath:(NSString*)path {
    NSLog(@"[DELETE] %@", path);
    
//    NSString *string = [NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0];
//    NSFileManager *fileManager = [NSFileManager defaultManager];
//    self.fileArray = [NSMutableArray arrayWithArray:[fileManager contentsOfDirectoryAtPath:string error:nil]];
//
//    [self.fileTableView reloadData];
}
    
- (void)webUploader:(GCDWebUploader*)uploader didCreateDirectoryAtPath:(NSString*)path {
    NSLog(@"[CREATE] %@", path);
}


- (void)didReceiveMemoryWarning {
    [super didReceiveMemoryWarning];
}



@end
