//
//  JXMacroHelper.h
//  Pods
//
//  Created by Colin on 17/1/10.
//
//

#import <UIKit/UIKit.h>
#import <pthread.h>
#import <sys/time.h>

#ifndef JXMacroHelper_h
#define JXMacroHelper_h

/**
 创建RGB颜色
 */
#ifndef JXRGB
#define JXRGB(r, g, b) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:1.0]
#endif

/**
 创建RGBA颜色
 */
#ifndef JXRGBA
#define JXRGBA(r, g, b, a) [UIColor colorWithRed:(r)/255.0 green:(g)/255.0 blue:(b)/255.0 alpha:(a)/1.0]
#endif

/**
 创建随机RGB颜色
 */
#ifndef JXRandomColor
#define JXRandomColor JXRGB(arc4random_uniform(256), arc4random_uniform(256), arc4random_uniform(256))
#endif

/**
 将X的值限定在[low, high]范围内
 */
#ifndef JX_CLAMP_VALUES
#define JX_CLAMP_VALUES(_x_, _low_, _high_)  (((_x_) > (_high_)) ? (_high_) : (((_x_) < (_low_)) ? (_low_) : (_x_)))
#endif

/**
 交换两个Value
 */
#ifndef JX_SWAP_VALUES
#define JX_SWAP_VALUES(_A_, _B_)  do { __typeof__(_A_) _tmp_ = (_A_); (_A_) = (_B_); (_B_) = _tmp_; } while (0)
#endif

#ifndef weakify
    #if DEBUG
        #if __has_feature(objc_arc)
        #define weakify(object) autoreleasepool{} __weak __typeof__(object) weak##_##object = object;
        #else
        #define weakify(object) autoreleasepool{} __block __typeof__(object) block##_##object = object;
        #endif
    #else
        #if __has_feature(objc_arc)
        #define weakify(object) try{} @finally{} {} __weak __typeof__(object) weak##_##object = object;
        #else
        #define weakify(object) try{} @finally{} {} __block __typeof__(object) block##_##object = object;
        #endif
    #endif
#endif

#ifndef strongify
    #if DEBUG
        #if __has_feature(objc_arc)
        #define strongify(object) autoreleasepool{} __typeof__(object) object = weak##_##object;
        #else
        #define strongify(object) autoreleasepool{} __typeof__(object) object = block##_##object;
        #endif
    #else
        #if __has_feature(objc_arc)
        #define strongify(object) try{} @finally{} __typeof__(object) object = weak##_##object;
        #else
        #define strongify(object) try{} @finally{} __typeof__(object) object = block##_##object;
        #endif
    #endif
#endif

#pragma mark - Queue
/**
 当前线程是否为主线程
 */
static inline bool jx_dispatch_is_main_queue() {
    return pthread_main_np() != 0;
}

/**
 异步到主线程
 */
static inline void jx_dispatch_async_on_main_queue(void (^block)(void)) {
    if (pthread_main_np()) {
        block();
    } else {
        dispatch_async(dispatch_get_main_queue(), block);
    }
}

/**
 同步到主线程
 */
static inline void jx_dispatch_sync_on_main_queue(void (^block)(void)) {
    if (pthread_main_np()) {
        block();
    } else {
        dispatch_sync(dispatch_get_main_queue(), block);
    }
}

/**
 生成线程互斥锁

 @param mutex 互斥锁
 @param recursive 是否递归
 */
static inline void jx_pthread_mutex_init_recursive(pthread_mutex_t *mutex, bool recursive) {
#define JXMUTEX_ASSERT_ON_ERROR(x_) do { \
__unused volatile int res = (x_); \
assert(res == 0); \
} while (0)
    assert(mutex != NULL);
    if (!recursive) {
        JXMUTEX_ASSERT_ON_ERROR(pthread_mutex_init(mutex, NULL));
    } else {
        pthread_mutexattr_t attr;
        JXMUTEX_ASSERT_ON_ERROR(pthread_mutexattr_init (&attr));
        JXMUTEX_ASSERT_ON_ERROR(pthread_mutexattr_settype (&attr, PTHREAD_MUTEX_RECURSIVE));
        JXMUTEX_ASSERT_ON_ERROR(pthread_mutex_init (mutex, &attr));
        JXMUTEX_ASSERT_ON_ERROR(pthread_mutexattr_destroy (&attr));
    }
#undef JXMUTEX_ASSERT_ON_ERROR
}

#pragma mark - Time
/**
 执行时间调试

 @param block 执行回调
 @param complete 完成回调
 */
static inline void JXBenchmark(void (^block)(void), void (^complete)(double ms)) {
    struct timeval t0, t1;
    gettimeofday(&t0, NULL);
    block();
    gettimeofday(&t1, NULL);
    double ms = (double)(t1.tv_sec - t0.tv_sec) * 1e3 + (double)(t1.tv_usec - t0.tv_usec) * 1e-3;
    complete(ms);
}

/**
 获取编译时间
 */
#ifndef JXCompileTime
#define JXCompileTime() _JXCompileTime(__DATE__, __TIME__)
#endif

static inline NSDate *_JXCompileTime(const char *data, const char *time) {
    NSString *timeStr = [NSString stringWithFormat:@"%s %s",data,time];
    NSLocale *locale = [[NSLocale alloc] initWithLocaleIdentifier:@"en_US"];
    NSDateFormatter *formatter = [[NSDateFormatter alloc] init];
    [formatter setDateFormat:@"MMM dd yyyy HH:mm:ss"];
    [formatter setLocale:locale];
    return [formatter dateFromString:timeStr];
}

/**
 获取相对当前时间的dispatch_time_t

 @param second 延迟时间
 @return dispatch_time_t
 */
static inline dispatch_time_t jx_dispatch_time_delay(NSTimeInterval second) {
    return dispatch_time(DISPATCH_TIME_NOW, (int64_t)(second * NSEC_PER_SEC));
}

/**
 获取相对当前时间的dispatch_walltime

 @param second 延迟时间
 @return dispatch_time_t
 */
static inline dispatch_time_t jx_dispatch_walltime_delay(NSTimeInterval second) {
    return dispatch_walltime(DISPATCH_TIME_NOW, (int64_t)(second * NSEC_PER_SEC));
}

/**
  获取相对指定日期的dispatch_walltime

 @param date 指定日期
 @return dispatch_time_t
 */
static inline dispatch_time_t jx_dispatch_walltime_date(NSDate *date) {
    NSTimeInterval interval;
    double second, subsecond;
    struct timespec time;
    dispatch_time_t milestone;
    
    interval = [date timeIntervalSince1970];
    subsecond = modf(interval, &second);
    time.tv_sec = second;
    time.tv_nsec = subsecond * NSEC_PER_SEC;
    milestone = dispatch_walltime(&time, 0);
    return milestone;
}

#pragma mark - Mathematics
/**
 获取两个正整数的最大公约数(无则返回0)
 */
extern unsigned long jx_gcd(unsigned long, unsigned long);

#endif /* JXMacroHelper_h */
