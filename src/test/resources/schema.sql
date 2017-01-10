CREATE TABLE OBJECTAISDATA (

OBJECTID INT,
MMSINUMBER INT,
IMONUMBER INT,
CALLSIGN VARCHAR(128),
VESSELNAME VARCHAR(128),
COUNTRY VARCHAR(128),
FLAGEFFDATE INT,
TYPE VARCHAR(128),
ICECLASS BOOLEAN,
DWT INT,
DRAFT REAL,
GRT INT,
NRT INT,
LOA REAL,
BREATHMOULDED REAL,
DEPTHMOULDED REAL,
ATSEASPEED REAL,
BUILDDATE INT,
DESTINATION VARCHAR(128),
ETA BIGINT,
PRIMARY KEY (objectId)
);

CREATE TABLE OBJECTGENERALDATA (

objectId INT,
objectName VARCHAR(128),
className VARCHAR(128),
classType INT,
objectEnvironment INT,
objectIdentity INT,
objectLength REAL,
objectWidth REAL,
objectHeight REAL,
weaponType INT,
modelName VARCHAR(128),
useSize INT,
minSize DOUBLE,
maxSize DOUBLE,
scale DOUBLE,
destination VARCHAR(128),
eta BIGINT,
PRIMARY KEY (objectId)
);

CREATE TABLE OBJECTPERIODICDATA (

objectId INT,
xPosition DOUBLE,
yPosition DOUBLE,
zPosition DOUBLE,
xVelocity DOUBLE,
yVelocity DOUBLE,
zVelocity DOUBLE,
latitude DOUBLE,
longitude DOUBLE,
altitude DOUBLE,
speed DOUBLE,
course DOUBLE,
heading DOUBLE,
elapsedMissionDuration REAL,
lastUpdatedTime BIGINT,
PRIMARY KEY (objectId)
);

CREATE TABLE VESSELAIS (

id INT auto_increment primary key,
inUse BOOLEAN,
antennaType VARCHAR(128),
antennaGain REAL,
antennaAltitude REAL,
powerOutput REAL,
aisClass VARCHAR(1),
callSign VARCHAR(128),
imo INT,
mmsi INT,
vesselName VARCHAR(128),
countryName VARCHAR(128),
flagEffDate VARCHAR(32),
typeName VARCHAR(128),
iceClass BOOLEAN,
dwt REAL,
draft REAL,
grt REAL,
nrt REAL,
loa REAL,
breathMoulded REAL,
depthMoulded REAL,
atSeaSpeed REAL,
buildDate VARCHAR(32)
);

CREATE TABLE TESTCLASS (

id INT auto_increment primary key,
name VARCHAR(128),
surname VARCHAR(128),
time BIGINT,
money REAL

);