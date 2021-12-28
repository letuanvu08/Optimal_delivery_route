drop table if exists user;
create table user
(
    id       int          not null
        primary key,
    username varchar(100) null,
    password varchar(100) not null,
    email    varchar(100) null,
    constraint username
        unique (username)
);
drop table if exists HistoryPath;
create table HistoryPath
(
    id          int      not null auto_increment
        primary key,
    user_id     int      null,
    createdtime datetime null,
    duration decimal null,
    distance decimal null,
    foreign key (user_id) references user (id)
        on update cascade on delete cascade
);
drop table if exists Location;
create table Location
(
    indexlocation int  not null,
    path_id int not null,
    latitude   decimal null,
    longitude  decimal null,
    distance decimal null,
    nosignnamelocation text          null,
    namelocation       text          null,
    primary key(indexlocation,path_id),
    foreign key (path_id) references HistoryPath (id)
);
drop table if exists Geometry;
create table Geometry(
                         path_id int not null,
                         coordinates Text,
                         type Varchar(255),
                         primary key(path_id),
                         foreign key (path_id) references HistoryPath (id)
);

DROP VIEW IF EXISTS historyPathUserLocation;
CREATE VIEW historyPathUserLocation AS
SELECT path.id as id,duration,path.distance,createdTime,username, L.namelocation,L.nosignnamelocation
FROM HistoryPath path join user U on U.id = path.User_Id
                      join Location L on L.path_id=path.id;

DROP PROCEDURE IF EXISTS GetHistoryPathMathchingOrderByTime;
DELIMITER //
Create PROCEDURE GetHistoryPathMathchingOrderByTime(username Varchar(255), keyword TEXT,sizenumber integer, offsetnumber integer )
BEGIN
Select id, duration,distance,createdtime
FROM historyPathUserLocation as history
WHERE history.username=username and history.nosignnamelocation like concat('%',keyword,'%')
group by id,duration,distance,createdtime
order by createdTime
    limit sizenumber
offset offsetnumber;
END
//
DELIMITER ;

DROP PROCEDURE IF EXISTS GetHistoryPathMathchingOrderByLength;
DELIMITER //
Create PROCEDURE GetHistoryPathMathchingOrderByLength(username Varchar(255), keyword TEXT,sizenumber integer, offsetnumber integer )
BEGIN
Select id, duration,distance,createdtime
FROM historyPathUserLocation as history
WHERE history.username=username and history.nosignnamelocation like concat('%',keyword,'%')
group by id,duration,distance,createdtime
order by distance
    limit sizenumber
offset offsetnumber;
END
//
DELIMITER ;

DROP PROCEDURE IF EXISTS GetListLocations;
DELIMITER //
Create PROCEDURE GetListLocations(Path_Id integer)
BEGIN
Select indexlocation,namelocation,latitude,longitude,distance
FROM Location as L
WHERE L.Path_Id = Path_Id
order by indexlocation;
END
//
DELIMITER ;

DROP PROCEDURE IF EXISTS InsertLocation;
DELIMITER //
Create PROCEDURE InsertLocation(
    indexv integer,
    id integer,
    nosignname text,
    namev text,
    latitudev decimal ,
    longitudev decimal,
    distancev decimal )
BEGIN
insert into Location(indexlocation,path_id,nosignnamelocation,namelocation,latitude,longitude,distance)
VALUES (indexv,id,nosignname,namev,latitudev,longitudev,distancev);
END
//
DELIMITER ;

insert into user(id, username, password, email) VALUES(1,'vu','1234','vu@gmail.com');
insert into HistoryPath(id, User_Id, length, createdTime) VALUES (1,1,100,CAST(N'2021-07-23 10:34:09.000' AS DateTime));
insert into HistoryPath(id, User_Id, length, createdTime)VALUES (2,1,100,CAST(N'2021-07-23 10:34:09.000' AS DateTime));
insert into Location(indexlocation,path_id,nosignnamelocation,namelocation,latitude,longitude) VALUES (1,1,'Thu duc','Thu duc',10,10);
insert into Location(indexlocation,path_id,nosignnamelocation,namelocation,latitude,longitude)  VALUES (3,2,'binh duong','binh duong',20,20);
select * from HistoryPath;
select * from user;

insert into Geometry(path_id, coordinates, type) VALUES (:path_id,:coordinates,:type);

alter table user;
 add column  dateOfBirth varchar(100) null,
add column transport varchar(100) null,