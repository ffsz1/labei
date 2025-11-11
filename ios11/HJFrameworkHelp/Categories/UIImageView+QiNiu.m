//
//  UIImageView+QiNiu.m
//  HJLive
//
//  Created by FF on 2020/7/10.
//  Copyright © 2020 com.wdqj.gz. All rights reserved.
//

#import "UIImageView+QiNiu.h"
#import "UIImageView+WebCache.h"
#import <YYWebImage.h>

@implementation UIImageView (QiNiu)
- (void)qn_setImageImageWithUrl:(NSString *)url placeholderImage:(NSString *)imageName type:(ImageType)type{
    
    if (type == ImageTypeRoomFace) {
        [self sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:[UIImage imageNamed:imageName]];
        return;
    }
    
    if (![url containsString:JX_IMAGE_HOST_URL] || type == ImageTypeUserLibaryDetail || type == ImageTypeRoomGift || type == ImageTypeUserRoomTag) {
//    if (![url containsString:@"https://pic.tian9k9.com"] || type == ImageTypeUserLibaryDetail || type == ImageTypeRoomGift || type == ImageTypeUserRoomTag) {
//        [self yy_setImageWithURL:[NSURL URLWithString:url] placeholder:[UIImage imageNamed:imageName]];
        [self sd_setImageWithURL:[NSURL URLWithString:url] placeholderImage:[UIImage imageNamed:imageName]];
        return;
    }
   
    NSMutableString *urlString = [NSMutableString stringWithString:url];
    if ([url containsString:@"?"]) {
        [urlString appendString:@"|"];
    }else{
        [urlString appendString:@"?"];
    }
    NSString *configUrl = nil;
    switch (type) {
        case ImageTypeRoomBg:
        {
            configUrl = kImageTypeRoomBg;
        }
            break;
        case ImageTypeHomeBanner:
        {
            configUrl = kImageTypeHomeBanner;
        }
            break;
        case ImageTypeHomePageItem:
        {
            configUrl = kImageTypeHomePageItem;
        }
            break;
        case ImageTypeUserIcon:
        {
            configUrl = kImageTypeUserIcon;
        }
            break;
        case ImageTypeUserLibary:
        {
            configUrl = kImageTypeUserLibary;
        }
            break;
            
        default:
            break;
    }
    [urlString appendString:configUrl];

    NSString *encodeStr = [self URLEncodedString:urlString];
    
    if (imageName) {
//        [self yy_setImageWithURL:[NSURL URLWithString:encodeStr] placeholder:[UIImage imageNamed:imageName]];
        [self sd_setImageWithURL:[NSURL URLWithString:encodeStr] placeholderImage:[UIImage imageNamed:imageName]];
    }else{
//        [self yy_setImageWithURL:[NSURL URLWithString:encodeStr] placeholder:nil];
        [self sd_setImageWithURL:[NSURL URLWithString:encodeStr] placeholderImage:nil];
    }
}

- (NSString *)URLEncodedString:(NSString *)string
{
    
    NSString *encodedString = (NSString *)
    CFBridgingRelease(CFURLCreateStringByAddingPercentEscapes(kCFAllocatorDefault,
                                                              (CFStringRef)string,
                                                              NULL,
                                                              (CFStringRef)@"|",
                                                              kCFStringEncodingUTF8));
    
    return encodedString;
}

- (void)qn_setImageImageWithUrl:(NSString *)url placeholderImage:(NSString *)imageName type:(ImageType)type success:(void (^)(UIImage *image))success{
    
    NSString *encodeStr = [self URLEncodedString:url];
    [self sd_setImageWithURL:[NSURL URLWithString:encodeStr] placeholderImage:[UIImage imageNamed:imageName] completed:^(UIImage * _Nullable image, NSError * _Nullable error, SDImageCacheType cacheType, NSURL * _Nullable imageURL) {
        
        if (success) {
            success(image);
        }
    }];
//    [self yy_setImageWithURL:[NSURL URLWithString:encodeStr] placeholder:[UIImage imageNamed:imageName] options:0 completion:^(UIImage * _Nullable image, NSURL * _Nonnull url, YYWebImageFromType from, YYWebImageStage stage, NSError * _Nullable error) {
//        success(image);
//    }];
}

@end
