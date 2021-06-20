drop database if exists SuperHero;

create database SuperHero;

use SuperHero;

create table location (
	id int primary key auto_increment,
    description varchar(200),
    city varchar(50) not null,
    latitude varchar(20) not null,
    longitude varchar(20) not null
    );
    
create table hero(
	id int primary key auto_increment,
    name varchar(30) not null,
    description varchar(200),
    superPower varchar(50) not null,
    heroType varchar(20) not null,
    imgUrl text
);

create table organization(
	id int primary key auto_increment,
    name varchar(30) not null,
    description varchar(200),
    city varchar(50) not null,
    email varchar(50) not null
    );
    
create table sighting(
	id int primary key auto_increment,
    locationId int not null,
    heroId int not null,
    date date not null,
	foreign key(locationId)
		references location(id),
	foreign key(heroId)
		references hero(id)
);


create table hero_organization(
	heroId int not null,
    organizationId int not null,
    primary key(heroId, organizationId),
    foreign key(heroId)
		references hero(id),
	foreign key(organizationId) 
		references organization(id)
);

        