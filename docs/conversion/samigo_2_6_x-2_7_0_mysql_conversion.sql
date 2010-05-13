ALTER TABLE SAM_PUBLISHEDASSESSMENT_T ADD LASTNEEDRESUBMITDATE DATE NULL;

alter table SAM_ITEM_T add PARTIAL_CREDIT_FLAG bit NULL;
alter table SAM_PUBLISHEDITEM_T add PARTIAL_CREDIT_FLAG  bit NULL;
alter table SAM_ANSWER_T add PARTIAL_CREDIT float NULL;
alter table SAM_PUBLISHEDANSWER_T add PARTIAL_CREDIT float NULL; 

create table SAM_GRADINGATTACHMENT_T (ATTACHMENTID bigint not null auto_increment, ATTACHMENTTYPE varchar(255) not null, RESOURCEID varchar(255), FILENAME varchar(255), MIMETYPE varchar(80), FILESIZE bigint, DESCRIPTION text, LOCATION text, ISLINK bit, STATUS integer not null, CREATEDBY varchar(255) not null, CREATEDDATE datetime not null, LASTMODIFIEDBY varchar(255) not null, LASTMODIFIEDDATE datetime not null, ITEMGRADINGID bigint, primary key (ATTACHMENTID));
alter table SAM_GRADINGATTACHMENT_T add index FK28156C6C4D7EA7B3 (ITEMGRADINGID), add constraint FK28156C6C4D7EA7B3 foreign key (ITEMGRADINGID) references SAM_ITEMGRADING_T; 
