//
//  ServiceCenter.h
//  
//

#import <Foundation/Foundation.h>

#if defined(HAS_COMMON_LOG) && HAS_COMMON_LOG

#import "YYLogger.h"
#define LOG_FRAMEWORK_DEBUG(format, arg...) [YYLogger debug:TBase message:format, ##arg]
#define LOG_FRAMEWORK_INFO(format, arg...) [YYLogger info:TBase message:format, ##arg]
#define LOG_FRAMEWORK_WARN(format, arg...) [YYLogger warn:TBase message:format, ##arg]
#define LOG_FRAMEWORK_ERROR(format, arg...) [YYLogger error:TBase message:format, ##arg]

#else

#define LOG_FRAMEWORK(level, format, arg...) { NSString *s = [NSString stringWithFormat:format, ##arg]; NSLog(@"[Framework][%@] %@",  level, s);}
#define LOG_FRAMEWORK_DEBUG(format, arg...) LOG_FRAMEWORK(@"Debug", format, ##arg)
#define LOG_FRAMEWORK_INFO(format, arg...) LOG_FRAMEWORK(@"Info", format, ##arg)
#define LOG_FRAMEWORK_WARN(format, arg...) LOG_FRAMEWORK(@"Warn", format, ##arg)
#define LOG_FRAMEWORK_ERROR(format, arg...) LOG_FRAMEWORK(@"Error", format, ##arg)

#endif

typedef Protocol * CommonServiceClientKey;

// 持久对象中心。 用来存放为全局服务的对象。
@interface CommonServiceCenter : NSObject

+ (CommonServiceCenter *)defaultCenter;

// 获取服务对象。
// 如果对象不存在， 会自动创建。 
- (id)getService:(Class) cls;

- (void)removeService:(Class) cls;

- (void)setNotifyingClientsWithKey:(CommonServiceClientKey)key isNotifying:(BOOL)isNotifying;
- (BOOL)isNotifyingClientsWithKey:(CommonServiceClientKey)key;
- (NSArray *)serviceClientsWithKey:(CommonServiceClientKey)key;
- (void)addServiceClient:(id)client withKey:(CommonServiceClientKey)key;
- (void)removeServiceClient:(id)client withKey:(CommonServiceClientKey)key;
- (void)removeServiceClient:(id)client;

@end

#define GET_SERVICE(obj) (obj *)[[CommonServiceCenter defaultCenter] getService:[obj class]]

#define REMOVE_SERVICE(obj) [[CommonServiceCenter defaultCenter] removeService:[obj class]]

/// 为了client不被增加引用计数，引入一个包装类。
@interface CommonServiceClient : NSObject

@property (nonatomic, weak) id object;
- (id)initWithObject:(id)object;

@end

#define ADD_SERVICE_CLIENT(protocolName, object) [[CommonServiceCenter defaultCenter] addServiceClient:object withKey:@protocol(protocolName)]
#define REMOVE_SERVICE_CLIENT(protocolName, object) [[CommonServiceCenter defaultCenter] removeServiceClient:object withKey:@protocol(protocolName)]
#define REMOVE_ALL_SERVICE_CLIENT(object) [[CommonServiceCenter defaultCenter] removeServiceClient:object]
#define NOTIFY_SERVICE_CLIENT(protocolName, selector, func) \
{ \
    NSAssert(![[CommonServiceCenter defaultCenter] isNotifyingClientsWithKey:@protocol(protocolName)], @"recusively call the same service clients."); \
    [[CommonServiceCenter defaultCenter] setNotifyingClientsWithKey:@protocol(protocolName) isNotifying:YES]; \
    NSArray *__clients__ = [[CommonServiceCenter defaultCenter] serviceClientsWithKey:@protocol(protocolName)]; \
    for (CommonServiceClient *client in __clients__) \
    { \
        id obj = client.object; \
        if ([obj respondsToSelector:selector]) \
        { \
            [obj func]; \
        } \
    } \
    [[CommonServiceCenter defaultCenter] setNotifyingClientsWithKey:@protocol(protocolName) isNotifying:NO]; \
}
