/*
* This header is generated by classdump-dyld 1.0
* on Sunday, September 27, 2020 at 11:52:07 AM Mountain Standard Time
* Operating System: Version 14.0 (Build 18A373)
* Image Source: /System/Library/PrivateFrameworks/MailSupport.framework/MailSupport
* classdump-dyld is licensed under GPLv3, Copyright © 2013-2016 by Elias Limneos.
*/

#import <MailSupport/MailSupport-Structs.h>
#import <ProtocolBuffer/PBCodable.h>
#import <libobjc.A.dylib/NSCopying.h>

@class NSString;

@interface AWDMailNetworkDiagnosticsReport : PBCodable <NSCopying> {

	unsigned long long _numActiveCalls;
	unsigned long long _numBackgroundWifiClients;
	unsigned long long _timestamp;
	NSString* _dataIndicator;
	int _dataInterface;
	unsigned _reachabilityFlags;
	BOOL _cellData;
	BOOL _dnsEnabled;
	BOOL _roamingAllowed;
	BOOL _wifiEnabled;
	SCD_Struct_AW5 _has;

}

@property (assign,nonatomic) BOOL hasTimestamp; 
@property (assign,nonatomic) unsigned long long timestamp;                             //@synthesize timestamp=_timestamp - In the implementation block
@property (assign,nonatomic) BOOL hasReachabilityFlags; 
@property (assign,nonatomic) unsigned reachabilityFlags;                               //@synthesize reachabilityFlags=_reachabilityFlags - In the implementation block
@property (assign,nonatomic) BOOL hasDnsEnabled; 
@property (assign,nonatomic) BOOL dnsEnabled;                                          //@synthesize dnsEnabled=_dnsEnabled - In the implementation block
@property (assign,nonatomic) BOOL hasWifiEnabled; 
@property (assign,nonatomic) BOOL wifiEnabled;                                         //@synthesize wifiEnabled=_wifiEnabled - In the implementation block
@property (assign,nonatomic) BOOL hasDataInterface; 
@property (assign,nonatomic) int dataInterface;                                        //@synthesize dataInterface=_dataInterface - In the implementation block
@property (assign,nonatomic) BOOL hasCellData; 
@property (assign,nonatomic) BOOL cellData;                                            //@synthesize cellData=_cellData - In the implementation block
@property (nonatomic,readonly) BOOL hasDataIndicator; 
@property (nonatomic,retain) NSString * dataIndicator;                                 //@synthesize dataIndicator=_dataIndicator - In the implementation block
@property (assign,nonatomic) BOOL hasRoamingAllowed; 
@property (assign,nonatomic) BOOL roamingAllowed;                                      //@synthesize roamingAllowed=_roamingAllowed - In the implementation block
@property (assign,nonatomic) BOOL hasNumActiveCalls; 
@property (assign,nonatomic) unsigned long long numActiveCalls;                        //@synthesize numActiveCalls=_numActiveCalls - In the implementation block
@property (assign,nonatomic) BOOL hasNumBackgroundWifiClients; 
@property (assign,nonatomic) unsigned long long numBackgroundWifiClients;              //@synthesize numBackgroundWifiClients=_numBackgroundWifiClients - In the implementation block
-(unsigned)reachabilityFlags;
-(void)setHasTimestamp:(BOOL)arg1 ;
-(void)setDataIndicator:(NSString *)arg1 ;
-(id)copyWithZone:(NSZone*)arg1 ;
-(void)writeTo:(id)arg1 ;
-(BOOL)readFrom:(id)arg1 ;
-(void)setReachabilityFlags:(unsigned)arg1 ;
-(unsigned long long)timestamp;
-(void)setTimestamp:(unsigned long long)arg1 ;
-(void)setCellData:(BOOL)arg1 ;
-(void)setDnsEnabled:(BOOL)arg1 ;
-(void)setRoamingAllowed:(BOOL)arg1 ;
-(void)setNumActiveCalls:(unsigned long long)arg1 ;
-(void)setNumBackgroundWifiClients:(unsigned long long)arg1 ;
-(void)setWifiEnabled:(BOOL)arg1 ;
-(NSString *)dataIndicator;
-(void)mergeFrom:(id)arg1 ;
-(void)copyTo:(id)arg1 ;
-(BOOL)isEqual:(id)arg1 ;
-(BOOL)hasTimestamp;
-(unsigned long long)hash;
-(BOOL)wifiEnabled;
-(id)description;
-(id)dictionaryRepresentation;
-(void)setHasReachabilityFlags:(BOOL)arg1 ;
-(BOOL)hasReachabilityFlags;
-(void)setHasDnsEnabled:(BOOL)arg1 ;
-(BOOL)hasDnsEnabled;
-(void)setHasWifiEnabled:(BOOL)arg1 ;
-(BOOL)hasWifiEnabled;
-(void)setDataInterface:(int)arg1 ;
-(void)setHasDataInterface:(BOOL)arg1 ;
-(BOOL)hasDataInterface;
-(void)setHasCellData:(BOOL)arg1 ;
-(BOOL)hasCellData;
-(BOOL)hasDataIndicator;
-(void)setHasRoamingAllowed:(BOOL)arg1 ;
-(BOOL)hasRoamingAllowed;
-(void)setHasNumActiveCalls:(BOOL)arg1 ;
-(BOOL)hasNumActiveCalls;
-(void)setHasNumBackgroundWifiClients:(BOOL)arg1 ;
-(BOOL)hasNumBackgroundWifiClients;
-(BOOL)dnsEnabled;
-(int)dataInterface;
-(BOOL)cellData;
-(BOOL)roamingAllowed;
-(unsigned long long)numActiveCalls;
-(unsigned long long)numBackgroundWifiClients;
@end

