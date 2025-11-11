//
//  NSArray+JXBase.m
//  JXCategories
//
//  Created by Colin on 17/1/3.
//  Copyright © 2017年 JuXiao. All rights reserved.
//

#import "NSArray+JXBase.h"
#import "NSData+JXBase.h"
#import "NSValue+JXBase.h"

@implementation NSArray (JXBase)

#pragma mark - Base
- (NSRange)jx_rangeOfAll {
    return NSMakeRange(0, self.count);
}

- (NSIndexSet *)jx_indexesOfAll {
    return [NSIndexSet indexSetWithIndexesInRange:self.jx_rangeOfAll];
}

- (id)jx_objectOrNilAtIndex:(NSUInteger)index {
    return index < self.count ? self[index] : nil;
}

- (id)jx_randomObject {
    if (self.count) {
        return self[arc4random_uniform((u_int32_t)self.count)];
    }
    return nil;
}

- (NSArray<id> *)jx_randomObjectsInCount:(NSUInteger)count {
    NSMutableArray<id> *array = @[].mutableCopy;
    if (!count) return array.copy;
    if (!self.count) return array.copy;
    if (count > self.count) return array.copy;
    
    for (NSUInteger i = 0; i < count; i++) {
        [array jx_addObject:[self jx_randomObject]];
    }
    return [array copy];
}

#pragma mark - Enumerate
- (void)jx_enumerateObjectsUsingBlock:(void (^)(id obj, NSIndexPath *indexPath, NSUInteger idx, BOOL *stop))block {
    [self jx_enumerateObjectsWithOptions:kNilOptions recursive:YES usingBlock:block];
}

- (void)jx_enumerateObjectsWithOptions:(NSEnumerationOptions)opts recursive:(BOOL)recursive usingBlock:(void (^)(id obj, NSIndexPath *indexPath, NSUInteger idx, BOOL *stop))block {
    [self jx_enumerateObjectsAtIndexes:self.jx_indexesOfAll options:opts recursive:recursive usingBlock:block];
}

- (void)jx_enumerateObjectsAtIndexes:(NSIndexSet *)indexes options:(NSEnumerationOptions)opts recursive:(BOOL)recursive usingBlock:(void (^)(id obj, NSIndexPath *indexPath, NSUInteger idx, BOOL *stop))block {
    [self _jx_enumerateObjectsAtIndexes:indexes options:opts recursive:recursive indexPath:nil usingBlock:block];
}

- (BOOL)_jx_enumerateObjectsAtIndexes:(NSIndexSet *)indexes options:(NSEnumerationOptions)opts recursive:(BOOL)recursive indexPath:(NSIndexPath *)indexPath usingBlock:(void (^)(id obj, NSIndexPath *indexPath, NSUInteger idx, BOOL *stop))block {
    __block BOOL allStop = NO;
    
    [self enumerateObjectsAtIndexes:indexes options:opts usingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        NSIndexPath *buffer = nil;
        if (indexPath == nil) {
            buffer = [NSIndexPath indexPathWithIndex:idx];
        } else {
            buffer = [indexPath indexPathByAddingIndex:idx];
        }
        
        if ([obj isKindOfClass:[NSArray class]] && recursive) {
            NSIndexSet *anIndexSet = [(NSArray<id> *)obj jx_indexesOfAll];
            allStop = [obj _jx_enumerateObjectsAtIndexes:anIndexSet options:opts recursive:recursive indexPath:buffer usingBlock:block];
            if (allStop) {
                *stop = YES;
            }
        } else {
            if (block) {
                block(obj, buffer, idx, stop);
            }
            allStop = *stop;
        }
    }];
    return allStop;
}

#pragma mark - Contain
- (BOOL)jx_containsIndex:(NSUInteger)index {
    NSUInteger count = self.count;
    if (!count) return NO;
    return index >= 0 && index < count;
}

- (BOOL)jx_containsIndexes:(NSIndexSet *)indexes {
    if(!indexes.count) return NO;
    
    return [self.jx_indexesOfAll containsIndexes:indexes];
}

- (BOOL)jx_containsString:(NSString *)string {
    if (!string) return NO;
    if ([string isKindOfClass:[NSString class]]) return NO;
    
    return [self jx_containsObjectUsingBlock:^BOOL(NSString *obj, NSUInteger idx) {
        return [obj isEqualToString:string];
    }];
}

- (BOOL)jx_containsObjectUsingBlock:(BOOL (^)(id obj, NSUInteger idx))block {
    return [self jx_containsObjectWithOptions:kNilOptions usingBlock:block];
}

- (BOOL)jx_containsObjectWithOptions:(NSEnumerationOptions)opts usingBlock:(BOOL (^)(id obj, NSUInteger idx))block {
    return [self jx_containsObjectAtIndexes:self.jx_indexesOfAll options:opts usingBlock:block];
}

- (BOOL)jx_containsObjectAtIndexes:(NSIndexSet *)indexes
                           options:(NSEnumerationOptions)opts
                        usingBlock:(BOOL (^)(id obj, NSUInteger idx))block {
    __block BOOL contains = NO;
    [self enumerateObjectsAtIndexes:indexes options:opts usingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if (block) {
            contains = block(obj, idx);
        }
        
        if (contains) {
            *stop = YES;
        }
    }];
    return contains;
}

- (BOOL)jx_containsObjectAtIndexes:(NSIndexSet *)indexes
                           options:(NSEnumerationOptions)opts
                         recursive:(BOOL)recursive
                        usingBlock:(BOOL (^)(id obj, NSIndexPath *indexPath, NSUInteger idx))block {
    __block BOOL contains = NO;
    [self jx_enumerateObjectsAtIndexes:indexes options:opts recursive:recursive usingBlock:^(id  _Nonnull obj, NSIndexPath * _Nonnull indexPath, NSUInteger idx, BOOL * _Nonnull stop) {
        if (block) {
            contains = block(obj, indexPath, idx);
        }
        
        if (contains) {
            *stop = YES;
        }
    }];
    return contains;
}

- (BOOL)jx_containsObjects:(NSArray<id> *)otherObjects {
    if (!otherObjects.count) return NO;
    if (!self.count) return NO;
    if ([otherObjects isEqualToArray:self]) return YES;
    
    for (id otherObject in otherObjects) {
        if (![self containsObject:otherObject]) return NO;
    }
    return YES;
}

- (BOOL)jx_containsObjects:(NSArray<id> *)otherObjects
                usingBlock:(BOOL (^)(id obj, NSUInteger idx, id otherObj, NSUInteger otherIdx))block {
    return [self jx_containsObjectsWithOptions:kNilOptions otherObjects:otherObjects usingBlock:block];
}

- (BOOL)jx_containsObjectsWithOptions:(NSEnumerationOptions)opts
                         otherObjects:(NSArray<id> *)otherObjects
                           usingBlock:(BOOL (^)(id obj, NSUInteger idx, id otherObj, NSUInteger otherIdx))block {
    return [self jx_containsObjectsAtIndexes:self.jx_indexesOfAll options:opts otherObjects:otherObjects usingBlock:block];
}

- (BOOL)jx_containsObjectsAtIndexes:(NSIndexSet *)indexes
                            options:(NSEnumerationOptions)opts
                       otherObjects:(NSArray<id> *)otherObjects
                         usingBlock:(BOOL (^)(id obj, NSUInteger idx, id otherObj, NSUInteger otherIdx))block {
    if (!otherObjects.count) return NO;
    if (!self.count) return NO;
    if ([otherObjects isEqualToArray:self]) return YES;
    
    __block BOOL allContains = YES;
    NSIndexSet *otherIndexes = otherObjects.jx_indexesOfAll;
    [otherObjects enumerateObjectsAtIndexes:otherIndexes options:opts usingBlock:^(id  _Nonnull otherObj, NSUInteger otherIdx, BOOL * _Nonnull stop) {
        allContains &= [self jx_containsObjectAtIndexes:indexes options:opts usingBlock:^BOOL(id  _Nonnull obj, NSUInteger idx) {
            BOOL contains = NO;
            if (block) {
                contains = block(obj, idx, otherObj, otherIdx);
            } else {
                contains = [obj isEqual:otherObj];
            }
            return contains;
        }];
        
        if (!allContains) {
            *stop = YES;
        }
    }];
    return allContains;
}

- (BOOL)jx_containsObjectsAtIndexes:(NSIndexSet *)indexes
                            options:(NSEnumerationOptions)opts
                          recursive:(BOOL)recursive
                       otherObjects:(NSArray<id> *)otherObjects
                         usingBlock:(BOOL (^)(id obj, NSIndexPath *indexPath, NSUInteger idx, id otherObj, NSIndexPath *otherIndexPath, NSUInteger otherIdx))block {
    if (!otherObjects.count) return NO;
    if (!self.count) return NO;
    if ([otherObjects isEqualToArray:self]) return YES;
    
    __block BOOL allContains = YES;
    NSIndexSet *otherIndexes = otherObjects.jx_indexesOfAll;
    [otherObjects jx_enumerateObjectsAtIndexes:otherIndexes options:opts recursive:recursive usingBlock:^(id  _Nonnull otherObj, NSIndexPath * _Nonnull otherIndexPath, NSUInteger otherIdx, BOOL * _Nonnull stop) {
        allContains &= [self jx_containsObjectAtIndexes:indexes options:opts recursive:recursive usingBlock:^BOOL(id  _Nonnull obj, NSIndexPath * _Nonnull indexPath, NSUInteger idx) {
            BOOL contains = NO;
            if (block) {
                contains = block(obj, indexPath, idx, otherObj, otherIndexPath, otherIdx);
            } else {
                contains = [obj isEqual:otherObj];
            }
            return contains;
        }];
        
        if (!allContains) {
            *stop = YES;
        }
    }];
    return allContains;
}

#pragma mark - Deduplicate
- (NSArray<id> *)jx_deduplicatedArray {
    NSMutableArray<id> *buffer = [NSMutableArray arrayWithArray:self];
    [buffer jx_deduplicate];
    return buffer.copy;
}

#pragma mark - Filter
- (NSArray<id> *)jx_filteredArray:(BOOL (^)(id obj, NSUInteger idx, BOOL *stop))block {
    NSMutableArray<id> *buffer = [NSMutableArray arrayWithArray:self];
    [buffer jx_filter:block];
    return buffer.copy;
}

#pragma mark - Sort
- (NSArray<id> *)jx_sortedArrayAtRandomInCount:(NSUInteger)count {
    if (count > self.count) return @[];
    
    NSArray<id> *array = [self jx_sortedArrayAtRandom];
    return [array subarrayWithRange:self.jx_rangeOfAll];
}

- (NSArray<id> *)jx_sortedArrayAtRandom {
    NSMutableArray<id> *array = [NSMutableArray arrayWithArray:self];
    [array jx_shuffle];
    return [array copy];
}

- (NSArray<id> *)jx_sortedArrayByReversed {
    NSMutableArray<id> *array = [NSMutableArray arrayWithArray:self];
    [array jx_reverse];
    return [array copy];
}

- (NSArray<id> *)jx_sortedArrayInAscending {
    NSMutableArray<id> *array = [NSMutableArray arrayWithArray:self];
    [array jx_sortInAscending];
    return [array copy];
}

- (NSArray<id> *)jx_sortedArrayInDescending {
    NSMutableArray<id> *array = [NSMutableArray arrayWithArray:self];
    [array jx_sortInDescending];
    return [array copy];
}

- (NSArray<id> *)jx_sortedArrayInAscendingWithKeys:(NSArray<NSString *> *)keys {
    NSMutableArray<id> *array = [NSMutableArray arrayWithArray:self];
    [array jx_sortInAscendingWithKeys:keys];
    return [array copy];
}

- (NSArray<id> *)jx_sortedArrayInDescendingWithKeys:(NSArray<NSString *> *)keys {
    NSMutableArray<id> *array = [NSMutableArray arrayWithArray:self];
    [array jx_sortInDescendingWithKeys:keys];
    return [array copy];
}

#pragma mark - JSON Array
- (NSString *)jx_JSONStringEncoded {
    if ([NSJSONSerialization isValidJSONObject:self]) {
        NSError *error = nil;
        NSData *JSONData = [NSJSONSerialization dataWithJSONObject:self options:0 error:&error];
        NSString *JSONString = [[NSString alloc] initWithData:JSONData encoding:NSUTF8StringEncoding];
        if (!error) return JSONString;
    }
    return nil;
}

- (NSString *)jx_JSONPrettyStringEncoded {
    if ([NSJSONSerialization isValidJSONObject:self]) {
        NSError *error = nil;
        NSData *JSONData = [NSJSONSerialization dataWithJSONObject:self options:NSJSONWritingPrettyPrinted error:&error];
        NSString *JSONString = [[NSString alloc] initWithData:JSONData encoding:NSUTF8StringEncoding];
        if (!error) return JSONString;
    }
    return nil;
}

#pragma mark - Property List
+ (NSArray<id> *)jx_arrayWithPlistData:(NSData *)plist {
    if (!plist) return nil;
    NSArray<id> *array = [NSPropertyListSerialization propertyListWithData:plist options:NSPropertyListImmutable format:NULL error:NULL];
    if ([array isKindOfClass:[NSArray class]]) return array;
    return nil;
}

+ (NSArray<id> *)jx_arrayWithPlistString:(NSString *)plist {
    if (!plist) return nil;
    NSData *data = [plist dataUsingEncoding:NSUTF8StringEncoding];
    return [self jx_arrayWithPlistData:data];
}

- (NSData *)jx_plistData {
    return [NSPropertyListSerialization dataWithPropertyList:self format:NSPropertyListBinaryFormat_v1_0 options:kNilOptions error:NULL];
}

- (NSString *)jx_plistString {
    NSData *xmlData = [NSPropertyListSerialization dataWithPropertyList:self format:NSPropertyListXMLFormat_v1_0 options:kNilOptions error:NULL];
    if (xmlData) return xmlData.jx_utf8String;
    return nil;
}

@end


@implementation NSMutableArray (JXBase)

#pragma mark - Base
+ (instancetype)jx_mutableArrayWithArray:(NSArray<id> *)array {
    if (!array.count) return @[].mutableCopy;
    
    __block NSMutableArray<id> *buffer = array.mutableCopy;
    [array enumerateObjectsUsingBlock:^(id  _Nonnull obj, NSUInteger idx, BOOL * _Nonnull stop) {
        if ([obj isKindOfClass:[NSArray class]]) {
            NSMutableArray<id> *item = [self jx_mutableArrayWithArray:obj];
            [buffer jx_replaceObjectAtIndex:idx withObject:item];
        }
    }];
    return buffer;
}

- (void)jx_addObject:(id)anObject {
    if (!anObject) return;
    [self addObject:anObject];
}

- (void)jx_addObjectsFromArray:(NSArray<id> *)objects {
    if (!objects || !objects.count) return;
    [self addObjectsFromArray:objects];
}

- (void)jx_insertObject:(id)anObject atIndex:(NSUInteger)index {
    if (!anObject) return;
    if (index > self.count) return;
    [self insertObject:anObject atIndex:index];
}

- (void)jx_insertObjects:(NSArray<id> *)objects atIndex:(NSUInteger)index {
    if (!objects || !objects.count) return;
    if (index > self.count) return;
    
    NSUInteger i = index;
    for (id obj in objects) {
        [self jx_insertObject:obj atIndex:i++];
    }
}

- (void)jx_insertObjects:(NSArray<id> *)objects atIndexes:(NSIndexSet *)indexes {
    if (!objects || !objects.count) return;
    if (!indexes.count) return;
    
    NSRange insertRange = NSMakeRange(0, self.count + 1);
    NSIndexSet *set = [NSIndexSet indexSetWithIndexesInRange:insertRange];
    if (![set containsIndexes:indexes]) return;
    
    [self insertObjects:objects atIndexes:indexes];
}

- (void)jx_exchangeObjectAtIndex:(NSUInteger)idx1 withObjectAtIndex:(NSUInteger)idx2 {
    if (![self jx_containsIndex:idx1]) return;
    if (![self jx_containsIndex:idx2]) return;
    [self exchangeObjectAtIndex:idx1 withObjectAtIndex:idx2];
}

- (void)jx_removeObject:(id)anObject {
    if (!anObject) return;
    [self removeObject:anObject];
}

- (void)jx_removeFirstObject {
    [self jx_removeObjectAtIndex:0];
}

- (void)jx_removeLastObject {
    if (self.count) {
        [self jx_removeObjectAtIndex:self.count - 1];
    }
}

- (void)jx_removeObjectAtIndex:(NSUInteger)index {
    if (![self jx_containsIndex:index]) return;
    [self removeObjectAtIndex:index];
}

- (void)jx_removeObject:(id)anObject inRange:(NSRange)range {
    if (!anObject) return;
    if (!JXNSRangeInRange(self.jx_rangeOfAll, range)) return;
    [self removeObject:anObject inRange:range];
}

- (void)jx_removeObjectIdenticalTo:(id)anObject inRange:(NSRange)range {
    if (!anObject) return;
    if (!JXNSRangeInRange(self.jx_rangeOfAll, range)) return;
    [self removeObjectIdenticalTo:anObject inRange:range];
}

- (void)jx_removeObjectIdenticalTo:(id)anObject {
    if (!anObject) return;
    [self removeObjectIdenticalTo:anObject];
}

- (void)jx_removeObjectsInArray:(NSArray<id> *)otherArray {
    if (!otherArray.count) return;
    [self removeObjectsInArray:otherArray];
}

- (void)jx_removeObjectsInRange:(NSRange)range {
    if (!JXNSRangeInRange(self.jx_rangeOfAll, range)) return;
    [self removeObjectsInRange:range];
}

- (void)jx_removeObjectsAtIndexes:(NSIndexSet *)indexes {
    if (![self jx_containsIndexes:indexes]) return;
    [self removeObjectsAtIndexes:indexes];
}

- (void)jx_replaceObjectAtIndex:(NSUInteger)index withObject:(id)anObject {
    if (!anObject) return;
    if (![self jx_containsIndex:index]) return;
    [self replaceObjectAtIndex:index withObject:anObject];
}

- (void)jx_replaceObjectsInRange:(NSRange)range withObjectsFromArray:(NSArray<id> *)otherArray range:(NSRange)otherRange {
    if (!JXNSRangeInRange(self.jx_rangeOfAll, range)) return;
    if (!JXNSRangeInRange(otherArray.jx_rangeOfAll, otherRange)) return;
    [self replaceObjectsInRange:range withObjectsFromArray:otherArray range:otherRange];
}

- (void)jx_replaceObjectsInRange:(NSRange)range withObjectsFromArray:(NSArray<id> *)otherArray {
    if (!JXNSRangeInRange(self.jx_rangeOfAll, range)) return;
    [self replaceObjectsInRange:range withObjectsFromArray:otherArray];
}

- (void)jx_replaceObjectsAtIndexes:(NSIndexSet *)indexes withObjects:(NSArray<id> *)objects {
    if (![self jx_containsIndexes:indexes]) return;
    if (!objects.count) return;
    if (indexes.count != objects.count) return;
    [self replaceObjectsAtIndexes:indexes withObjects:objects];
}

- (id)jx_popFirstObject {
    id obj = nil;
    if (self.count) {
        obj = self.firstObject;
        [self jx_removeFirstObject];
    }
    return obj;
}

- (id)jx_popLastObject {
    id obj = nil;
    if (self.count) {
        obj = self.lastObject;
        [self jx_removeLastObject];
    }
    return obj;
}

- (void)jx_appendObject:(id)anObject {
    [self jx_addObject:anObject];
}

- (void)jx_prependObject:(id)anObject {
    [self jx_insertObject:anObject atIndex:0];
}

- (void)jx_appendObjects:(NSArray<id> *)objects {
    if (!objects || !objects.count) return;
    [self jx_addObjectsFromArray:objects];
}

- (void)jx_prependObjects:(NSArray<id> *)objects {
    if (!objects || !objects.count) return;
    NSUInteger i = 0;
    for (id obj in objects) {
        [self jx_insertObject:obj atIndex:i++];
    }
}

#pragma mark - Deduplicate
- (void)jx_deduplicate {
    NSOrderedSet *set = [[NSOrderedSet alloc] initWithArray:self];
    if (!set.array.count) return;
    [self removeAllObjects];
    [self jx_addObjectsFromArray:set.array];
}

#pragma mark - Filter
- (void)jx_filter:(BOOL (^)(id obj, NSUInteger idx, BOOL *stop))block {
    [self jx_filterWithOptions:kNilOptions usingBlock:block];
}

- (void)jx_filterWithOptions:(NSEnumerationOptions)opts usingBlock:(BOOL (^)(id obj, NSUInteger idx, BOOL *stop))block {
    if (!block) return;
    [self enumerateObjectsWithOptions:opts usingBlock:^(id  _Nonnull object, NSUInteger index, BOOL * _Nonnull aStop) {
        if (!block(object, index, aStop)) {
            [self jx_removeObject:object];
        }
    }];
}

#pragma mark - Sort
- (void)jx_reverse {
    NSUInteger count = self.count;
    int mid = floor(count / 2.0); // 向下取整中间值 9.99 -> 9, -3.14 -> -4
    for (NSUInteger i = 0; i < mid; i++) {
        [self jx_exchangeObjectAtIndex:i withObjectAtIndex:(count - (i + 1))]; // 互相交换两个元素位置 i + (count - (i + 1)) = count - 1
    }
}

- (void)jx_shuffle {
    for (NSUInteger i = self.count; i > 1; i--) {
        [self jx_exchangeObjectAtIndex:(i - 1)
                  withObjectAtIndex:arc4random_uniform((u_int32_t)i)]; // 机会递减: (A B C) -> B (A C) -> B C (A)
    }
}

- (void)jx_sortInAscending {
    if (self.count <= 1) return;
    
    [self sortUsingSelector:@selector(compare:)];
}

- (void)jx_sortInDescending {
    if (self.count <= 1) return;
    
    [self sortWithOptions:NSSortStable usingComparator:^NSComparisonResult(id  _Nonnull obj1, id  _Nonnull obj2) {
        return - [obj1 compare:obj2];
    }];
}

- (void)jx_sortInAscendingWithKeys:(NSArray<NSString *> *)keys {
    if (!keys.count || !keys) return;
    if (self.count <= 1) return;
    
    NSMutableArray<id> *descriptors = @[].mutableCopy;
    for (NSString *key in keys) {
        NSSortDescriptor *descriptor = [NSSortDescriptor sortDescriptorWithKey:key ascending:YES];
        [descriptors jx_addObject:descriptor];
    }
    [self sortUsingDescriptors:descriptors];
}

- (void)jx_sortInDescendingWithKeys:(NSArray<NSString *> *)keys {
    if (!keys.count || !keys) return;
    if (self.count <= 1) return;
    
    NSMutableArray<id> *descriptors = @[].mutableCopy;
    for (NSString *key in keys) {
        NSSortDescriptor *descriptor = [NSSortDescriptor sortDescriptorWithKey:key ascending:NO];
        [descriptors jx_addObject:descriptor];
    }
    [self sortUsingDescriptors:descriptors];
}

#pragma mark - Property List
+ (NSMutableArray<id> *)jx_arrayWithPlistData:(NSData *)plist {
    if (!plist) return nil;
    NSMutableArray<id> *array = [NSPropertyListSerialization propertyListWithData:plist options:NSPropertyListMutableContainersAndLeaves format:NULL error:NULL];
    if ([array isKindOfClass:[NSMutableArray class]]) return array;
    return nil;
}

+ (NSMutableArray<id> *)jx_arrayWithPlistString:(NSString *)plist {
    if (!plist) return nil;
    NSData *data = [plist dataUsingEncoding:NSUTF8StringEncoding];
    return [self jx_arrayWithPlistData:data];
}

@end
