//
//  YPJXAuthorizationAlertView.h
//  XChat
//
//  Created by Colin on 2019/2/18.
//

#import <UIKit/UIKit.h>

NS_ASSUME_NONNULL_BEGIN

typedef NS_ENUM(NSInteger, JXAuthorizationAlertActionStyle) {
    JXAuthorizationAlertActionStyleNormal,
    JXAuthorizationAlertActionStyleCancel
};

typedef void(^JXAuthorizationAlertViewActionDidTapHandler)(void);

@interface JXAuthorizationAlertAction : NSObject

@property (nonatomic, copy) NSString *title;
@property (nonatomic, assign) JXAuthorizationAlertActionStyle style;
@property (nonatomic, copy) JXAuthorizationAlertViewActionDidTapHandler didTapHandler;

- (instancetype)initWithTitle:(NSString *)title
                        style:(JXAuthorizationAlertActionStyle)style
                      handler:(JXAuthorizationAlertViewActionDidTapHandler)handler;

@end


@interface YPJXAuthorizationAlertView : UIView

- (instancetype)initWithFrame:(CGRect)frame
                        title:(NSString *)title
                      actions:(NSArray<JXAuthorizationAlertAction *> *)actions;

@end

NS_ASSUME_NONNULL_END
