
alter table sys_organization change `Name` `Name` varchar(255) character set utf8 collate utf8_unicode_ci not null default '';
SHOW FULL COLUMNS FROM sys_organization;

alter table sys_Role change `DisplayName` `DisplayName` varchar(255) character set utf8 collate utf8_unicode_ci not null default '';
alter table sys_Role change `Description` `Description` varchar(255) character set utf8 collate utf8_unicode_ci null;
SHOW FULL COLUMNS FROM sys_Role;

alter table sys_User change `DisplayName` `DisplayName` varchar(255) character set utf8 collate utf8_unicode_ci not null default '';
SHOW FULL COLUMNS FROM sys_User;

alter table sys_menunode change `Name` `Name` varchar(255) character set utf8 collate utf8_unicode_ci not null default '';
alter table sys_menunode change `Description` `Description` varchar(255) character set utf8 collate utf8_unicode_ci null default '';
SHOW FULL COLUMNS FROM sys_menunode;

alter table sys_permission change `Name` `Name` varchar(255) character set utf8 collate utf8_unicode_ci not null default '';
alter table sys_permission change `Description` `Description` varchar(255) character set utf8 collate utf8_unicode_ci not null default '';
SHOW FULL COLUMNS FROM sys_permission;

alter table sys_SysSequence change `Comments` `Comments` varchar(255) character set utf8 collate utf8_unicode_ci null;
SHOW FULL COLUMNS FROM sys_SysSequence;

