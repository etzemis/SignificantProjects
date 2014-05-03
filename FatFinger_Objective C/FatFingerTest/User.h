//
//  User.h
//  FatFinger
//
//  Created by Evangelos Tzemis on 4/11/14.
//  Copyright (c) 2014 Evangelos Tzemis. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>

@class FDTrial, FNDTrial, NFDTrial, NFNDTrial;

@interface User : NSManagedObject

@property (nonatomic, retain) NSNumber * age;
@property (nonatomic, retain) NSString * email;
@property (nonatomic, retain) NSNumber * experience;
@property (nonatomic, retain) NSNumber * gender;
@property (nonatomic, retain) NSNumber * lefthanded;
@property (nonatomic, retain) NSNumber * maxArea;
@property (nonatomic, retain) NSNumber * minArea;
@property (nonatomic, retain) NSString * name;
@property (nonatomic, retain) NSString * surname;
@property (nonatomic, retain) NSSet *fdtrials;
@property (nonatomic, retain) NSSet *fndtrials;
@property (nonatomic, retain) NSSet *nfdtrials;
@property (nonatomic, retain) NSSet *nfndtrials;
@end

@interface User (CoreDataGeneratedAccessors)

- (void)addFdtrialsObject:(FDTrial *)value;
- (void)removeFdtrialsObject:(FDTrial *)value;
- (void)addFdtrials:(NSSet *)values;
- (void)removeFdtrials:(NSSet *)values;

- (void)addFndtrialsObject:(FNDTrial *)value;
- (void)removeFndtrialsObject:(FNDTrial *)value;
- (void)addFndtrials:(NSSet *)values;
- (void)removeFndtrials:(NSSet *)values;

- (void)addNfdtrialsObject:(NFDTrial *)value;
- (void)removeNfdtrialsObject:(NFDTrial *)value;
- (void)addNfdtrials:(NSSet *)values;
- (void)removeNfdtrials:(NSSet *)values;

- (void)addNfndtrialsObject:(NFNDTrial *)value;
- (void)removeNfndtrialsObject:(NFNDTrial *)value;
- (void)addNfndtrials:(NSSet *)values;
- (void)removeNfndtrials:(NSSet *)values;

@end
