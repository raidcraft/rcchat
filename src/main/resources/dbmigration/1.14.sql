-- apply changes
create table rc_chat_channels (
  id                            integer auto_increment not null,
  name                          varchar(255),
  permission                    varchar(255),
  prefix                        varchar(255),
  color                         varchar(255),
  aliases                       varchar(255),
  type                          varchar(255),
  constraint pk_rc_chat_channels primary key (id)
);

create table rc_chat_channel_worlds (
  id                            integer auto_increment not null,
  channel                       integer not null,
  world                         varchar(255),
  constraint pk_rc_chat_channel_worlds primary key (id)
);

create table rc_chat_mutes (
  id                            integer auto_increment not null,
  player_id                     varchar(40),
  muted_player                  varchar(40),
  created                       datetime(6),
  constraint pk_rc_chat_mutes primary key (id)
);

create table rc_chat_player_prefixes (
  id                            integer auto_increment not null,
  prefix                        varchar(255),
  permission                    varchar(255),
  priority                      integer not null,
  constraint pk_rc_chat_player_prefixes primary key (id)
);

create table rc_chat_players_channel (
  id                            integer auto_increment not null,
  player                        varchar(255),
  player_id                     varchar(40),
  channel                       varchar(255),
  type                          varchar(255),
  constraint pk_rc_chat_players_channel primary key (id)
);

create table rc_chat_players_prefix (
  id                            integer auto_increment not null,
  player                        varchar(255),
  player_id                     varchar(40),
  prefix                        integer not null,
  constraint pk_rc_chat_players_prefix primary key (id)
);

create table rc_chat_world_prefixes (
  id                            integer auto_increment not null,
  world                         varchar(255),
  prefix                        varchar(255),
  constraint pk_rc_chat_world_prefixes primary key (id)
);

