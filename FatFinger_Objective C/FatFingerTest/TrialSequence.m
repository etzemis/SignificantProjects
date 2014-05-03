//
//  TrialSequence.m
//  FatFinger
//
//  Created by Evangelos Tzemis on 3/28/14.
//  Copyright (c) 2014 Evangelos Tzemis. All rights reserved.
//

#import "TrialSequence.h"


@interface TrialSequence ()
@property (nonatomic, strong) NSMutableArray* trialSequence;    // of Trial info
@end

@implementation TrialSequence

-(TrialInfo *)getNextTrial{
    TrialInfo *t  = [self.trialSequence firstObject];
    if(t) [self.trialSequence removeObjectAtIndex:0]; // if not nil;
    return t;
}

-(instancetype)init
{
    self = [super init];
    
    if (self){
        int trialID = 0;    //Trial ID
        for( int i = 0; i < [TrialSequence validRepetitions]; i++)      // for each repetition
        {
            NSMutableArray * DFTrials = [TrialSequence sequenceofTrailsforValidN_IsDescrete:YES hasFeedback:YES];
            NSMutableArray * NDFTrials = [TrialSequence sequenceofTrailsforValidN_IsDescrete:NO hasFeedback:YES];
            NSMutableArray * DNFTrials = [TrialSequence sequenceofTrailsforValidN_IsDescrete:YES hasFeedback:NO];
            NSMutableArray * NDNFTrials = [TrialSequence sequenceofTrailsforValidN_IsDescrete:NO hasFeedback:NO];
            
            
            NSMutableArray * allTrials = [NSMutableArray
                                          arrayWithCapacity:([DFTrials count] + [NDFTrials count]
                                                             + [DNFTrials count] + [NDNFTrials count])];
            [allTrials addObjectsFromArray:DFTrials];
            [allTrials addObjectsFromArray:NDFTrials];
            [allTrials addObjectsFromArray:DNFTrials];
            [allTrials addObjectsFromArray:NDNFTrials];
            
            while (true) {
                NSUInteger count = [allTrials count];  // how many left
                
                if (!count) break;                      // nothing left
                
                int r = arc4random()%count;             // choose randomly
                
                TrialInfo *trial = [allTrials objectAtIndex:r];
                trial.trialID = @(trialID++);
                [allTrials removeObjectAtIndex:r];
             
                [self.trialSequence addObject:trial];
            }
        }
    }
    return self;
}
#pragma mark - Getters

-  (NSMutableArray * )trialSequence
{
    if(!_trialSequence) _trialSequence = [[NSMutableArray alloc] init];
    return _trialSequence;
}

#pragma mark - Class functions

+ (int)validRepetitions
{
    return 2;
}

+ (NSArray *)validN
{
    return @[@2];   //@[@2, @3, @4......]
}

# pragma mark - Trial Creators

+ (NSMutableArray*)sequenceofTrailsforValidN_IsDescrete:(BOOL)isDescrete hasFeedback:(BOOL)hasFeedback
{
    
    NSMutableArray *trialSequence = [[NSMutableArray alloc] init];
    for (NSNumber *N in [TrialSequence validN])
    {
        //create the target for that N
        NSArray * targets  = [TrialSequence validTargetsForN:N];
        
        for (NSNumber *target in targets){
            //create Descrete
            TrialInfo *t1 = [[TrialInfo alloc] init];
            t1.n = N;
            t1.target = target;
            t1.isDescrete = isDescrete;
            t1.hasFeedback = hasFeedback;
            [trialSequence addObject:t1];
        }
    }
    return trialSequence;
}

//set targets for a specific N -- actually 1..N
+ (NSArray *)validTargetsForN:(NSNumber*)N{
    NSMutableArray *helper = [[NSMutableArray alloc] init];
    for (int i = 1; i<=[N integerValue]; i++) {
        [helper addObject:@(i)];
    }
    return [NSArray arrayWithArray:helper];
}


@end
